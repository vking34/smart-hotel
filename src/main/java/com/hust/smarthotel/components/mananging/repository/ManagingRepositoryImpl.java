package com.hust.smarthotel.components.mananging.repository;

import com.hust.smarthotel.components.mananging.domain_model.ExtendedManaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManagingRepositoryImpl implements ManagingRepositoryCustom {

    @Autowired
    private final MongoTemplate mongoTemplate;

    public ManagingRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<ExtendedManaging> findDetailManagingRecords(String userId, Pageable pageable) {

        LookupOperation lookupOperation = LookupOperation.newLookup()
                .from("Hotel")
                .localField("hotel_id")
                .foreignField("_id")
                .as("hotel");

        Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("user_id").is(userId)),
                                                            Aggregation.skip(pageable.getPageNumber() * pageable.getPageSize()),
                                                            Aggregation.limit(pageable.getPageSize()),
                                                            lookupOperation);

        List<ExtendedManaging> managingList = mongoTemplate.aggregate(aggregation, "Managing", ExtendedManaging.class).getMappedResults();

        return new PageImpl<>(managingList, pageable, managingList.size());
    }
}
