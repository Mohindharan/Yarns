package com.mako.srikrishnayarns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/21/2017.
 */

public class Person {
    public String contactType;
    public String totalNumbers;
    public String chargeValue;
    public String buyertype;
    public String email;
    public String name;
    public String company;
    int amt;

    public int getAmt() {
        return amt;
    }

    public void setAmt(int amt) {
        this.amt = amt;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public boolean percentage;
    public List<String> fullAddress = new ArrayList<String>();
    public List<String> fullBank = new ArrayList<String>();
    public List<String> phone = new ArrayList<String>();

    public Person(){}

    public String getBuyertype() {
        return buyertype;
    }

    public void setBuyertype(String buyertype) {
        this.buyertype = buyertype;
    }

    public List<String> getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(List<String> fullAddress) {
        this.fullAddress = fullAddress;
    }

    public List<String> getFullBank() {
        return fullBank;
    }

    public void setFullBank(List<String> fullBank) {
        this.fullBank = fullBank;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getNumOfPhone() {
        return  totalNumbers;
    }

    public void setNumOfPhone(String numOfPhone) {
         totalNumbers = numOfPhone;
    }

    public String getChargeValue() {
        return chargeValue;
    }

    public void setChargeValue(String chargeValue) {
        this.chargeValue = chargeValue;
    }

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPercentage() {
        return percentage;
    }

    public void setPercentage(boolean percentage) {
        this.percentage = percentage;
    }
}