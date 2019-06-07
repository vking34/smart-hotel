package com.hust.smarthotel.components.booking.repository;

import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookingRepositoryCustom {
    public Page<DetailBookingRecord> findBookingRecordsOfUser(String userId, Integer page, Integer pageSize);

    public DetailBookingRecord findDetailBookingRecordById(String id);

    public Page<DetailBookingRecord> findBookingRecordsNotFetched(String userId, Integer page, Integer pageSize);
}
