package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

public class RegisterActivity extends AppCompatActivity {


    static int mode;
    static int id;

    final String[] provinces = new String[JsonHandler.places.length];
    final List<String> districts = new ArrayList<>();
    final List<String> subdistricts = new ArrayList<>();
    int provinceNo;
    int districtNo;
    int subdistrictNo;

    //************************* Initializing ************************************************
    Spinner provinceDrop;
    Spinner districtDrop;
    Spinner subdistrictDrop;
    Spinner genderDrop;
    Spinner monthDrop;

    EditText userInp;             EditText firstnameInp;        EditText lastnameInp;
    EditText dateInp;             EditText yearInp;             EditText emailInp;
    EditText phonenumberInp;      EditText addressInp;          EditText identInp;
    EditText postcodeInp;         EditText passwordInp;         EditText confirmpassInp;
    EditText moneyInp;

    TextView modeHeader;         TextView confirmpassTxt;    TextView redText;

    Button submitButt;
    Button editButt;
    Button topupButt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mode = getIntent().getIntExtra("mode",2);
        id = getIntent().getIntExtra("id",-1);


        for(int i=0; i<JsonHandler.places.length; i++){
            provinces[i] = JsonHandler.places[i][0][0];
        }

        //********************************************  FIND VIEW *******************************
        provinceDrop = findViewById(R.id.provinceSpin);
        districtDrop = findViewById(R.id.districtSpin);
        subdistrictDrop = findViewById(R.id.subdistrictSpin);
        genderDrop = findViewById(R.id.genderSpin);
        monthDrop = findViewById(R.id.monthSpin);

        userInp = findViewById(R.id.usernameInp);          firstnameInp = findViewById(R.id.firstnameInp);    lastnameInp = findViewById(R.id.lastnameInp);
        dateInp = findViewById(R.id.dateInp);              yearInp = findViewById(R.id.yearInp);              emailInp = findViewById(R.id.emailInp);
        phonenumberInp = findViewById(R.id.phoneNoInp);    addressInp = findViewById(R.id.addressInp);        identInp = findViewById(R.id.IDInp);
        postcodeInp = findViewById(R.id.zipcodeInp);       passwordInp = findViewById(R.id.expiryField);      confirmpassInp = findViewById(R.id.confirmpassInp);
        moneyInp = findViewById(R.id.moneyInp);

        modeHeader = findViewById(R.id.modeHeader);        confirmpassTxt = findViewById(R.id.confirmpassText);   redText = findViewById(R.id.regisRedText);

        submitButt = findViewById(R.id.submitButt);
        editButt = findViewById(R.id.editButt);
        topupButt = findViewById(R.id.topupButt);

        //******************************************** Spinner set Adapter *********************
        final ArrayAdapter<String> provinceAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        provinceDrop.setAdapter(provinceAdapt);

        final ArrayAdapter<String> districtAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        districtDrop.setAdapter(districtAdapt);

        final ArrayAdapter<String> subdistrictAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subdistricts);
        subdistrictDrop.setAdapter(subdistrictAdapt);

        final String[] genders = new String[]{"Male", "Female"};
        final ArrayAdapter<String> genderSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        genderDrop.setAdapter(genderSpinAdapt);

        final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        final ArrayAdapter<String> monthSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthDrop.setAdapter(monthSpinAdapt);

        //********************************************* Assisgn to arrays *************************
        final EditText[] editTexts = {userInp,passwordInp,confirmpassInp,firstnameInp,lastnameInp,yearInp,dateInp,emailInp,phonenumberInp,addressInp,postcodeInp};
        final Spinner[] spinners = {genderDrop, monthDrop, provinceDrop};

        //********************************************* Selected Listener for Places spinners ***********
        provinceDrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                districts.clear();
                for(int i=1; i<JsonHandler.places[position].length; i++){
                    districts.add(JsonHandler.places[position][i][0]);
                }
                districtAdapt.notifyDataSetChanged();
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
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {            }
        });

        //******************************************** On Click Listener for Button *******************************
        topupButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {       //TODO: Reconstruct php request  I don't think lines below startActivity is necessary ********************************
                Intent topupAct = new Intent(RegisterActivity.this, TopupActivity.class);
                topupAct.putExtra("id",id);
                startActivity(topupAct);
            }
        });

        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redText.setText("Please wait...");

                //***** Set delay *********************
                submitButt.setEnabled(false);
                submitButt.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        submitButt.setEnabled(true);
                    }
                }, 5000);

                //********************************************** Check for space *********************************************
                if (firstnameInp.getText().toString().matches("") & lastnameInp.getText().toString().matches("") &
                        passwordInp.getText().toString().matches("") & emailInp.getText().toString().matches("") &
                        phonenumberInp.getText().toString().matches("") & dateInp.getText().toString().matches("") &
                        yearInp.getText().toString().matches("") & postcodeInp.getText().toString().matches("")) {
                    redText.setText("Please fill every space provided");
                } else {

                    if (mode == 1) {
                        if (passwordInp.getText().toString().matches(confirmpassInp.getText().toString())) {    // Matching Password and Confirm Password
                            String url = "http://theatre.sit.kmutt.ac.th/customer/androidRegist?user=" + userInp.getText() + "&pass=" + passwordInp.getText() +
                                    "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + genderDrop.getSelectedItem().toString() +
                                    "&birthdate=" + yearInp.getText() + "-" + (monthDrop.getSelectedItemPosition() + 1) + "-" + dateInp.getText() +
                                    "&email=" + emailInp.getText() + "&phonenumber=" + phonenumberInp.getText() + "&address=" + addressInp.getText() +
                                    "&district=" + districtDrop.getSelectedItem().toString() + "&province=" + provinceDrop.getSelectedItem().toString() + "&postcode=" + postcodeInp.getText() +
                                    "&subdist=" + subdistrictDrop.getSelectedItem().toString() + "&idcard=" + identInp.getText();

                            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            if (response.toString().length() > 2) {
                                                String done = JsonHandler.parseString(response, "done");
                                                if(done.matches("true")) {
                                                    redText.setText("Account have been created successfully");
                                                    Intent main = new Intent(RegisterActivity.this, MainActivity.class);
                                                    id = Integer.parseInt(JsonHandler.parseString(response, "userID"));
                                                    main.putExtra("id", id);
                                                    main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(main);
                                                }else{
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
                            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonArrayRequest);
                        } else {
                            redText.setText("Passwords aren't matching");
                        }
                    }
                    if (mode == 3) {
                        if (passwordInp.getText().toString().matches(confirmpassInp.getText().toString())) {
                            redText.setText("");
                            String url = "http://theatre.sit.kmutt.ac.th/group6/update?id=" + id + "&pass=" + passwordInp.getText() +
                                    "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + genderDrop.getSelectedItem().toString() +
                                    "&birthdate=" + yearInp.getText() + "-" + (monthDrop.getSelectedItemPosition() + 1) + "-" + dateInp.getText() +
                                    "&email=" + emailInp.getText() + "&phonenumber=" + phonenumberInp.getText() + "&address=" + addressInp.getText() +
                                    "&district=" + districtDrop.getSelectedItem().toString() + "&province=" + provinceDrop.getSelectedItem().toString() + "&postcode=" + postcodeInp.getText();
                            runPHP(url);
                            mode = 2;
                            modeHeader.setText("Personal info");
                            submitButt.setText("SUBMIT");
                            editButt.setVisibility(View.VISIBLE);
                            submitButt.setVisibility(View.INVISIBLE);
                            confirmpassInp.setVisibility(View.INVISIBLE);
                            topupButt.setVisibility(View.VISIBLE);
                            moneyInp.setVisibility(View.VISIBLE);

                            for (int i = 0; i < editTexts.length; i++) {
                                editTexts[i].setFocusable(false);
                                editTexts[i].setFocusableInTouchMode(false);
                                editTexts[i].setClickable(false);
                            }
                            String urll = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id=" + id;
                            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                                    (Request.Method.GET, urll, null, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            //addressInp.setText(response.toString());
                                            userInp.setText(JsonHandler.parseString(response, "Username"));
                                            passwordInp.setText(JsonHandler.parseString(response, "Password"));
                                            firstnameInp.setText(JsonHandler.parseString(response, "FirstName"));
                                            lastnameInp.setText(JsonHandler.parseString(response, "LastName"));
                                            String birthDate = JsonHandler.parseString(response, "Birthdate");
                                            yearInp.setText(birthDate.substring(0, 5));
                                            dateInp.setText(birthDate.substring(8, 10));
                                            monthDrop.setSelection(Integer.parseInt(birthDate.substring(5, 7)) - 1);
                                            emailInp.setText(JsonHandler.parseString(response, "Email"));
                                            phonenumberInp.setText(JsonHandler.parseString(response, "PhoneNumber"));
                                            addressInp.setText(JsonHandler.parseString(response, "Address"));
                                            //districtInp.setText( JsonHandler.parseString(response,"District") );
                                            postcodeInp.setText(JsonHandler.parseString(response, "Postcode"));
                                            moneyInp.setText(JsonHandler.parseString(response, "Money"));
                                            String gender = JsonHandler.parseString(response, "Gender");
                                            String province = JsonHandler.parseString(response, "Province");
                                            genderDrop.setSelection(gender.compareToIgnoreCase("Male") == 0 ? 0 : 1);
                                            for (int i = 0; i < provinces.length; i++) {
                                                if (province.equals(provinces[i])) {
                                                    provinceDrop.setSelection(i);
                                                    break;
                                                }
                                            }
                                            //Todo Spinnerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            // TODO: Handle error
                                        }
                                    }) {
                            };
                            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjectRequest);

                            for (int i = 0; i < spinners.length; i++) {
                                spinners[i].setEnabled(false);
                            }

                        } else {
                            redText.setText("Password didn't matches");
                        }
                    }
                                        /*} else {
                                            redText.setText("Username or Phone Number is already exist : ");
                                        }
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // TODO: Handle error
                                        //uniqness = true;
                                        //redText.setText(uniqness+ "Successssss");
                                    }
                                }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                return params;
                            }
                        };
                        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjectRequest);
                        */

                }
            }
        });

        editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode = 3;

                Intent editIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
                editIntent.putExtra("id",id);
                editIntent.putExtra("mode",3);
                editIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);


/*
                submitButt.setText("SAVE");
                modeHeader.setText("Editing..");
                editButt.setVisibility(View.INVISIBLE);
                submitButt.setVisibility(View.VISIBLE);
                confirmpassInp.setVisibility(View.VISIBLE);
                topupButt.setVisibility(View.INVISIBLE);
                moneyInp.setVisibility(View.INVISIBLE);
                for(int i=0; i<editTexts.length; i++) {
                    editTexts[i].setFocusable(true);
                    editTexts[i].setFocusableInTouchMode(true);
                    editTexts[i].setClickable(true);
                }
                userInp.setFocusable(false);
                userInp.setFocusableInTouchMode(false);
                userInp.setClickable(false);
                for(int i=0; i<spinners.length; i++){
                    spinners[i].setEnabled(true);
                }
*/
            }
        });
    }

    protected void runPHP(String url){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest

                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {


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
                Map<String, String>  params = new HashMap<String, String>();
                return params;
            }
        };
        MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onBackPressed() {
        if(mode == 2) { //TODO: Are all these code really necessary? Won't finish() just send it back to mainActivity anyways?
            /*Intent main = new Intent(RegisterActivity.this, MainActivity.class);
            main.putExtra("id",id);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);*/
            finish();
        }
        if(mode == 3){
            Intent personalInfo = new Intent(RegisterActivity.this, RegisterActivity.class);
            personalInfo.putExtra("id",id);
            personalInfo.putExtra("mode",2);
            personalInfo.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(personalInfo);
            finish();
        }
        if(mode == 1){
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        final EditText[] editTexts = {userInp,passwordInp,confirmpassInp,firstnameInp,lastnameInp,yearInp,dateInp,emailInp,phonenumberInp,addressInp,postcodeInp};
        final Spinner[] spinners = {genderDrop, monthDrop, provinceDrop};

        if (mode == 1) {
            confirmpassTxt.setVisibility(View.VISIBLE);
            modeHeader.setText("REGISTER");
            editButt.setVisibility(View.INVISIBLE);
            submitButt.setVisibility(View.VISIBLE);
            confirmpassInp.setVisibility(View.VISIBLE);
            topupButt.setVisibility(View.INVISIBLE);
            moneyInp.setVisibility(View.INVISIBLE);
        }
        if (mode == 2){
            confirmpassTxt.setVisibility(View.INVISIBLE);
            modeHeader.setText("Personal info");
            editButt.setVisibility(View.VISIBLE);
            submitButt.setVisibility(View.INVISIBLE);
            confirmpassInp.setVisibility(View.INVISIBLE);
            topupButt.setVisibility(View.VISIBLE);
            moneyInp.setVisibility(View.VISIBLE);
            for(int i=0; i<editTexts.length; i++) {
                editTexts[i].setFocusable(false);
                editTexts[i].setFocusableInTouchMode(false);
                editTexts[i].setClickable(false);
            }
            String url = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id="+id;
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //addressInp.setText(response.toString());
                            userInp.setText( JsonHandler.parseString(response,"Username") );
                            passwordInp.setText( JsonHandler.parseString(response,"Password") );
                            firstnameInp.setText( JsonHandler.parseString(response,"FirstName") );
                            lastnameInp.setText( JsonHandler.parseString(response,"LastName") );
                            String birthDate = JsonHandler.parseString(response,"Birthdate");
                            yearInp.setText( birthDate.substring(0,5) );
                            dateInp.setText( birthDate.substring(8,10) );
                            monthDrop.setSelection( Integer.parseInt(birthDate.substring(5,7))-1 );
                            emailInp.setText( JsonHandler.parseString(response,"Email") );
                            phonenumberInp.setText( JsonHandler.parseString(response,"PhoneNumber") );
                            addressInp.setText( JsonHandler.parseString(response,"Address") );
                            //districtInp.setText( JsonHandler.parseString(response,"District") );
                            postcodeInp.setText( JsonHandler.parseString(response,"Postcode") );
                            moneyInp.setText( JsonHandler.parseString(response,"Money") );
                            String gender = JsonHandler.parseString(response,"Gender");
                            String province = JsonHandler.parseString(response,"Province");
                            genderDrop.setSelection( gender.compareToIgnoreCase("Male") == 0 ? 0 : 1 );
                            for(int i=0; i<provinces.length; i++){
                                if (province.equals(provinces[i])){
                                    provinceDrop.setSelection(i);
                                    break;
                                }
                            }
                            //Todo Spinnerrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String>  params = new HashMap<String, String>();
                    return params;
                }
            };
            MySingleton.getInstance(RegisterActivity.this).addToRequestQueue(jsonObjectRequest);

            for(int i=0; i<spinners.length; i++){
                spinners[i].setEnabled(false);
            }
        }
        if (mode == 3) {
            confirmpassTxt.setVisibility(View.VISIBLE);
            submitButt.setText("SAVE");
            modeHeader.setText("Editing..");
            editButt.setVisibility(View.INVISIBLE);
            submitButt.setVisibility(View.VISIBLE);
            confirmpassInp.setVisibility(View.VISIBLE);
            topupButt.setVisibility(View.INVISIBLE);
            moneyInp.setVisibility(View.INVISIBLE);
            for(int i=0; i<editTexts.length; i++) {
                editTexts[i].setFocusable(true);
                editTexts[i].setFocusableInTouchMode(true);
                editTexts[i].setClickable(true);
            }
            userInp.setFocusable(false);
            userInp.setFocusableInTouchMode(false);
            userInp.setClickable(false);
            for(int i=0; i<spinners.length; i++){
                spinners[i].setEnabled(true);
            }
        }
    }
}
