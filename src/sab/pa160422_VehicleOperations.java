package sab;

import rs.etf.sab.operations.VehicleOperations;
import rs.etf.sab.student.jdbc.DB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class pa160422_VehicleOperations implements VehicleOperations {

    public static HashMap<String, Integer> parkiranaVozila = new HashMap<>();

    @Override
    public boolean insertVehicle(String tablice, int tipGoriva, BigDecimal potrosnja, BigDecimal kapacitet) {// TODO: msm da cim se kreira vozilo ono je parkirano( ili ne, proveri)
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO vozila  VALUES( ?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, tablice);
            statement.setInt(2, tipGoriva);
            statement.setBigDecimal(3, potrosnja);
            statement.setBigDecimal(4, kapacitet);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKeys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();

            return false;
        }
    }

    @Override
    public int deleteVehicles(String... names) { // TODO: proveri i iz parkiranih je l se obrise
        if(names.length<1) return 0;
        Connection connection = DB.getInstance().getConnection();

        String moreNames="";

        if(names.length>1){
            for(int i =1; i<names.length; i++){
                moreNames+=" OR registracija = ?";
            }
        }
        String sqlQuery = "DELETE FROM vozila WHERE registracija = ?"+moreNames;
        parkiranaVozila.remove(names[0]);
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
            statement.setString(1, names[0]);
            for(int i =1; i < names.length;i++){
                statement.setString(i+1, names[i]);
                parkiranaVozila.remove(names[i]);
            }
            return statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<String> getAllVehichles() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM vozila";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<String> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getString("registracija"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public boolean checkParked(String tablica){
        return parkiranaVozila.containsKey(tablica);
    }
    @Override
    public boolean changeFuelType(String tablica, int tip) {// TODO: ovo je moguce samo kada je vozilo u magacinu. to se mora proveriti
        if(!checkParked(tablica)){
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE vozila Set tip_goriva = ? where registracija = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(2, tablica);
            statement.setInt(1, tip);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKeys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changeConsumption(String tablica, BigDecimal potrosnja) {
        if(!checkParked(tablica)){
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE vozila Set potrosnja = ? where registracija = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(2, tablica);
            statement.setBigDecimal(1, potrosnja);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKeys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean changeCapacity(String tablica, BigDecimal kapacitet) {
        // TODO: mozda kod ovih stvari da se proveri jel kapacitet pozitivna vrednost i tkt.. vazi i za gorivo i tome slicno
        if(!checkParked(tablica)){
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE vozila Set nosivost = ? where registracija = ?";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(2, tablica);
            statement.setBigDecimal(1, kapacitet);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKeys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean parkVehicle(String registracija, int idMagacin) {
        //TODO: ovde treba da se parkira i posle mozes da proveravas jel parkirano vozilo, ovo je moguce samo ako neka voznja sa ovim vozilom nije u toku
        pa160422_StockroomOperations magacini = new pa160422_StockroomOperations();
        if(!this.getAllVehichles().contains(registracija) || !magacini.getAllStockrooms().contains(idMagacin)){
            return false;
        }

        if(!parkiranaVozila.containsKey(registracija)){
            parkiranaVozila.put(registracija,idMagacin);
            return true;
        }

        return false;
    }


}
