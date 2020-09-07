package tests;

import sab.*;
import rs.etf.sab.operations.*;
import rs.etf.sab.tests.TestHandler;
import rs.etf.sab.tests.TestRunner;


public class StudentMain {

    public static void main(String[] args) {
        AddressOperations addressOperations = new pa160422_AddressOperation(); // Change this to your implementation.
        CityOperations cityOperations = new pa160422_CityOperations(); // Do it for all classes.
        CourierOperations courierOperations = new pa160422_CourierOperations(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new pa160422_CourierRequestOperation();
        DriveOperation driveOperation = new pa160422_DriveOperation();
        GeneralOperations generalOperations = new pa160422_GeneralOperations();
        PackageOperations packageOperations = new pa160422_PackageOperations();
        StockroomOperations stockroomOperations = new pa160422_StockroomOperations();
        UserOperations userOperations = new pa160422_UserOperations();
        VehicleOperations vehicleOperations = new pa160422_VehicleOperations();


        TestHandler.createInstance(
                addressOperations,
                cityOperations,
                courierOperations,
                courierRequestOperation,
                driveOperation,
                generalOperations,
                packageOperations,
                stockroomOperations,
                userOperations,
                vehicleOperations);

        TestRunner.runTests();
    }
}
