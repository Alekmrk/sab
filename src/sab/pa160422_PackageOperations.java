package sab;

import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.student.jdbc.DB;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 0–„zahtev kreiran“, 1 –„prihvaćena ponuda“, 2 -„paket preuzet“, 3 –„isporučen“, 4 -„ponuda odbijena“

public class pa160422_PackageOperations implements PackageOperations {
    //TODO: napraviti ponudu odmah, ujedno i posle svake promene isto napraviti ponudu. neka privatna metoda. podrazumevana tezina je 10

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
            int result = resultSet.getInt(1);
            this.createOffer(result);
            return result;
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return -1;
    }

    private BigDecimal calculatePrice(int idPackage) {
        //CenaIsporuke=(OSNOVNA_CENA[tip_paketa] + weight * CENA_PO_KG[tip paketa] ) * (distanca izmedju od do adrese)
        int x1 = 0, x2 = 0, y1 = 0, y2 = 0;//treba mi adresa1 i adresa 2
        int weight = 0;
        int basePrice = 0;//treba tip
        int pricePerKG = 0;
        int adresaOd = 0;
        int adresaDo = 0;
        int tip = -1;


        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM paketi where id_paket=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                weight = resultSet.getInt("tezina");// ili stavi 1
                tip = resultSet.getInt("tip_paketa");// ili stavi 1
                adresaOd = resultSet.getInt("adresa_od");// ili stavi 1
                adresaDo = resultSet.getInt("adresa_do");// ili stavi 1
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        sqlQuery = "SELECT * FROM adrese where id_adresa=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, adresaOd);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                x1 = resultSet.getInt("x_koordinata");// ili stavi 1
                y1 = resultSet.getInt("y_koordinata");// ili stavi 1
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        sqlQuery = "SELECT * FROM adrese where id_adresa=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, adresaDo);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                x2 = resultSet.getInt("x_koordinata");// ili stavi 1
                y2 = resultSet.getInt("y_koordinata");// ili stavi 1
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        switch (tip) {
            case 0:
                basePrice = 115;
                pricePerKG = 0;
                break;
            case 1:
                basePrice = 175;
                pricePerKG = 100;
                break;
            case 2:
                basePrice = 250;
                pricePerKG = 100;
                break;
            case 3:
                basePrice = 350;
                pricePerKG = 500;
                break;

            default:
                return null;
        }
        return new BigDecimal((basePrice + weight * basePrice) * Math.sqrt(((x1 - x2) * (x1 - x2))+((y1 - y2) * (y1 - y2))));
    }

    private boolean updateOffer(int idPackage) {
        //TODO: ovo se zove posle svake promene paketa, treba cena da se promeni mozda i trenutna adresa
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE ponude Set cena = ?where id_paket = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(2, idPackage);
            statement.setBigDecimal(1, calculatePrice(idPackage));
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            return statement.executeUpdate() == 0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return false;
        }

    }


    private int createOffer(int idPackage) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "INSERT INTO ponude(id_paket, status, cena, vreme_kreiranja) VALUES( ?,?,?,?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            BigDecimal cena = calculatePrice(idPackage);
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
    public boolean acceptAnOffer(int idPackage) {
        if (getDeliveryStatus(idPackage) != 0) {
            return false;
        }

        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE ponude Set status = 1 and vreme_prihvatanja=? where id_paket = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(2, idPackage);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            return statement.executeUpdate() == 0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return false;
        }
    }

    @Override
    public boolean rejectAnOffer(int idPackage) {
        if (getDeliveryStatus(idPackage) != 0) {
            return false;
        }

        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE ponude Set status = 4 and vreme_prihvatanja=? where id_paket = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(2, idPackage);
            statement.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            return statement.executeUpdate() == 0 ? false : true;

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            return false;
        }
    }

    @Override
    public List<Integer> getAllPackages() {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM paketi";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {

            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result = new ArrayList<>();
            //resultSet.first();
            while (resultSet.next()) {
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
            statement.setInt(1, tip);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result = new ArrayList<>();
            //resultSet.first();
            while (resultSet.next()) {
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
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude where status =? or status=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, 1);
            statement.setInt(2, 2);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result = new ArrayList<>();
            //resultSet.first();
            while (resultSet.next()) {
                result.add(resultSet.getInt("id_paket"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getAllUndeliveredPackagesFromCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude p JOIN adrese a ON a.id_adresa=p.trenutna_adresa and a.id_grad=? and (p.status=? or p.status=?)";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idCity);
            statement.setInt(2, 1);
            statement.setInt(3, 2);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result = new ArrayList<>();
            //resultSet.first();
            while (resultSet.next()) {
                result.add(resultSet.getInt("id_paket"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Integer> getAllPackagesCurrentlyAtCity(int idCity) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude p JOIN adrese a ON a.id_adresa=p.trenutna_adresa and a.id_grad=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idCity);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();
            List<Integer> result = new ArrayList<>();
            //resultSet.first();
            while (resultSet.next()) {
                result.add(resultSet.getInt("id_paket"));// ili stavi 1
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean deletePackage(int idPackage) {//TODO: kada se obrise paket treba i ponuda da se obrise(automatski)
        int status = getDeliveryStatus(idPackage);
        if (status != 0 && status != 4) {
            return false;
        }

        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "DELETE FROM paketi WHERE id_paket = ?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            return statement.executeUpdate() == 0 ? false : true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean changeWeight(int idPackage, BigDecimal newWeight) {
        if (getDeliveryStatus(idPackage) != 0) {
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE paket Set tezina = ? where id_paket = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(2, idPackage);
            statement.setBigDecimal(1, newWeight);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            if (statement.executeUpdate() == 0) {
                return false;
            } else {
                updateOffer(idPackage);
                return true;
            }

        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            //return false;
        }

        return false;
    }

    @Override
    public boolean changeType(int idPackage, int newType) {
        if (getDeliveryStatus(idPackage) != 0) {
            return false;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "UPDATE paket Set tip_paketa = ? where id_paket = ?";// vrv mora da se stavi 1 umesto true
        try (PreparedStatement statement = connection.prepareStatement(sqlQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(2, idPackage);
            statement.setInt(1, newType);
            //statement.executeUpdate();
            //ResultSet resultSet = statement.getGeneratedKesys();
            //resultSet.next();
            if (statement.executeUpdate() == 0) {
                return false;
            } else {
                updateOffer(idPackage);
                return true;
            }
        } catch (Exception e) {
            //e.printStackTrace();
            //mora -1 zato sto smo u bazi zabranili da se dodaje dupli postanski_broj
            //return false;
        }
        return false;
    }

    @Override
    public int getDeliveryStatus(int idPackage) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude where id_paket=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                return resultSet.getInt("status");// ili stavi 1
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int idPackage) {
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT cena FROM ponude where id_paket=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                return resultSet.getBigDecimal(1);// ili stavi 1
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int getCurrentLocationOfPackage(int idPackage) {
        // TODO: ovo mora da se pazi da se stalno menja lokacija paketa
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude where id_paket=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                int addr = resultSet.getInt("trenutna_adresa");// ili stavi 1
                addr = addr < 0 ? -1 : addr;//ovo ako stavim da je adresa posiljke  minus id auta
                return addr;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public Date getAcceptanceTime(int idPackage) {
        int status = getDeliveryStatus(idPackage);
        if (status == 0 || status == 4) {
            return null;
        }
        Connection connection = DB.getInstance().getConnection();
        String sqlQuery = "SELECT * FROM ponude where id_paket=?";

        try (PreparedStatement statement = connection.prepareStatement(sqlQuery);) {
            statement.setInt(1, idPackage);
            statement.execute();
            ResultSet resultSet = statement.getResultSet();

            //resultSet.first();
            if (resultSet.next()) {
                return resultSet.getDate("vreme_prihvatanja");// ili stavi 1
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
