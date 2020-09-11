package sab;

import rs.etf.sab.operations.AddressOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class pa160422_AddressOperation implements AddressOperations {

    @Override
    public int insertAddress(String ulica, int broj, int id_grad, int x_koordinata, int y_koordinata) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO adrese VALUES( ?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, ulica);
            statement.setInt(2, broj);
            statement.setInt(3, id_grad);
            statement.setInt(4, x_koordinata);
            statement.setInt(5, y_koordinata);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    public int deleteAddresses(String ulica, int broj) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM adrese WHERE ulica = ? AND broj = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setString(1, ulica);
            statement.setInt(2, broj);
            return statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean deleteAdress(int id_adresa) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM adrese WHERE id_adresa = ? ";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, id_adresa);
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public int deleteAllAddressesFromCity(int id_grad) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM adrese WHERE id_grad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, id_grad);

            return statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public List<Integer> getAllAddresses() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM adrese";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt("id_adresa"));
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<Integer> getAllAddressesFromCity(int id_grad) {
        Connection connection = DB.getInstance().getConnection();

        String sqlQuery = "SELECT TOP 1 * FROM gradovi WHERE id_grad = ?";
        boolean city=false;
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, id_grad);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            city= resultSet.next();
        }catch (Exception e){
            e.printStackTrace();
        }

        if(!city){
            return null;
        }

        sqlQuery = "SELECT * FROM adrese WHERE id_grad = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, id_grad);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt("id_adresa"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
