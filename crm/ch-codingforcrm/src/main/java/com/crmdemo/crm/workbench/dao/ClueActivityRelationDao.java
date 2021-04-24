package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {

    List<ClueActivityRelation> getListByClueId(String clueId);


    int setRelation(ClueActivityRelation activityRelation);
    int unbund(String id);

    int delete(ClueActivityRelation clueActivityRelation);
}
