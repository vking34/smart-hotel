package com.hust.smarthotel.components.publish;


import com.pusher.rest.Pusher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;


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

    public void announce(){
        pusher.trigger("my-channel", "my-event", Collections.singletonMap("message", "hello world"));
    }
}
