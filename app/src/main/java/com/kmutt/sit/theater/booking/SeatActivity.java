package com.kmutt.sit.theater.booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

public class SeatActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat);

        Movie movie = MainActivity.clickedMovie;
        TextView title = findViewById(R.id.seatAct_movieName);
        title.setText(movie.name);

    }
}
