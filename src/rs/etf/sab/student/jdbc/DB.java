package rs.etf.sab.student.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DB {

    private final static String username = "sa";
    private final static String password = "123";
    private final static String database = "sab_baza";
    private final static String serverName = "localhost";
    private final static int port = 1433;

    /*String url = "jdbc:sqlserver://<Computer Name>\\SQLFULL:1433;databaseName=BA_ELTRUN;";

        Connection conn = DriverManager.getConnection(url,"<user>","<password>"):*/

    private final static String connectionString = "jdbc:sqlserver://" + serverName + ":" + port + ";"
            + "database=" + database
            + ";user=" + username
            + ";password=" + password;

    private static DB db = null;

    private Connection connection;

    private DB(){
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(connectionString);
        }
        catch(SQLException | ClassNotFoundException ex){
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static DB getInstance(){
        if (db == null){
            db = new DB();
        }
        return db;
    }


    public Connection getConnection(){
        return connection;
    }
}
