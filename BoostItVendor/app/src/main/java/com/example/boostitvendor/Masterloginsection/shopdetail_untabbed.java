package com.example.boostitvendor.Masterloginsection;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.boostitvendor.Adapters.ShopAdapter;
import com.example.boostitvendor.MainActivity;
import com.example.boostitvendor.Models.shop;
import com.example.boostitvendor.MyApplication;
import com.example.boostitvendor.Profile;
import com.example.boostitvendor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class shopdetail_untabbed extends AppCompatActivity {
    StringBuffer shopURL = new StringBuffer("https://bi-stag.herokuapp.com/vend/getshop?vend_id=");
    List<shop> shopslist;
    JSONArray items, shops, bills;
    RecyclerView recyclerView;
    LinearLayout progbar;
    SwipeRefreshLayout refresh;
    ShopAdapter adapters;
    int shop_id, vendid;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetail_untabbed);
        pref = getSharedPreferences("user_details", MODE_PRIVATE);
        vendid = pref.getInt("vend_id", 0);
        shopURL.append(vendid);
        refresh = findViewById(R.id.refreshView);
        progbar = findViewById(R.id.progbar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                shopslist = new ArrayList<>();
                requestshop(shopslist,recyclerView);
                refresh.setRefreshing(false);
            }
        });
        shopslist = new ArrayList<>();
        progbar.setVisibility(View.VISIBLE);
        requestshop(shopslist,recyclerView);

    }
    public void requestshop(final List<shop> shopList, final RecyclerView recyclerView) {
        JsonObjectRequest orderRequest = new JsonObjectRequest(Request.Method.GET, shopURL.toString(), null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(ShopSummaries.this, response.toString(), Toast.LENGTH_SHORT).show();
                        try {
                            shops = response.getJSONArray("shops");
                            for (int i = 0; i < shops.length(); i++) {
                                JSONObject t = shops.getJSONObject(i);

                                try {
                                    shopList.add(
                                            new shop(
                                                    t.getString("shop_name"),
                                                    t.getInt("shop_id"),
                                                    t.getBoolean("servicing")
                                            )
                                    );
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                            //shop shop = shopList.get(1);
                            //Toast.makeText(shopdetail_untabbed.this, shop.get_shopname(), Toast.LENGTH_SHORT).show();
                            progbar.setVisibility(View.INVISIBLE);
                            adapters = new ShopAdapter(shopdetail_untabbed.this, shopList);
                            recyclerView.setAdapter(adapters);
                            adapters.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    final int id;
                                    id = shopList.get(position).get_shopid();
                                    Intent intent = new Intent(shopdetail_untabbed.this, ShopSummaries.class);
                                    intent.putExtra("shop_id", id);;
                                    startActivity(intent);
                                }
                            });


                            //Toast.makeText(ShopSummaries.this, itemlist.toString(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progbar.setVisibility(View.INVISIBLE);
                        Toast.makeText(shopdetail_untabbed.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();

                headers.put("pvtkey", "97381f90401474fdbb9354c6ec41aef2c2de7d4ff19dc9898c8b7f416f838bf3");
                return headers;
            }

        };

        MyApplication.getInstance().addToRequestQueue(orderRequest, "abc");
    }
    public void logOut(View view){
        SharedPreferences pref = getSharedPreferences("user_details",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("username");
        editor.remove("password");
        editor.remove("name");
        editor.remove("staff_id");
        editor.remove("vend_id");
        editor.remove("first_name");
        editor.remove("last_name");
        editor.remove("shop_id");
        editor.commit();
        startActivity(new Intent(shopdetail_untabbed.this, MainActivity.class));

    }
    public void profile(View view){
        startActivity(new Intent(shopdetail_untabbed.this, Profile.class));
    }
}