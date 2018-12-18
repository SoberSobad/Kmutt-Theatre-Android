package com.kmutt.sit.theater.membership;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

import java.util.concurrent.TimeUnit;

public class ChangePasswordActivity extends AppCompatActivity {

    EditText oldPassInp;    EditText newPassInp;    EditText confirmPassInp;
    Button submitButt;
    TextView redText;

    static int memberID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        memberID = sp.getInt("memberID",-1);

        oldPassInp = findViewById(R.id.oldPassInput);
        newPassInp = findViewById(R.id.newPassInput);
        confirmPassInp = findViewById(R.id.confirmPassInput);
        redText = findViewById(R.id.redText);
        submitButt = findViewById(R.id.passwordSubmitButt);

        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassInp.getText().toString();
                String newPass = newPassInp.getText().toString();
                String confirmPass = confirmPassInp.getText().toString();
                if(newPass.matches(confirmPass)){
                    String url = "http://theatre.sit.kmutt.ac.th/customer/androidChangePassword?userID=" + memberID + "&oldpass=" + JsonHandler.md5(oldPass) + "&newpass=" + JsonHandler.md5(newPass);

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response.toString().length() > 2) {
                                        String done = JsonHandler.parseString(response, "done");
                                        if (done.matches("true")) {
                                            redText.setText("");
                                            AlertDialog.Builder builder = new AlertDialog.Builder(ChangePasswordActivity.this);
                                            builder.setMessage("Your password has been changed successfully!")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            finish();
                                                        }
                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        } else {
                                            String note = JsonHandler.parseString(response, "note");
                                            redText.setText(note);
                                        }
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    redText.setText(error.getMessage());
                                }
                            }) {
                    };
                    //******** No retry*************
                    jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(25), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(ChangePasswordActivity.this).addToRequestQueue(jsonArrayRequest);
                }else redText.setText("New passwords aren't matching");
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        memberID = sp.getInt("memberID",-1);
    }
}
