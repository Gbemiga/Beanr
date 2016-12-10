package com.example.bemi.beanr.entites;

/**
 * Created by gbemigaadeosun on 29/11/2016.
 */
public class FavouriteShop {

    private int id;
    private String customerName;
    private String businessName;

    public FavouriteShop() {
    }

    public FavouriteShop(String customerName, String businessName) {
        this.customerName = customerName;
        this.businessName = businessName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    @Override
    public String toString() {
        return "FavouriteShop{" +
                "id=" + id +
                ", customerName='" + customerName + '\'' +
                ", businessName='" + businessName + '\'' +
                '}';
    }
}
