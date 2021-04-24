package com.crmdemo.crm.workbench.dao;

import com.crmdemo.crm.workbench.domain.Clue;

import java.util.List;

public interface ClueDao {

     int delete(String clueId);

    int saveClue(Clue c );

    Clue getDetail(String id);

    Clue getClueById(String clueId);
}
