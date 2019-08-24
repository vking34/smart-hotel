package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.hust.smarthotel.generic.constant.BookingState.NEW_CREATED;

@Component
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    private static final Sort DESC_CREATED_DATE = new Sort(Sort.Direction.DESC, "created_date");
    private static final String USER_ID = "user._id";
    private static final String ID = "_id";
    private static final String CLIENT_FETCHED = "client_fetched";
    private static final String STATUS = "status";
    private static final List<DetailBookingRecord> EMPTY_LIST = new ArrayList<>();

    private static final LookupOperation LOOKUP_OPERATION = LookupOperation.newLookup()
            .from("Hotel")
            .localField("hotel_ref")
            .foreignField("_id")
            .as("hotel");

    private static final SortOperation SORT_OPERATION = Aggregation.sort(DESC_CREATED_DATE);

    @Autowired
    private final MongoTemplate mongoTemplate;

    public BookingRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Page<DetailBookingRecord> findBookingRecordsOfUser(String userId, Integer page, Integer pageSize) {

        MatchOperation matchOperation = Aggregation.match(Criteria.where(USER_ID).is(new ObjectId(userId)));
        return getDetailBookingRecords(page, pageSize, matchOperation);
    }

    @Override
    public DetailBookingRecord findDetailBookingRecordById(String id) {
        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("Hotel")
                .localField("hotel_ref")
                .foreignField("_id")
                .as("hotel");

        MatchOperation matchOperation = Aggregation.match(Criteria.where(ID).is(new ObjectId(id)));

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, lookupOperation);

        return mongoTemplate.aggregate(aggregation, "Reservation", DetailBookingRecord.class).getUniqueMappedResult();
    }

    @Override
    public Page<DetailBookingRecord> findBookingRecordsNotFetchedByClient(String userId, Integer page, Integer pageSize) {

        MatchOperation matchOperation = Aggregation
                .match(Criteria.where(USER_ID).is(new ObjectId(userId))
                        .andOperator(Criteria.where(CLIENT_FETCHED).is(false)
                        .and(STATUS).ne(NEW_CREATED)));

        return getDetailBookingRecords(page, pageSize, matchOperation);
    }



    private Page<DetailBookingRecord> getDetailBookingRecords(Integer page, Integer pageSize, MatchOperation matchOperation) {

        Aggregation aggregation = Aggregation.newAggregation(matchOperation, LOOKUP_OPERATION, SORT_OPERATION);

        List<DetailBookingRecord> bookingRecordList = mongoTemplate.aggregate(aggregation, "Reservation", DetailBookingRecord.class).getMappedResults();

        int listSize = bookingRecordList.size();

        int endPoint = (page + 1)*pageSize < listSize ? (page + 1)*pageSize : listSize;

        List<DetailBookingRecord> result;

        try {
            result = bookingRecordList.subList(page*pageSize, endPoint);
        } catch (Exception e){
            result = EMPTY_LIST;
        }

        return new PageImpl<>(result, PageRequest.of(page, pageSize), listSize);
    }
}
