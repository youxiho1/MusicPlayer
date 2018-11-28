package com.yang.util;

import com.yang.model.Music;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * @author riddleli
 * This util exposes methods to manage a SQLite database.
 *
 * <p>
 * SQLiteDatabase has methods to create, delete, query and
 * execute other SQL commands.
 * </p><p>
 * Database names must be unique within an application, not across all applications.
 * </p>
 */
public class SQLiteDatabase{
    private String databaseName;
    private static final int CONFLICT_NONE = 0;
    private static final int CONFLICT_REPLACE = 1;
    private static final String[] CONFLICT_VALUES = new String[]
            {"", " OR REPLACE ", " OR ABORT ", " OR FAIL ", " OR IGNORE ", " OR ROLLBACK "};

    /**
     * Create a database named as databaseName if that database does not exist.
     * @param databaseName The database's name you want to create or open
     */
    public SQLiteDatabase(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * A convenient (also the most simple) method to insert a row into the database.
     * Not a standard SQLiteDatabase method (as it's too simple) but maybe useful
     * @param table the name of the table you want to insert the row into
     * @param values indeed a hashmap, contains column names and column values
     *               as key-value pairs
     * @return the row Id of the newly inserted row or -1 if an error occured
     */
    public int insert(String table, ContentValues values) {
        try {
            return insertWithOnConflict(table, null, values, CONFLICT_NONE);
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        }
    }

    /**
     * The standard method to insert a row into the database.
     * @param table the name of the table you want to insert the row into
     * @param nullColumnHack optional; may be <code>null</code>.
     *            SQL doesn't allow inserting a completely empty row without
     *            naming at least one column name.  If your provided <code>values</code> is
     *            empty, no column names are known and an empty row can't be inserted.
     *            If not set to null, the <code>nullColumnHack</code> parameter
     *            provides the name of nullable column name to explicitly insert a NULL into
     *            in the case where your <code>values</code> is empty.
     * @param values indeed a hashmap, contains column names and column values
     *                      as key-value pairs
     * @return the row Id of the newly inserted row or -1 if an error occured
     */
    public int insert(String table, String nullColumnHack, ContentValues values) {
        try {
            return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        }
    }

    /**
     * The standard method to insert a row into the database.
     * @param table the name of the table you want to insert the row into
     * @param nullColumnHack optional; may be <code>null</code>.
     *                       SQL doesn't allow inserting a completely empty row without
     *                       naming at least one column name.  If your provided <code>values</code> is
     *                       empty, no column names are known and an empty row can't be inserted.
     *                       If not set to null, the <code>nullColumnHack</code> parameter
     *                       provides the name of nullable column name to explicitly insert a NULL into
     *                       in the case where your <code>values</code> is empty.
     * @param values indeed a hashmap, contains column names and column values
     *               as key-value pairs
     * @throws SQLException
     * @return the row Id of the newly inserted row or -1 if an error occured
     */
    public int insertOrThrow(String table, String nullColumnHack, ContentValues values)
            throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, values, CONFLICT_NONE);
    }

    /**
     * A convinent method for replacing a row in the database.
     * Insert the row into the databse if that row does not already exist(Judged by the primary key).
     * @param table the name of the table in which to replace the row you want
     * @param nullColumnHack optional; may be <code>null</code>.
     *                       SQL doesn't allow inserting a completely empty row without
     *                       naming at least one column name.  If your provided <code>values</code> is
     *                       empty, no column names are known and an empty row can't be inserted.
     *                       If not set to null, the <code>nullColumnHack</code> parameter
     *                       provides the name of nullable column name to explicitly insert a NULL into
     *                       in the case where your <code>values</code> is empty.
     * @param initialValues indeed a hashmap, contains column names and column values
     *                      as key-value pairs
     * @return the row Id of the newly inserted row or -1 if an error occured
     */
    public int replace(String table, String nullColumnHack, ContentValues initialValues) {
        try {
            return insertWithOnConflict(table, nullColumnHack, initialValues, CONFLICT_REPLACE);
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        }
    }

    /**
     * A convinent method for replacing a row in the database.
     * Insert the row into the databse if that row does not already exist(Judged by the primary key).
     * @param table the name of the table in which to replace the row you want
     * @param nullColumnHack optional; may be <code>null</code>.
     *                       SQL doesn't allow inserting a completely empty row without
     *                       naming at least one column name.  If your provided <code>values</code> is
     *                       empty, no column names are known and an empty row can't be inserted.
     *                       If not set to null, the <code>nullColumnHack</code> parameter
     *                       provides the name of nullable column name to explicitly insert a NULL into
     *                       in the case where your <code>values</code> is empty.
     * @param initialValues indeed a hashmap, contains column names and column values
     *                      as key-value pairs
     * @throws SQLException
     * @return the row Id of the newly inserted row or -1 if an error occured
     */
    public int replaceOrThrow(String table, String nullColumnHack, ContentValues initialValues)
            throws SQLException {
        return insertWithOnConflict(table, nullColumnHack, initialValues, CONFLICT_REPLACE);
    }

    //The private inner method to deal with all kind of insertings
    //This method could also be exposed if one wants to use INSET OR IGNORE, INSERT OR ABORT etc.
    private int insertWithOnConflict(String table, String nullColumnHack, ContentValues initialValues, int conflictAlgorithm) throws SQLException {
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            //通过StringBuilder构建sql语句
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(" INTO ");
            sql.append(table);
            sql.append('(');

            Object[] bindArgs = null;
            int size = (initialValues != null && !initialValues.isEmpty()) ? initialValues.size() : 0;
            if (size > 0) {
                bindArgs = new Object[size];
                int i = 0;
                for (String colName : initialValues.keySet()) {
                    sql.append((i > 0) ? "," : "");
                    sql.append(colName);
                    bindArgs[i++] = initialValues.get(colName);
                }
                sql.append(')');
                sql.append(" VALUES (");
                for (i = 0; i < size; i++) {
                    sql.append((i > 0) ? ",?" : "?");
                }
            }
            else {
                sql.append(nullColumnHack + ") VALUES (NULL");
            }
            sql.append(')');

            connection = getConn();
            pst = connection.prepareStatement(sql.toString());
            if(size > 0) {
                for(int i = 1; i <= bindArgs.length; i++) {
                    pst.setObject(i, bindArgs[i - 1]);
                }
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            logSqlException(e);
            throw e;
        } finally {
            try {
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * The standard method used to delete rows in the database.
     * @param table the name of the table you want to delete from
     * @param whereClause the optional clause to apply when deleting
     *                    Passing null will delete all rows.
     * @param whereArgs if you include ?(s) in your whereClause, you should
     *                  give those values in the whereArgs. These values will
     *                  be bound as strings.
     * @return the number of rows affected if a whereclause is passed in,
     * 0 otherwise. If you want to delete all rows in the table and you
     * also want to know how many rows are affected, pass 1 as the whereclause.
     */
    public int delete(String table, String whereClause, String[] whereArgs) {
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            String sql = "DELETE FROM " + table;
            if(whereClause != null && whereClause.length() != 0) {
                sql += " WHERE " + whereClause;
            }

            connection = getConn();
            pst = connection.prepareStatement(sql);

            int length = (whereArgs != null) ? whereArgs.length : 0;
            for(int i = 1; i <= length; i++) {
                pst.setString(i, whereArgs[i - 1]);
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        } finally {
            try {
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * The standard method to update rows in the database.
     * @param table the name of the table which the rows you want to update are in
     * @param values a special map containing column names and new values
     * @param whereClause the optional clause to apply when updating
     *                    Passing null will update all rows.
     * @param whereArgs if you include ?(s) in your whereClause, you should
     *                  give those values in the whereArgs. These values will
     *                  be bound as strings.
     * @return the number of rows affected
     */
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        return updateWithOnConflict(table, values, whereClause, whereArgs, CONFLICT_NONE);
    }

    //the inner method to deal with updating
    private int updateWithOnConflict(String table, ContentValues values, String whereClause, String[] whereArgs, int conflictAlgorithm) {
        if (values == null || values.isEmpty()) {
            throw new IllegalArgumentException("Empty values");
        }

        Connection connection = null;
        PreparedStatement pst = null;
        try {
            StringBuilder sql = new StringBuilder(120);
            sql.append("UPDATE ");
            sql.append(CONFLICT_VALUES[conflictAlgorithm]);
            sql.append(table);
            sql.append(" SET ");

            //Move all bind args to one array
            int setValuesSize = values.size();
            int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
            Object[] bindArgs = new Object[bindArgsSize];
            int i = 0;
            for (String colName : values.keySet()) {
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = values.get(colName);
                sql.append("=?");
            }
            if (whereArgs != null) {
                for (i = setValuesSize; i < bindArgsSize; i++) {
                    bindArgs[i] = whereArgs[i - setValuesSize];
                }
            }
            if (whereClause != null && whereClause.length() != 0) {
                sql.append(" WHERE ");
                sql.append(whereClause);
            }

            connection = getConn();
            pst = connection.prepareStatement(sql.toString());
            for(i = 1; i <= bindArgsSize; i++) {
                pst.setObject(i, bindArgs[i - 1]);
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        } finally {
            try {
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * Runs the sql you passed and returns a list over the result set.
     * This method has no selectionArgs.
     * @param clazz Bean.class
     * @param sql the sql query you want to run
     * @return a list over the result set.
     */
    //rawQuery without selectionArgs
    public <T> List<T> rawQuery(Class<T> clazz, String sql) {
        return rawQuery(clazz, sql, null);
    }

    /**
     * Runs the sql you passed and returns a list over the result set.
     * @param clazz Bean.class
     * @param sql the sql query you want to run
     * @return a list over the result set.
     */
    //rawQuery with selectionArgs
    public <T> List<T> rawQuery(Class<T> clazz, String sql, String[] selectionArgs) {
        //Tired and sleepy……SO THIS FUNCTION HAS ALMOST NO CHECKING
        //Caution: Resultset needs to be closed
        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet resultSet = null;
        try {
            connection = getConn();
            pst = connection.prepareStatement(sql);
            int length = (selectionArgs != null) ? selectionArgs.length : 0;
            for(int i = 1; i <= length; i++) {
                pst.setString(i, selectionArgs[i - 1]);
            }
            resultSet = pst.executeQuery();
            return resultToList(resultSet, clazz);
        } catch (SQLException e) {
            logSqlException(e);
            return null;
        } finally {
            try {
                if(resultSet != null) {
                    resultSet.close();
                }
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * The extra most simple query function I offered.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @return a list over the result set.
     */
    //Offer a simplest query function
    public <T> List<T> query(Class<T> clazz, String table, String[] columns, String selection, String[] selectionArgs) {
        return query(clazz, false, table, columns, selection, selectionArgs, null, null, null, null);
    }

    /**
     * A simple query function with DISTINCT.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @return a list over the result set.
     */
    //Simplest query function with DISTINCT
    public <T> List<T> query(Class<T> clazz, Boolean distinct, String table, String[] columns, String selection, String[] selectionArgs) {
        return query(clazz, distinct, table, columns, selection, selectionArgs, null, null, null, null);
    }

    /**
     * Classic query function with 7 parameters and a clazz.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @return a list over the result set.
     */
    public <T> List<T> query(Class<T> clazz, String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy,
                           String having, String orderBy) {
        return query(clazz, false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    /**
     * Classic query function with DISTINCT.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @return a list over the result set.
     */
    public <T> List<T> query(Class<T> clazz, Boolean distinct, String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy,
                           String having, String orderBy) {
        return query(clazz, distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    /**
     * The query method with many parameters but without distinct.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return a list over the result set.
     */
    //A query function with 8 parameters emmmmmmm
    //I don't think we need this function indeed
    public <T> List<T> query(Class<T> clazz, String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit) {
        return query(clazz, false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    /**
     * The query method with most parameters.
     * Query the given table, returning a list over the result set.
     * @param clazz Bean.class.
     * @param distinct true if you want each row to be unique, false otherwise.
     * @param table The table name to compile the query against.
     * @param columns A list of which columns to return. Passing null will
     *            return all columns, which is discouraged to prevent reading
     *            data from storage that isn't going to be used.
     * @param selection A filter declaring which rows to return, formatted as an
     *            SQL WHERE clause (excluding the WHERE itself). Passing null
     *            will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *         replaced by the values from selectionArgs, in order that they
     *         appear in the selection. The values will be bound as Strings.
     * @param groupBy A filter declaring how to group rows, formatted as an SQL
     *            GROUP BY clause (excluding the GROUP BY itself). Passing null
     *            will cause the rows to not be grouped.
     * @param having A filter declare which row groups to include in the cursor,
     *            if row grouping is being used, formatted as an SQL HAVING
     *            clause (excluding the HAVING itself). Passing null will cause
     *            all row groups to be included, and is required when row
     *            grouping is not being used.
     * @param orderBy How to order the rows, formatted as an SQL ORDER BY clause
     *            (excluding the ORDER BY itself). Passing null will use the
     *            default sort order, which may be unordered.
     * @param limit Limits the number of rows returned by the query,
     *            formatted as LIMIT clause. Passing null denotes no LIMIT clause.
     * @return a list over the result set.
     */
    //The most …… query function
    public <T> List<T> query(Class<T> clazz, Boolean distinct, String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy,
                           String having, String orderBy, String limit) {
        String sql = buildQuerySql(distinct, table, columns, selection,
                groupBy, having, orderBy, limit);
        return rawQuery(clazz, sql, selectionArgs);
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT
     * or any other SQL statement that returns data.
     * It has no means to return any data (such as the number of affected rows).
     * Instead, you're encouraged to use {@link #insert(String, String, ContentValues)},
     * {@link #update(String, ContentValues, String, String[])}, et al, when possible.
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are
     * not supported.
     */
    public void execSQL(String sql){
        rawSql(sql, null);
    }

    /**
     * Execute a single SQL statement that is NOT a SELECT
     * or any other SQL statement that returns data.
     * It has no means to return any data (such as the number of affected rows).
     * Instead, you're encouraged to use {@link #insert(String, String, ContentValues)},
     * {@link #update(String, ContentValues, String, String[])}, et al, when possible.
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are
     * @param bindArgs the values used to replace ?(s) in the sql statement(I do no checking about it)
     */
    public void execSQL(String sql, Object[] bindArgs){
        if (bindArgs == null) {
            throw new IllegalArgumentException("Empty bindArgs");
        }
        rawSql(sql, bindArgs);
    }

    /**
     * Execute SQL statements that are NOT SELECT
     * or any other SQL statement that returns data.
     * It has no means to return any data (such as the number of affected rows).
     * Instead, you're encouraged to use {@link #insert(String, String, ContentValues)},
     * {@link #update(String, ContentValues, String, String[])}, et al, when possible.
     * @param sql the SQL statement to be executed. Multiple statements separated by semicolons are
     * @param bindArgs the values used to replace ?(s) in the sql statements(I do no checking about it)
     */
    //You can use this function to insert/update/delete but not query!
    public void execSQLs(String[] sql, Object[][] bindArgs) {
        //Without Checking, Maybe DANGEROUS???
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = getConn();
            for(int i = 0; i < sql.length; i++) {
                pst = connection.prepareStatement(sql[i]);
                int length = (bindArgs[i] != null) ? bindArgs[i].length : 0;
                for(int j = 1; j <= length; j++) {
                    pst.setObject(j, bindArgs[i][j - 1]);
                }
                pst.executeUpdate();
            }
        } catch (SQLException e) {
            logSqlException(e);
        } finally {
            try {
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    //the inner method to deal with raw sql
    private int rawSql(String sql, Object[] bindArgs){
        Connection connection = null;
        PreparedStatement pst = null;
        try {
            connection = getConn();
            pst = connection.prepareStatement(sql);
            int length = (bindArgs != null) ? bindArgs.length : 0;
            for(int i = 1; i <= length; i++) {
                pst.setObject(i, bindArgs[i - 1]);
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            logSqlException(e);
            return -1;
        } finally {
            try {
                if(pst != null) {
                    pst.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * Execute a single SQL statement used to create table or alter table.
     * It has no means to return any data (such as the number of affected rows).
     * So it can't be able to be used to select.
     * Not recommended to use this method to insert/update/delete.
     * Instead, you're encouraged to use {@link #execSQL(String)}, or {@link #execSQL(String, Object[])}.
     * @param sql the SQL statement to be executed.
     */
    public void executeSQL(String sql) {
        Connection connection = null;
        Statement st = null;
        try {
            connection = getConn();
            st = connection.createStatement();
            st.execute(sql);
        } catch (SQLException e) {
            logSqlException(e);
        } finally {
            try {
                if(st != null) {
                    st.close();
                }
                if(connection != null) {
                    connection.close();
                }
            } catch(SQLException e) {
                logSqlException(e);
            }
        }
    }

    /**
     * get the connection of the sqlite.
     * @return The sqlite's connection.
     */
    public Connection getConn() {
        Connection connection = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:" + databaseName;
            connection = DriverManager.getConnection(url);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void logSqlException(SQLException e) {
        Logger lgr = Logger.getLogger(SQLiteDatabase.class.getName());
        lgr.log(Level.SEVERE, e.getMessage(), e);
    }

    //将形参转化为sql查询语句
    private static String buildQuerySql(Boolean distinct, String table, String[] columns,
                                        String selection, String groupBy, String having,
                                        String orderBy, String limit) {
        if((groupBy == null || groupBy.length() == 0) && (having != null && having.length() != 0)) {
            throw new IllegalArgumentException(
                    "HAVING clauses are only permitted when using a groupBy clause");
        }
        final Pattern sLimitPattern = Pattern.compile("\\s*\\d+\\s*(,\\s*\\d+\\s*)?");
        if((limit != null && limit.length() != 0) && !sLimitPattern.matcher(limit).matches()) {
            throw new IllegalArgumentException(
                    "invalid LIMIT clauses:" + limit);
        }
        StringBuilder sql = new StringBuilder(120);
        sql.append("SELECT ");
        if (distinct) {
            sql.append("DISTINCT ");
        }
        if (columns != null && columns.length != 0) {
            int size = columns.length;
            for (int i = 0; i < size; i++) {
                String column = columns[i];
                if (column != null) {
                    if (i > 0) {
                        sql.append(", ");
                    }
                    sql.append(column);
                }
            }
            sql.append(' ');
        } else {
            sql.append("* ");
        }
        sql.append("FROM ");
        sql.append(table);
        if(selection != null && selection.length() != 0) {
            sql.append(" WHERE ");
            sql.append(selection);
        }
        if(groupBy != null && groupBy.length() != 0) {
            sql.append(" GROUP BY ");
            sql.append(groupBy);
        }
        if(having != null && having.length() != 0) {
            sql.append(" HAVING ");
            sql.append(having);
        }
        if(orderBy != null && orderBy.length() != 0) {
            sql.append(" ORDER BY ");
            sql.append(orderBy);
        }
        if(limit != null && limit.length() != 0) {
            sql.append(" LIMIT ");
            sql.append(limit);
        }
        return sql.toString();
    }

    //将resultset通过泛型和反射机制转变为对应类型的list
    private static <T> List<T> resultToList(ResultSet rs, Class<T> obj) {
        try {
            ArrayList<T> arrayList = new ArrayList<T>();
            ResultSetMetaData metaData = rs.getMetaData();
            //获取总列数
            int count = metaData.getColumnCount();
            while (rs.next()) {
                //创建对象实例
                T newInstance = obj.newInstance();
                for (int i = 1; i <= count; i++) {
                    //给对象的某个属性赋值
                    String name = metaData.getColumnName(i).toLowerCase();
                    name = toJavaField(name);// 改变列名格式成java命名格式
                    String substring = name.substring(0, 1);// 首字母大写
                    String replace = name.replaceFirst(substring, substring.toUpperCase());
                    Class<?> type = obj.getDeclaredField(name).getType();// 获取字段类型
                    Method method = obj.getMethod("set" + replace, type);
                    /**
                     * 判断读取数据的类型
                     */
                    if(type.isAssignableFrom(String.class)){
                        method.invoke(newInstance, rs.getString(i));
                    }
                    else if(type.isAssignableFrom(int.class) || type.isAssignableFrom(Integer.class)){
                        method.invoke(newInstance, rs.getInt(i));
                    }
                    else if(type.isAssignableFrom(Boolean.class) || type.isAssignableFrom(boolean.class)){
                        method.invoke(newInstance, rs.getBoolean(i));
                    }
                    else if(type.isAssignableFrom(Date.class)){
                        method.invoke(newInstance, rs.getDate(i));
                    }
                    else if(type.isAssignableFrom(Double.class) || type.isAssignableFrom(double.class)) {
                        method.invoke(newInstance, rs.getDouble(i));
                    }
                }
                arrayList.add(newInstance);
            }
            return arrayList;

        } catch (InstantiationException | IllegalAccessException | SQLException | SecurityException
                | NoSuchMethodException | IllegalArgumentException | InvocationTargetException
                | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String toJavaField(String str) {
        String[] split = str.split("_");
        StringBuilder builder = new StringBuilder();
        builder.append(split[0]);// 拼接第一个字符
        // 如果数组不止一个单词
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                // 去掉下划线，首字母变为大写
                String string = split[i];
                String substring = string.substring(0, 1);
                split[i] = string.replaceFirst(substring, substring.toUpperCase());
                builder.append(split[i]);
            }
        }
        return builder.toString();
    }

    //传入Connection的？？？遇到再说？大不了再用原生的手撕……
}