package com.hust.smarthotel.components.mananging.repository;

import com.hust.smarthotel.components.mananging.domain_model.ExtendedManaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManagingRepositoryCustom {

    Page<ExtendedManaging> findDetailManagingRecords(String userId, Pageable pageable);
}
