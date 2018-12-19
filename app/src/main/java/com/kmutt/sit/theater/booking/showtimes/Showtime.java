package com.kmutt.sit.theater.booking.showtimes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Showtime {
    public String json;

    //
    // Fields
    //
    public int branchId;
    public String branchName;
    public List<Room> rooms;

    public Showtime() {
        rooms = new ArrayList<>();
    }

    public static class Room {
        public int roomNo;
        public String roomType;
        public String roomName;

        public List<MovieInfo> movies;

        public Room() {
            movies = new ArrayList<>();
        }

        public static Room fromJson(JSONObject json) throws JSONException {
            Room r = new Room();

            r.roomNo = json.getInt("room_no");
            r.roomType = json.getString("roomtype");
            r.roomName = json.getString("room_name");

            // load movies
            if (r.movies == null)
                r.movies = new ArrayList<>();
            r.movies.clear();

            JSONArray moviesJson = json.getJSONArray("movies");
            for (int i = 0; i < moviesJson.length(); i++) {
                MovieInfo m = MovieInfo.fromJson(moviesJson.getJSONObject(i));
                r.movies.add(m);
            }


            return r;
        }
    }

    public static class MovieInfo {
        public int movieId;
        public String soundtrack;
        public String subtitle;
        public String startTime;
        public String EndTime;
        public String showtime;
        public String status;
        public boolean clickable;

        public MovieInfo() {
        }

        public static MovieInfo fromJson(JSONObject json) throws JSONException {
            MovieInfo m = new MovieInfo();

            m.movieId = json.getInt("movie_id");
            m.soundtrack = json.getString("soundtrack");
            m.subtitle = json.getString("subtitle");
            m.startTime = json.getString("startTime");
            m.EndTime = json.getString("EndTime");
            m.showtime = json.getString("showtime");
            m.status = json.getString("status");
            m.clickable = json.getBoolean("clickable");

            return m;
        }
    }

    //
    // Factory Method
    //
    public static Showtime fromJson(JSONObject json) throws JSONException {
        Showtime s = new Showtime();
        s.json = json.toString();
        s.branchId = json.getInt("branch_id");
        s.branchName = json.getString("branch_name");

        // load rooms
        if (s.rooms == null)
            s.rooms = new ArrayList<>();
        s.rooms.clear();

        JSONArray roomsJson = json.getJSONArray("rooms");
        for (int i = 0; i < roomsJson.length(); i++) {
            Room r = Room.fromJson(roomsJson.getJSONObject(i));
            s.rooms.add(r);
        }

        return s;
    }
}
