package sab;

import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Main {

    public static void main(String[] args) {
	// write your code here
        System.out.println();
        Connection connection = DB.getInstance().getConnection();

        String sqlQuery = "INSERT INTO gradovi VALUES('Beograd', '11000')";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){

            statement.execute();

        }
        catch(Exception e){
            e.printStackTrace();
        }

        new pa160422_AddressOperation().insertAddress("ulica",1,1,2,3);

    }
}
