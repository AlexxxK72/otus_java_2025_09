package ru.otus.jdbc.mapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.repository.DataTemplateException;
import ru.otus.core.repository.executor.DbExecutor;

/** Сохраняет объект в базу, читает объект из базы */
@SuppressWarnings("java:S1068")
public class DataTemplateJdbc<T> implements DataTemplate<T> {

    private final DbExecutor dbExecutor;
    private final EntitySQLMetaData<T> entitySQLMetaData;

    public DataTemplateJdbc(DbExecutor dbExecutor, EntitySQLMetaData<T> entitySQLMetaData) {
        this.dbExecutor = dbExecutor;
        this.entitySQLMetaData = entitySQLMetaData;
    }

    @Override
    public Optional<T> findById(Connection connection, long id) {
        return dbExecutor.executeSelect(connection, entitySQLMetaData.getSelectByIdSql(), List.of(id), rs -> {
            try {
                if (!rs.next()) {
                    return null;
                }
                return getEntity(rs);
            } catch (SQLException | ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public List<T> findAll(Connection connection) {
        return dbExecutor
                .executeSelect(connection, entitySQLMetaData.getSelectAllSql(), Collections.emptyList(), rs -> {
                    var entityList = new ArrayList<T>();
                    try {
                        while (rs.next()) {
                            entityList.add(getEntity(rs));
                        }
                        return entityList;
                    } catch (SQLException | ReflectiveOperationException e) {
                        throw new DataTemplateException(e);
                    }
                })
                .orElseThrow(() -> new RuntimeException("Unexpected error"));
    }

    @Override
    public long insert(Connection connection, T instance) {
        try {
            return dbExecutor.executeStatement(connection, entitySQLMetaData.getInsertSql(), getFieldParams(instance));
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    @Override
    public void update(Connection connection, T client) {
        try {
            var fieldParams = getFieldParams(client);
            fieldParams.add(getIdValue(client));
            dbExecutor.executeStatement(connection, entitySQLMetaData.getUpdateSql(), fieldParams);
        } catch (Exception e) {
            throw new DataTemplateException(e);
        }
    }

    private List<Object> getFieldParams(T instance) {
        return entitySQLMetaData.entityClassMetaData().getFieldsWithoutId().stream()
                .map(field -> {
                    try {
                        return field.get(instance);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access field " + field.getName(), e);
                    }
                })
                .collect(Collectors.toList());
    }

    private Object getIdValue(T instance) {
        var fieldId = entitySQLMetaData.entityClassMetaData().getIdField();
        try {
            return fieldId.get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Cannot access fieldId " + fieldId.getName(), e);
        }
    }

    private T getEntity(ResultSet rs) throws SQLException, ReflectiveOperationException {
        Constructor<T> ctor = entitySQLMetaData.entityClassMetaData().getConstructor();
        T instance = ctor.newInstance();

        for (Field field : entitySQLMetaData.entityClassMetaData().getAllFields()) {
            Object value = rs.getObject(field.getName());
            field.set(instance, value);
        }
        return instance;
    }
}
