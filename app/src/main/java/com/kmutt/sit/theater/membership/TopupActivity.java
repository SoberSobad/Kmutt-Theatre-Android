package com.kmutt.sit.theater.membership;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class TopupActivity extends AppCompatActivity {

    static int id;
    static int add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        id = getIntent().getIntExtra("id",-1);

        final EditText amountInp = findViewById(R.id.amoutInp);
        Button topupButt = findViewById(R.id.topupButt);
        topupButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id != -1) {
                    String url = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id=" + id;
                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    int current = Integer.parseInt(JsonarrayParseString.parseString2(response, "Money", 0));
                                    int topup = Integer.parseInt(amountInp.getText().toString());
                                    add = current + topup;
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            return params;
                        }
                    };
                    MySingleton.getInstance(TopupActivity.this).addToRequestQueue(jsonObjectRequest);

                    String urll = "http://theatre.sit.kmutt.ac.th/group6/setMoney?id=" + id + "&money=" + add;
                    jsonObjectRequest = new JsonArrayRequest

                            (Request.Method.GET, urll, null, new Response.Listener<JSONArray>() {


                                @Override
                                public void onResponse(JSONArray response) {
                                    //addressInp.setText(response.toString());
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            return params;
                        }
                    };
                    MySingleton.getInstance(TopupActivity.this).addToRequestQueue(jsonObjectRequest);
                }
                finish();
            }
        });
    }
}
