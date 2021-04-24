package com.crmdemo.crm.workbench.service;

import com.crmdemo.crm.workbench.domain.Clue;
import com.crmdemo.crm.workbench.domain.Tran;

import java.util.List;

public interface ClueService {
    boolean save(Clue c);

    Clue getDetail(String id);

    Boolean unbund(String id);

    Boolean setRelation(String cid, String[] aid);

    Boolean convert(String clueId, Tran t, String createBy);
}
