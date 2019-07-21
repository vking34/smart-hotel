package com.hust.smarthotel.generic.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

public class PageRequestCreator {
    public static PageRequest getSimplePageRequest(Integer page, Integer pageSize){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 10;
        return PageRequest.of(page, pageSize);
    }

    public static PageRequest getDescPageRequest(Integer page, Integer pageSize, String sort){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 10;
        return PageRequest.of(page, pageSize, new Sort(Sort.Direction.DESC, sort));
    }

    public static PageRequest getPageRequest(Integer page, Integer pageSize, String sortName, Integer dir){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 10;
        Sort.Direction direction = Sort.Direction.ASC;
        if (dir != null && dir == 1)
            direction = Sort.Direction.DESC;

        return PageRequest.of(page, pageSize, new Sort(direction, sortName));
    }


    public static PageRequest getDescPageRequest(Integer page, Integer pageSize, String sort1, String sort2){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 10;
        return PageRequest.of(page, pageSize, new Sort(Sort.Direction.DESC, sort1, sort2));
    }

    public static PageRequest getAscPageRequest(Integer page, Integer pageSize, String sort){
        if(page == null)
            page = 0;
        if(pageSize == null)
            pageSize = 10;
        return PageRequest.of(page, pageSize, new Sort(Sort.Direction.ASC, sort));
    }
}
