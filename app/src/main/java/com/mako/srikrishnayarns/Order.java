package com.mako.srikrishnayarns;

/**
 * Created by Mako on 1/27/2017.
 */

public class Order {
    String buyer,buyerKey,seller,sellerKey,transport,transportKey,OrderNo,typeOfSale,category,estimateDelivery,billDate;
    int NoOfbags,count,total,advance,quantity;
    boolean isAdvance;
    boolean paid;
    boolean e1Form;
    boolean cform;
    boolean dispatched;

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

    public String getEstimateDelivery() {
        return estimateDelivery;
    }

    public void setEstimateDelivery(String estimateDelivery) {
        this.estimateDelivery = estimateDelivery;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

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

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getTypeOfSale() {
        return typeOfSale;
    }

    public void setTypeOfSale(String typeOfSale) {
        this.typeOfSale = typeOfSale;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getNoOfbags() {
        return NoOfbags;
    }

    public void setNoOfbags(int noOfbags) {
        NoOfbags = noOfbags;
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

    public int getAdvance() {
        return advance;
    }

    public void setAdvance(int advance) {
        this.advance = advance;
    }

    public boolean isAdvance() {
        return isAdvance;
    }

    public void setAdvance(boolean advance) {
        isAdvance = advance;
    }
}
