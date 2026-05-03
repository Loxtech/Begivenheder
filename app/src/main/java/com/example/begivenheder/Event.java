package com.example.begivenheder;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Event implements Serializable {
    private String name;
    private String date;
    private String shortDescription;
    private String fullDescription;
    private String link;
    private String imageUri;
    private boolean isRegistered;

    public Event(String name, String date, String shortDescription, String fullDescription, String link) {
        this.name = name;
        this.date = date;
        this.shortDescription = shortDescription;
        this.fullDescription = fullDescription;
        this.link = link;
        this.isRegistered = false;
        this.imageUri = null;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    /**
     * Konverterer tekst-datoen til et Date-objekt for korrekt sortering.
     */
    public Date getDateAsObject() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd. MMMM yyyy", new Locale("da", "DK"));
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return new Date(0); // Fallback hvis formatet fejler
        }
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getFullDescription() {
        return fullDescription;
    }

    public String getLink() {
        return link;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        isRegistered = registered;
    }
}