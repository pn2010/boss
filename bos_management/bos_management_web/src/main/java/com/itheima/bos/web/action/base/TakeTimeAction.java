package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.TakeTime;
import com.itheima.bos.service.base.TakeTimeService;
import com.itheima.bos.web.action.CommonAction;


@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class TakeTimeAction extends CommonAction<TakeTime> {

    public TakeTimeAction() {

        super(TakeTime.class);
    }

    @Autowired
    private TakeTimeService takeTimeService;

    @Action(value = "takeTime_findAll")
    public String findAll() throws IOException {
        List<TakeTime> list = takeTimeService.findAll();
        
        list2json(list, null);
        return NONE;
    }
}
