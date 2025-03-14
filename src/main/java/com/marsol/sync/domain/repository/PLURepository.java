package com.marsol.sync.domain.repository;

import com.marsol.sync.domain.model.Scale;

import java.util.List;

/**
 * Interfaz del repositorio en el dominio, definen metodos de acceso a datos desde la perspectiva del dominio
 */
public interface PLURepository {
    public List<String> getAllArticles(Scale scale);
    public List<Integer> getDisabledArticles(Scale scale);
    public List<Integer> getEnabledArticles(Scale scale);
}
