package com.hust.smarthotel.components.mananging.domain_service;

import com.hust.smarthotel.components.mananging.app_model.ManagingResponse;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.repository.ManagingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_EXISTING_RECORD;

@Service
public class ManagingService {

    @Autowired
    private ManagingRepository managingRepository;

    @Autowired
    private ManagingAsyncTask asyncTask;

    public Managing findManaging(String managerId, String hotelId){
        return managingRepository.findManagingByUserIdAndAndHotelId(managerId, hotelId);
    }

    public ManagingResponse addHotelToManager(String managerId, String hotelId){
        if (managingRepository.findManagingByUserIdAndAndHotelId(managerId, hotelId) != null)
            return MANAGING_EXISTING_RECORD;
        Managing managing = new Managing(managerId, hotelId);
        managing = managingRepository.save(managing);
        return new ManagingResponse(true, null, null, managing);
    }

    public ManagingResponse removeHotelFromManager(String managerId, String hotelId){
        Managing managing = managingRepository.findManagingByUserIdAndAndHotelId(managerId, hotelId);
        if (managing == null)
            return MANAGING_EXISTING_RECORD;

        asyncTask.deleteRecord(managerId, hotelId);

        return new ManagingResponse(true, null, null, managing);
    }

}
