package com.kmutt.sit.theater.booking.movies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;
import com.kmutt.sit.theater.booking.SeatActivity;
import com.kmutt.sit.theater.booking.showtimes.ShowtimesActivity;
import com.kmutt.sit.theater.membership.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    //
    // Views
    //
    View rootView;
    RecyclerView moviesListView;
    SwipeRefreshLayout swipeRefreshLayout;

    //
    // TEST
    //
    Button btnTest;

    //
    // List Adapter
    //
    MoviesListAdapter mAdapter;

    public MoviesFragment() {
//        memberID = getArguments().getInt("memberID",-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        //
        // RecyclerView
        //
        this.moviesListView = rootView.findViewById(R.id.moviesListView);
        moviesListView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // set up "Swipe to Refresh"
        this.swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMovies();
            }
        });

        //
        // TEST
        //
        btnTest = rootView.findViewById(R.id.btnTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent seatAct = new Intent(getActivity(), SeatActivity.class);
                seatAct.putExtra("movie_date", "19_12_2018");
                seatAct.putExtra("movie_name", "Horrible Bossess 2");
                seatAct.putExtra("movie_showtimes", "6:00 9:00 15:00");
                startActivity(seatAct);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        refreshMovies();
    }

    @Override
    public void onResume() {
        super.onResume();

//        refreshMovies();
    }

    //
    // Fetch movies
    //
    public void refreshMovies() {
        // start refreshing
        swipeRefreshLayout.setRefreshing(true);

        // make a connection request to server
        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/movies";
        JsonArrayRequest movieRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // success
                List<Movie> tmp = new ArrayList<>();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject json = response.getJSONObject(i);
                        Movie mov = Movie.fromJson(json);
                        tmp.add(mov);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                // Create list mAdapter
                if (mAdapter == null) {
                    mAdapter = new MoviesListAdapter(getActivity(), tmp);
                    mAdapter.setOnClickListener(new MoviesListAdapter.OnClickListener() {
                        @Override
                        public void onClick(MoviesListAdapter.ViewHolder v) {
//                            Intent i = new Intent(getActivity(), SeatActivity.class);
                            Intent i = new Intent(getActivity(), ShowtimesActivity.class);
                            i.putExtra("movie_name", v.movie.name);
                            i.putExtra("movie_id", v.movie.id);
                            i.putExtra("movie_img_url", v.movie.imageUrl);
                            startActivity(i);
                        }
                    });
                }
                mAdapter.updateMoviesList(tmp);
                moviesListView.setAdapter(mAdapter);

                // stop refreshing
                swipeRefreshLayout.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
//                memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                Toast.makeText(getActivity(), "Error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(movieRequest);
    }
}
