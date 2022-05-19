package com.bips.reserve.repository.custom;

import com.bips.reserve.domain.entity.Btable;

public interface BtableCustomRepository {

    Btable findBtable(Long brestId, String btableName);

    Btable findBtableDisabled(Long brestId, String btableName);
}