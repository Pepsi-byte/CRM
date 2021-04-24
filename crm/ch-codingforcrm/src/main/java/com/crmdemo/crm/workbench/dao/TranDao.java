package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.Tran;

public interface TranDao {

    int save(Tran t);

    Tran getTranDetail(String id);
}
