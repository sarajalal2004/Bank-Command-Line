package com.ga.bank;

public interface IUser {
    public boolean signup()throws Exception;
    public boolean login() throws Exception;
    public String hashPassword(String password) throws Exception;
    public boolean userFetch(String username);
    public int userFetch(String username, String password) throws Exception;
    public void addAccount();
    abstract public String toString();
}
