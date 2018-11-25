package com.mariobgr.falcon.dao;

import java.util.List;
import java.util.Map;

public interface GenericDao<T> {

    Map<String, Object> get(int id);
    List<Map<String, Object>> getAll(int page);
    int save(T t);
    void update(T t);
    void delete(T t);

}
