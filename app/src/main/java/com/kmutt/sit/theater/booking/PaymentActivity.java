package com.kmutt.sit.theater.booking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.kmutt.sit.theater.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        final Intent paymentAct = new Intent(PaymentActivity.this, PaymentActivity.class);

        Button paymentButt = findViewById(R.id.payButt);
        final TextView cardNo = findViewById(R.id.cardNoField);
        final TextView cardHolder = findViewById(R.id.cardHolderField);
        final TextView cardCsv = findViewById(R.id.csvField);
        final TextView cardExpiry = findViewById(R.id.expiryField);
        final TextView errorMsg = findViewById(R.id.errorMsg);

        String[] cardTypes = {"Visa","MasterCard"};

        Spinner cardType = findViewById(R.id.cardType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, cardTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cardType.setAdapter(adapter);

        paymentButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( !(
                        cardNo.getText().toString().length() < 16 ||
                        cardHolder.getText().toString().length() < 3 ||
                        cardCsv.getText().toString().length() < 3 ||
                        !cardExpiry.getText().toString().matches("^\\d{2}/\\d{4}")
                ))
                {
                    errorMsg.setVisibility(View.INVISIBLE);
                    //TODO: Send the input data to payment SQL table
                    //TODO: Move to next activity
                } else {
                    errorMsg.setVisibility(View.VISIBLE);
                }
            }
        });

    }


}
