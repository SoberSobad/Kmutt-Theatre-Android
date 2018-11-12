package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;
import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity {


    static boolean uniqness;
    static int mode;
    static int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mode = getIntent().getIntExtra("mode",2);
        id = getIntent().getIntExtra("id",-1);

        final TextView modeHeader = findViewById(R.id.modeHeader);

        final Spinner provinceDrop = findViewById(R.id.provinceSpin);
        final String[] provinces = new String[]{"Amnat Charoen","Ang Thong","Bangkok","Bueng Kan"
                + "Buriram","Chachoengsao","Chai Nat","Chaiyaphum"
                + "Chanthaburi","Chiang Mai","Chiang Rai","Chonburi"
                + "Chumphon","Kalasin","Kamphaeng Phet","Kanchanaburi"
                + "Khon Kaen","Krabi","Lampang","Lamphun"
                + "Loei","Lopburi","Mae Hong Son","Maha Sarakham"
                + "Mukdahan","Nakhon Nayok","Nakhon Pathom","Nakhon Phanom"
                + "Nakhon Ratchasima","Nakhon Sawan","Nakhon Si Thammarat","Nan"
                + "Narathiwat","Nong Bua Lam Phu","Nong Khai","Nonthaburi"
                + "Pathum Thani","Pattani","Phang Nga","Phatthalung"
                + "Phayao","Phetchabun","Phetchaburi","Phichit"
                + "Phitsanulok","Phra Nakhon Si Ayutthaya","Phrae","Phuket"
                + "Prachinburi","Prachuap Khiri Khan","Ranong","Ratchaburi"
                + "Rayong","Roi Et","Sa Kaeo","Sakon Nakhon"
                + "Samut Prakan","Samut Sakhon","Samut Songkhram","Saraburi"
                + "Satun","Sing Buri","Sisaket","Songkhla"
                + "Sukhothai","Suphan Buri","Surat Thani","Surin"
                + "Tak","Trang","Trat","Ubon Ratchathani"
                + "Udon Thani","Uthai Thani","Uttaradit","Yala"
                + "Yasothon"};
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        provinceDrop.setAdapter(adapter);

        final Spinner genderDrop = findViewById(R.id.genderSpin);
        String[] genders = new String[]{"Male", "Female"};
        ArrayAdapter<String> genderSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        genderDrop.setAdapter(genderSpinAdapt);

        final Spinner monthDrop = findViewById(R.id.monthSpin);
        final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        ArrayAdapter<String> monthSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, months);
        monthDrop.setAdapter(monthSpinAdapt);

        final EditText userInp = findViewById(R.id.usernameInp);          final EditText firstnameInp = findViewById(R.id.firstNameInp);    final EditText lastnameInp = findViewById(R.id.lastNameInp);
        final EditText dateInp = findViewById(R.id.dateInp);              final EditText yearInp = findViewById(R.id.yearInp);              final EditText emailInp = findViewById(R.id.emailInp);
        final EditText phonenumberInp = findViewById(R.id.phoneNoInp);    final EditText addressInp = findViewById(R.id.addressInp);        final EditText districtInp = findViewById(R.id.districtInp);
        final EditText postcodeInp = findViewById(R.id.zipcodeInp);       final EditText passwordInp = findViewById(R.id.passwordInp);      final EditText confirmpassInp = findViewById(R.id.confirmpassInp);

        final TextView redText = findViewById(R.id.regisRedText);

        final Button submitButt = findViewById(R.id.submitButt);
        final Button editButt = findViewById(R.id.editButt);
        final Button topupButt = findViewById(R.id.topupButt);



    /*
        genderDrop.setFocusable(false);
        genderDrop.setFocusableInTouchMode(false);
        genderDrop.setClickable(false);

        genderDrop.setSelection(1);
        genderDrop.setEnabled(false);
    */
        //EditText[] editTexts = {userInp, firstnameInp, lastnameInp, dateInp, yearInp, emailInp, phonenumberInp, addressInp, districtInp, postcodeInp, passwordInp, confirmpassInp};
        final EditText[] editTexts = {userInp,passwordInp,confirmpassInp,firstnameInp,lastnameInp,yearInp,dateInp,emailInp,phonenumberInp,addressInp,districtInp,postcodeInp};
        final Spinner[] spinners = {genderDrop, monthDrop, provinceDrop};
        final EditText moneyInp = findViewById(R.id.moneyInp);

        topupButt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent topupAct = new Intent(RegisterActivity.this, TopupActivity.class);
                topupAct.putExtra("id",id);
                startActivity(topupAct);
                //moneyInp.setText("99999");
                String url = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id="+id;
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                        (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                moneyInp.setText( JsonarrayParseString.parseString2(response,"Money",0) );
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
        });

        if(mode == 1){  //Register
            modeHeader.setText("REGISTER");
            editButt.setVisibility(View.INVISIBLE);
            submitButt.setVisibility(View.VISIBLE);
            confirmpassInp.setVisibility(View.VISIBLE);
            topupButt.setVisibility(View.INVISIBLE);
            moneyInp.setVisibility(View.INVISIBLE);
        }

        if(mode == 2){
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
                                userInp.setText( JsonarrayParseString.parseString2(response,"Username",0) );
                                passwordInp.setText( JsonarrayParseString.parseString2(response,"Password",0) );
                                firstnameInp.setText( JsonarrayParseString.parseString2(response,"FirstName",0) );
                                lastnameInp.setText( JsonarrayParseString.parseString2(response,"LastName",0) );
                                String birthDate = JsonarrayParseString.parseString2(response,"Birthdate",0);
                                yearInp.setText( birthDate.substring(0,5) );
                                dateInp.setText( birthDate.substring(8,10) );
                                monthDrop.setSelection( Integer.parseInt(birthDate.substring(5,7))-1 );
                                emailInp.setText( JsonarrayParseString.parseString2(response,"Email",0) );
                                phonenumberInp.setText( JsonarrayParseString.parseString2(response,"PhoneNumber",0) );
                                addressInp.setText( JsonarrayParseString.parseString2(response,"Address",0) );
                                districtInp.setText( JsonarrayParseString.parseString2(response,"District",0) );
                                postcodeInp.setText( JsonarrayParseString.parseString2(response,"Postcode",0) );
                                moneyInp.setText( JsonarrayParseString.parseString2(response,"Money",0) );
                                String gender = JsonarrayParseString.parseString2(response,"Gender",0);
                                String province = JsonarrayParseString.parseString2(response,"Province",0);
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

        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //boolean uniqness = checkUniqness("http://theatre.sit.kmutt.ac.th/group6/checkUniqness?user=" + userInp.getText() + "&phoneno=" + phonenumberInp.getText());


                if(mode == 1) {
                    String checkUniqUrl = "http://theatre.sit.kmutt.ac.th/group6/checkUniqness?user=" + userInp.getText() + "&phoneno=" + phonenumberInp.getText();
                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest

                            (Request.Method.GET, checkUniqUrl, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response.toString().length() > 2) uniqness = false;
                                    else uniqness = true;
                                    //redText.setText(uniqness+ response.toString());
                                    if (uniqness) {
                                        if (firstnameInp.getText().toString().matches("") & lastnameInp.getText().toString().matches("") &
                                                passwordInp.getText().toString().matches("") & emailInp.getText().toString().matches("") &
                                                phonenumberInp.getText().toString().matches("") & dateInp.getText().toString().matches("") &
                                                yearInp.getText().toString().matches("") & postcodeInp.getText().toString().matches("")) {
                                            redText.setText("Please fill every space provided");
                                        } else {
                                            if (passwordInp.getText().toString().matches(confirmpassInp.getText().toString())) {
                                                redText.setText("");
                                                String url = "http://theatre.sit.kmutt.ac.th/group6/regis?user=" + userInp.getText() + "&pass=" + passwordInp.getText() +
                                                        "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + genderDrop.getSelectedItem().toString() +
                                                        "&birthdate=" + yearInp.getText() + "-" + (monthDrop.getSelectedItemPosition() + 1) + "-" + dateInp.getText() +
                                                        "&email=" + emailInp.getText() + "&phonenumber=" + phonenumberInp.getText() + "&address=" + addressInp.getText() +
                                                        "&district=" + districtInp.getText() + "&province=" + provinceDrop.getSelectedItem().toString() + "&postcode=" + postcodeInp.getText();
                                                runPHP(url);
                                            } else {
                                                redText.setText("Password didn't matches");
                                            }

                                        }
                                    } else {
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

                    // TODO insert delay here ***********************************************************************************************
                }
                if (mode == 3){
                    /*String checkUniqUrl = "http://theatre.sit.kmutt.ac.th/group6/checkUniqness?user=" + userInp.getText() + "&phoneno=" + phonenumberInp.getText();
                    JsonArrayRequest jsonObjectRequest = new JsonArrayRequest

                            (Request.Method.GET, checkUniqUrl, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    if (response.toString().length() > 2) uniqness = false;
                                    else uniqness = true;
                                    //redText.setText(uniqness+ response.toString());
                                    if (uniqness) {*/
                                        if (firstnameInp.getText().toString().matches("") & lastnameInp.getText().toString().matches("") &
                                                passwordInp.getText().toString().matches("") & emailInp.getText().toString().matches("") &
                                                phonenumberInp.getText().toString().matches("") & dateInp.getText().toString().matches("") &
                                                yearInp.getText().toString().matches("") & postcodeInp.getText().toString().matches("")) {
                                            redText.setText("Please fill every space provided");
                                        } else {
                                            if (passwordInp.getText().toString().matches(confirmpassInp.getText().toString())) {
                                                redText.setText("");
                                                String url = "http://theatre.sit.kmutt.ac.th/group6/update?id=" + id + "&pass=" + passwordInp.getText() +
                                                        "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + genderDrop.getSelectedItem().toString() +
                                                        "&birthdate=" + yearInp.getText() + "-" + (monthDrop.getSelectedItemPosition() + 1) + "-" + dateInp.getText() +
                                                        "&email=" + emailInp.getText() + "&phonenumber=" + phonenumberInp.getText() + "&address=" + addressInp.getText() +
                                                        "&district=" + districtInp.getText() + "&province=" + provinceDrop.getSelectedItem().toString() + "&postcode=" + postcodeInp.getText();
                                                runPHP(url);
                                                mode=2;
                                                modeHeader.setText("Personal info");
                                                submitButt.setText("SUBMIT");
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
                                                String urll = "http://theatre.sit.kmutt.ac.th/group6/getInfo?id="+id;
                                                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                                                        (Request.Method.GET, urll, null, new Response.Listener<JSONArray>() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {
                                                                //addressInp.setText(response.toString());
                                                                userInp.setText( JsonarrayParseString.parseString2(response,"Username",0) );
                                                                passwordInp.setText( JsonarrayParseString.parseString2(response,"Password",0) );
                                                                firstnameInp.setText( JsonarrayParseString.parseString2(response,"FirstName",0) );
                                                                lastnameInp.setText( JsonarrayParseString.parseString2(response,"LastName",0) );
                                                                String birthDate = JsonarrayParseString.parseString2(response,"Birthdate",0);
                                                                yearInp.setText( birthDate.substring(0,5) );
                                                                dateInp.setText( birthDate.substring(8,10) );
                                                                monthDrop.setSelection( Integer.parseInt(birthDate.substring(5,7))-1 );
                                                                emailInp.setText( JsonarrayParseString.parseString2(response,"Email",0) );
                                                                phonenumberInp.setText( JsonarrayParseString.parseString2(response,"PhoneNumber",0) );
                                                                addressInp.setText( JsonarrayParseString.parseString2(response,"Address",0) );
                                                                districtInp.setText( JsonarrayParseString.parseString2(response,"District",0) );
                                                                postcodeInp.setText( JsonarrayParseString.parseString2(response,"Postcode",0) );
                                                                moneyInp.setText( JsonarrayParseString.parseString2(response,"Money",0) );
                                                                String gender = JsonarrayParseString.parseString2(response,"Gender",0);
                                                                String province = JsonarrayParseString.parseString2(response,"Province",0);
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

    protected Boolean checkUniqness(String url){
        //JSONArray jsonArray;
        final String[] responseLength = new String[1];
        final boolean[] result = new boolean[1];
        result[0] = true;
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest

                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {


                    @Override
                    public void onResponse(JSONArray response) {
                        //addressInp.setText(response.toString());
                        //result[0] = JsonarrayParseString.parseString(response,"ID",0);
                        //jsonArray = response;
                        boolean resulttt = (response.toString().length() != 0);
                        Toast.makeText(RegisterActivity.this, resulttt+" response = "+response.toString() , Toast.LENGTH_SHORT).show();
                        if(response.toString().length() != 0) result[0] = false;
                        responseLength[0] = response.toString();
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
        //Toast.makeText(this, "Uniqueness = "+responseLength[0], Toast.LENGTH_SHORT).show();
        return result[0];
    }

    @Override
    public void onBackPressed() {
        if(mode == 2) {
            Intent main = new Intent(RegisterActivity.this, MainActivity.class);
            main.putExtra("id",id);
            main.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(main);
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
}
