package com.hust.smarthotel.components.photo.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.photo.app_model.PhotoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INTERNAL_ERROR;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_FOUND;


@Service
public class PhotoService {

    @Value("${photo.base-path}")
    public String basePath;

    @Value("${photo.base-url}")
    public String baseUrl;

//    @Value("${photo.file-ending}")
//    public String fileEnding;

    @Autowired
    private HotelService hotelService;

    @Autowired
    private PhotoAsyncTask asyncTask;

    public PhotoResponse setLogoToHotel(String hotelId, MultipartFile multipartFile){

        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null)
            return PHOTO_HOTEL_NOT_FOUND;

        System.out.println(basePath);
        System.out.println(baseUrl);
        String fileName = generateFileName(hotelId);
        String filePath = basePath.concat(fileName);
        String url = baseUrl.concat(fileName);

        try{
            System.out.printf("File name = %s, size= %s", multipartFile.getOriginalFilename(), multipartFile.getSize());
            File file = new File(filePath);
            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
            return PHOTO_INTERNAL_ERROR;
        }

        asyncTask.changeLogoHotel(hotel, url);
        return new PhotoResponse(true, null, null, url);
    }

    private String generateFileName(String id){
        StringBuilder fileName = new StringBuilder(id);
        fileName.append(String.valueOf(new Date().getTime()));
//        fileName.append(fileEnding);
        return fileName.toString();
    }
}
