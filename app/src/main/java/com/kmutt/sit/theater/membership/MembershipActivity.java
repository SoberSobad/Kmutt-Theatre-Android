package com.kmutt.sit.theater.membership;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.kmutt.sit.theater.MainActivityV2;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

public class MembershipActivity extends AppCompatActivity {

    static int memberID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";

    Button forgetButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_membership);
        //Toast.makeText(this, "CREATE MembershipActivity", Toast.LENGTH_SHORT).show();

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        final EditText userInput = findViewById(R.id.userInput);
        final EditText passInput = findViewById(R.id.passInput);

        final Intent regisAct = new Intent(MembershipActivity.this, RegisterActivity.class);
        regisAct.putExtra("mode",1);
        //regisAct.putExtra("id",-1);

        forgetButt = findViewById(R.id.forgetButt);
        forgetButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgetPassword = new Intent(MembershipActivity.this, ForgetPasswordActivity.class);
                startActivity(forgetPassword);
            }
        });

        Button regisButt = findViewById(R.id.regisButt);
        regisButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //regisAct.putExtra("mode",1);
                //regisAct.putExtra("id",-1);
                startActivity(regisAct);

            }
        });

        final TextView redText = findViewById(R.id.redText);

        Button loginButt = findViewById(R.id.loginButt);
        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://theatre.sit.kmutt.ac.th/customer/androidLogin?user="+ userInput.getText() + "&pass=" + JsonHandler.md5(passInput.getText().toString());
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                String result = JsonHandler.parseString(response,"userID");
                                if(result.length() != 0 & (memberID = Integer.parseInt(result)) != -1) {
                                    //Intent mainAct = new Intent(MembershipActivity.this, MainActivityV2.class);
                                    //mainAct.putExtra("memberID",memberID);
                                    //mainAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    editor.putInt("memberID", memberID);
                                    editor.commit();
                                    //startActivity(mainAct);
                                    finish();
                                }else {
                                    redText.setText("Invalid username or password.\nPlease try again.");
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                redText.setText(error.getMessage());

                            }
                        });
                MySingleton.getInstance(MembershipActivity.this).addToRequestQueue(jsonObjectRequest);
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

}
