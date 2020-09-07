package sab;

import rs.etf.sab.operations.PackageOperations;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class pa160422_PackageOperations implements PackageOperations {
    @Override
    public int insertPackage(int i, int i1, String s, int i2, BigDecimal bigDecimal) {
        return 0;
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
        return null;
    }

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int i) {
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
