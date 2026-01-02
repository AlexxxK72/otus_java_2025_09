package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record EntitySQLMetaDataImpl<T>(EntityClassMetaData<T> entityClassMetaData) implements EntitySQLMetaData<T> {

    @Override
    public String getSelectAllSql() {
        return "select * from %s".formatted(entityClassMetaData.getName());
    }

    @Override
    public String getSelectByIdSql() {
        return "select * from %s where %s = ?"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getIdField().getName());
    }

    @Override
    public String getInsertSql() {
        return "insert into %s(%s) values(%s)"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(Field::getName)
                                .collect(Collectors.joining(",")),
                        Stream.generate(() -> "?")
                                .limit(entityClassMetaData.getFieldsWithoutId().size())
                                .collect(Collectors.joining(",")));
    }

    @Override
    public String getUpdateSql() {
        return "update %s set %s where %s = ?"
                .formatted(
                        entityClassMetaData.getName(),
                        entityClassMetaData.getFieldsWithoutId().stream()
                                .map(field -> field.getName() + " = ?")
                                .collect(Collectors.joining(",")),
                        entityClassMetaData.getIdField().getName());
    }
}
