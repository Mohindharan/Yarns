package com.mako.srikrishnayarns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mako on 1/27/2017.
 */

public class Order {
    String buyer,buyerKey,seller,sellerKey,transport,transportKey,typeOfSale,typeofpayment;
    int noOfbags;
    int count;
    int total;
    int advanceamt;
    int quantity;
    int billdate;
    int deliveydate;
    int discount;
    int adjustments;
    int shippping;

    public int getGrand_total() {
        return grand_total;
    }

    public void setGrand_total(int grand_total) {
        this.grand_total = grand_total;
    }

    int grand_total;
    List<product> productList=new ArrayList<>();
    boolean isAdvance;
    boolean paid;
    boolean e1Form;
    boolean cform;
    boolean dispatched;

    public String getTypeofpayment() {
        return typeofpayment;
    }

    public void setTypeofpayment(String typeofpayment) {
        this.typeofpayment = typeofpayment;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public int getAdjustments() {
        return adjustments;
    }

    public void setAdjustments(int adjustments) {
        this.adjustments = adjustments;
    }

    public int getShippping() {
        return shippping;
    }

    public void setShippping(int shippping) {
        this.shippping = shippping;
    }

    public boolean isServicePaid() {
        return servicePaid;
    }

    public void setServicePaid(boolean servicePaid) {
        this.servicePaid = servicePaid;
    }

    public boolean isDispatched() {
        return dispatched;
    }

    public void setDispatched(boolean dispatched) {
        this.dispatched = dispatched;
    }

    public boolean isCform() {
        return cform;
    }

    public void setCform(boolean cform) {
        this.cform = cform;
    }

    public boolean isE1Form() {
        return e1Form;
    }

    public void setE1Form(boolean e1Form) {
        this.e1Form = e1Form;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    boolean servicePaid;


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getTransportKey() {
        return transportKey;
    }

    public void setTransportKey(String transportKey) {
        this.transportKey = transportKey;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getBuyerKey() {
        return buyerKey;
    }

    public void setBuyerKey(String buyerKey) {
        this.buyerKey = buyerKey;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getSellerKey() {
        return sellerKey;
    }

    public void setSellerKey(String sellerKey) {
        this.sellerKey = sellerKey;
    }


    public String getTypeOfSale() {
        return typeOfSale;
    }

    public void setTypeOfSale(String typeOfSale) {
        this.typeOfSale = typeOfSale;
    }

    public int getBilldate() {
        return billdate;
    }

    public void setBilldate(int billdate) {
        this.billdate = billdate;
    }

    public int getDeliveydate() {
        return deliveydate;
    }

    public void setDeliveydate(int deliveydate) {
        this.deliveydate = deliveydate;
    }

    public List<product> getProductList() {
        return productList;
    }

    public void setProductList(List<product> productList) {
        this.productList = productList;
    }

    public int getNoOfbags() {
        return noOfbags;
    }

    public void setNoOfbags(int noOfbags) {
        this.noOfbags = noOfbags;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getAdvanceamt() {
        return advanceamt;
    }

    public void setAdvanceamt(int advanceamt) {
        this.advanceamt = advanceamt;
    }

    public boolean isAdvance() {
        return isAdvance;
    }

    public void setAdvance(boolean advance) {
        isAdvance = advance;
    }
}
