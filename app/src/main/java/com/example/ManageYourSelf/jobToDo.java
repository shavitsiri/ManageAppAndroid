package com.example.ManageYourSelf;

public class jobToDo {
    private String ClientName;
    private String ClientAddress;
    private String ClientPhoneNumber;
    private String ClientDate;
    private String ClientParquet;
    private String ClientMeters;
    private String ClientPricePerMeter;
    private  String id;

    public jobToDo(){}

    public jobToDo(String ClientName, String ClientAddress, String ClientPhoneNumber, String ClientDate, String ClientParquet, String ClientMeters, String ClientPricePerMeter, String id)
    {
        this.ClientName = ClientName;
        this.ClientAddress = ClientAddress;
        this.ClientPhoneNumber = ClientPhoneNumber;
        this.ClientDate = ClientDate;
        this.ClientParquet = ClientParquet;
        this.ClientMeters = ClientMeters;
        this.ClientPricePerMeter = ClientPricePerMeter;
        this.id = id;
    }

    public String getId(){return id;}
    public void setId(String id){this.id = id;}

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String ClientName) {
        this.ClientName = ClientName;
    }

    public String getClientAddress() {
        return ClientAddress;
    }

    public void setClientAddress(String ClientAddress) {
        this.ClientAddress = ClientAddress;
    }

    public String getClientPhoneNumber() {
        return ClientPhoneNumber;
    }

    public void setClientPhoneNumber(String ClientPhoneNumber) {
        this.ClientPhoneNumber = ClientPhoneNumber;
    }

    public String getClientDate() {
        return ClientDate;
    }

    public void setClientDate(String ClientDate) {
        this.ClientDate = ClientDate;
    }

    public String getClientParquet() {
        return ClientParquet;
    }

    public void setClientParquet(String ClientParquet) {
        this.ClientParquet = ClientParquet;
    }

    public String getClientMeters() {
        return ClientMeters;
    }

    public void setClientMeters(String ClientMeters) {
        this.ClientMeters = ClientMeters;
    }

    public String getClientPricePerMeter() {
        return ClientPricePerMeter;
    }

    public void setClientPricePerMeter(String ClientPricePerMeter) {
        this.ClientPricePerMeter = ClientPricePerMeter;
    }

    public String toString(){
        return     "שם: " + this.ClientName+ "\nטלפון: "  + this.ClientPhoneNumber +
                "\nכתובת: " + this.ClientAddress + "\nתאריך: " + this.ClientDate + "\nמחיר למטר: " + this.ClientPricePerMeter +
                 "\nמטר רבוע: " + this.ClientMeters + "\nמספר פרקט: " + this.ClientParquet ;
    }

}
