package com.kmutt.sit.theater.membership;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EditActivity extends AppCompatActivity {

    final String[] provinces = new String[JsonHandler.places.length];
    final List<String> districts = new ArrayList<>();
    final List<String> subdistricts = new ArrayList<>();
    ArrayAdapter<String> districtAdapt;
    ArrayAdapter<String> subdistrictAdapt;

    EditText[] editTexts;
    Spinner[] spinners;
    String[] months;

    //************************* Initializing ************************************************
    Spinner provinceDrop;
    Spinner districtDrop;
    Spinner subdistrictDrop;
    Spinner genderDrop;
    Spinner monthDrop;

    EditText firstnameInp;        EditText lastnameInp;
    EditText dateInp;             EditText yearInp;
    EditText phonenumberInp;      EditText addressInp;
    EditText postcodeInp;         EditText confirmpassInp;

    TextView redText;

    Button submitButt;

    static int memberID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //memberID = getIntent().getIntExtra("memberID",-1);
        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        memberID = sp.getInt("memberID",-1);

        for(int i=0; i<JsonHandler.places.length; i++){
            provinces[i] = JsonHandler.places[i][0][0];
        }

        //********************************************  FIND VIEW *******************************
        provinceDrop = findViewById(R.id.provinceSpinEdit);
        districtDrop = findViewById(R.id.districtSpinEdit);
        subdistrictDrop = findViewById(R.id.subdistrictSpinEdit);
        genderDrop = findViewById(R.id.genderSpinEdit);
        monthDrop = findViewById(R.id.monthSpinEdit);

        firstnameInp = findViewById(R.id.firstnameEdit);    lastnameInp = findViewById(R.id.lastnameEdit);
        dateInp = findViewById(R.id.dateEdit);              yearInp = findViewById(R.id.yearEdit);
        phonenumberInp = findViewById(R.id.phoneNoEdit);    addressInp = findViewById(R.id.addressEdit);
        postcodeInp = findViewById(R.id.zipcodeEdit);       confirmpassInp = findViewById(R.id.confirmpassEdit);

        redText = findViewById(R.id.regisRedText);

        submitButt = findViewById(R.id.submitButt);

        //******************************************** Spinner set Adapter *********************
        final ArrayAdapter<String> provinceAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        provinceDrop.setAdapter(provinceAdapt);

        districtAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        districtDrop.setAdapter(districtAdapt);

        subdistrictAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subdistricts);
        subdistrictDrop.setAdapter(subdistrictAdapt);

        final String[] genders = new String[]{"Male", "Female"};
        final ArrayAdapter<String> genderSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        genderDrop.setAdapter(genderSpinAdapt);

        months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        final ArrayAdapter<String> monthSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthDrop.setAdapter(monthSpinAdapt);

        //********************************************* Assisgn to arrays *************************
        editTexts = new EditText[]{confirmpassInp,firstnameInp,lastnameInp,yearInp,dateInp,phonenumberInp,addressInp,postcodeInp};
        spinners = new Spinner[]{genderDrop, monthDrop, provinceDrop,districtDrop,subdistrictDrop};

        //********************************************* Selected Listener for Places spinners ***********
        provinceDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                districts.clear();
                for(int i=1; i<JsonHandler.places[position].length; i++){
                    districts.add(JsonHandler.places[position][i][0]);
                }
                districtAdapt.notifyDataSetChanged();
                districtDrop.setSelection(0);
                subdistricts.clear();
                for(int i=1; i<JsonHandler.places[position][1].length; i++){
                    subdistricts.add(JsonHandler.places[position][1][i]);
                }
                subdistrictAdapt.notifyDataSetChanged();
                subdistrictDrop.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {            }
        });
        districtDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                subdistricts.clear();
                int provNo = provinceDrop.getSelectedItemPosition();
                int distNo = districtDrop.getSelectedItemPosition();
                for(int i=1; i<JsonHandler.places[provNo][distNo+1].length; i++){
                    subdistricts.add(JsonHandler.places[provNo][distNo+1][i]);
                }
                subdistrictAdapt.notifyDataSetChanged();
                subdistrictDrop.setSelection(0);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {            }
        });


        //******************************************OnClickListener for Submit Button***********************
        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //********************************************** Check for space *********************************************
                if (firstnameInp.getText().toString().matches("") | lastnameInp.getText().toString().matches("") |
                        phonenumberInp.getText().toString().matches("") | dateInp.getText().toString().matches("") |
                        yearInp.getText().toString().matches("") | postcodeInp.getText().toString().matches("")) {
                    redText.setText("Please fill every space provided");
                }else {

                    //******************************** Birthdate format*************************
                    int day = Integer.parseInt(dateInp.getText().toString());
                    int month = monthDrop.getSelectedItemPosition()+1;
                    int year = Integer.parseInt(yearInp.getText().toString());
                    boolean leapyear;
                    if(year%4==0) { if(year%100==0) { if(year%400==0) { leapyear = true; } else { leapyear = false; } } else { leapyear = true; } } else { leapyear = false; }
                    if (year < 1) yearInp.setText("0001");
                    if (day < 1) dateInp.setText("01");
                    //if (day>28 & month == 2 & (year %4 == 0 | (year %100 == 0 & year %400 != 0) ) ) dateInp.setText("28");
                    //if (day>29 & month == 2) dateInp.setText("29");
                    if (day>28 & month == 2 & !leapyear) dateInp.setText("28");
                    if (day>29 & month == 2 & leapyear) dateInp.setText("29");
                    if (day>30 & (month == 4 | month == 6 | month == 9 | month == 11)) dateInp.setText("30");
                    if (day>31 & (month == 1 | month == 3 | month == 5 | month == 7 | month == 8 | month == 10 | month == 12)) dateInp.setText("31");


                    String url = "http://theatre.sit.kmutt.ac.th/customer/androidUpdate?userID=" + memberID + "&pass=" + JsonHandler.md5(confirmpassInp.getText().toString()) +
                                "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + (genderDrop.getSelectedItem().toString().matches("Male") ? "M" : "F") +
                                "&birthdate=" + yearInp.getText() + "-" + (monthDrop.getSelectedItemPosition() + 1) + "-" + dateInp.getText() +
                                "&phonenumber=" + phonenumberInp.getText() + "&address=" + addressInp.getText() +
                                "&district=" + districtDrop.getSelectedItem().toString() + "&province=" + provinceDrop.getSelectedItem().toString() + "&postcode=" + postcodeInp.getText() +
                                "&subdist=" + subdistrictDrop.getSelectedItem().toString();

                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response.toString().length() > 2) {
                                        String done = JsonHandler.parseString(response, "done");
                                        if (done.matches("true")) {
                                            redText.setText("Profile Update Succesfully");
                                            //id = Integer.parseInt(JsonHandler.parseString(response, "userID"));
                                            AlertDialog.Builder builder = new AlertDialog.Builder(EditActivity.this);
                                            builder.setMessage("Profile Update Succesfully")
                                                    .setCancelable(false)
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            //Intent info = new Intent(EditActivity.this, InfoActivity.class);
                                                            //main.putExtra("id", id);
                                                            //info.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                            //startActivity(main);
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
                                    //redText.setText("Username or Email Address already exist!");
                                }
                            }) {
                    };
                    //******** No retry*************
                    jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(30), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(EditActivity.this).addToRequestQueue(jsonArrayRequest);
                }
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        //memberID = getIntent().getIntExtra("memberID",-1);
        memberID = sp.getInt("memberID",-1);
        if(memberID != -1) {
            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + memberID;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            firstnameInp.setText(JsonHandler.parseString(response, "Fname"));
                            lastnameInp.setText(JsonHandler.parseString(response, "Lname"));
                            String gender = (JsonHandler.parseString(response,"Gender"));
                            genderDrop.setSelection(gender.compareToIgnoreCase("M") == 0 ? 0 : 1 );
                            phonenumberInp.setText(JsonHandler.parseString(response, "PhoneNumber"));
                            String birthDate = JsonHandler.parseString(response,"Birthdate");
                            yearInp.setText(birthDate.substring(0,5) );
                            dateInp.setText(birthDate.substring(8,10) );
                            monthDrop.setSelection(Integer.parseInt(birthDate.substring(5,7))-1 > 0 ? Integer.parseInt(birthDate.substring(5,7))-1 : 0);
                            addressInp.setText(JsonHandler.parseString(response, "Address"));
                            postcodeInp.setText(JsonHandler.parseString(response, "ZipCode"));
                            String province = (JsonHandler.parseString(response,"Province"));
                            String district = (JsonHandler.parseString(response,"District"));
                            String subdistrict = (JsonHandler.parseString(response, "SubDistrict"));
                            for(int i=0; i<provinces.length; i++){
                                if (province.equals(provinces[i])){
                                    provinceDrop.setSelection(i);
                                    districts.clear();
                                    for(int j=1; j<JsonHandler.places[i].length; j++){
                                        districts.add(JsonHandler.places[i][j][0]);
                                        if(district.matches(JsonHandler.places[i][j][0])){
                                            districtAdapt.notifyDataSetChanged();
                                            districtDrop.setSelection(j-1);
                                            subdistricts.clear();
                                            for(int k=1; k<JsonHandler.places[i][j].length; k++){
                                                subdistricts.add(JsonHandler.places[i][j][k]);
                                                if(subdistrict.matches(JsonHandler.places[i][j][k])){
                                                    subdistrictAdapt.notifyDataSetChanged();
                                                    subdistrictDrop.setSelection(k-1);
                                                }
                                            }
                                        }
                                    }
                                    districtAdapt.notifyDataSetChanged();
                                    break;
                                }
                            }


                            //********************** Collect All data to see if anything have been change **********************
                            /*for(int i=0; i<editTexts.length; i++){
                                data[i] = editTexts[i].getText().toString();
                            }
                            for(int i=editTexts.length; i<editTexts.length+spinners.length; i++){
                                data[i] = spinners[i-editTexts.length].getSelectedItem().toString();
                            }*/
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            redText.setText(error.getMessage()+ "\nFailed to retrive information \nMemberID = "+memberID);
                        }
                    }) {
            };
            MySingleton.getInstance(EditActivity.this).addToRequestQueue(jsonArrayRequest);
        }else{
            redText.setText("Failed to recognize member");
        }
    }
}
