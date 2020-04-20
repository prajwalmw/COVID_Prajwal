package com.prajwal.AyurNivid;

public class Card_DataModel {
    private String state, date, confirm, recover, death;


    public Card_DataModel(String state, String date, String confirm, String recover, String death) {
        this.state = state;
        this.date = date;
        this.confirm = confirm;
        this.recover = recover;
        this.death = death;
    }


    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
