package com.imaginationcreators.bvnlibrary;

/**
 * Created by Ajay_Krish on 2/12/2018.
 */

public class Books {

    private String title;
    private String authorFirstName;
    private String authorLastName;
    private String ISBN;
    private String numberOfCopies;
    private String availablity;

    public Books() {
        this.authorFirstName = "UNKNOWN";
        this.authorLastName = "UNKNOWN";
        this.title = "UNKNOWN";
        this.ISBN = "UNKNOWN";
        this.availablity = "UNKNOWN";
    }

    public String[][] copyList;

    public Books(String title, String authorFirstName, String authorLastName, String ISBN, String availablity) {
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.ISBN = ISBN;
        this.availablity = availablity;
    }

    public String getTitle() {
        return title.toLowerCase();
    }

    public String getAuthorFirstName() {
        return authorFirstName.toLowerCase();
    }

    public String getAuthorLastName() {
        return authorLastName.toLowerCase();
    }

    public String getISBN() {
        return ISBN.toLowerCase();
    }
}
