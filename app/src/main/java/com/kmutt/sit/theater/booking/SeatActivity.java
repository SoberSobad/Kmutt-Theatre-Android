package com.kmutt.sit.theater.booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

public class SeatActivity extends AppCompatActivity {

    //
    // PARAMS
    //
    String movie_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        if (getIntent() != null) {
            movie_name = getIntent().getStringExtra("movie_name");

            if (movie_name == null)
                movie_name = "";
        }

        TextView title = findViewById(R.id.seatAct_movieName);
        title.setText(movie_name);

    }
}
