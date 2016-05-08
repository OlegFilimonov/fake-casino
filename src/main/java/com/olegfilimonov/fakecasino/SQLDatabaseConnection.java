package com.olegfilimonov.fakecasino;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class SQLDatabaseConnection {

    // Connect to your database.
    // Replace server name, username, and password with your credentials
    public static boolean checkUser(String username, String passwrod) {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String connectionString =
                "jdbc:sqlserver://filimonov.database.windows.net:1433;" +
                        "database=maindb;" +
                        "user=olegfilimonov@filimonov;" +
                        "password=mrtester;" +
                        "encrypt=true;" +
                        "trustServerCertificate=false;" +
                        "hostNameInCertificate=*.database.windows.net;" +
                        "loginTimeout=30;";

        // Declare the JDBC objects.
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        boolean valid = false;

        try {
            connection = DriverManager.getConnection(connectionString);

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

}