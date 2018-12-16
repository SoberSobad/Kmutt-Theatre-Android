package com.kmutt.sit.theater.booking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

public class ConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        final Intent main = new Intent(ConfirmationActivity.this, MainActivity.class);
        Button returnButt = findViewById(R.id.returnButt);
        returnButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(main);
            }
        });
    }

}
