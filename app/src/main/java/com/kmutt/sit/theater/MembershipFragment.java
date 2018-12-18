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
    //Button logoutButt;
    EditText memberInfo;
    //ConstraintLayout bottomArea;
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
        avatar = rootView.findViewById(R.id.avatar);
        //logoutButt = rootView.findViewById(R.id.logoutButt);

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

        /*logoutButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                memberID = -1;
                editor.putInt("memberID", memberID);
                editor.commit();
                onResume();
            }
        });*/

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

            String url = "http://theatre.sit.kmutt.ac.th/customer/androidGetInfo?id=" + memberID;
            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
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
            MySingleton.getInstance(getActivity()).addToRequestQueue(jsonArrayRequest);
            avatar.setImageResource(R.drawable.logined);
            loginButt.setText("Log out");
            infoButt.setVisibility(View.VISIBLE);
            //logoutButt.setVisibility(View.VISIBLE);
            //bottomArea.setVisibility(View.INVISIBLE);

        }else{
            avatar.setImageResource(R.drawable.anonymous);
            memberInfo.setText("Anonymous");
            loginButt.setText("Log in");
            infoButt.setVisibility(View.INVISIBLE);
            //logoutButt.setVisibility(View.INVISIBLE);
        }
    }
}
