package com.brideglabz.addressbook;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddressBookDBService {

    private static AddressBookDBService addressBookDBService;
    private PreparedStatement addressBookPreparedStatement;
    private List<AddressBookData> addressBookData;

    private AddressBookDBService() {
    }

    private Connection getConnection() throws SQLException {
        String jdbcURL = "jdbc:mysql://localhost:3306/AddressBookService1?useSSL=false";
        String username = "root";
        String password = "Usha@1234";
        Connection con;
        System.out.println("Connecting to database:" + jdbcURL);
        con = DriverManager.getConnection(jdbcURL, username, password);
        System.out.println("Connection is successful:" + con);
        return con;

    }

    public static AddressBookDBService getInstance() {
        if (addressBookDBService == null)
            addressBookDBService = new AddressBookDBService();
        return addressBookDBService;
    }

    public List<AddressBookData> readData() throws AddressBookException {
        String query = null;
        query = "select * from addressBook";
        return getAddressBookDataUsingDB(query);
    }

    private List<AddressBookData> getAddressBookDataUsingDB(String sql) throws AddressBookException {
        List<AddressBookData> addressBookData = new ArrayList<>();
        try (Connection connection = this.getConnection()) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            addressBookData = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
        return addressBookData;
    }
    private void prepareAddressBookStatement() throws AddressBookException {
        try {
            Connection connection = this.getConnection();
            String query = "select * from addressBook where First_Name = ?";
            addressBookPreparedStatement = connection.prepareStatement(query);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }

    }
    private List<AddressBookData> getAddressBookDetails(ResultSet resultSet) throws AddressBookException {
        List<AddressBookData> addressBookData = new ArrayList<>();
        try {
            while (resultSet.next()) {
                String firstName = resultSet.getString("First_Name");
                String lastName = resultSet.getString("Last_Name");
                String address = resultSet.getString("Address");
                String city = resultSet.getString("City");
                String state = resultSet.getString("State");
                String zip = resultSet.getString("Zip");
                String phoneNo = resultSet.getString("Phone_Number");
                String email = resultSet.getString("Email");
                addressBookData.add(new AddressBookData(firstName, lastName, address, city, state, zip, phoneNo, email));
            }
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.DATABASE_EXCEPTION);
        }
        return addressBookData;
    }
    public int updateAddressBookData(String firstname, String address) throws AddressBookException {
        try (Connection connection = this.getConnection()) {
            String query = String.format("update addressBook set Address = '%s' where First_Name = '%s';", address,
                    firstname);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return preparedStatement.executeUpdate(query);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
        }
    }

    public List<AddressBookData> getAddressBookData(String firstname) throws AddressBookException {
        if (this.addressBookPreparedStatement == null)
            this.prepareAddressBookStatement();
        try {
            addressBookPreparedStatement.setString(1, firstname);
            ResultSet resultSet = addressBookPreparedStatement.executeQuery();
            addressBookData = this.getAddressBookDetails(resultSet);
        } catch (SQLException e) {
            throw new AddressBookException(e.getMessage(), AddressBookException.ExceptionType.CONNECTION_FAILED);
        }
        System.out.println(addressBookData);
        return addressBookData;
    }

    public List<AddressBookData> readData(LocalDate startLocalDate, LocalDate endLocalDate) {
        return addressBookDBService.readData(startLocalDate, endLocalDate);
    }
}
