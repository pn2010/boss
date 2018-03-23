package com.itheima.bos.fore.web.action;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.type.descriptor.java.PrimitiveByteArrayTypeDescriptor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.ctc.wstx.util.StringUtil;
import com.itheima.bos.domain.base.Area;
import com.itheima.bos.domain.take_delivery.Order;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class OrderAction extends ActionSupport implements ModelDriven<Order> {

    private Order model = new Order();

    @Override
    public Order getModel() {

        return model;
    }

    private String sendAreaInfo;
    
    private String recAreaInfo;
    
    public void setRecAreaInfo(String recAreaInfo) {
        this.recAreaInfo = recAreaInfo;
    }

    public void setSendAreaInfo(String sendAreaInfo) {
        this.sendAreaInfo = sendAreaInfo;
    }

    @Action(value = "orderAction_add",
            results = {@Result(name = "success", location = "/index.html", type = "redirect")})
    public String saveOrder() {
        // 获取发件区域数据
        if (StringUtils.isNotEmpty(sendAreaInfo)) {
            // 切割数据

            String[] split = sendAreaInfo.split("/");
           //去掉省市区 因为数据库中插入数据要求不能带省市区
            String province = split[0];
            String city = split[1];
            String district = split[2];
            
            province = province.substring(0, province.length()-1);
            city = city.substring(0, city.length()-1);
            district = district.substring(0, district.length()-1);
            
            //封装area
            Area area=new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //设置数据
            model.setSendArea(area);
            
        }
        
        if (StringUtils.isNotEmpty(recAreaInfo)) {
            // 切割数据

            String[] split = recAreaInfo.split("/");
           //去掉省市区 因为数据库中插入数据要求不能带省市区
            String province = split[0];
            String city = split[1];
            String district = split[2];
            
            province = province.substring(0, province.length()-1);
            city = city.substring(0, city.length()-1);
            district = district.substring(0, district.length()-1);
            
            //封装area
            Area area=new Area();
            area.setProvince(province);
            area.setCity(city);
            area.setDistrict(district);
            //设置数据
            model.setRecArea(area);
            
        }
        
        //调用webService保存订单
        WebClient.create("http://localhost:8080/bos_management_web/webService/orderService/saveOrder").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
        
        .post(model);

        return SUCCESS;
    }

}
