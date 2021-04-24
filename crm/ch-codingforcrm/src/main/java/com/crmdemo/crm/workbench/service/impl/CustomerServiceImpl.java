package com.crmdemo.crm.workbench.service.impl;

import com.crmdemo.crm.utils.SqlSessionUtil;
import com.crmdemo.crm.workbench.dao.CustomerDao;
import com.crmdemo.crm.workbench.domain.Customer;
import com.crmdemo.crm.workbench.service.CustomerService;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {

    private CustomerDao customerDao= SqlSessionUtil.getSqlSession().getMapper(CustomerDao.class);

    @Override
    public List<String> getCustomerName(String name) {
        List<String> nameList=customerDao.getCustomer(name);
        return nameList;
    }

}
