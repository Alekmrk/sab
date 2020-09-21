package sab;

import rs.etf.sab.operations.CourierRequestOperation;
import rs.etf.sab.student.jdbc.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class pa160422_CourierRequestOperation implements CourierRequestOperation {

    public static HashMap<String,String> mapaZahteva=new HashMap<>();
    @Override
    public boolean insertCourierRequest(String korime, String br_vozacke) {

        pa160422_CourierOperations courierOperations = new pa160422_CourierOperations();
        pa160422_UserOperations userOperations = new pa160422_UserOperations();
        if(!userOperations.getAllUsers().contains(korime)){
            return false;
        }
        if(courierOperations.getAllCouriers().contains(korime)){
            return false;
        }
        if(mapaZahteva.containsValue(br_vozacke)){
            return false;
        }
        if(!mapaZahteva.containsKey(korime)) {
            mapaZahteva.put(korime, br_vozacke);
        }
        return true;
    }

    @Override
    public boolean deleteCourierRequest(String korime) {
        if (!mapaZahteva.containsKey(korime)) {
            return false;
        }
        mapaZahteva.remove(korime);
        return true;
    }

    @Override
    public boolean changeDriverLicenceNumberInCourierRequest(String korime, String br_vozacke) {
        if (!mapaZahteva.containsKey(korime)) {
            return false;
        }

        mapaZahteva.replace(korime,br_vozacke);

        return true;
    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> lista = new ArrayList<>();

        Set<String> keySet = mapaZahteva.keySet();
        lista = new ArrayList<String>(keySet);

        return lista;
    }

    @Override
    public boolean grantRequest(String korime) {
        if (!mapaZahteva.containsKey(korime)) {
            return false;
        }

        String dozvola = mapaZahteva.get(korime);
        mapaZahteva.remove(korime);
        pa160422_CourierOperations courierOperations = new pa160422_CourierOperations();

        return courierOperations.insertCourier(korime,dozvola);

    }



}
