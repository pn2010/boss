package com.itheima.crm.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.itheima.crm.domain.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByFixedAreaIdIsNull();

    List<Customer> findByFixedAreaId(String fixedAreaId);
   //把关联到定区的id的客户全部解绑
    @Modifying
    @Query("update Customer set fixedAreaId=null where fixedAreaId=?")
    void unbindByFixedAreaId(String fixedAreaId);
    @Modifying
    @Query("update Customer set fixedAreaId=? where id=?")
    void bindFixedAreaById(String fixedAreaId, Long id);
    @Modifying
    @Query("update Customer set type=1 where telephone=?")
    void active(String telephone);

    Customer findByTelephone(String telephone);

    Customer findByTelephoneAndPassword(String telephone, String password);
    
  

}
  
