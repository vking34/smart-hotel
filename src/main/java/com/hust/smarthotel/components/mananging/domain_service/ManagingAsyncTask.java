package com.hust.smarthotel.components.mananging.domain_service;

import com.hust.smarthotel.components.mananging.repository.ManagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class ManagingAsyncTask {

    @Autowired
    private ManagingRepository managingRepository;

    @Async
    void deleteRecord(String managerId, String hotelId){
        managingRepository.deleteByUserIdAndAndHotelId(managerId, hotelId);
    }
}
