package com.itheima.bos.fore.web.action;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.hibernate.procedure.internal.Util.ResultClassesResolutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.crm.domain.Customer;
import com.itheima.utils.MailUtils;
import com.itheima.utils.SmsUtils;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.ModelDriven;

@Namespace("/")
@ParentPackage("struts-default")
@Scope("prototype")
@Controller
public class CustomerAction extends ActionSupport implements ModelDriven<Customer> {

    private Customer model = new Customer();

    @Override
    public Customer getModel() {

        return model;
    }

    @Action(value = "customerAction_sendSms")
    public String sendSms() {

        try {

            String code = RandomStringUtils.randomNumeric(6);
            System.out.println(code);
            // 存入session中
            ServletActionContext.getRequest().getSession().setAttribute("serverCode", code);
            // 发送短信
            SmsUtils.sendSms(model.getTelephone(), code);
        } catch (ClientException e) {

            e.printStackTrace();

        }

        return NONE;
    }

    private String checkCode;

    public void setCheckCode(String checkCode) {
        this.checkCode = checkCode;
    }

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Action(value = "customerAction_regist",
            results = {
                    @Result(name = "success", location = "signup-success.html", type = "redirect"),
                    @Result(name = "error", location = "signup-fail.html", type = "redirect")})
    public String regist() {
        String serverCode =
                (String) ServletActionContext.getRequest().getSession().getAttribute("serverCode");
        System.out.println(serverCode);
        System.out.println(checkCode);
        if (StringUtils.isNotEmpty(serverCode) && StringUtils.isNotEmpty(checkCode) &&

                checkCode.equals(serverCode)) {

            WebClient.create("http://localhost:8180/crm/webService/customerService/save")
                    .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                    .post(model);

            // 生成验证码
            String activeCode = RandomStringUtils.randomNumeric(32);
            // 存储验证码
            redisTemplate.opsForValue().set(model.getTelephone(), activeCode, 1, TimeUnit.DAYS);

            String emailBody =
                    "感谢您注册本网站的帐号，请在24小时之内点击<a href='http://localhost:8280/protal/customerAction_active.action?activeCode="
                            + activeCode + "&telephone=" + model.getTelephone()
                            + "'>本链接</a>激活您的帐号";
            MailUtils.sendMail(model.getEmail(), "激活邮件", emailBody);

            return SUCCESS;
        }

        return ERROR;
    }

    private String activeCode;

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    @Action(value="customerAction_active",results={
        @Result(name="success",location="/login.html",type="redirect"),
        @Result(name="error",location="/signup-fail.html",type="redirect")    
    })
    public String active(){
       
        String serverCode = redisTemplate.opsForValue().get(model.getTelephone());
        System.out.println(serverCode);
        if (StringUtils.isNotEmpty(serverCode)&&StringUtils.isNotEmpty(activeCode)&&serverCode.equals(activeCode)) {
          //激活
            WebClient.create("http://localhost:8180/crm/webService/customerService/active").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
            .query("telephone", model.getTelephone())
            .put(null);
            return SUCCESS;
        }
       return ERROR;
    }
    
    @Action(value="customerAction_login",results={@Result(name="success",location="/index.html",type="redirect")
    ,@Result(name="error",location="/login.html",type="redirect")})
    public String  login(){
       //判断是否et激活
        
        String  serverCode = (String) ServletActionContext.getRequest().getSession().getAttribute("validateCode");
        if (StringUtils.isNotEmpty(serverCode)&&StringUtils.isNotEmpty(checkCode)&&serverCode.equals(checkCode)) {
            Customer customer = WebClient.create("http://localhost:8180/crm/webService/customerService/isActived").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
            .query("telephone", model.getTelephone())
            .get(Customer.class);
         if(customer!=null&&customer.getType()!=null){
             
             if (customer.getType()==1) {
                 //激活了
                  //登录
                Customer c = WebClient.create("http://localhost:8180/crm/webService/customerService/login").accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).
                query("telephone", model.getTelephone()).
                query("password", model.getPassword()).get(Customer.class);
                
                if (c!=null) {
                    ServletActionContext.getRequest().getSession().setAttribute("user", c);
                return SUCCESS;
                }else{
                  return ERROR; 
                    
                }
                 
                
            }else{
                //用户登录了，未激活
                return "unactived";
            }
             
             
         }  
            
            
            
        }
        
        
       return ERROR; 
    }
}
