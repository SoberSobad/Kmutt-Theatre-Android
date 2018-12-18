package com.kmutt.sit.theater.booking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.kmutt.sit.theater.R;

import by.anatoldeveloper.hallscheme.hall.HallScheme;
import by.anatoldeveloper.hallscheme.hall.SeatListener;
import by.anatoldeveloper.hallscheme.view.ZoomableImageView;

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



        ZoomableImageView imageView = (ZoomableImageView) findViewById(R.id.zoomable_image);
        SeatExample seats[][] = new SeatExample[10][10];
        HallScheme scheme = new HallScheme(imageView, seats, this);

        scheme.setSeatListener(new SeatListener() {

            @Override
            public void selectSeat(int id) {
                Toast.makeText(SeatActivity.this, "select seat " + id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void unSelectSeat(int id) {
                Toast.makeText(SeatActivity.this, "unSelect seat " + id, Toast.LENGTH_SHORT).show();
            }

        });

    }
}
