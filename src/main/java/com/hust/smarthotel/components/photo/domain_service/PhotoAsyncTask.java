package com.hust.smarthotel.components.photo.domain_service;

import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import com.hust.smarthotel.components.hotel.repository.HotelRepository;
import com.hust.smarthotel.components.user.domain_model.User;
import com.hust.smarthotel.components.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class PhotoAsyncTask {

    @Autowired
    private HotelRepository hotelRepository;

    @Autowired
    private UserRepository userRepository;

    @Async
    public void changeLogoHotel(Hotel hotel, String logoUrl){
        hotel.setLogo(logoUrl);
        hotelRepository.save(hotel);
    }

    @Async
    public void addPhotoToHotel(Hotel hotel, String photoUrl){
        hotel.getPhotos().add(photoUrl);
        hotelRepository.save(hotel);
    }

    @Async
    public void deleteHotelLogo(Hotel hotel){
        hotel.setLogo("");
        hotelRepository.save(hotel);
    }

    @Async
    public void deleteAllPhotosOfHotel(Hotel hotel){
        hotel.setLogo("");
        hotel.getPhotos().clear();
        hotelRepository.save(hotel);
    }

    @Async
    public void removePhoto(Hotel hotel, int index){
        hotel.getPhotos().remove(index);
        hotelRepository.save(hotel);
    }

    @Async
    public void changeUserAvatar(User user, String url){
        user.setPicture(url);
        userRepository.save(user);
    }

    @Async
    public void removeUserAvatar(User user){
        user.setPicture("");
        userRepository.save(user);
    }

}
