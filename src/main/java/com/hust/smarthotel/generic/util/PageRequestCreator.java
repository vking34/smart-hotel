package com.hust.smarthotel.generic.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

public class PageRequestCreator {
    public static PageRequest getSimplePageRequest(Integer page, Integer pageSize){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 5;
        return PageRequest.of(page, pageSize);
    }

    public static PageRequest getDescPageRequest(Integer page, Integer pageSize, String sort){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 5;
        return PageRequest.of(page, pageSize, new Sort(Sort.Direction.DESC, sort));
    }

    public static PageRequest getAscPageRequest(Integer page, Integer pageSize, String sort){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 5;
        return PageRequest.of(page, pageSize, new Sort(Sort.Direction.ASC, sort));
    }

}
