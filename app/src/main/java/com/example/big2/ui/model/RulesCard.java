package com.example.big2.ui.model;

/* Rule card object class. Utilized in rules.xml */

public class RulesCard {
    private int imageId;
    private String title;
    private String description;

    public RulesCard(int imageResId, String title, String description) {
        this.imageId = imageResId;
        this.title = title;
        this.description = description;
    }

    public int getImageId() { return imageId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
}
