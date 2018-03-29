package com.billingrecovery.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.billingrecovery.CircleLayout;
import com.billingrecovery.R;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.szugyi.circlemenu.view.CircleImageView;

/**
 * Created by Admin on 29-11-2017.
 */

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener, CircleLayout.OnItemSelectedListener,
        CircleLayout.OnItemClickListener, CircleLayout.OnRotationFinishedListener{

    Button Logout;
    //ImageView generatebill,updatebill,report,custreport,aboutus;
    TextView username;

    Context context;
    private SharedPref sharedPref;
    private ConnectionDetector cd;

    protected CircleLayout circleLayout;

    AnimationDrawable animationDrawable;
    RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);

        Logout = (Button)findViewById(R.id.btn_logout);

        relativeLayout = (RelativeLayout)findViewById(R.id.dashboardlayout);
        animationDrawable = (AnimationDrawable) relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(5000);
        animationDrawable.setExitFadeDuration(2000);

       // username = (TextView)findViewById(R.id.tv_h_username);

        //generatebill = (ImageView)findViewById(R.id.generatebill);
        //updatebill = (ImageView)findViewById(R.id.updatebill);
        //report = (ImageView)findViewById(R.id.report);
        //custreport = (ImageView)findViewById(R.id.custreport);
        //aboutus = (ImageView)findViewById(R.id.aboutus);

        String strusername = sharedPref.getuserName();
        //username.setText(strusername);

        Logout.setOnClickListener(this);
        //generatebill.setOnClickListener(this);
        //updatebill.setOnClickListener(this);
        //report.setOnClickListener(this);
        //custreport.setOnClickListener(this);
        //aboutus.setOnClickListener(this);

        // Set listeners
        circleLayout = (CircleLayout) findViewById(R.id.circle_layout);
        circleLayout.setOnItemSelectedListener(this);
        circleLayout.setOnItemClickListener(this);
        circleLayout.setOnRotationFinishedListener(this);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning())
            animationDrawable.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning())
            animationDrawable.stop();
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

          /*  case R.id.generatebill:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));
                Intent gbill = new Intent(getApplicationContext(),GenerateBillActivity.class);
                startActivity(gbill);
                break;

            case R.id.updatebill:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.report:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));
                Intent i = new Intent(getApplicationContext(),BillReportsActivity.class);
                startActivity(i);
                break;

            case R.id.custreport:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));

                break;

            case R.id.aboutus:
                v.startAnimation(AnimationUtils.loadAnimation(DashboardActivity.this, R.anim.button_click));
                Intent about = new Intent(getApplicationContext(),SampleActivity.class);
                startActivity(about);
                break;*/
        }

    }

    @Override
    public void onItemSelected(View view) {
        switch (view.getId()) {
            case R.id.generatebill_image:
                // Handle calendar selection
                Intent gbill = new Intent(getApplicationContext(),
                        GenerateBillActivity.class);
                startActivity(gbill);
                break;
            case R.id.updatebill_image:
                // Handle cloud selection
                break;
            case R.id.billsreport_image:
                // Handle key selection
                Intent i = new Intent(getApplicationContext(),BillReportsActivity.class);
                startActivity(i);
                break;
            case R.id.custreport_image:
                // Handle mail selection
                Intent gbill1 = new Intent(getApplicationContext(),
                        GenerateBillActivity.class);
                startActivity(gbill1);
                break;
            case R.id.aboutus_image:
                // Handle profile selection
                Intent i1 = new Intent(getApplicationContext(),BillReportsActivity.class);
                startActivity(i1);
                break;

        }
    }

    @Override
    public void onItemClick(View view) {
        switch (view.getId()) {
            case R.id.generatebill_image:
                // Handle calendar click
                Intent gbill = new Intent(getApplicationContext(),
                        GenerateBillActivity.class);
                startActivity(gbill);
                break;
            case R.id.updatebill_image:
                // Handle cloud click
                break;
            case R.id.billsreport_image:
                // Handle key click
                Intent i = new Intent(getApplicationContext(),BillReportsActivity.class);
                startActivity(i);
                break;
            case R.id.custreport_image:
                // Handle mail click
                Intent gbill1 = new Intent(getApplicationContext(),
                        GenerateBillActivity.class);
                startActivity(gbill1);
                break;
            case R.id.aboutus_image:
                // Handle profile click
                Intent i1 = new Intent(getApplicationContext(),BillReportsActivity.class);
                startActivity(i1);
                break;

        }
    }

    @Override
    public void onRotationFinished(View view) {
        Animation animation = new RotateAnimation(0, 360, view.getWidth() / 2, view.getHeight() / 2);
        animation.setDuration(500);
        view.startAnimation(animation);
    }
}
