package com.itheima.bos.web.action.base;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.provider.PrimitiveTextProvider;
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
import com.itheima.bos.domain.base.SubArea;
import com.itheima.bos.service.base.FixedAreaService;
import com.itheima.bos.service.base.SubAreaService;
import com.itheima.bos.web.action.CommonAction;
import com.itheima.crm.domain.Customer;

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
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setExcludes(new String[] {"subareas", "couriers"});

        page2json(page, jsonConfig);

        return NONE;
    }

    @Action(value = "fixedAreaAction_findCustomersUnAssociated")
    public String findCustomersUnAssociated() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersUnAssociated")
                .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                .getCollection(Customer.class);

        list2json(list, null);

        return NONE;
    }
    
    @Action(value = "fixedAreaAction_findCustomersAssociated2FixedArea")
    public String findCustomersAssociated2FixedArea() throws IOException {
        List<Customer> list = (List<Customer>) WebClient
                .create("http://localhost:8180/crm/webService/customerService/findCustomersAssociated2FixedArea")
                .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                .query("fixedAreaId", getModel().getId()).getCollection(Customer.class);

        list2json(list, null);

        return NONE;
    }
    
    //使用属性驱动获取被选中的客户的id
    private Long[] customerIds;
    
    public void setCustomerIds(Long[] customerIds) {
        this.customerIds = customerIds;
    }
    
    
    
    @Action(value="fixedAreaAction_assignCustomers2FixedArea",
            results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")})
    public String assignCustomers2FixedArea(){
        WebClient.create("http://localhost:8180/crm/webService/customerService/assignCustomers2FixedArea").accept(MediaType.APPLICATION_JSON)
        .type(MediaType.APPLICATION_JSON).query("fixedAreaId", getModel().getId())
        .query("customerIds", customerIds)
        .put(null);
        
        return SUCCESS;
    }

    private Long courierId;
    private Long takeTimeId;
    public void setCourierId(Long courierId) {
        this.courierId = courierId;
    }
    public void setTakeTimeId(Long takeTimeId) {
        this.takeTimeId = takeTimeId;
    }
    
    
    @Action(value="fixedAreaAction_associationCourierToFixedArea",results={@Result(name="success",location="pages/base/fixed_area.html",type="redirect")})
    public String  associationCourierToFixedArea(){
        
       fixedAreaService.associationCourierToFixedArea(getModel().getId(),courierId,takeTimeId);
        return SUCCESS;
    }
    
    private Long[] subAreaIds;
    public void setSubAreaIds(Long[] subAreaIds) {
        this.subAreaIds = subAreaIds;
    }
    
    @Action(value="fixedAreaAction_assignSubArea2FixedArea", results={@Result(name="success",location="/pages/base/fixed_area.html",type="redirect")}) 
    public String assignSubArea2FixedArea(){
        
        fixedAreaService.assignSubArea2FixedArea(getModel().getId(),subAreaIds);
        
        return SUCCESS;
    }
   
}
