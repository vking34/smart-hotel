package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookingRepositoryImpl implements BookingRepositoryCustom {

    private static final Sort DESC_CREATED_DATE = new Sort(Sort.Direction.DESC, "created_date");
    private static final String USER_ID = "user._id";
    private static final String ID = "_id";

    @Autowired
    private final MongoTemplate mongoTemplate;

    public BookingRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }


    @Override
    public Page<DetailBookingRecord> findBookingRecordsOfUser(String userId, Integer page, Integer pageSize) {

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("Hotel")
                .localField("hotel_ref")
                .foreignField("_id")
                .as("hotel");

        MatchOperation matchOperation = Aggregation.match(Criteria.where(USER_ID).is(new ObjectId(userId)));
        SortOperation sortOperation = Aggregation.sort(DESC_CREATED_DATE);

        Aggregation aggregation = Aggregation.newAggregation(matchOperation,lookupOperation, sortOperation);

        List<DetailBookingRecord> bookingRecordList = mongoTemplate.aggregate(aggregation, "Reservation", DetailBookingRecord.class).getMappedResults();

        List<DetailBookingRecord> result = bookingRecordList.subList(page*pageSize, (page + 1)*pageSize - 1);

        return new PageImpl<>(result, PageRequest.of(page, pageSize), bookingRecordList.size());
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
}
