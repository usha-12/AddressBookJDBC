package com.brideglabz.addressbook;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testng.annotations.BeforeClass;

import java.util.List;

public class AddressBookTest {
    static AddressBookService addressBookService;

    @BeforeClass
    public static void createAddressBookObj() {
        addressBookService = new AddressBookService();
    }

    @Test
    public void givenAddressBookContactsInDB_WhenRetrieved_ShouldMatchContactsCount() throws AddressBookException {
        List<AddressBookData> addressBookData = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO);
        Assertions.assertEquals(5, addressBookData.size());
    }

    @Test
    public void givenAddressBook_WhenUpdate_ShouldSyncWithDB() throws AddressBookException {
        addressBookService.updateRecord("pratiik", "NKB");
        boolean result = addressBookService.checkUpdatedRecordSyncWithDatabase("pratiik");
        Assertions.assertTrue(result);
    }

    @Test
    public void givenAddressBook_WhenRetrieved_ShouldMatchCountInGivenRange() throws AddressBookException {
        List<AddressBookData> addressBookData = addressBookService.readAddressBookData(AddressBookService.IOService.DB_IO, "2018-02-14",
                "2020-06-02");
        Assertions.assertEquals(3, addressBookData.size());
    }

    @Test
    public void givenAddresBook_WhenRetrieved_ShouldReturnCountOfCity() throws AddressBookException {
        Assertions.assertEquals(1, addressBookService.readAddressBookData("Maharashtra", "Mumbai"));
    }
}
