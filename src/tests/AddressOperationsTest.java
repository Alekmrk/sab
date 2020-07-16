//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package rs.etf.sab.tests;

import java.util.Random;

import sab.pa160422_AddressOperation;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rs.etf.sab.operations.*;
import sab.pa160422_CityOperations;

public class AddressOperationsTest {
    private GeneralOperations generalOperations;
    private AddressOperations addressOperations;
    private CityOperations cityOperations;
    private TestHandler testHandler;

    public AddressOperationsTest() {
        AddressOperations addressOperations = new pa160422_AddressOperation(); // Change this to your implementation.
        CityOperations cityOperations = new pa160422_CityOperations(); // Do it for all classes.
        CourierOperations courierOperations = null; // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = null;
        DriveOperation driveOperation = null;
        GeneralOperations generalOperations = null;
        PackageOperations packageOperations = null;
        StockroomOperations stockroomOperations = null;
        UserOperations userOperations = null;
        VehicleOperations vehicleOperations = null;


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

    @Before
    public void setUp() {
        this.testHandler = TestHandler.getInstance();
        Assert.assertNotNull(this.testHandler);
        this.cityOperations = this.testHandler.getCityOperations();
        Assert.assertNotNull(this.cityOperations);
        this.addressOperations = this.testHandler.getAddressOperations();
        Assert.assertNotNull(this.addressOperations);
        this.generalOperations = this.testHandler.getGeneralOperations();
        Assert.assertNotNull(this.generalOperations);
        this.generalOperations.eraseAll();
    }

    @After
    public void tearDown() {
        this.generalOperations.eraseAll();
    }

    @Test
    public void insertAddress_ExistingCity() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        int idAddress = this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        Assert.assertNotEquals(-1L, (long)idAddress);
        Assert.assertTrue(this.addressOperations.getAllAddresses().contains(idAddress));
    }

    @Test
    public void insertAddress_MissingCity() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        Random random = new Random();
        int idCity = random.nextInt();
        int idAddress = this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        Assert.assertEquals(-1L, (long)idAddress);
        Assert.assertEquals(0L, (long)this.addressOperations.getAllAddresses().size());
    }

    @Test
    public void deleteAddress_existing() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        int idAddress = this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        Assert.assertEquals(1L, (long)this.addressOperations.getAllAddresses().size());
        Assert.assertTrue(this.addressOperations.deleteAdress(idAddress));
        Assert.assertEquals(0L, (long)this.addressOperations.getAllAddresses().size());
    }

    @Test
    public void deleteAddress_missing() {
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        Assert.assertTrue(this.cityOperations.getAllCities().contains(new Integer(idCity)));
        Random random = new Random();
        int idAddress = random.nextInt();
        Assert.assertFalse(this.addressOperations.deleteAdress(idAddress));
    }

    @Test
    public void deleteAddresses_multiple_existing() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        this.addressOperations.insertAddress(streetOne, numberOne, idCity, 100, 100);
        Assert.assertEquals(2L, (long)this.addressOperations.getAllAddresses().size());
        Assert.assertEquals(2L, (long)this.addressOperations.deleteAddresses(streetOne, numberOne));
        Assert.assertEquals(0L, (long)this.addressOperations.getAllAddresses().size());
    }

    @Test
    public void deleteAddresses_multiple_missing() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        Assert.assertEquals(0L, (long)this.addressOperations.deleteAddresses(streetOne, numberOne));
        Assert.assertEquals(0L, (long)this.addressOperations.getAllAddresses().size());
    }

    @Test
    public void getAllAddressesFromCity() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        String streetTwo = "Kraljice Natalije";
        int numberTwo = 37;
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        int idAddressOne = this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        int idAddressTwo = this.addressOperations.insertAddress(streetTwo, numberTwo, idCity, 100, 100);
        Assert.assertNotEquals(-1L, (long)idAddressOne);
        Assert.assertNotEquals(-1L, (long)idAddressTwo);
        Assert.assertEquals(2L, (long)this.addressOperations.getAllAddressesFromCity(idCity).size());
        Assert.assertNull(this.addressOperations.getAllAddressesFromCity(idCity + 1));
        Assert.assertTrue(this.addressOperations.getAllAddressesFromCity(idCity).contains(idAddressOne));
        Assert.assertTrue(this.addressOperations.getAllAddressesFromCity(idCity).contains(idAddressTwo));
    }

    @Test
    public void deleteAllAddressesFromCity() {
        String streetOne = "Bulevar kralja Aleksandra";
        int numberOne = 73;
        String streetTwo = "Kraljice Natalije";
        int numberTwo = 37;
        int idCity = this.cityOperations.insertCity("Belgrade", "11000");
        Assert.assertNotEquals(-1L, (long)idCity);
        int idAddressOne = this.addressOperations.insertAddress(streetOne, numberOne, idCity, 10, 10);
        int idAddressTwo = this.addressOperations.insertAddress(streetTwo, numberTwo, idCity, 100, 100);
        Assert.assertNotEquals(-1L, (long)idAddressOne);
        Assert.assertNotEquals(-1L, (long)idAddressTwo);
        Assert.assertEquals(0L, (long)this.addressOperations.deleteAllAddressesFromCity(idCity + 1));
        Assert.assertEquals(2L, (long)this.addressOperations.getAllAddresses().size());
        Assert.assertEquals(2L, (long)this.addressOperations.deleteAllAddressesFromCity(idCity));
        Assert.assertEquals(0L, (long)this.addressOperations.getAllAddresses().size());
    }
}
