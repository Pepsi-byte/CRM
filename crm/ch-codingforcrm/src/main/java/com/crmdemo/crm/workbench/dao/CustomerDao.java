package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int save(Customer customer);

    List<String> getCustomer(String name);
}
