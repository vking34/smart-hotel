package com.hust.smarthotel.components.photo.controller;


import com.hust.smarthotel.generic.constant.UrlConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import javax.validation.constraints.NotNull;
import java.io.IOException;


@RestController
@RequestMapping(UrlConstants.API + "/photos")
@CrossOrigin("*")
public class PhotoController {

    @PostMapping
    public ResponseEntity uploadPhoto(@NotNull @RequestParam("file") MultipartFile multipartFile){
        System.out.println("upload photo...");
        System.out.println(multipartFile);

        try{
            System.out.printf("File name = %s, size= %s", multipartFile.getOriginalFilename(), multipartFile.getSize());

            File file = new File("/root/photos/" + multipartFile.getOriginalFilename());

            multipartFile.transferTo(file);

        }catch (IOException e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }
}
