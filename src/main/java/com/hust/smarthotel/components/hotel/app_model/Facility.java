package com.hust.smarthotel.components.hotel.app_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Facility {
    private Boolean wifi;
    private Boolean bar;
    private Boolean laundry;
    private Boolean fitness;

//    public Facility(){
//        this.wifi = false;
//        this.bar = false;
//        this.laundry = false;
//        this.fitness = false;
//    }

    public void checkAll(){
        this.wifi = true;
        this.bar = true;
        this.laundry = true;
        this.fitness = true;
    }
}
