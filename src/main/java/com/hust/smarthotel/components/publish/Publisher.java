package com.hust.smarthotel.components.publish;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.smarthotel.components.booking.domain_model.BookingRecord;
import com.hust.smarthotel.components.booking.domain_model.DetailBookingRecord;
import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.hust.smarthotel.generic.constant.ChannelConstant.*;
import static com.hust.smarthotel.generic.constant.BookingState.STATUS;
import static com.hust.smarthotel.generic.constant.BookingState.NEW_CREATED;
import static com.hust.smarthotel.generic.constant.BookingState.CANCELED;

@Service
public class Publisher {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Value("${pusher.id}")
    private String appId;

    @Value("${pusher.key}")
    private String appKey;

    @Value("${pusher.secret}")
    private String appSecret;

    @Value("${pusher.cluster}")
    private String cluster;

    @Value("${pusher.encrypted}")
    private Boolean isEncrypted;

    private Pusher pusher;

    @PostConstruct // a callback method upon initialization / after injecting values
    public void configure(){
        pusher = new Pusher(appId, appKey, appSecret);
        pusher.setCluster(cluster);
        pusher.setEncrypted(isEncrypted);
    }

    @Async
    public void announceBookingRequest(String hotelId, String requestId){
        Map<String, String> data = new HashMap<>();
        data.put(BOOKING_REQUEST_ID, requestId);
        data.put(STATUS, NEW_CREATED);
//        pusher.trigger(BOOKING_CHANNEL, EVENT_PREFIX_BOOKING + hotelId, Collections.singletonMap(BOOKING_REQUEST_ID, requestId));
        pusher.trigger(BOOKING_CHANNEL, EVENT_PREFIX_BOOKING + hotelId, data);
    }

    @Async
    public void announceBookingStateToClient(String requestId, BookingRecord bookingRecord){
        Map<String, String> data = new HashMap<>();
        data.put(BOOKING_REQUEST_ID, bookingRecord.getId());
        data.put(STATUS, bookingRecord.getStatus());

        pusher.trigger(CLIENT_CHANNEL, EVEN_PREFIX_CLIENT + requestId, data);
    }

    @Async
    public void announceBookingCancelation(DetailBookingRecord bookingRecord){
        Map<String, String> data = new HashMap<>();
        data.put(BOOKING_REQUEST_ID, bookingRecord.getId());
        data.put(STATUS, CANCELED);

        pusher.trigger(BOOKING_CHANNEL, EVENT_PREFIX_BOOKING + bookingRecord.getHotel().getId(), data);
    }

}
