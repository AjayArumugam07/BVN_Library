package com.imaginationcreators.bvnlibrary;

/**
 * Created by Ajay_Krish on 2/11/2018.
 */

public class ListItem {

    private String heading;
    private String description;

    public ListItem(String heading, String description) {
        this.heading = heading;
        this.description = description;
    }

    public String getHeading() {
        return heading;
    }

    public String getDescription() {
        return description;
    }
}
