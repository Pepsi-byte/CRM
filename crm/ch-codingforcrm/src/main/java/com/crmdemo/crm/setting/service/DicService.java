package com.crmdemo.crm.setting.service;

import com.crmdemo.crm.setting.domain.DicValue;

import java.util.List;
import java.util.Map;

public interface DicService {
    Map<String, List<DicValue>> getAllValue();
}
