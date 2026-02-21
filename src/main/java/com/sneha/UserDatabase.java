package com.sneha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserDatabase {

    private final Connection connection;

    UserDatabase(){
        String jdbcURL = "jdbc:postgresql://localhost:5432/userService";
        String username = "sneha";
        String password = "password";

        // Establish the connection
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(jdbcURL, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    void add(String name, String email, String id, long createdat, long updatedat){
        try {
            String insertStatement = String.format(
                    "Insert into users (id, name, email, createdat, updatedat) values ('%s', '%s','%s','%d','%d')",
                    id,
                    name,
                    email,
                    createdat,
                    updatedat
            );

            Statement statement = connection.createStatement();
            statement.execute(insertStatement);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

     boolean validateUserByEmail(String email){
         String queryStatement = String.format("Select * from users where email = '%s'",
                 email);
         try {
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(queryStatement);
             return resultSet.next();

         } catch (SQLException e) {
             throw new RuntimeException(e);
         }
    }

    boolean validateUserById(String id){
        String queryStatement = String.format("Select * from users where id = '%s'",
                id);
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryStatement);
            return resultSet.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    List<User> getUsers(){
        List<User> result = new ArrayList<>();
        try{
            String selectStatement = String.format("Select * from userservice");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectStatement);

            while(resultSet.next()){
                result.add(
                        new User(
                                resultSet.getString("id"),
                                resultSet.getString("name"),
                                resultSet.getString("email")
                        )
                );


            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
