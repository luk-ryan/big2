package com.example.big2.ui.model;

/* Rule card object class. Utilized in rules.xml */

public class RulesCard {
    private int imageId;
    private String title;
    private String description;
    private String rank;  // e.g. "A", "2", "3"
    private String suitSymbol;  // e.g. "♠", "♥"

    public RulesCard(int imageResId, String title, String description, String rank, String suitSymbol) {
        this.imageId = imageResId;
        this.title = title;
        this.description = description;
        this.rank = rank;
        this.suitSymbol = suitSymbol;
    }

    public int getImageId() { return imageId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRank() { return rank; }
    public String getSuitSymbol() { return suitSymbol; }
}
