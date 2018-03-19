package com.itheima.bos.service.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.itheima.bos.domain.base.FixedArea;

public interface FixedAreaService {

    void save(FixedArea model);

    Page<FixedArea> findAll(Pageable pageable);

}
  
