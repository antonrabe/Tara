package edu.ateneo.cie199.tara;

/**
 * Created by Ruby Bajet on 5/1/2017.
 */

public class User {

    private String mUsername = "";
    private String mName = "";
    private String mContact = "";
    private String mCompany = "";
    private String mPassword = "";

    public User(String username, String name, String company, String contact, String passsword)
    {
        mUsername = username;
        mName = name;
        mContact = contact;
        mCompany = company;
        mPassword = passsword;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public String getName()
    {
        return mName;
    }
    public String getContact()
    {
        return mContact;
    }
    public String getCompany()
    {
        return mCompany;
    }
}

