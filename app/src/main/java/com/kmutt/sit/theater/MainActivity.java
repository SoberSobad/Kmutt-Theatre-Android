package com.kmutt.sit.theater;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.booking.PaymentActivity;
import com.kmutt.sit.theater.membership.JsonHandler;
import com.kmutt.sit.theater.membership.MembershipActivity;
import com.kmutt.sit.theater.membership.MySingleton;
import com.kmutt.sit.theater.membership.RegisterActivity;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity {

    static int id = -1;
    Button loginButt;
    Button infoButt;
    Button logoutButt;
    EditText memberInfo;
    ConstraintLayout bottomArea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        id = getIntent().getIntExtra("id",-1);

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
                if (id == -1) {
                    startActivity(mbshipAct);
                } else {
                    Intent main = new Intent(MainActivity.this, MainActivity.class);
                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(main);
                    finish();
                }
            }
        });

        logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = -1;
                Intent main = new Intent(MainActivity.this, MainActivity.class);
                main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(main);
                finish();
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
                Intent infoAct = new Intent(MainActivity.this, RegisterActivity.class);
                infoAct.putExtra("id",id);
                infoAct.putExtra("mode",2);
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
        id = getIntent().getIntExtra("id",-1);
        if(id != -1) {
            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + id;
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
                            memberInfo.setText("fail to retrieve member's information \nMemberID = "+id);
                        }
                    });
            MySingleton.getInstance(MainActivity.this).addToRequestQueue(jsonArrayRequest);
            infoButt.setVisibility(View.VISIBLE);
            logoutButt.setVisibility(View.VISIBLE);
            bottomArea.setVisibility(View.INVISIBLE);

        }else{
            memberInfo.setText("Anonymous");
            loginButt.setText("Log in");
            infoButt.setVisibility(View.INVISIBLE);
            logoutButt.setVisibility(View.INVISIBLE);
            bottomArea.setVisibility(View.VISIBLE);
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
