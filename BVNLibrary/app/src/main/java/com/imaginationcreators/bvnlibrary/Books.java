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

    public Books() {
        this.authorFirstName = "UNKNOWN";
        this.authorLastName = "UNKNOWN";
        this.title = "UNKNOWN";
        this.ISBN = "UNKNOWN";
        this.numberOfCopies = "UNKNOWN";
    }

    public String[][] copyList;

    public Books(String title, String authorFirstName, String authorLastName, String ISBN, String numberOfCopies, String[][] copyList) {
        this.title = title;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.ISBN = ISBN;
        this.numberOfCopies = numberOfCopies;
        this.copyList = copyList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getNumberOfCopies() {
        return numberOfCopies;
    }

    public void setNumberOfCopies(String numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public String[][] getCopyList() {
        return copyList;
    }

    public void setCopyList(String[][] copyList) {
        this.copyList = copyList;
    }
}
