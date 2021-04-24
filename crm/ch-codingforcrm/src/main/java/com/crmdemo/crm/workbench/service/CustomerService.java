package com.crmdemo.crm.workbench.service;

import com.crmdemo.crm.workbench.domain.Customer;

import java.util.List;

public interface CustomerService {
    List<String> getCustomerName(String name);
}
