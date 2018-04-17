package de.htwg.swqs.order.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Entity
public class CustomerInfo implements Comparable<CustomerInfo> {

  @Id
  @GeneratedValue
  private long id;
  @Email
  @Column(unique = true)
  private String email;
  @NotBlank
  private String surname;
  @NotBlank
  private String firstname;
  @NotBlank
  private String street;
  @NotBlank
  private String city;
  @NotBlank
  private String postcode;
  @NotBlank
  private String isoCountryCode;


  public CustomerInfo() {

  }

  /**
   * Creates a new customer info.
   *
   * @param id id of the new customer info
   * @param email email of the address
   * @param surname the surname
   * @param firstname the firstname
   * @param street the street
   * @param city the city
   * @param postcode the postcode
   */
  public CustomerInfo(long id, String email, String surname, String firstname, String street,
      String city,
      String postcode, String isoCountryCode) {
    this.id = id;
    this.email = email;
    this.surname = surname;
    this.firstname = firstname;
    this.street = street;
    this.city = city;
    this.postcode = postcode;
    this.isoCountryCode = isoCountryCode;
  }

  public String getEmail() {
    return this.email;
  }

  public long getId() {
    return this.id;
  }

  public String getSurname() {
    return this.surname;
  }

  public String getFirstname() {
    return this.firstname;
  }

  public String getStreet() {
    return this.street;
  }

  public String getCity() {
    return this.city;
  }

  public String getPostcode() {
    return this.postcode;
  }

  public String getIsoCountryCode() {
    return isoCountryCode;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setSurname(String surname) {
    this.surname = surname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public void setIsoCountryCode(String isoCountryCode) {
    this.isoCountryCode = isoCountryCode;
  }

  public String getFullName() {
    return this.firstname + " " + this.surname;
  }

  public String getFullAddress() {
    return this.street + " " + this.city + " " + this.postcode;
  }

  /**
   * Compares two customer infos.
   */
  public int compareTo(CustomerInfo customerInfo) {
    int compare = this.surname.compareToIgnoreCase(customerInfo.surname);
    if (compare == 0) {
      compare = this.firstname.compareToIgnoreCase(customerInfo.firstname);
    }
    return compare;
  }

  @Override
  public String toString() {
    return "CustomerInfo{"
        + "email=" + email
        + ", surname='" + surname + '\''
        + ", firstname='" + firstname + '\''
        + ", street='" + street + '\''
        + ", city='" + city + '\''
        + ", postcode='" + postcode + '\''
        + ", isoCountryCode='" + isoCountryCode + '\''
        + '}';
  }
}