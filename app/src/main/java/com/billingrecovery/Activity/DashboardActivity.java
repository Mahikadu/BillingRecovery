package com.billingrecovery.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.billingrecovery.R;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;

/**
 * Created by Admin on 29-11-2017.
 */

public class DashboardActivity extends Activity implements View.OnClickListener{

    Button Logout;
    ImageView generatebill,updatebill,report,custreport,aboutus;
    TextView username;

    Context context;
    private SharedPref sharedPref;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboardactivity);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);

        Logout = (Button)findViewById(R.id.btn_logout);

        username = (TextView)findViewById(R.id.tv_h_username);

        generatebill = (ImageView)findViewById(R.id.generatebill);
        updatebill = (ImageView)findViewById(R.id.updatebill);
        report = (ImageView)findViewById(R.id.report);
        custreport = (ImageView)findViewById(R.id.custreport);
        aboutus = (ImageView)findViewById(R.id.aboutus);

        String strusername = sharedPref.getuserName();
        username.setText(strusername);

        Logout.setOnClickListener(this);
        generatebill.setOnClickListener(this);
        updatebill.setOnClickListener(this);
        report.setOnClickListener(this);
        custreport.setOnClickListener(this);
        aboutus.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_logout:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.generatebill:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.updatebill:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.report:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.custreport:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.aboutus:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;
        }

    }
}
