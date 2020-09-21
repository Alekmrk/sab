package sab;

import rs.etf.sab.operations.StockroomOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class pa160422_StockroomOperations implements StockroomOperations {

    //public static List<Integer> cityIDs= new ArrayList<>();

    @Override
    public int insertStockroom(int adresa) {// TODO: proveri ako dve razlicite adrese imaju isti grad
        Connection connection = DB.getInstance().getConnection();
        int cityID=-1;
        String query = "SELECT id_grad FROM adrese WHERE id_adresa = ?";
        try (PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setInt(1, adresa);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            resultSet.next();
            cityID=resultSet.getInt(1);
            pa160422_AddressOperation addressOperation=new pa160422_AddressOperation();
            List<Integer> adreseUGradu=addressOperation.getAllAddressesFromCity(cityID);
            List<Integer> magacini= this.getAllStockroomsAddresess();
            for(int i=0;i<adreseUGradu.size();i++){
                if(magacini.contains(adreseUGradu.get(i))) {
                    return -1;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //String sqlQuery = "SELECT * from magacini where id_adresa = ?";
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

        String sqlQuery = "INSERT INTO magacini VALUES( ?)";
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, adresa);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int val= resultSet.getInt(1);
            //cityIDs.add(cityID);
            return val;
        } catch (Exception e) {
            //e.printStackTrace();
            return -1;
        }
    }



        @Override
    public boolean deleteStockroom(int idMagacin) {// TODO: moze se obrisati ukoliko postoji i prazan je
            Connection connection = DB.getInstance().getConnection();

            if(pa160422_VehicleOperations.parkiranaVozila.containsValue(idMagacin)){
                return false;
            }

            String sqlQuery = "DELETE FROM magacini WHERE id_magacin = ?";

            try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
                statement.setInt(1, idMagacin);
                 boolean result=  statement.executeUpdate()==0 ? false : true;
                 if(result){
                    // ne radi nista zato sto pri ubacaju gledamo da li postoji magacin u tom gradu
                 }
                 return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        return false;
    }
    public int getStockroomByCity(int idCity){
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM magacini ma JOIN adrese ad ON ad.id_adresa = ma.id_adresa WHERE ad.id_grad = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){
            statement.setInt(1, idCity);
            statement.execute();
            ResultSet resultSet=statement.getResultSet();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        catch (Exception ex){
        }

        return -1;
    }
    @Override
    public int deleteStockroomFromCity(int idCity) {// TODO: moze se obrisati ukoliko postoji i prazan je

        pa160422_AddressOperation addressOperation=new pa160422_AddressOperation();
        //List<Integer> lista=addressOperation.getAllAddressesFromCity(idCity);
        //napravi da dohvatis magacin iz grada i onda odatle mozes lako da obrise
        int idMagacin=this.getStockroomByCity(idCity);
        if(pa160422_VehicleOperations.parkiranaVozila.containsValue(idMagacin)){
            return -1;
        }
        if(idMagacin!=-1){
             this.deleteStockroom(idMagacin);
             return idMagacin;
        }
        return -1;
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

    public List<Integer> getAllStockroomsAddresess() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM magacini";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt(2));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
