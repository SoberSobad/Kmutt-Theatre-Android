package com.kmutt.sit.theater.booking.showtimes;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;
import com.kmutt.sit.theater.booking.SeatActivity;
import com.kmutt.sit.theater.booking.movies.Movie;
import com.kmutt.sit.theater.booking.movies.MoviesListAdapter;
import com.kmutt.sit.theater.membership.MySingleton;
import com.kmutt.sit.theater.shared.libs.GlideApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class ShowtimesActivity extends AppCompatActivity {

    //
    // PARAMS
    //
    int movieId;
    int branchId;
    String movieName;
    String movieImageUrl;
    String movieLength;

    //
    // MovieInfo Info
    //
    TextView tvMovieTitle;
    TextView tvMovieGenre;
    TextView tvMovieLength;
    TextView tvMovieDetail;
    ImageView poster;

    //
    // Views
    //
    RecyclerView showtimesListView;
    SwipeRefreshLayout swipeRefreshLayout;

    //
    // TabLayout
    //
//    List<String> availableDatesList = new ArrayList<>();
    TabLayout datesTabLayout;

    //
    // TEST
    //
    Button btnTest;

    //
    // List Adapter
    //
//    ShowtimesListAdapter mAdapter;
    SectionedRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);

        //
        // Params
        //
        this.movieId = getIntent().getIntExtra("movie_id", -1);
        this.branchId = getIntent().getIntExtra("branch_id", -1);
//        this.movieName = getIntent().getStringExtra("movie_name");
//        this.movieImageUrl = getIntent().getStringExtra("movie_img_url");
//        this.movieLength = getIntent().getStringExtra("movie_length");

        //
        // Toolbar
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Showtime");
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        // MovieInfo info
        //
        this.tvMovieTitle = findViewById(R.id.tvMovieTitle);
        this.tvMovieGenre = findViewById(R.id.tvMovieGenre);
        this.tvMovieLength = findViewById(R.id.tvMovieLength);
        this.tvMovieDetail = findViewById(R.id.tvMovieDetail);
        this.poster = findViewById(R.id.poster);
//        tvMovieTitle.setText(movieName);

        //
        // TabLayout
        //
        datesTabLayout = findViewById(R.id.dataTabLayout);
        datesTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        datesTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        datesTabLayout.removeAllTabs();
        datesTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String strDate = tab.getTag().toString();

                loadShowtimesForDate(strDate);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //
        // RecyclerView & Adapter
        //
        this.showtimesListView = findViewById(R.id.showtimesListView);
        showtimesListView.setLayoutManager(new GridLayoutManager(this, 1));
        mAdapter = new SectionedRecyclerViewAdapter();
        showtimesListView.setAdapter(mAdapter);

        // set up "Swipe to Refresh"
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMovieInfo();
                refreshAvailableDates();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshMovieInfo();
        refreshAvailableDates();
    }

    public void refreshMovieInfo() {
        // start refreshing
        swipeRefreshLayout.setRefreshing(true);

        // make a connection request to server
        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/movies/" + movieId;
        JsonArrayRequest showtimeRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    JSONObject json = response.getJSONObject(0);
                    Movie m = Movie.fromJson(json);

                    //
                    // TITLE
                    //
                    tvMovieTitle.setText(m.name);

                    //
                    // GENRE
                    //
                    tvMovieGenre.setText(m.genre);

                    //
                    // DESCRIPTION
                    tvMovieDetail.setText(m.detail);

                    //
                    tvMovieGenre.setText(m.genre);

                    //
                    // POSTER
                    //
                    m.imageUrl = m.imageUrl.replace('\\', '\0');    // remove all the \ symbol
                    GlideApp.with(ShowtimesActivity.this)
                            .load(m.imageUrl)
                            .into(poster);

                    //
                    // LENGTH
                    //
                    String[] length = m.length.split(":");
                    String hr = length[0];
                    String min = length[1];

                    // remove 0 from first digit
                    if (hr.charAt(0) == '0')
                        hr = hr.substring(1);
                    if (min.charAt(0) == '0')
                        min = min.substring(1);

                    tvMovieLength.setText(hr + " hr " + min + " min.");

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }

                // stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
//                memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                Toast.makeText(ShowtimesActivity.this, "Error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(showtimeRequest);
    }

    //
    // Fetch movies
    //
    public void refreshAvailableDates() {
        // start refreshing
        swipeRefreshLayout.setRefreshing(true);

        // make a connection request to server
        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/showtimes/" + movieId;
        if (branchId != -1) {
            movieUrl += ("/" + branchId);
        }
//        Toast.makeText(this, movieUrl, Toast.LENGTH_LONG).show();
        JsonArrayRequest showtimeRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // clear existing data
                datesTabLayout.removeAllTabs();

                // date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("(EEE)\ndd MMM yyyy");

                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
//                        Showtime s = Showtime.fromJson(json);
//                        tmp.add(s);

//                        Toast.makeText(ShowtimesActivity.this, json.toString(), Toast.LENGTH_LONG).show();

                        //
                        // GET RAW DATE (in String)
                        //
                        String strDate = json.getString("start_date");

                        // CONVERT TO DATE
                        Date showDate = dateFormat.parse(strDate);

                        // FORMAT DATE
                        String displayDate = displayDateFormat.format(showDate);
                        datesTabLayout.addTab(datesTabLayout.newTab().setText(displayDate).setTag(strDate));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (datesTabLayout.getTabAt(0) != null)
                    datesTabLayout.getTabAt(0).select();
                else {
                    Toast.makeText(ShowtimesActivity.this, "No showtime available", Toast.LENGTH_SHORT).show();
                    finish();
                }

                // stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
//                memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                Toast.makeText(ShowtimesActivity.this, "Error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(showtimeRequest);
    }

    //
    // Load showtime for a date
    //
    private void loadShowtimesForDate(String strDate) {
        // start refreshing
        swipeRefreshLayout.setRefreshing(true);

        // make a connection request to server
        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/showtime/all/" + movieId + "/" + strDate;
        if (branchId != -1) {
            movieUrl += ("/" + branchId);
        }
        JsonArrayRequest showtimeRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                //
                // Clear old data
                //
                mAdapter.removeAllSections();

                List<Showtime> showtimes = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
//                        Toast.makeText(ShowtimesActivity.this, json.toString(), Toast.LENGTH_LONG).show();

                        Showtime s = Showtime.fromJson(json);
                        showtimes.add(s);

                        //
                        // Create new Section
                        //
                        ShowtimeLocationSection newSection = new ShowtimeLocationSection(ShowtimesActivity.this, s);
                        newSection.setOnClickListener(new ShowtimeLocationSection.OnClickListener() {
                            @Override
                            public void onClick(ShowtimeLocationSection.ViewHolder v) {
//                            Intent i = new Intent(getActivity(), SeatActivity.class);
                                Intent i = new Intent(ShowtimesActivity.this, SeatActivity.class);
                                i.putExtra("movie_name", movieName);
                                i.putExtra("movie_id", movieId);
                                startActivity(i);
                            }
                        });

                        // Add section
                        mAdapter.addSection(newSection);
                        mAdapter.notifyDataSetChanged();

//                        Toast.makeText(ShowtimesActivity.this, "" + i, Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle error
                Toast.makeText(ShowtimesActivity.this, "Error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();

                // stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(showtimeRequest);
    }
}
