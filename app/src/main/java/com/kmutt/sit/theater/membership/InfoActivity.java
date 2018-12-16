package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

public class InfoActivity extends AppCompatActivity {

    TextView firstname;     TextView lastname;      TextView identNo;
    TextView username;      TextView gender;        TextView email;
    TextView phoneNo;       TextView birthdate;     TextView address;
    TextView province;      TextView district;      TextView subdistrict;
    TextView zipcode;

    static int id;

    Button editButton;
    TextView redText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        id = getIntent().getIntExtra("id",-1);

        firstname = findViewById(R.id.firstName);   lastname = findViewById(R.id.lastName);     identNo = findViewById(R.id.identificationNumber);
        username = findViewById(R.id.username);     gender = findViewById(R.id.gender);         email = findViewById(R.id.email);
        phoneNo = findViewById(R.id.phoneNumber);   birthdate = findViewById(R.id.birthdate);   address = findViewById(R.id.address);
        province = findViewById(R.id.province);     district = findViewById(R.id.district);     subdistrict = findViewById(R.id.subdistrict);
        zipcode = findViewById(R.id.zipcode);

        editButton = findViewById(R.id.editButt);
        redText = findViewById(R.id.infoRedText);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editIntent = new Intent(InfoActivity.this, RegisterActivity.class);
                editIntent.putExtra("id",id);
                editIntent.putExtra("mode",3);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(editIntent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        id = getIntent().getIntExtra("id",-1);
        if(id != -1) {
            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + id;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            firstname.setText(JsonHandler.parseString(response, "Fname"));
                            lastname.setText(JsonHandler.parseString(response, "Lname"));
                            identNo.setText(JsonHandler.parseString(response, "ID_Card"));
                            username.setText(JsonHandler.parseString(response, "username"));
                            gender.setText(JsonHandler.parseString(response, "Gender"));
                            email.setText(JsonHandler.parseString(response, "email"));
                            phoneNo.setText(JsonHandler.parseString(response, "PhoneNumber"));
                            birthdate.setText(JsonHandler.parseString(response, "Birthdate"));
                            address.setText(JsonHandler.parseString(response, "Address"));
                            province.setText(JsonHandler.parseString(response, "Province"));
                            district.setText(JsonHandler.parseString(response, "Province"));
                            subdistrict.setText(JsonHandler.parseString(response, "Province"));
                            zipcode.setText(JsonHandler.parseString(response, "ZipCode"));
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            redText.setText(error.getMessage()+ "\nFailed to retrive information \nMemberID = "+id);
                        }
                    }) {
            };
            MySingleton.getInstance(InfoActivity.this).addToRequestQueue(jsonArrayRequest);
        }else{
            redText.setText("Failed to recognize member");
        }
    }
}