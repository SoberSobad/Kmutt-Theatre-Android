package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

import org.json.JSONObject;

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

        final TextView redText = findViewById(R.id.redText);

        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://theatre.sit.kmutt.ac.th/group6";

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                redText.setText("Response: " + response.toString());
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                redText.setText(error.getMessage() + "Error !!!");

                            }
                        });
                MySingleton.getInstance(MembershipActivity.this).addToRequestQueue(jsonObjectRequest);
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
