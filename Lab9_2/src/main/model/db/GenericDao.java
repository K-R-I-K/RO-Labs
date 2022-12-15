package main.model.db;

import java.util.List;

public interface GenericDao<T> {
    void add(T entity);

    T findById(Long id);

    List<T> findAll();

    void update(T entity);

    void delete(long id);
}
