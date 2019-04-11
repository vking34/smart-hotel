package com.hust.smarthotel.components.publish;


import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;

import static com.hust.smarthotel.generic.constant.ChannelConstant.*;

@Service
public class Publisher {

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
    public void announceBookRequest(String hotelId, String requestId){
        pusher.trigger(BOOKING_CHANNEL, EVENT_PREFIX_BOOKING + hotelId, Collections.singletonMap(BOOKING_REQUEST_ID, requestId));
    }

    @Async
    public void announceBookingStateToClient(String requestId, String status){
        pusher.trigger(CLIENT_CHANNEL, EVEN_PREFIX_CLIENT + requestId, Collections.singletonMap(STATUS_RESULT, status));
    }

}
