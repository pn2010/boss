package com.itheima.crm.service.impl;

import java.util.List;

import javax.sound.midi.VoiceStatus;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.itheima.crm.dao.CustomerRepository;
import com.itheima.crm.domain.Customer;
import com.itheima.crm.service.CustomerService;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public List<Customer> findAll() {
       
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findCustomersUnAssociated() {
          
        return customerRepository.findByFixedAreaIdIsNull();
    }

    @Override
    public List<Customer> findCustomersAssociated2FixedArea(String fixedAreaId) {
          
        return customerRepository.findByFixedAreaId(fixedAreaId);
    }
    
    public void assignCustomers2FixedArea(String fixedAreaId, Long[] customerIds){
        if (StringUtils.isNotEmpty(fixedAreaId)) {
            
        
        //把关联到定区id的客户全部解绑
        customerRepository.unbindByFixedAreaId(fixedAreaId);
        
        if (customerIds!=null&&customerIds.length>0) {
            for (Long id : customerIds) {
               customerRepository.bindFixedAreaById(fixedAreaId,id); 
            }
        }
        
        }  
    }

    @Override
    public void save(Customer customer) {
          customerRepository.save(customer);
        
    }

    @Override
    public void active(String telephone) {
        customerRepository.active(telephone);
         
        
    }

    @Override
    public Customer isActived(String telephone) {
          
        return customerRepository.findByTelephone(telephone);
    }

    @Override
    public Customer login(String telephone, String password) {
          
        
        return customerRepository.findByTelephoneAndPassword(telephone,password);
    }

}
