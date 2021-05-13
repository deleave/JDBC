package com.yth.JDBC上.jdbc2.bean;

/**
 * @ClassName User
 * @Description TODO
 * @Author deleave
 * @Date 2021/5/9 15:58
 * @Version 1.0
 **/
public class User {
    private String user;
    private String password;
    private int balance;

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                '}';
    }

    public User(String user, String password, int balance) {
        this.user = user;
        this.password = password;
        this.balance = balance;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public int getBalance() {
        return balance;
    }

    public User() {
    }
}
