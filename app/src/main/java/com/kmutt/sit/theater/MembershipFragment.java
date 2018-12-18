package com.kmutt.sit.theater;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.kmutt.sit.theater.booking.movies.Movie;
import com.kmutt.sit.theater.membership.InfoActivity;
import com.kmutt.sit.theater.membership.JsonHandler;
import com.kmutt.sit.theater.membership.MembershipActivity;
import com.kmutt.sit.theater.membership.MySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MembershipFragment extends Fragment {

    static int memberID = -1;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    final String P_NAME = "App_Config";
    //
    // Views
    //
    View rootView;
    Button loginButt;
    Button infoButt;
    Button editButt;
    Button passwordChangeButt;
    TextView tvPoint;
    TextView pointShow;
    EditText memberInfo;
    ImageView avatar;

    public MembershipFragment() {
//        memberID = getArguments().getInt("memberID",-1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        this.rootView = inflater.inflate(R.layout.fragment_membership, container, false);

        sp = this.getActivity().getSharedPreferences(P_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();

        memberInfo = rootView.findViewById(R.id.memberInfo);
        loginButt = rootView.findViewById(R.id.loginButt);
        infoButt = rootView.findViewById(R.id.infoButt);
        editButt = rootView.findViewById(R.id.editButt);
        passwordChangeButt = rootView.findViewById(R.id.passwordChangeButt);
        avatar = rootView.findViewById(R.id.avatar);
        tvPoint = rootView.findViewById(R.id.tvPoint);
        pointShow = rootView.findViewById(R.id.pointShow);

        final Intent mbshipAct = new Intent(getActivity(), MembershipActivity.class);

        loginButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberID == -1) {
                    startActivity(mbshipAct);
                } else {
                    memberID = -1;
                    editor.putInt("memberID", memberID);
                    editor.commit();
                    onResume();
                }
            }
        });

        infoButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent infoAct = new Intent(getActivity(), InfoActivity.class);
                startActivity(infoAct);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        memberID = sp.getInt("memberID",-1);
        if(memberID != -1) {

            String url1 = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + memberID;
            JsonArrayRequest jsonArrayRequest1 = new JsonArrayRequest
                    (Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            memberInfo.setText("Hi, " + JsonHandler.parseString(response, "Fname") + " " +
                                    JsonHandler.parseString(response, "Lname")
                            );
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                        }
                    });
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest1);

            String url2 = "http://theatre.sit.kmutt.ac.th/customer/androidGetPoint?memberID=" + memberID;
            JsonArrayRequest jsonArrayRequest2 = new JsonArrayRequest
                    (Request.Method.GET, url2, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            if (response.toString().length() > 2) {
                                pointShow.setText(JsonHandler.parseString(response, "totalpoint"));
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // TODO: Handle error
                            memberInfo.setText("fail to retrieve member's information \nMemberID = "+memberID);
                        }
                    });
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest2);

            avatar.setImageResource(R.drawable.logined);
            loginButt.setText("Log out");
            infoButt.setVisibility(View.VISIBLE);
            editButt.setVisibility(View.VISIBLE);
            passwordChangeButt.setVisibility(View.VISIBLE);
            tvPoint.setVisibility(View.VISIBLE);
            pointShow.setVisibility(View.VISIBLE);

        }else{
            avatar.setImageResource(R.drawable.anonymous);
            memberInfo.setText("Anonymous");
            loginButt.setText("Log in");
            infoButt.setVisibility(View.INVISIBLE);
            editButt.setVisibility(View.INVISIBLE);
            passwordChangeButt.setVisibility(View.INVISIBLE);
            tvPoint.setVisibility(View.INVISIBLE);
            pointShow.setVisibility(View.INVISIBLE);
        }
    }
}
