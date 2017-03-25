package address.app.com.shareaddress;

public class Address {

    String city;
    String streetName;
    String streetNumber;
    String postalCode;

    Address() {

    }

    Address(String city, String streetName, String streetNumber, String postalCode) {
        this.city = city;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
        this.postalCode = postalCode;
    }

    String getCity() {
        return city;
    }

    String getStreetName() {
        return streetName;
    }

    String getStreetNumber() {
        return streetNumber;
    }

    String getPostalCode() {
        return postalCode;
    }
}
