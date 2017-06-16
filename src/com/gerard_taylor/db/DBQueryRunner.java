package com.gerard_taylor.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.Scanner;

/**
 * Created by Gerard on 6/11/2017.
 */
public class DBQueryRunner {

    DatabaseConnectionHandler databaseConnectionHandler;
    Connection conn;

    public DBQueryRunner(){
        databaseConnectionHandler = new DatabaseConnectionHandler();
        conn = databaseConnectionHandler.connectToDB();
    }

    public void insertUsersIntoTable() throws FileNotFoundException{
        Scanner scanner = new Scanner(new File("A:\\Graduate School\\Enterprise Computing with Java\\Module 2\\users.txt"));
        if(conn != null){
            while(scanner.hasNextLine()){
                String lineAsString =  scanner.nextLine();
                String[] line = lineAsString.split(" ");
                String statement = "INSERT INTO STUDENT (FIRST_NAME, LAST_NAME, SSN, EMAIL, ADDRESS, USERID, PASSWORD) VALUES(?,?,?,?,?,?,?)";
                try {
                    PreparedStatement preparedStatement = conn.prepareStatement(statement);
                    for(int i = 0; i < line.length; i++){
                        preparedStatement.setString(i+1, line[i]);
                    }
                    preparedStatement.execute();
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else {
            System.out.println("CONN IS NULL");
        }
    }

    public void getTableResults(){
        try {
            Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ResultSet resultSet = statement.executeQuery("SELECT * from STUDENT");
            ResultSetMetaData metaData = resultSet.getMetaData();
            while(resultSet.next()){
                for(int i = 1; i <= metaData.getColumnCount(); i++){
                    System.out.print(String.valueOf(resultSet.getObject(i)) + " ");
                }
                System.out.println("");
            }

            System.out.println("");

            while(resultSet.previous()){
                for(int i = 1; i <= metaData.getColumnCount(); i++){
                    System.out.print(String.valueOf(resultSet.getObject(i)) + " ");
                }
                System.out.println("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        DBQueryRunner dbQueryRunner = new DBQueryRunner();
        try {
           // dbQueryRunner.insertUsersIntoTable();
            dbQueryRunner.getTableResults();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
