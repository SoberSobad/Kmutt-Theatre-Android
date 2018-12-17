package com.kmutt.sit.theater.membership;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

    EditText[] editTexts;
    Spinner[] spinners;
    String data[];
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        memberID = getIntent().getIntExtra("memberID",-1);

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

        final ArrayAdapter<String> districtAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, districts);
        districtDrop.setAdapter(districtAdapt);

        final ArrayAdapter<String> subdistrictAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, subdistricts);
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
        data = new String [editTexts.length + spinners.length];

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


        //******************************************OnClickListener for Submit Button***********************
        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //********************** Check if anything have been change **********************
                /*boolean changed = true;
                for(int i=1; i<editTexts.length; i++){
                    if(!(data[i].matches(editTexts[i].getText().toString()))) {
                        changed = false;
                        break;
                    }
                }
                for(int i=editTexts.length; i<editTexts.length+spinners.length; i++){
                    if(!(data[i].matches(spinners[i-editTexts.length].getSelectedItem().toString()))) {
                        changed = false;
                        break;
                    }
                }
                if(changed) {
                */
                    String url = "http://theatre.sit.kmutt.ac.th/customer/androidUpdate?userID=" + memberID + "&pass=" + JsonHandler.md5(confirmpassInp.getText().toString()) +
                            "&firstname=" + firstnameInp.getText() + "&lastname=" + lastnameInp.getText() + "&gender=" + genderDrop.getSelectedItem().toString() +
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
                    jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20), 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    MySingleton.getInstance(EditActivity.this).addToRequestQueue(jsonArrayRequest);
                /*}else{
                    redText.setText("Nothing have been changed");
                }*/
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        memberID = getIntent().getIntExtra("memberID",-1);
        if(memberID != -1) {
            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + memberID;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            firstnameInp.setText(data[1] = JsonHandler.parseString(response, "Fname"));
                            lastnameInp.setText(data[2] = JsonHandler.parseString(response, "Lname"));
                            String gender = (data[8] = JsonHandler.parseString(response,"Gender"));
                            genderDrop.setSelection(gender.compareToIgnoreCase("M") == 0 ? 0 : 1 );
                            phonenumberInp.setText(data[5] = JsonHandler.parseString(response, "PhoneNumber"));
                            String birthDate = JsonHandler.parseString(response,"Birthdate");
                            yearInp.setText(data[3] = birthDate.substring(0,5) );
                            dateInp.setText(data[4] = birthDate.substring(8,10) );
                            data[9] = months[Integer.parseInt(birthDate.substring(5,7))-1];
                            monthDrop.setSelection(Integer.parseInt(birthDate.substring(5,7))-1);
                            addressInp.setText(data[6] = JsonHandler.parseString(response, "Address"));
                            postcodeInp.setText(data[7] = JsonHandler.parseString(response, "ZipCode"));
                            String province = (data[10] = JsonHandler.parseString(response,"Province"));
                            String district = (data[11] = JsonHandler.parseString(response,"District"));
                            String subdistrict = (data[12] = JsonHandler.parseString(response, "SubDistrict"));
                            for(int i=0; i<provinces.length; i++){
                                if (province.equals(provinces[i])){
                                    provinceDrop.setSelection(i);
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
