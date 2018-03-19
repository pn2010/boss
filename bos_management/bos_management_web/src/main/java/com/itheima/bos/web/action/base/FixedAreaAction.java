package com.itheima.bos.web.action.base;

import java.io.IOException;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.bouncycastle.jce.provider.JDKDSASigner.noneDSA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;

import com.itheima.bos.domain.base.FixedArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.web.action.CommonAction;

import net.sf.json.JsonConfig;

@Namespace("/")
@ParentPackage("struts-default")
@Controller
@Scope("prototype")
public class FixedAreaAction extends CommonAction<FixedArea> {

    public FixedAreaAction() {
        super(FixedArea.class);

    }

    @Autowired
    private FixedAreaService fixedAreaService;

    @Action(value = "fixedAreaAction_save", results = {
            @Result(name = "success", location = "/pages/base/fixed_area.html", type = "redirect")})
    public String save() {
        fixedAreaService.save(getModel());

        return SUCCESS;
    }

    @Action(value = "fixedAreaAction_pageQuery")
    public String pageQuery() throws IOException {
        Pageable pageable = new PageRequest(page - 1, rows);
        Page<FixedArea> page = fixedAreaService.findAll(pageable);
        JsonConfig jsonConfig=new JsonConfig();
        jsonConfig.setExcludes(new String[]{"subareas","couriers"});
        
       page2json(page, jsonConfig);
       

        return NONE;
    }

}
