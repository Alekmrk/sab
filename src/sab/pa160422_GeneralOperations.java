package sab;

import rs.etf.sab.operations.GeneralOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class pa160422_GeneralOperations implements GeneralOperations {
    @Override
    public void eraseAll() {
        Connection connection = DB.getInstance().getConnection();
        //TODO: ovo izgleda moraju da se dodaju procedure, ne moze samo ovako da se obrise baza... (valjda, not sure)
        // ovo pogledaj ko procedure i store operation u ssms-u
        try (CallableStatement callableStatement = connection.prepareCall("EXEC EraseAll")){

            callableStatement.execute();
            pa160422_CourierRequestOperation.mapaZahteva=new HashMap<>();
            pa160422_VehicleOperations.parkiranaVozila= new HashMap<>();
        } catch (SQLException e) {

        }
    }
}