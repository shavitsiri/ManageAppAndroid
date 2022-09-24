package com.example.ManageYourSelf;

public class Meet {

    private String clientName;
    private String clientPhone;
    private String clientAddress;
    private String clientMeetDate;
    private  String id;

    public Meet(){ }

    public Meet(String clientName, String clientPhone, String clientAddress, String clientMeetDate, String id) {
        this.clientName = clientName;
        this.clientPhone = clientPhone;
        this.clientAddress = clientAddress;
        this.clientMeetDate = clientMeetDate;
        this.id = id;
    }
    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getClientName(){return clientName;}
    public void setClientName(String name){this.clientName = name;}

    public String getClientPhone(){return clientPhone;}
    public void setClientPhone(String phone){this.clientPhone = phone;}

    public String getClientAddress(){return clientAddress;}
    public void setClientAddress(String address){this.clientAddress = address;}

    public String getClientMeetDate(){return clientMeetDate;}
    public void setClientMeetDate(String name){this.clientMeetDate = name;}

    public String toString(){
        return     "שם: " + this.clientName+ "\nטלפון: "  + this.clientPhone +
                         "\nכתובת: " + this.clientAddress + "\nתאריך: " + this.clientMeetDate;
    }

}
