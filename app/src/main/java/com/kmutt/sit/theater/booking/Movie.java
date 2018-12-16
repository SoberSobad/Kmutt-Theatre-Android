package com.kmutt.sit.theater.booking;

import java.util.ArrayList;
import java.util.Collection;

public class Movie {
    public String name = null;
    public String date = null; //dd_mm_yyyy
    public Collection<String> showTimes = null;

    public Movie (String name, String date, Collection<String> showTimes) {
        this.name = name;
        this.date = date;
        this.showTimes = showTimes;
    }

}
