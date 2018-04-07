package com.billingrecovery.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TextView;

import com.billingrecovery.Adapter.BillReportAdapter;
import com.billingrecovery.Adapter.RemainCustReportAdapter;
import com.billingrecovery.Dbconfig.DbHelper;
import com.billingrecovery.Model.BillReportModel;
import com.billingrecovery.R;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.billingrecovery.libs.Utils;

import java.util.ArrayList;

/**
 * Created by Mahesh on 3/30/2018.
 */

public class RemainCustReportsActivity extends Activity implements View.OnClickListener{

    Button Logout, Home;
    TextView username;

    Context context;
    Activity activity;
    private SharedPref sharedPref;
    private Utils utils;
    private ConnectionDetector cd;

    TextView headingtitle;
    ListView custreportlistview;
    HorizontalScrollView horizantalscrollviewforstock;

    public BillReportModel billReportModel;
    private ArrayList<BillReportModel> remaincustReportDetailsArraylist;
    RemainCustReportAdapter adapter;

    String total_amount,created_date,customer_name,strdate,remaining_amount,image_url,updated_date,bill_number,paid_amount,strstatus;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.remain_customer_report_activity);

        context = getApplicationContext();
        activity = RemainCustReportsActivity.this;

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);
        utils = new Utils(context);

        Logout = (Button) findViewById(R.id.btn_logout1);
        Home = (Button) findViewById(R.id.btn_home1);
        headingtitle = (TextView) findViewById(R.id.imageView1);
        headingtitle.setText("Remaining Customer Report");

        custreportlistview = (ListView)findViewById(R.id.remaincustreport_list);

        horizantalscrollviewforstock = (HorizontalScrollView) findViewById(R.id.horizantal_scrollview_customer_report);

        horizantalscrollviewforstock.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub

                int scrollX = v.getScrollX();
                int scrollY = v.getScrollY();

                horizantalscrollviewforstock.scrollTo(scrollX, scrollY);

                return false;

            }
        });

        Logout.setOnClickListener(this);
        Home.setOnClickListener(this);

        ShowRemainCustReport();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(getApplicationContext(),
                DashboardActivity.class);
        startActivity(home);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_logout1:
                view.startAnimation(AnimationUtils.loadAnimation(RemainCustReportsActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.btn_home1:
                view.startAnimation(AnimationUtils.loadAnimation(RemainCustReportsActivity.this, R.anim.button_click));
                Intent home = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(home);
                break;

        }
    }

    public void ShowRemainCustReport(){
        try {

            strstatus = "R";
            String where = " where status = '" + strstatus + "'";
            Cursor cursor = BillingRecovery.dbCon.fetchFromSelect(DbHelper.TABLE_GENERATE_BILL, where);

            remaincustReportDetailsArraylist = new ArrayList<>();
            if (cursor != null && cursor.moveToFirst()) {
                cursor.moveToFirst();
                do {
                    billReportModel = new BillReportModel();
                    billReportModel.setDoc_id(cursor.getString(cursor.getColumnIndex("object_id")));
                    billReportModel.setTotal_amount(cursor.getString(cursor.getColumnIndex("total_amount")));
                    billReportModel.setCustomer_name(cursor.getString(cursor.getColumnIndex("customer_name")));
                    billReportModel.setCreated_date(cursor.getString(cursor.getColumnIndex("created_date")));
                    billReportModel.setStrdate(cursor.getString(cursor.getColumnIndex("strdate")));
                    billReportModel.setRemaining_amount(cursor.getString(cursor.getColumnIndex("remaining_amount")));
                    billReportModel.setUpdated_date(cursor.getString(cursor.getColumnIndex("updated_date")));
                    billReportModel.setBill_number(cursor.getString(cursor.getColumnIndex("bill_number")));
                    billReportModel.setPaid_amount(cursor.getString(cursor.getColumnIndex("paid_amount")));
                    billReportModel.setStrstatus(cursor.getString(cursor.getColumnIndex("status")));
                    billReportModel.setImgurl(cursor.getString(cursor.getColumnIndex("image_url")));

                    remaincustReportDetailsArraylist.add(billReportModel);

                } while (cursor.moveToNext());

            }

            adapter = new RemainCustReportAdapter(context,activity, remaincustReportDetailsArraylist);
            custreportlistview.setAdapter(adapter);
            adapter.notifyDataSetChanged();

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
}
