package com.itheima.protal.test;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext.xml")
public class RedisTest {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Test
    public void test(){
      //存储数据
       // redisTemplate.opsForValue().set("name", "zhangsan");
      //存储数据并设置有效期
        
        //redisTemplate.opsForValue().set("age", "18", 10, TimeUnit.SECONDS);
    //删除数据
        redisTemplate.delete("name");
    
    }
 }
  
