package com.hust.smarthotel.components.photo.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.domain_service.HotelService;
import com.hust.smarthotel.components.photo.app_model.DeletePhotoRequest;
import com.hust.smarthotel.components.photo.app_model.PhotoResponse;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.domain_service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INTERNAL_ERROR;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_HOTEL_NOT_FOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INVALID_TYPE;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_MAX_PHOTOS;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_INDEX_OUT_OF_BOUND;
import static com.hust.smarthotel.generic.response.ErrorResponses.PHOTO_USER_NOT_FOUND;

@Service
public class PhotoService {

    public static final String ABSOLUTE_PATH = System.getProperty("user.dir");

    // types
    private static final String LOGO = "logo";
    private static final String PHOTO = "photo";
    private static final String ALL = "all";

    @Value("${photo.dir-path}")
    public String dirPath;

    @Value("${photo.base-url}")
    public String baseUrl;

    @Value("${photo.photo-max}")
    public Integer photoMax;


    @Autowired
    private HotelService hotelService;

    @Autowired
    private UserService userService;

    @Autowired
    private PhotoAsyncTask asyncTask;


    public PhotoResponse addPhotoToHotel(String hotelId, MultipartFile multipartFile, String type){

        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null)
            return PHOTO_HOTEL_NOT_FOUND;

        if (type.equals(PHOTO) && hotel.getPhotos().size() == photoMax)
            return PHOTO_MAX_PHOTOS;


        String fileName = generateFileName(hotelId);
        String filePath = ABSOLUTE_PATH.concat(dirPath).concat(fileName);
//        System.out.println(filePath);

        String url = baseUrl.concat(fileName);

        try{
            File file = new File(filePath);
            multipartFile.transferTo(file);
        }catch (IOException e){
            e.printStackTrace();
            return PHOTO_INTERNAL_ERROR;
        }

        switch (type){
            case LOGO:
                asyncTask.changeLogoHotel(hotel, url);
                break;
            case PHOTO:
                asyncTask.addPhotoToHotel(hotel, url);
                break;
            default:
                return PHOTO_INVALID_TYPE;
        }

        return new PhotoResponse(true, null, null, url);
    }

    public PhotoResponse deletePhoto(String hotelId, DeletePhotoRequest request){
        Hotel hotel = hotelService.findHotelById(hotelId);
        if (hotel == null)
            return PHOTO_HOTEL_NOT_FOUND;

        PhotoResponse photoResponse = null;
        switch (request.getType()){
            case LOGO:
                asyncTask.deleteHotelLogo(hotel);
                photoResponse = new PhotoResponse(true, null, null, hotel.getLogo());
                break;

            case PHOTO:
                if (request.getPosition() >= hotel.getPhotos().size()){
                    return PHOTO_INDEX_OUT_OF_BOUND;
                }
                asyncTask.removePhoto(hotel, request.getPosition());
                photoResponse = new PhotoResponse(true, null, null, null);
                break;

            case ALL:
                asyncTask.deleteAllPhotosOfHotel(hotel);
                photoResponse = new PhotoResponse(true, null, null, null);
                break;
        }

        return photoResponse;
    }

    public PhotoResponse setUserAvatar(String userId, MultipartFile multipartFile){
        User user = userService.findUser(userId);
        if (user == null)
            return PHOTO_USER_NOT_FOUND;

        String fileName = generateFileName(userId);
        String filePath = ABSOLUTE_PATH.concat(dirPath).concat(fileName);
        String url = baseUrl.concat(fileName);

        try{
            File file = new File(filePath);
            multipartFile.transferTo(file);
        }catch (IOException e){
            return PHOTO_INTERNAL_ERROR;
        }

        asyncTask.changeUserAvatar(user, url);
        return new PhotoResponse(true, null, null, url);
    }

    public PhotoResponse removeUserAvatar(String userId){
        User user = userService.findUser(userId);
        if (user == null)
            return PHOTO_USER_NOT_FOUND;

        asyncTask.removeUserAvatar(user);
        return new PhotoResponse(true, null, null, user.getPicture());
    }

    private String generateFileName(String id){
        StringBuilder fileName = new StringBuilder(id);
        fileName.append(String.valueOf(new Date().getTime()));
//        fileName.append(fileEnding);
        return fileName.toString();
    }
}
