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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    final String[] provinces = new String[JsonHandler.places.length];
    final List<String> districts = new ArrayList<>();
    final List<String> subdistricts = new ArrayList<>();

    EditText[] editTexts;
    Spinner[] spinners;

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

        final String[] months = new String[]{"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
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
                            firstnameInp.setText(JsonHandler.parseString(response, "Fname"));
                            lastnameInp.setText(JsonHandler.parseString(response, "Lname"));
                            String gender = JsonHandler.parseString(response,"Gender");
                            genderDrop.setSelection( gender.compareToIgnoreCase("M") == 0 ? 0 : 1 );
                            phonenumberInp.setText(JsonHandler.parseString(response, "PhoneNumber"));
                            String birthDate = JsonHandler.parseString(response,"Birthdate");
                            yearInp.setText( birthDate.substring(0,5) );
                            dateInp.setText( birthDate.substring(8,10) );
                            monthDrop.setSelection( Integer.parseInt(birthDate.substring(5,7))-1 );
                            addressInp.setText(JsonHandler.parseString(response, "Address"));
                            postcodeInp.setText(JsonHandler.parseString(response, "ZipCode"));
                            String province = JsonHandler.parseString(response,"Province");
                            String district = JsonHandler.parseString(response,"District");
                            String subdistrict = JsonHandler.parseString(response, "SubDistrict");
                            for(int i=0; i<provinces.length; i++){
                                if (province.equals(provinces[i])){
                                    provinceDrop.setSelection(i);
                                    break;
                                }
                            }
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
