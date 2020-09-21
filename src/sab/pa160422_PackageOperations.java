package sab;

import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.student.jdbc.DB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class pa160422_PackageOperations implements PackageOperations { //TODO: napraviti ponudu odmah, ujedno i posle svake promene isto napraviti ponudu. neka privatna metoda
    @Override
    public int insertPackage(int addressFrom, int addressTo, String userName, int packageType, BigDecimal weight) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO paketi VALUES( ?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, addressFrom);
            statement.setInt(2, addressTo);
            statement.setString(3, userName);
            statement.setInt(4, packageType);
            statement.setBigDecimal(5, weight);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int result= resultSet.getInt(1);
            this.createOffer(result);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return -1;
    }
    private void updateOffer(int idPackage){
        //TODO: ovo se zove posle svake promene paketa
    }
    private int createOffer(int idPackage){
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO ponude(id_paket, status,cena, vreme_kreiranja) VALUES( ?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            BigDecimal cena=null;
            statement.setInt(1, idPackage);
            statement.setInt(2, 0);
            statement.setBigDecimal(3, cena);
            statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
//            statement.setBigDecimal(5, weight);
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            return resultSet.getInt(1);

        } catch (Exception e) {
            //e.printStackTrace();
        }

        return -1;
    }

    @Override
    public boolean acceptAnOffer(int i) {
        return false;
    }

    @Override
    public boolean rejectAnOffer(int i) {
        return false;
    }

    @Override
    public List<Integer> getAllPackages() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM paketi";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt("id_paket"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int tip) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM paketi where tip_paketa =?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1,tip);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result=new ArrayList<>();
            //resultSet.first();
            while(resultSet.next()){
                result.add(resultSet.getInt("id_paket"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getAllUndeliveredPackages() {
        return null;
    }

    @Override
    public List<Integer> getAllUndeliveredPackagesFromCity(int i) {
        return null;
    }

    @Override
    public List<Integer> getAllPackagesCurrentlyAtCity(int i) {
        return null;
    }

    @Override
    public boolean deletePackage(int i) {
        return false;
    }

    @Override
    public boolean changeWeight(int i, BigDecimal bigDecimal) {
        return false;
    }

    @Override
    public boolean changeType(int i, int i1) {
        return false;
    }

    @Override
    public int getDeliveryStatus(int i) {
        return 0;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int i) {
        return null;
    }

    @Override
    public int getCurrentLocationOfPackage(int i) {
        return 0;
    }

    @Override
    public Date getAcceptanceTime(int i) {
        return null;
    }
}
