package com.kmutt.sit.theater.fromweb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocationsMapFragment extends Fragment {

    //
    // Views
    //
    View rootView;
    WebView mapWebView;
    SwipeRefreshLayout swipeRefreshLayout;

    public LocationsMapFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_locations_map, container, false);

        // WebView
        this.mapWebView = rootView.findViewById(R.id.mapWebView);
        mapWebView.loadUrl("http://theatre.sit.kmutt.ac.th/customer/group14/map");
        mapWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }


        });
//        mapWebView.reload();

        // set up "Swipe to Refresh"
        this.swipeRefreshLayout = rootView.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mapWebView.reload();
            }
        });

        return rootView;
    }

}
