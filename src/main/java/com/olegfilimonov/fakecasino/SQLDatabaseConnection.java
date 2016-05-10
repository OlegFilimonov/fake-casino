package com.olegfilimonov.fakecasino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * SQL Connector
 *
 * @author Oleg Filimonov
 */

public class SQLDatabaseConnection {

    private static final String CONNECTION_STRING =
            "jdbc:sqlserver://filimonov.database.windows.net:1433;" +
                    "database=maindb;" +
                    "user=olegfilimonov@filimonov;" +
                    "password=Action23;" +
                    "encrypt=true;" +
                    "trustServerCertificate=false;" +
                    "hostNameInCertificate=*.database.windows.net;" +
                    "loginTimeout=30;";

    public static boolean checkUser(String username, String passwrod) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        boolean valid = false;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT ALL username,password FROM db_accessadmin.users";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)
                        && resultSet.getString("password").equals(passwrod))
                    valid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            //TODO: remove. this is for testing
            valid = true;
        } finally {
            // Close the connections after the data has been handled.
            if (resultSet != null) try {
                resultSet.close();
            } catch (Exception ignored) {
            }
            if (statement != null) try {
                statement.close();
            } catch (Exception ignored) {
            }
            if (connection != null) try {
                connection.close();
            } catch (Exception ignored) {
            }
        }

        return valid;
    }

    public static int getUserMoney(String username) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT username,balance FROM db_accessadmin.users JOIN db_accessadmin.casino_data ON users.user_id = casino_data.user_id;";

            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)) {
                    return resultSet.getInt("balance");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the connections after the data has been handled.
            if (resultSet != null) try {
                resultSet.close();
            } catch (Exception ignored) {
            }
            if (statement != null) try {
                statement.close();
            } catch (Exception ignored) {
            }
            if (connection != null) try {
                connection.close();
            } catch (Exception ignored) {
            }
        }

        return -1;
    }

}