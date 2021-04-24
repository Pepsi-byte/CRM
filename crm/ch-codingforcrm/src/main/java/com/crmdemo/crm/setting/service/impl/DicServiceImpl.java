package com.crmdemo.crm.setting.service.impl;

import com.crmdemo.crm.setting.dao.DicTypeDao;
import com.crmdemo.crm.setting.dao.DicValueDao;
import com.crmdemo.crm.setting.domain.DicType;
import com.crmdemo.crm.setting.domain.DicValue;
import com.crmdemo.crm.setting.service.DicService;
import com.crmdemo.crm.utils.SqlSessionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DicServiceImpl implements DicService {

    private DicTypeDao dicTypeDao= SqlSessionUtil.getSqlSession().getMapper(DicTypeDao.class);
    private DicValueDao dicValueDao=SqlSessionUtil.getSqlSession().getMapper(DicValueDao.class);


    @Override
    public Map<String, List<DicValue>> getAllValue() {

        Map<String,List<DicValue>> map=new HashMap<>();
        //首先tbl_dic_type中获取到所有的字典数据类型
        //在数据库对应的表中，数据自担的类型在类型表中对应的就是code
     List<DicType> dicTypeList=dicTypeDao.getCode();

     //这个list中存放的是在数据库中查询出来的一个个dicType对象，遍历，取出code
        for(DicType dicType:dicTypeList){
            //根据取出来的code,到dicValue表中查询出需要的7个List集合
            String code=dicType.getCode();
            List<DicValue> dicValueList=dicValueDao.getDicValueList(code);

            map.put(code,dicValueList);
        }
        return map;
    }
}
