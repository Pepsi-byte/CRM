package com.crmdemo.crm.setting.dao;

import com.crmdemo.crm.setting.domain.DicValue;

import java.util.List;

public interface DicValueDao {
    public List<DicValue> getDicValueList(String code);
}

