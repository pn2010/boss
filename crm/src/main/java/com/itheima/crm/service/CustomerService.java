package com.itheima.crm.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CustomerService {
    @GET
    @Path("/findAll")
    public List<Customer> findAll();
    //查找未关联定区的客户
    @GET
    @Path("/findCustomersUnAssociated")
    public List<Customer> findCustomersUnAssociated();
    
    
    
    //查找关联到指定定区的客户
    @GET
    @Path("/findCustomersAssociated2FixedArea")
    public List<Customer> findCustomersAssociated2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId);

 
    @PUT
    @Path("/assignCustomers2FixedArea")
    public void assignCustomers2FixedArea(@QueryParam("fixedAreaId") String fixedAreaId,
            @QueryParam("customerIds") Long[] customerIds);
   
    @POST
    @Path("/save")
    public void  save(Customer customer);
    
    
    //激活用户
    @PUT
    @Path("/active")
    public void active(@QueryParam("telephone") String telephone);
    
    @GET
    @Path("/isActived")
    public Customer isActived(@QueryParam("telephone") String telephone);
    @GET
    @Path("/login")
    public Customer  login(@QueryParam("telephone") String telephone,@QueryParam("password") String password);
}
  
