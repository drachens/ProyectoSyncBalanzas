package com.marsol.sync.domain.repository;

import com.marsol.sync.domain.model.Scale;

import java.util.List;

public interface ScaleRepository {
    List<Scale> findEnabledScales();
    void setUpdateLastupdateScale(Scale scale, boolean isSuccess, String message);
    void disabledMassive(Scale scale);
}
