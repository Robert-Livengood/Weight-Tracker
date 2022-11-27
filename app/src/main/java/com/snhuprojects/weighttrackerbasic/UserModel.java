package com.snhuprojects.weighttrackerbasic;

public class UserModel {

    private int id;
    private String userName;
    private String password;
    private int goalWeight;
    private String phoneNumber;
    private boolean isSmsEnabled;

    // constructors

    public UserModel(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.goalWeight = 0;
        this.phoneNumber = null;
        this.isSmsEnabled = false;
    }

    // default
    public UserModel() {
    }

    public UserModel(int id, String userName, String password, int goalWeight, String phoneNumber, boolean isSmsEnabled) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.goalWeight = goalWeight;
        this.phoneNumber = phoneNumber;
        this.isSmsEnabled = isSmsEnabled;
    }

    // toString is necessary for printing the contents of a class object
    @Override
    public String toString() {
        return "UserModel{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password +
                ", goalWeight=" + goalWeight +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", isSmsEnabled=" + isSmsEnabled +
                '}';
    }

    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGoalWeight() { return goalWeight; }

    public void setGoalWeight(int currWeight) {
        this.goalWeight = currWeight;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSmsEnabled() {
        return isSmsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        isSmsEnabled = smsEnabled;
    }
}
