package com.kmutt.sit.theater.membership;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import java.sql.*;

import com.kmutt.sit.theater.MainActivity;
import com.kmutt.sit.theater.R;

public class RegisterActivity extends AppCompatActivity {


        /*try {
            Connection myConn = DriverManager.getConnection("10.4.56.40","user01","user01");
        } catch (SQLException e) {
            e.printStackTrace();
        }*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner provinceDrop = findViewById(R.id.provinceSpin);
        String[] provinces = new String[]{"Amnat Charoen","Ang Thong","Bangkok","Bueng Kan"
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provinces);
        provinceDrop.setAdapter(adapter);

        Spinner genderDrop = findViewById(R.id.genderSpin);
        String[] genders = new String[]{"Male", "Female"};
        //ArrayAdapter<String> genderSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        ArrayAdapter<String> genderSpinAdapt = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, genders);
        genderDrop.setAdapter(genderSpinAdapt);


    }
}
