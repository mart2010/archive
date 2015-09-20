package ca.canvac.webstore.domain;

import java.io.Serializable;


public class Account implements Serializable {
    private String address1;
    private String address2;
    private String category;
    private String city;
    private String company;
    private String country;
    private String email;
    private String fax;
    private String fedex;
    private String firstName;
    private String languagePref;
    private String lastName;
    private String password;
    private String phone;
    private String phoneLab;
    private String postalZip;
    private String prefix;
    private String provState;
    private String title;

    /* Private Fields */

    private String userId;


    public String getAddress1() {
        return address1;
    }

    public String getAddress2() {
        return address2;
    }

    public String getCategory() {
        return category;
    }

    public String getCity() {
        return city;
    }

    public String getCompany() {
        return company;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getFax() {
        return fax;
    }

    public String getFedex() {
        return fedex;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLanguagePref() {
        return languagePref;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getPhoneLab() {
        return phoneLab;
    }

    public String getPostalZip() {
        return postalZip;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getProvState() {
        return provState;
    }

    public String getTitle() {
        return title;
    }

    /* JavaBeans Properties */

    public String getUserId() {
        return userId;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public void setFedex(String fedex) {
        this.fedex = fedex;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLanguagePref(String languagePref) {
        this.languagePref = languagePref;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPhoneLab(String phoneLab) {
        this.phoneLab = phoneLab;
    }

    public void setPostalZip(String postalZip) {
        this.postalZip = postalZip;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setProvState(String provState) {
        this.provState = provState;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
