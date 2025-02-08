package dataaccess.dao;

import java.util.List;
import java.util.Optional;

public interface BaseDAO<T, U> {
    void save(T entity);
    Optional<T> findById(U id);
    List<T> findAll();
    void update(T entity);
    void delete(U id);
}
