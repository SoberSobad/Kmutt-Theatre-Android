package com.kmutt.sit.theater.membership;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class PointHistoryActivity extends AppCompatActivity {

    static int memberID;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";

    TableLayout tl;
    TextView tvName;
    TextView tvPoint;
    //ImageView avatar;

    String[] dates;
    String[] types;
    String[] points;
    String[] prefixs;
    List<TextView> textViews = new ArrayList<>();
    List<TableRow> tableRows = new ArrayList<>();

    String name = "";
    String point = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_history);

        sp = getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
        memberID = sp.getInt("memberID",-1);

        tl = (TableLayout) findViewById(R.id.tableLayout);
        tvName = findViewById(R.id.tvName);
        tvPoint = findViewById(R.id.tvPoint);
    }

    @Override
    protected void onResume() {
        super.onResume();
        memberID = sp.getInt("memberID", -1);
        tvName.setText(sp.getString("name",""));
        tvPoint.setText("Point : " + sp.getString("point", ""));

        String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetPointHistory?memberID=" + memberID;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        dates = JsonHandler.parseString2(response, "date");
                        types = JsonHandler.parseString2(response, "type");
                        points = JsonHandler.parseString2(response, "point");
                        prefixs = JsonHandler.parseString2(response, "prefix");
                        refresh();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //redText.setText(error.getMessage()+ "\nFailed to retrive information \nMemberID = "+memberID);
                    }
                }) {
        };
        MySingleton.getInstance(PointHistoryActivity.this).addToRequestQueue(jsonArrayRequest);

    }

    protected void refresh() {

        tl.removeAllViews();

        for (int i = 0; i < dates.length; i++) {
            TableRow tr = new TableRow(this);
            tr.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));

            TextView tvDate = new TextView(this);
            tvDate.setText(dates[i]);
            tvDate.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 3));

            TextView tvType = new TextView(this);
            tvType.setText(types[i]);
            tvType.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 3));

            TextView tvPoint = new TextView(this);
            tvPoint.setText(prefixs[i]+" "+points[i]);
            tvPoint.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT, 2));

            TableRow.LayoutParams params = (TableRow.LayoutParams) tvDate.getLayoutParams();
            params.height = 100;
            tvDate.setLayoutParams(params);

            tr.addView(tvDate);
            tr.addView(tvType);
            tr.addView(tvPoint);
            /*
            textViews.add(tvDate);
            textViews.add(tvType);
            textViews.add(tvPoint);
            */
            tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }
}
