package com.kmutt.sit.theater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.booking.movies.Movie;
import com.kmutt.sit.theater.booking.PaymentActivity;
import com.kmutt.sit.theater.membership.InfoActivity;
import com.kmutt.sit.theater.booking.SeatActivity;
import com.kmutt.sit.theater.membership.JsonHandler;
import com.kmutt.sit.theater.membership.MembershipActivity;
import com.kmutt.sit.theater.membership.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static int memberID = -1;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";
    Button loginButt;
    Button infoButt;
    Button logoutButt;
    EditText memberInfo;
    ConstraintLayout bottomArea;
    List<Button> movieButtonList;
    public static Movie clickedMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //memberID = getIntent().getIntExtra("memberID",-1);
        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        memberID = sp.getInt("memberID",-1);

        memberInfo = findViewById(R.id.memberInfo);
        loginButt = findViewById(R.id.loginButt);
        infoButt = findViewById(R.id.infoButt);
        logoutButt = findViewById(R.id.logoutButt);
        bottomArea = findViewById(R.id.bottomArea);

        final Intent mbshipAct = new Intent(MainActivity.this, MembershipActivity.class);
        final Intent paymentAct = new Intent(MainActivity.this, PaymentActivity.class);

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberID == -1) {
                    startActivity(mbshipAct);
                } else {
                    /*Intent main = new Intent(MainActivity.this, MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);
                    finish();*/
                }
            }
        });

        logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberID = -1;
                /*Intent main = new Intent(MainActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                finish();*/
            }
        });

        //TODO: REMOVE DEV BUTTON
        Button devPaymentButt = findViewById(R.id.devPaymentButt);
        devPaymentButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(paymentAct);
            }
        });

        infoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoAct = new Intent(MainActivity.this, InfoActivity.class);
                infoAct.putExtra("memberID",memberID);
                //infoAct.putExtra("mode",2);
                startActivity(infoAct);
            }
        });



    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        memberID = sp.getInt("memberID",-1);
        if(memberID != -1) {
//            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + id;
//            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
            ConstraintLayout cl = findViewById(R.id.mainScrollArea);
            ConstraintSet constraintSet;
            constraintSet = new ConstraintSet();
            constraintSet.clone(cl);
            constraintSet.connect(infoButt.getId(),constraintSet.TOP,memberInfo.getId(),constraintSet.BOTTOM);
            constraintSet.applyTo(cl);

            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + memberID;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            memberInfo.setText("Hi, " + JsonHandler.parseString(response, "Fname") + " " +
                                    JsonHandler.parseString(response, "Lname")
                            );
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                        }
                    });
            MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
            infoButt.setVisibility(View.VISIBLE);
            logoutButt.setVisibility(View.VISIBLE);
            bottomArea.setVisibility(View.INVISIBLE);

        }else{
            ConstraintLayout cl = findViewById(R.id.mainScrollArea);
            ConstraintSet constraintSet;
            constraintSet = new ConstraintSet();
            constraintSet.clone(cl);
            constraintSet.connect(infoButt.getId(),constraintSet.TOP,constraintSet.PARENT_ID,constraintSet.TOP);
            constraintSet.applyTo(cl);

            memberInfo.setText("Anonymous");
            loginButt.setText("Log in");
            infoButt.setVisibility(View.INVISIBLE);
            logoutButt.setVisibility(View.INVISIBLE);
            bottomArea.setVisibility(View.VISIBLE);
        }

        String movieUrl = "http://theatre.sit.kmutt.ac.th/customer/mobile/getShowtime";
        JsonArrayRequest movieRequest = new JsonArrayRequest (Request.Method.GET, movieUrl, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
            // success
//            List<MovieInfo> tmp = Arrays.asList(
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00")),
//                    new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00")),
//                    new MovieInfo("Harry Potter and test", "16_12_2018", Arrays.asList("13:00","16:00","","")),
//                    new MovieInfo("Harry Potter and test2", "15_12_2018", Arrays.asList("13:00","16:00","",""))
//            ); //TODO: REMOVE
            List<Movie> tmp = new ArrayList<>();
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject mov = response.getJSONObject(i);
//                    Toast.makeText(MainActivity.this, mov.toString(), Toast.LENGTH_LONG).show();
                    tmp.add(new Movie(mov.getString("title"), "15_12_2018", mov.getString("Image")));
                    Toast.makeText(MainActivity.this, "added!", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
//            tmp.add(new MovieInfo("Harry Potter and test", "15_12_2018", Arrays.asList("13:00","16:00","18:00","21:00","22:00","23:00")));
            populateMovieList(tmp, "15_12_2018"); //TODO: REMOVE
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error
//                memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                Toast.makeText(MainActivity.this, "Error: \n" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        MySingleton.getInstance(MainActivity.this).addToRequestQueue(movieRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void populateMovieList(Collection<Movie> list, String dd_mm_yyyy) {

        TextView currentDayTitle = findViewById(R.id.dayTitle);
        TextView currentDay = findViewById(R.id.currentDay);
        View lowestElement = currentDay;

        currentDayTitle.setVisibility(View.INVISIBLE);
        currentDay.setText("Loading movies...");
        Log.d("DEBUG", "Starting to populate showtime list.");

        ConstraintLayout cl = findViewById(R.id.mainScrollArea);
        for (Movie H : list) {

            if (!(H.date.split("_")[0].equals(dd_mm_yyyy.split("_")[0]))) {Log.d("TESTI", "SKIPPERINO KRIPPERINO"); continue;}
//            final Button myButton = new Button(this);

            // ROW
            final View row = getLayoutInflater().inflate(R.layout.item_movie_showtime, null);
            row.setId(View.generateViewId());

            // TEXT VIEWS
            TextView tvMovieTitle =  row.findViewById(R.id.tvMovieTitle);
            TextView tvShowtimes =  row.findViewById(R.id.tvShowtimes);

            // BIND MOVIE TITLE
            tvMovieTitle.setText(H.name);

            // BIND SHOWTIMES
//            String showtimesText = "";
////            showtimesText += H.json + "\n";
//            for (String showTime : H.showTimes) {
//                showtimesText += " " + showTime + ",";
//            }
//            showtimesText = showtimesText.substring(0, showtimesText.length() - 1); //remove the extra comma at the end
//            tvShowtimes.setText(showtimesText);


//            myButton.setText(showtimesText);
//            myButton.setTextSize(24);
//            myButton.setTag(H);

            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = (Movie)row.getTag();
                    Intent seatAct = new Intent(MainActivity.this, SeatActivity.class);
                    seatAct.putExtra("movie_date", movie.date);
                    seatAct.putExtra("movie_name", movie.name);
//                    seatAct.putExtra("movie_showtimes", showtime.showTimes.toArray());
                    clickedMovie = movie;
                    startActivity(seatAct);
                }
            });

            cl.addView(row);


            ConstraintSet constraintSet;
            constraintSet = new ConstraintSet();
            constraintSet.clone(cl);
            constraintSet.constrainWidth(row.getId(),ConstraintSet.MATCH_CONSTRAINT);
            constraintSet.constrainHeight(row.getId(),ConstraintSet.WRAP_CONTENT);
            constraintSet.connect(row.getId(),ConstraintSet.TOP,lowestElement.getId(),ConstraintSet.BOTTOM,0);
            constraintSet.connect(row.getId(),ConstraintSet.LEFT,ConstraintSet.PARENT_ID,ConstraintSet.LEFT,0);
            constraintSet.connect(row.getId(),ConstraintSet.RIGHT,ConstraintSet.PARENT_ID,ConstraintSet.RIGHT,0);

            constraintSet.constrainDefaultHeight(row.getId(), 200);
            constraintSet.applyTo(cl);

            lowestElement = row;
            row.setVisibility(View.VISIBLE);
            Log.d("TESTI", Float.toString(row.getY()));
        }

        currentDayTitle.setVisibility(View.VISIBLE);
        currentDay.setText(dd_mm_yyyy.split("_")[0] + "." + dd_mm_yyyy.split("_")[1]);

    }
}
