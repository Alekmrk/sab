package sab;

import rs.etf.sab.operations.StockroomOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class pa160422_StockroomOperations implements StockroomOperations {
    @Override
    public int insertStockroom(int adresa) {// TODO: proveri ako dve razlicite adrese imaju isti grad
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * from magacini where id_adresa = ?";
        // ovo ne mora, zabranili smo u bazi da se dupliraju adrese
        /*try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, adresa);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                // vec postoji magacin u tom gradu
                return -1;
            }
        }catch  (Exception e) {
            //e.printStackTrace();//mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return -1;
        }*/

        sqlQuery = "INSERT INTO magacini VALUES( ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, adresa);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            //e.printStackTrace();
            return -1;
        }
    }

        @Override
    public boolean deleteStockroom(int i) {
        return false;
    }

    @Override
    public int deleteStockroomFromCity(int i) {
        return 0;
    }

    @Override
    public List<Integer> getAllStockrooms() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM magacini";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt(1));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
