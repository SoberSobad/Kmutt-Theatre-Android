package com.kmutt.sit.theater.booking.showtimes;

import org.json.JSONException;
import org.json.JSONObject;

public class Showtime {
    public String name = null;
    public String id = null;
    public String date = null; //dd_mm_yyyy
    public String imageUrl = null;
    public String length;
//    public Collection<String> showTimes = null;     // TODO: remove this

    public Showtime(String name, String date, String imageUrl) {
        this.name = name;
        this.date = date;
        this.imageUrl = imageUrl;
//        this.showTimes = showTimes;
    }

    public Showtime() {

    }

    public static Showtime fromJson(JSONObject json) throws JSONException {
        Showtime m = new Showtime();
        m.name = json.toString();
//        m.name = json.getString("title");
//        m.id = json.getString("id");
//        m.length = json.getString("length");
//        m.imageUrl = "http://theatre.sit.kmutt.ac.th" + json.getString("Image");
////        m.showTimes = Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00");

        return m;
    }
}
