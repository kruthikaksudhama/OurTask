package com.example.boostitvendor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Profile extends AppCompatActivity {
    SharedPreferences pref;
    TextView fname,lname,mob;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pref = getSharedPreferences("user_details",MODE_PRIVATE);
        fname=(TextView)findViewById(R.id.fnamedisp);
        lname = (TextView)findViewById(R.id.lnamedisp);
        mob = (TextView)findViewById(R.id.mobdisp);
        fname.setText(pref.getString("first_name",null));
        lname.setText(pref.getString("last_name",null));
        mob.setText(pref.getString("username",null));
        edit=(Button)findViewById(R.id.editb);
        edit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(Profile.this,Profile_editor.class));
            }
        });
    }

}
