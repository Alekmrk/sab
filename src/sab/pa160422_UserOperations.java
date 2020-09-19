package sab;

import rs.etf.sab.operations.UserOperations;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;

public class pa160422_UserOperations implements UserOperations {
    @Override
    public boolean insertUser(String korime, String ime, String prezime, String sifra, int id_adrese) {
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
        String sqlQuery = "UPDATE korisnici Set jeAdmin = true where korime = ?";

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
    public int getSentPackages(String... strings) {
        return 0;
    }

    @Override
    public int deleteUsers(String... strings) {
        return 0;
    }

    @Override
    public List<String> getAllUsers() {
        return null;
    }
}
