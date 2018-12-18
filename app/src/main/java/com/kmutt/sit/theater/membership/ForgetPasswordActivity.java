package com.kmutt.sit.theater.membership;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText emailInput;
    Button emailSubmitButt;
    TextView redText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        emailInput = findViewById(R.id.emailInput);
        emailSubmitButt = findViewById(R.id.emailSubmitButt);
        redText = findViewById(R.id.redText);

        emailSubmitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redText.setText("Please wait...");
                String url = "http://theatre.sit.kmutt.ac.th/customer/androidForgetPassword?email=" + emailInput.getText().toString();

                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                if (response.toString().length() > 2) {
                                    String sent = JsonHandler.parseString(response, "sent");
                                    if (sent.matches("true")) {
                                        redText.setText("Password reset successfully");
                                        AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPasswordActivity.this);
                                        builder.setMessage("Password reset successfully.\nPlease follow the link in your email.")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        finish();
                                                    }
                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                    } else {
                                        String data = JsonHandler.parseString(response, "data");
                                        redText.setText(data);
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
                jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(30), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                MySingleton.getInstance(ForgetPasswordActivity.this).addToRequestQueue(jsonArrayRequest);
            }
        });
    }
}
