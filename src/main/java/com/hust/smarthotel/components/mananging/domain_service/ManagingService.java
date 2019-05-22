package com.hust.smarthotel.components.mananging.domain_service;

import com.hust.smarthotel.components.mananging.app_model.ManagingResponse;
import com.hust.smarthotel.components.mananging.domain_model.ExtendedManaging;
import com.hust.smarthotel.components.mananging.domain_model.Managing;
import com.hust.smarthotel.components.mananging.repository.ManagingRepository;
import com.hust.smarthotel.generic.util.PageRequestCreator;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_EXISTING_RECORD;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_RECORD_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.MANAGING_OUT_OF_HOTEL;


@Service
public class ManagingService {

    @Autowired
    private ManagingRepository managingRepository;

    @Autowired
    private ManagingAsyncTask asyncTask;

    public Managing findManaging(String managerId, String hotelId){
        return managingRepository.findManagingByUserIdAndAndHotelId(managerId, new ObjectId(hotelId));
    }

    public Page<ExtendedManaging> findHotelsOfManager(String mangerId, Integer page, Integer pageSize){
        Page<ExtendedManaging> managingPage =  managingRepository.findDetailManagingRecords(mangerId, PageRequestCreator.getSimplePageRequest(page, pageSize));
        return managingPage;
    }

    public ManagingResponse addHotelToManager(String managerId, String hotelId){
        if (managingRepository.findManagingByUserIdAndAndHotelId(managerId, new ObjectId(hotelId)) != null)
            return MANAGING_EXISTING_RECORD;

        List<Managing> managingList = findManagingByManagerId(managerId);
        if (managingList.size() >= 5)
            return MANAGING_OUT_OF_HOTEL;

        Managing managing = new Managing(managerId, hotelId);
        managing = managingRepository.save(managing);
        return new ManagingResponse(true, null, null, managing);
    }

    public ManagingResponse removeHotelFromManager(String managerId, String hotelId){
        Managing managing = managingRepository.findManagingByUserIdAndAndHotelId(managerId, new ObjectId(hotelId));
        if (managing == null)
            return MANAGING_RECORD_NOT_FOUND;

        asyncTask.deleteRecord(managerId, hotelId);

        return new ManagingResponse(true, null, null, managing);
    }

    public List<Managing> findManagingByManagerId(String managerId){
        return managingRepository.findManagingByUserId(managerId);
    }
}
