package com.kmutt.sit.theater;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.membership.JsonarrayParseString;
import com.kmutt.sit.theater.membership.MembershipActivity;
import com.kmutt.sit.theater.membership.MySingleton;
import com.kmutt.sit.theater.membership.RegisterActivity;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    //final EditText memberInfo = findViewById(R.id.memberInfo);
    static int id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final EditText memberInfo = findViewById(R.id.memberInfo);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("id",-1);

       FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });




        //Create MembershipActivity
        final Intent mbshipAct = new Intent(MainActivity.this, MembershipActivity.class);
        //mbshipAct.putExtras(this.getIntent());
        //mbshipAct.putExtra("ID",(int) Math.ceil(Math.random() * 100));      //Test create once activity

        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id == -1)  startActivity(mbshipAct);
                else{
                    Intent main = new Intent(MainActivity.this, MainActivity.class);
                    //personalInfo.putExtra("id",id);
                    //personalInfo.putExtra("mode",2);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);
                    finish();
                }
            }
        });

        Button infoButt = findViewById(R.id.infoButt);
        infoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoAct = new Intent(MainActivity.this, RegisterActivity.class);
                infoAct.putExtra("id",id);
                infoAct.putExtra("mode",2);
                startActivity(infoAct);
            }
        });

/*
        memberInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(mbshipAct);
            }
        });
*/
    }

    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onResume() {
        super.onResume();
        final EditText memberInfo = findViewById(R.id.memberInfo);
        if(id != -1) {
            String url = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id=" + id;
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            memberInfo.setText("Hi, " + JsonarrayParseString.parseString2(response, "FirstName", 0) + " " +
                                    JsonarrayParseString.parseString2(response, "LastName", 0) + "\n" +
                                    "Money : " + JsonarrayParseString.parseString2(response, "Money", 0)


                            );
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
            MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonObjectRequest);
            Button loginButt = findViewById(R.id.loginButt);
            loginButt.setText("Log out");
            Button infoButt = findViewById(R.id.infoButt);
            infoButt.setVisibility(View.VISIBLE);
        }else{
            memberInfo.setText("Anonymous");
            Button loginButt = findViewById(R.id.loginButt);
            loginButt.setText("Log in");
            Button infoButt = findViewById(R.id.infoButt);
            infoButt.setVisibility(View.INVISIBLE);
        }


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
}
