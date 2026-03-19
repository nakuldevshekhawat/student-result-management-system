package com.srms.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD contract for all repository classes.
 * @param <T>  the entity type
 * @param <ID> the primary key type
 */
public interface Repository<T, ID> {

    void        save(T entity)   throws SQLException;
    void        update(T entity) throws SQLException;
    void        delete(ID id)    throws SQLException;
    Optional<T> findById(ID id)  throws SQLException;
    List<T>     findAll()        throws SQLException;
}
