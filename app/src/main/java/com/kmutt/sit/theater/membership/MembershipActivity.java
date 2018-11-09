package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

public class MembershipActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        Toast.makeText(this, "CREATE MembershipActivity", Toast.LENGTH_SHORT).show();



        final Intent regisAct = new Intent(MembershipActivity.this, RegisterActivity.class);

        Button regisButt = findViewById(R.id.regisButt);
        regisButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(regisAct);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        Toast.makeText(this, "PAUSE MembershipActivity", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Toast.makeText(this, "RESUME MembershipActivity"+getIntent().getIntExtra("ID",000), Toast.LENGTH_SHORT).show();
    }

    /*
    //@Override
    public void onBackPressed() {
        Toast.makeText(this, "BackPressed", Toast.LENGTH_SHORT).show();
        //finish();
        //getIntent().getExtra
        //Intent mainAct = new Intent(getApplicationContext(), MainActivity.class);
        //mainAct.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        this.getIntent().setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
    }*/

}
