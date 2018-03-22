package com.itheima.bos.service.base.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.bos.dao.base.CourierRepository;
import com.itheima.bos.dao.base.FixedAreaRepository;
import com.itheima.bos.dao.base.SubAreaRepository;
import com.itheima.bos.dao.base.TakeTimeRepository;
import com.itheima.bos.domain.base.Courier;
import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.FixedAreaService;

@Transactional
@Service
public class FixedAreaServiceImpl implements FixedAreaService {
    @Autowired
    private FixedAreaRepository fixedAreaRepository;
    @Autowired
    private CourierRepository courierRepository;
    @Autowired
    private TakeTimeRepository takeTimeRepository;
    @Autowired
    private SubAreaRepository subAreaRepository;

    @Override
    public void save(FixedArea model) {
        fixedAreaRepository.save(model);
    }

    @Override
    public Page<FixedArea> findAll(Pageable pageable) {

        return fixedAreaRepository.findAll(pageable);
    }

    @Override
    public void associationCourierToFixedArea(Long fixedAreaId, Long courierId, Long takeTimeId) {
        // 持久态对象
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Courier courier = courierRepository.findOne(courierId);
        TakeTime takeTime = takeTimeRepository.findOne(takeTimeId);
        // 建立快递员和时间的关联

        courier.setTakeTime(takeTime);

        // 建立快递员和定区的关联
        // 因为
        fixedArea.getCouriers().add(courier);

    }

    // 关联分区到指定的定区
    @Override
    public void assignSubArea2FixedArea(Long fixedAreaId, Long[] subAreaIds) {
        // 关系是由分区维护的
        // 先解绑，把当前定区的所有分区全部解绑
        FixedArea fixedArea = fixedAreaRepository.findOne(fixedAreaId);
        Set<SubArea> subareas = fixedArea.getSubareas();
        for (SubArea subArea : subareas) {
            subArea.setFixedArea(null);
        }
        // 在绑定
        for (Long subAreaId : subAreaIds) {
            SubArea subArea = subAreaRepository.findOne(subAreaId);
            subArea.setFixedArea(fixedArea);
        }

    }

}
