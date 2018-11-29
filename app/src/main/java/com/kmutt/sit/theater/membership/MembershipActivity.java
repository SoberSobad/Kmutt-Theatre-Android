package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

public class MembershipActivity extends AppCompatActivity {

    static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        //Toast.makeText(this, "CREATE MembershipActivity", Toast.LENGTH_SHORT).show();

        final EditText userInput = findViewById(R.id.userInput);
        final EditText passInput = findViewById(R.id.passInput);

        final Intent regisAct = new Intent(MembershipActivity.this, RegisterActivity.class);
        regisAct.putExtra("mode",1);
        regisAct.putExtra("id",-1);

        Button regisButt = findViewById(R.id.regisButt);
        regisButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                regisAct.putExtra("mode",1);
                regisAct.putExtra("id",-1);
                startActivity(regisAct);

            }
        });

        final TextView redText = findViewById(R.id.redText);

        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String url = "http://theatre.sit.kmutt.ac.th/customer/group6/loginn?user="+ userInput.getText() + "&pass=" + passInput.getText();
                String url = "http://theatre.sit.kmutt.ac.th/customer/group6/loginn?user=cs18@gmail.com&pass=0812345678";
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                String result = JsonHandler.parseString(response,"MemberID");
                                if(result.length() != 0) {
                                    id = Integer.parseInt(result);
                                    Intent mainAct = new Intent(MembershipActivity.this, MainActivity.class);
                                    mainAct.putExtra("id",id);
                                    mainAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainAct);
                                    finish();
                                }else {
                                    redText.setText("Invalid username or password.\nPlease try again.");
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                redText.setText(error.getMessage() + "Error !!!");

                            }
                        });
                MySingleton.getInstance(MembershipActivity.this).addToRequestQueue(jsonObjectRequest);
                /*  It don't wait for response so it return null ********************************************
                JSONArray response = JsonHandler.getMethod(url, MembershipActivity.this, redText);
                String result = JsonHandler.parseString(response,"MemberID",0);
                //String display;
                if(result.length() != 0) {
                    id = Integer.parseInt(result);
                    regisAct.putExtra("mode",2);
                    regisAct.putExtra("id",id);
                    startActivity(regisAct);

                }else {
                    redText.setText("Invalid username or password.\nPlease try again.");
                }
                */
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
