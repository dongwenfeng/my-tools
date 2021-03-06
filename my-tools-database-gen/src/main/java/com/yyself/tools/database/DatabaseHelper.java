package com.yyself.tools.database;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.io.StringReader;
import java.util.List;

/**
 * @Author yangyu
 * @create 2020/5/20 上午10:19
 */
public class DatabaseHelper {


    public static String formatSql(String sql) {
        return sql.replaceAll("'", "`")
                .replaceAll("\"", "`")
                .replaceAll("USING BTREE", "");
    }

    public static String tableName(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        CreateTable stmt = (CreateTable) parser.parse(new StringReader(formatSql(sql)));
        return stmt.getTable().getName().replaceAll("`", "");
    }

    public static String primaryKey(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        CreateTable stmt = (CreateTable) parser.parse(new StringReader(formatSql(sql)));
        return stmt.getIndexes().stream().filter(index -> "PRIMARY KEY".equals(index.getType())).map(index -> index.getColumnsNames().stream().findFirst().orElse("")).findFirst().orElse("").replaceAll("`", "");
    }

    public static List<Index> indexs(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        CreateTable stmt = (CreateTable) parser.parse(new StringReader(formatSql(sql)));
        return stmt.getIndexes();
    }

    public static List<ColumnDefinition> columnList(String sql) throws JSQLParserException {
        CCJSqlParserManager parser = new CCJSqlParserManager();
        Statement stmt = parser.parse(new StringReader(formatSql(sql)));
        return ((CreateTable) stmt).getColumnDefinitions();
    }

    public static String comment(ColumnDefinition columnDefinition) {
        List<String> columnSpecStrings = columnDefinition.getColumnSpecStrings();
        if (!columnSpecStrings.contains("COMMENT")) {
            return "";
        }
        String comment = columnSpecStrings.get(columnSpecStrings.size() - 1);
        return ((comment == null || "NULL".equals(comment.toUpperCase())) ? "" : comment).replaceAll("`", "");
    }


}
