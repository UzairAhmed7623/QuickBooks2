package com.quickbooks2.Models;

public class CountryCodeModel {
    private String iso, phoneCode, countryName, countryFlag;

    public CountryCodeModel(String iso, String countryName, String phoneCode, String countryFlag) {
        this.iso = iso;
        this.countryName = countryName;
        this.phoneCode = phoneCode;
        this.countryFlag = countryFlag;
    }

    public String getIso() {
        return iso;
    }

    public void setIso(String iso) {
        this.iso = iso;
    }

    public String getPhoneCode() {
        return phoneCode;
    }

    public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryFlag() {
        return countryFlag;
    }

    public void setCountryFlag(String countryFlag) {
        this.countryFlag = countryFlag;
    }
}
