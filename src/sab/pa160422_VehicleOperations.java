package sab;

import rs.etf.sab.operations.VehicleOperations;

import java.math.BigDecimal;
import java.util.List;

public class pa160422_VehicleOperations implements VehicleOperations {
    @Override
    public boolean insertVehicle(String s, int i, BigDecimal bigDecimal, BigDecimal bigDecimal1) {
        return false;
    }

    @Override
    public int deleteVehicles(String... strings) {
        return 0;
    }

    @Override
    public List<String> getAllVehichles() {
        return null;
    }

    @Override
    public boolean changeFuelType(String s, int i) {
        return false;
    }

    @Override
    public boolean changeConsumption(String s, BigDecimal bigDecimal) {
        return false;
    }

    @Override
    public boolean changeCapacity(String s, BigDecimal bigDecimal) {
        return false;
    }

    @Override
    public boolean parkVehicle(String s, int i) {
        return false;
    }
}
