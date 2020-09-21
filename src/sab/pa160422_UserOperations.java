package sab;

import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class pa160422_UserOperations implements UserOperations {
    @Override
    public boolean insertUser(String korime, String ime, String prezime, String sifra, int id_adrese) {
        // proveri velika slova imena i sifre i tkt

        if (Character.isLowerCase(ime.charAt(0)) || Character.isLowerCase(prezime.charAt(0)) || sifra.length() < 8){
            return false;
        }
        String regex = "^(?=.*?\\p{Lu})(?=.*?\\p{Ll})(?=.*?\\d)" +
                "(?=.*?[`~!@#$%^&*()\\-_=+\\\\|\\[{\\]};:'\",<.>/?]).*$";
        if(!sifra.matches(regex)){
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO korisnici VALUES( ?,?,?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, korime);
            statement.setString(2, ime);
            statement.setString(3, prezime);
            statement.setString(4, sifra);
            statement.setInt(5, id_adrese);
            statement.setInt(6, 0);
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
    public boolean declareAdmin(String korime) {
        Connection connection = DB.getInstance().getConnection();

        String sqlQuery = "SELECT jeAdmin FROM korisnici WHERE korime = ?";


        try (PreparedStatement statement = connection.prepareStatement(sqlQuery)){

            statement.setString(1, korime);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            if (resultSet.next()) {
                if( resultSet.getInt(1) == 1){
                    return false;
                }
            }

        } catch (Exception e) {
        }
        sqlQuery = "UPDATE korisnici Set jeAdmin = 1 where korime = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, korime);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            return  statement.executeUpdate()==0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return false;
        }
    }

    @Override
    public int getSentPackages(String... names) {// TODO: ovo mora da se odradi sa dve tabele, proveri i odradi
        if(names.length==0){
            return -1;
        }
        int ukupnoPosiljki=0;
        boolean postoji=false;
        pa160422_UserOperations userOperations = new pa160422_UserOperations();
        ArrayList lista = (ArrayList) userOperations.getAllUsers();

        for(int i =0; i < names.length;i++){
            if(lista.contains(names[i])){
               postoji=true;
               break;
            }
        }
        if(!postoji){
            return -1;
        }

        if(names.length<1) return 0;
        Connection connection = DB.getInstance().getConnection();

        String moreNames="";

        if(names.length>1){
            for(int i =1; i<names.length; i++){
                moreNames+=" OR korime = ?";
            }
        }
        String sqlQuery = "Select * FROM kuriri WHERE korime = ?"+moreNames;

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setString(1, names[0]);
            for(int i =1; i < names.length;i++){
                statement.setString(i+1, names[i]);
            }

            if(statement.execute()){
                ResultSet resultSet = statement.getResultSet();
                while(resultSet.next()){
                    ukupnoPosiljki+=resultSet.getInt("broj_isporucenh_paketa");
                }
                    return ukupnoPosiljki;



            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return -1;
    }

    @Override
    public int deleteUsers(String... names) {
        if(names.length<1) return 0;
        Connection connection = DB.getInstance().getConnection();

        String moreNames="";

        if(names.length>1){
            for(int i =1; i<names.length; i++){
                moreNames+=" OR korime = ?";
            }
        }
        String sqlQuery = "DELETE FROM korisnici WHERE korime = ?"+moreNames;

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
    public List<String> getAllUsers() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM korisnici";

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
}
