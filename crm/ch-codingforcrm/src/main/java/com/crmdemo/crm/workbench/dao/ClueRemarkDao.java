package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getClueRemarkByClueId(String clueId);

    int delete(ClueRemark clueRemark);
}
