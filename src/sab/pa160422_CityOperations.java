package sab;

import rs.etf.sab.operations.CityOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class pa160422_CityOperations implements CityOperations {

    @Override
    public int insertCity(String name, String postalCode) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO gradovi VALUES( ?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, postalCode);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return -1;
        }


    }

    @Override
    public int deleteCity(String... names) {
        if(names.length<1) return 0;
        Connection connection = DB.getInstance().getConnection();

        String moreNames="";

        if(names.length>1){
            for(int i =1; i<names.length; i++){
                moreNames+=" OR naziv = ?";
            }
        }
        String sqlQuery = "DELETE FROM gradovi WHERE naziv = ?"+moreNames;

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setString(1, names[0]);
            for(int i =1; i < names.length;i++){
                statement.setString(i+1, names[i]);
            }
            return statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public boolean deleteCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM gradovi WHERE id_grad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idCity);
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Integer> getAllCities() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM gradovi";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt("id_grad"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

