package com.example.practicerestfulwebservice.util;

import com.example.practicerestfulwebservice.annotation.Column;
import com.example.practicerestfulwebservice.annotation.ForeignKey;
import com.example.practicerestfulwebservice.annotation.Id;
import com.example.practicerestfulwebservice.annotation.Table;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

public class MigrationJava {
    public static final String scanRange = "com.example.practicerestfulwebservice";

    public static void main(String[] args) {
        for (Class<?> table : listAnnotated(scanRange)
        ) {
            CreateTableFromEntity(table);
        }
    }

    public static Set<Class<?>> listAnnotated(String scanRange) {
        // Quét trong package của project
        Reflections reflections = new Reflections(scanRange);

        // Lấy ra tất cả class được đặt là annotation là @Table
        return reflections.getTypesAnnotatedWith(Table.class);
    }

    public static void CreateTableFromEntity(Class clazz) {
        // Check class có phải được đặt là annotation này không
        if (!clazz.isAnnotationPresent(Table.class)) {
            return;
        }

        StringBuilder createStringBuilder = new StringBuilder();
        createStringBuilder.append(SQLConfig.CREATE_TABLE);
        createStringBuilder.append(SQLConfig.SPACE);

        String tableName = clazz.getSimpleName().toLowerCase() + "s";
        Table table = (Table) clazz.getDeclaredAnnotation(Table.class);
        if (table.name().length() > 0) {
            tableName = table.name();
        }

        if (checkExitsTable(tableName)) {
            return;
        }

        createStringBuilder.append(tableName);
        createStringBuilder.append(SQLConfig.SPACE);
        createStringBuilder.append(SQLConfig.OPEN_BRACKET);

        // Lấy ra các trường của class
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields
        ) {
            String columnName;
            String columnType;

            if (field.isAnnotationPresent(Column.class)) {
                Column column = field.getDeclaredAnnotation(Column.class);
                columnName = column.name();
                columnType = column.type();

                createStringBuilder.append(columnName);
                createStringBuilder.append(SQLConfig.SPACE);
                createStringBuilder.append(columnType);

                if (field.isAnnotationPresent(Id.class)) {
                    Id id = field.getDeclaredAnnotation(Id.class);
                    createStringBuilder.append(SQLConfig.SPACE);
                    createStringBuilder.append(SQLConfig.PRIMARY_KEY);
                    if (id.autoIncrement()) {
                        createStringBuilder.append(SQLConfig.SPACE);
                        createStringBuilder.append(SQLConfig.AUTO_INCREMENT);
                    }
                }

                if (field.isAnnotationPresent(ForeignKey.class)) {
                    ForeignKey foreignKey = field.getDeclaredAnnotation(ForeignKey.class);
                    if (!checkExitsTable(foreignKey.referenceTable())) {
                        for (Class<?> entityTable : listAnnotated(scanRange)
                        ) {
                            Table entityTableName = entityTable.getDeclaredAnnotation(Table.class);
                            String entityGetName;
                            if (entityTableName.name().length() > 0) {
                                entityGetName = entityTableName.name();
                            } else {
                                entityGetName = entityTable.getSimpleName().toLowerCase() + "s";
                            }

                            if (entityGetName.equals(foreignKey.referenceTable())) {
                                CreateTableFromEntity(entityTable);
                            }
                        }
                    }
                    createStringBuilder.append(SQLConfig.COMMA);
                    createStringBuilder.append(SQLConfig.FOREIGN_KEY);
                    createStringBuilder.append(SQLConfig.SPACE);
                    createStringBuilder.append(SQLConfig.OPEN_BRACKET);
                    createStringBuilder.append(columnName);
                    createStringBuilder.append(SQLConfig.CLOSE_BRACKET);
                    createStringBuilder.append(SQLConfig.SPACE);
                    createStringBuilder.append(SQLConfig.REFERENCES);
                    createStringBuilder.append(SQLConfig.SPACE);
                    createStringBuilder.append(foreignKey.referenceTable());
                    createStringBuilder.append(SQLConfig.OPEN_BRACKET);
                    createStringBuilder.append(foreignKey.referenceColumn());
                    createStringBuilder.append(SQLConfig.CLOSE_BRACKET);
                }

                createStringBuilder.append(SQLConfig.COMMA);
            }
        }

        createStringBuilder.setLength(createStringBuilder.length() - 1);
        createStringBuilder.append(SQLConfig.CLOSE_BRACKET);
        System.out.println(createStringBuilder.toString());

        try {
            Connection connection = ConnectionHelper.getConnection();
            Statement stt = connection.createStatement();
            stt.execute(createStringBuilder.toString());
            System.out.println(String.format("Create table %s success!", tableName));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public static boolean checkExitsTable(String tableName) {
        StringBuilder sqlStringBuilder = new StringBuilder();
        sqlStringBuilder.append(SQLConfig.SELECT);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.COUNT);
        sqlStringBuilder.append(SQLConfig.OPEN_BRACKET);
        sqlStringBuilder.append(SQLConfig.STAR);
        sqlStringBuilder.append(SQLConfig.CLOSE_BRACKET);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.FROM);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.INFORMATION_SCHEMA);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.WHERE);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.TABLE_SCHEMA);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.EQUAL_SIGN);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.PARENTHESIS);
        sqlStringBuilder.append(Config.DATABASE_NAME);
        sqlStringBuilder.append(SQLConfig.PARENTHESIS);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.AND);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.TABLE_NAME);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.EQUAL_SIGN);
        sqlStringBuilder.append(SQLConfig.SPACE);
        sqlStringBuilder.append(SQLConfig.PARENTHESIS);
        sqlStringBuilder.append(tableName);
        sqlStringBuilder.append(SQLConfig.PARENTHESIS);

        try {
            Connection connection = ConnectionHelper.getConnection();
            Statement stt = connection.createStatement();
            ResultSet resultSet = stt.executeQuery(sqlStringBuilder.toString());
            if (resultSet.next()) {
                if (resultSet.getInt(1) > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return false;
    }
}
