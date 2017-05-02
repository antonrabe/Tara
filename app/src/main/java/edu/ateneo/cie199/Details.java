package edu.ateneo.cie199.tara;

/**
 * Created by Ruby Bajet on 4/29/2017.
 */

public class Details {

    private String mName = "";
    private String mUsername = "";
    private String mCompany = "";
    private String mContact = "";


    public Details(String username, String name, String company, String contact) {
        mUsername = username;
        mName = name;
        mCompany = company;
        mContact = contact;
    }

    public String getUsername()
    {
        return mUsername;
    }

    public String getName()
    {
        return mName;
    }

    public String getCompany()
    {
        return mCompany;
    }

    public String getContact()
    {
        return mUsername;
    }

}