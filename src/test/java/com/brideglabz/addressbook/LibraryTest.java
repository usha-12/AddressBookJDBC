package com.brideglabz.addressbook;

import org.junit.jupiter.api.Test;

import static org.testng.AssertJUnit.assertTrue;

public class LibraryTest {
    @Test
    public void testSomeLibraryMethod() {
        Library classUnderTest = new Library();
        assertTrue("someLibraryMethod should return 'true'", classUnderTest.someLibraryMethod());
    }
}
