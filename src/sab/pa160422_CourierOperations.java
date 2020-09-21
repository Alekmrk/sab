package sab;

import rs.etf.sab.operations.CourierOperations;
import rs.etf.sab.student.jdbc.DB;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class pa160422_CourierOperations implements CourierOperations {
    @Override
    public boolean insertCourier(String courierUserName, String driverLicenceNumber) {

        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO kuriri (korime, br_vozacke, broj_isporucenih_paketa, status, ostvaren_profit) VALUES( ?,?,0,0,0)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, courierUserName);
            statement.setString(2, driverLicenceNumber);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKeys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return false;
        }


    }

    @Override
    public boolean deleteCourier(String username) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM kuriri WHERE korime = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setString(1, username);
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;}

    @Override
    public List<String> getCouriersWithStatus(int status) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM kuriri where status = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1,status);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<String> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getString("korime"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<String> getAllCouriers() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM kuriri ORDER BY ostvaren_profit DESC";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<String> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getString(1));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int broj_isporucenih_paketa) {

        BigDecimal result=new BigDecimal(0);

        Connection connection = DB.getInstance().getConnection();
        String sqlQuery;
        if(broj_isporucenih_paketa==-1) {
            sqlQuery = "SELECT avg(ostvaren_profit) FROM kuriri";
        }else{
            sqlQuery = "SELECT avg(ostvaren_profit) FROM kuriri where broj_isporucenih_paketa = ?";
        }
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            if(broj_isporucenih_paketa != -1){
                statement.setInt(1,broj_isporucenih_paketa);
            }
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                result = new BigDecimal(resultSet.getDouble(1)).setScale(3, RoundingMode.FLOOR);
                return result;
            }
            else{
                return null;
            }
            //resultSet.first();



        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}