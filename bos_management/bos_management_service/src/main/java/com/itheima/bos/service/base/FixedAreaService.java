package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;

public interface FixedAreaService {

    void save(FixedArea model);

    Page<FixedArea> findAll(Pageable pageable);

    void associationCourierToFixedArea(Long fixedAreaId, Long courierId, Long takeTimeId);

    void assignSubArea2FixedArea(Long fixedAreaId, Long[] subAreaIds);

}
  
