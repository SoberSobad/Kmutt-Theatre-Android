package com.kmutt.sit.theater.booking.showtimes;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.kmutt.sit.theater.booking.movies.MoviesListAdapter;
import com.kmutt.sit.theater.membership.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowtimesActivity extends AppCompatActivity {

    //
    // PARAMS
    //
    String movieId;
    String movieName;
    String movieImageUrl;
    String movieLength;

    //
    // Movie Info
    //
    TextView tvMovieTitle;
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
    ShowtimesListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showtimes);

        //
        // Params
        //
        this.movieId = getIntent().getStringExtra("movie_id");
        this.movieName = getIntent().getStringExtra("movie_name");
        this.movieImageUrl = getIntent().getStringExtra("movie_img_url");
        this.movieLength = getIntent().getStringExtra("movie_length");

        //
        // Toolbar
        //
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Showtime");
        this.setSupportActionBar(toolbar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //
        // Movie info
        //
        this.tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvMovieTitle.setText(movieName);
        this.poster = findViewById(R.id.poster);

        //
        // TabLayout
        //
        datesTabLayout = findViewById(R.id.dataTabLayout);
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
        // RecyclerView
        //
        this.showtimesListView = findViewById(R.id.showtimesListView);
        showtimesListView.setLayoutManager(new LinearLayoutManager(this));

        // set up "Swipe to Refresh"
        this.swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAvailableDates();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshAvailableDates();
    }

    //
    // Fetch movies
    //
    public void refreshAvailableDates() {
        // start refreshing
        swipeRefreshLayout.setRefreshing(true);

        // make a connection request to server
        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/showtimes/" + movieId;
        JsonArrayRequest showtimeRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // clear existing data
                datesTabLayout.removeAllTabs();

                // date format
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
                SimpleDateFormat displayDateFormat = new SimpleDateFormat("MMM dd\n(EEE)");

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

                datesTabLayout.getTabAt(0).select();

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
        JsonArrayRequest showtimeRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                List<Showtime> showtimes = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
//                        Toast.makeText(ShowtimesActivity.this, json.toString(), Toast.LENGTH_LONG).show();

                        Showtime s = Showtime.fromJson(json);
                        showtimes.add(s);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Create list mAdapter
                if (mAdapter == null) {
                    mAdapter = new ShowtimesListAdapter(ShowtimesActivity.this, showtimes);
                    mAdapter.setOnClickListener(new ShowtimesListAdapter.OnClickListener() {
                        @Override
                        public void onClick(ShowtimesListAdapter.ViewHolder v) {
//                            Intent i = new Intent(getActivity(), SeatActivity.class);
                            Intent i = new Intent(ShowtimesActivity.this, SeatActivity.class);
                            i.putExtra("movie_name", movieName);
                            i.putExtra("movie_id", movieId);
                            startActivity(i);
                        }
                    });
                }
                mAdapter.updateMoviesList(showtimes);
                showtimesListView.setAdapter(mAdapter);

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
