package com.prajwal.AyurNivid;

public class Card_DataModel implements Comparable{
    private String state;
    private String  total;
    private String confirm;
    private String recover;
    private String death;


    public Card_DataModel(String state, String total, String confirm, String recover, String death) {
        this.state = state;
        this.total = total;
        this.confirm = confirm;
        this.recover = recover;
        this.death = death;
    }

    public String  getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getRecover() {
        return recover;
    }

    public void setRecover(String recover) {
        this.recover = recover;
    }

    public String getDeath() {
        return death;
    }

    public void setDeath(String death) {
        this.death = death;
    }

    @Override
    public int compareTo(Object o) {
        String compareToString = ((Card_DataModel) o).getTotal();
        int compareTo = Integer.parseInt(compareToString);
        return compareTo - Integer.parseInt(this.total); //Descending order...
    }
}
