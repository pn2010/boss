package com.itheima.bos.dao.test;

import java.util.Collection;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.config.CustomEditorConfigurer;

import com.itheima.crm.domain.Customer;

import net.sf.json.JSONObject;

public class JsonTest {

    public static void main(String[] args) {
        Collection<? extends Customer> collection =
                WebClient.create("http://localhost:8180/crm/webService/customerService/findAll")
                        .accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON)
                        .getCollection(Customer.class);

        for (Customer customer : collection) {
            System.out.println(customer);
        }

    }

}
