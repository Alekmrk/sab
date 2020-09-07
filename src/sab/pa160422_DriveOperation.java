package sab;

import rs.etf.sab.operations.DriveOperation;

import java.util.List;

public class pa160422_DriveOperation implements DriveOperation {
    @Override
    public boolean planingDrive(String s) {
        return false;
    }

    @Override
    public int nextStop(String s) {
        return 0;
    }

    @Override
    public List<Integer> getPackagesInVehicle(String s) {
        return null;
    }
}
