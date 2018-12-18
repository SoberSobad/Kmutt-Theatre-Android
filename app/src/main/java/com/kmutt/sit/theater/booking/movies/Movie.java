package com.kmutt.sit.theater.booking.movies;

import java.util.ArrayList;
import java.util.Collection;

public class Movie {
    public String name = null;
    public String date = null; //dd_mm_yyyy
    public String imageUrl = null;
    public Collection<String> showTimes = null;

    public Movie (String name, String date, String imageUrl, Collection<String> showTimes) {
        this.name = name;
        this.date = date;
        this.imageUrl = imageUrl;
        this.showTimes = showTimes;
    }

}
