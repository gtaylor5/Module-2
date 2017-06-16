package com.gerard_taylor.db; /**
 * Created by Gerard on 6/11/2017.
 */

import java.sql.*;

public class DatabaseConnectionHandler {

    private Connection connection;

    public Connection connectToDB(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            connection = DriverManager.getConnection("jdbc:derby:C:\\Users\\Gerard\\Desktop\\JHU");
        } catch(Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

}
