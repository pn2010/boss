package com.itheima.bos.dao.base;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;

public interface SubAreaRepository extends JpaRepository<SubArea, Long> {
     List<SubArea> findByFixedAreaIsNull();
     
     List<SubArea> findByFixedArea(FixedArea fixedArea);
     
     
     
}
