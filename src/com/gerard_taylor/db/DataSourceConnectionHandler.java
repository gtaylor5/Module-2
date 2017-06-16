package com.gerard_taylor.db; /**
 * Created by Gerard on 6/11/2017.
 */

import java.sql.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.Hashtable;

public class DataSourceConnectionHandler {

    private Connection connection;
    public String url = "t3:\\localhost:7001";

    public static void main(String[] args) {
        DataSourceConnectionHandler connectionHandler = new DataSourceConnectionHandler();
        DataSource dataSource = null;
        Hashtable env = new Hashtable();
        // This *required* property specifies the factory to be used
        // to create the context.
        env.put(
                Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory"
        );
        env.put(Context.PROVIDER_URL, connectionHandler.url);

        try {
            Context context = new InitialContext(env);
            dataSource = (DataSource) context.lookup("jhuDataSource");
            connectionHandler.connection = dataSource.getConnection();
            Statement statement = connectionHandler.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from STUDENT");
            ResultSetMetaData metaData = resultSet.getMetaData();
            while(resultSet.next()){
                for(int i = 1; i <= metaData.getColumnCount(); i++){
                    System.out.print(String.valueOf(resultSet.getObject(i)) + " ");
                }
                System.out.println("");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}
