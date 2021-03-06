package com.kmutt.sit.theater.booking.movies;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class Movie {
    public String name = null;
    public int id;
    public String date = null; //dd_mm_yyyy
    public String imageUrl = null;
    public String length;
    public String genre;
    public String detail;
//    public Collection<String> showTimes = null;     // TODO: remove this

    public Movie (String name, String date, String imageUrl) {
        this.name = name;
        this.date = date;
        this.imageUrl = imageUrl;
//        this.showTimes = showTimes;
    }

    public Movie() {

    }

    public static Movie fromJson(JSONObject json) throws JSONException {
        Movie m = new Movie();
        m.name = json.getString("title");
        m.id = json.getInt("id");
        m.length = json.getString("length");
        m.detail = json.getString("detail");
        m.imageUrl = "http://theatre.sit.kmutt.ac.th" + json.getString("Image");
        m.imageUrl = m.imageUrl.replace('\\', '\0');    // remove all the \ symbol
//        m.showTimes = Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00");

        if (json.has("genre")) {
            m.genre = json.getString("genre");
        }

        return m;
    }
}
