package com.olegfilimonov.fakecasino;

import java.sql.*;

/**
 * SQL Connector
 *
 * @author Oleg Filimonov
 */

public class SQLDatabaseConnection {

    /**
     * Real connection string is hidden from VCS for obvious reasons
     */
    private static final String CONNECTION_STRING = Hidden.CONNECTION_STRING;

    public static boolean updateUserMoney(int id, int balance){
        String request = "UPDATE db_accessadmin.casino_data" +
                " SET balance=" + String.valueOf(balance) +
                " WHERE user_id =" + String.valueOf(id);

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Declare the JDBC objects.
        Connection connection = null;
        ResultSet resultSet = null;
        PreparedStatement prepsInsertProduct = null;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);


            prepsInsertProduct = connection.prepareStatement(
                    request,
                    Statement.RETURN_GENERATED_KEYS);
            prepsInsertProduct.execute();

            // Retrieve the generated key from the insert.
            resultSet = prepsInsertProduct.getGeneratedKeys();

            // Print the ID of the inserted row.
            while (resultSet.next()) {
                System.out.println("Generated: " + resultSet.getString(1));
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the connections after the data has been handled.
            if (prepsInsertProduct != null) try {
                prepsInsertProduct.close();
            } catch (Exception ignored) {
            }
            if (resultSet != null) try {
                resultSet.close();
            } catch (Exception ignored) {
            }
            if (connection != null) try {
                connection.close();
            } catch (Exception ignored) {
            }
        }

        return false;

    }

    public static int checkUser(String username, String passwrod) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        int res = -1;

        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);

            // Create and execute a SELECT SQL statement.
            String selectSql = "SELECT ALL user_id,username,password FROM db_accessadmin.users";
            statement = connection.createStatement();
            resultSet = statement.executeQuery(selectSql);

            // Print results from select statement
            while (resultSet.next()) {
                if (resultSet.getString("username").equals(username)
                        && resultSet.getString("password").equals(passwrod))
                    res = resultSet.getInt("user_id");
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

        return res;
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