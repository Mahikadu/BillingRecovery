package com.billingrecovery.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.billingrecovery.Adapter.BillReportAdapter;
import com.billingrecovery.Dbconfig.DbHelper;
import com.billingrecovery.Model.BillReportModel;
import com.billingrecovery.R;
import com.billingrecovery.app42services.StorageService;
import com.billingrecovery.app42services.UploadService;
import com.billingrecovery.libs.Config;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.billingrecovery.libs.Utils;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Storage;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mahesh on 12/22/2017.
 */

public class BillReportsActivity extends Activity implements View.OnClickListener {

    Button Logout, Home;
    TextView username;

    Context context;
    Activity activity;
    private SharedPref sharedPref;
    private Utils utils;
    private ConnectionDetector cd;

    String strusername;

    ListView stocklistview;
    TextView headingtitle;

    public BillReportModel billReportModel;
    private ArrayList<BillReportModel> billReportDetailsArraylist;
    BillReportAdapter adapter;

    ProgressDialog mProgressDialog;
    HorizontalScrollView horizantalscrollviewforstock;
    String total_amount,created_date,customer_name,strdate,remaining_amount,image_url,updated_date,bill_number,paid_amount,strstatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.billreports_activity);

        context = getApplicationContext();
        activity = BillReportsActivity.this;

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);
        utils = new Utils(context);

        Logout = (Button) findViewById(R.id.btn_logout1);
        Home = (Button) findViewById(R.id.btn_home1);
        headingtitle = (TextView) findViewById(R.id.imageView1);
        headingtitle.setText("Bill Reports");

        //username = (TextView) findViewById(R.id.tv_h_username);
        strusername = sharedPref.getuserName();
        //username.setText(strusername);

        stocklistview = (ListView) findViewById(R.id.billreport_list);

        horizantalscrollviewforstock = (HorizontalScrollView) findViewById(R.id.horizantal_scrollview_stock_report);

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

        InsertBillReportInDB();
        ShowBillReport();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_logout1:
                view.startAnimation(AnimationUtils.loadAnimation(BillReportsActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.btn_home1:
                view.startAnimation(AnimationUtils.loadAnimation(BillReportsActivity.this, R.anim.button_click));
                Intent home = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(home);
                break;

        }
    }

    public void InsertBillReportInDB() {

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();

        if (cd.isConnectingToInternet()) {


            StorageService storageService = new StorageService(BillReportsActivity.this);


            storageService.findAllDocs(Config.collectionGenerateBill, new App42CallBack() {

                @Override
                public void onSuccess(Object o) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    Storage response = (Storage) o;

                    if (response != null) {

                        if (response.getJsonDocList().size() > 0) {
                            try {
                                for (int i = 0; i < response.getJsonDocList().size(); i++) {

                                    Storage.JSONDocument jsonDocument = response.
                                            getJsonDocList().get(i);

                                    String strDocument = jsonDocument.getJsonDoc();
                                    String strDocumentId = jsonDocument.getDocId();

                                    try {
                                        JSONObject jsonObject = new JSONObject(strDocument);


                                        if (jsonObject != null && jsonObject.length() > 0) {

                                            if (jsonObject.getString("total_amount") != null &&
                                                    !jsonObject.getString("total_amount").equalsIgnoreCase("")) {
                                                total_amount = jsonObject.optString("total_amount");
                                            } else {
                                                total_amount = "";
                                            }

                                            if (jsonObject.getString("created_date") != null &&
                                                    !jsonObject.getString("created_date").equalsIgnoreCase("")) {
                                                created_date = jsonObject.optString("created_date");
                                            } else {
                                                created_date = "";
                                            }

                                            if (jsonObject.getString("customer_name") != null &&
                                                    !jsonObject.getString("customer_name").equalsIgnoreCase("")) {
                                                customer_name = jsonObject.optString("customer_name");
                                            } else {
                                                customer_name = "";
                                            }

                                            if (jsonObject.getString("date") != null &&
                                                    !jsonObject.getString("date").equalsIgnoreCase("")) {
                                                strdate = jsonObject.optString("date");
                                            } else {
                                                strdate = "";
                                            }

                                            if (jsonObject.getString("remaining_amount") != null &&
                                                    !jsonObject.getString("remaining_amount").equalsIgnoreCase("")) {
                                                remaining_amount = jsonObject.optString("remaining_amount");
                                            } else {
                                                remaining_amount = "";
                                            }

                                            if (jsonObject.getString("updated_date") != null &&
                                                    !jsonObject.getString("updated_date").equalsIgnoreCase("")) {
                                                updated_date = jsonObject.optString("updated_date");
                                            } else {
                                                updated_date = "";
                                            }

                                            if (jsonObject.getString("bill_number") != null &&
                                                    !jsonObject.getString("bill_number").equalsIgnoreCase("")) {
                                                bill_number = jsonObject.optString("bill_number");
                                            } else {
                                                bill_number = "";
                                            }

                                            if(jsonObject.getString("image_url") != null &&
                                                    !jsonObject.getString("image_url").equalsIgnoreCase("")){
                                                image_url = jsonObject.optString("image_url");
                                            }else{
                                                image_url = "";
                                            }

                                            if (jsonObject.getString("paid_amount") != null &&
                                                    !jsonObject.getString("paid_amount").equalsIgnoreCase("")) {
                                                paid_amount = jsonObject.optString("paid_amount");
                                            } else {
                                                paid_amount = "";
                                            }
                                            if (total_amount.equals(paid_amount)) {
                                                strstatus = "P";
                                            } else {
                                                strstatus = "R";
                                            }

                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String selection = "object_id = ?";
                                    // WHERE clause arguments
                                    String[] selectionArgs = {jsonDocument.getDocId()};
                                    //total_amount,created_date,customer_name,strdate,remaining_amount,updated_date,bill_number,image_url,paid_amount

                                    String valuesArray[] = {jsonDocument.getDocId(),
                                            customer_name,bill_number,strdate,total_amount,paid_amount,remaining_amount,
                                            image_url,created_date,updated_date,
                                            Config.collectionGenerateBill,strstatus};
                                    boolean output = BillingRecovery.dbCon.updateBulk(DbHelper.TABLE_GENERATE_BILL, selection, valuesArray, utils.columnNamesGenerateBill, selectionArgs);
                                    //cd.displayMessage("Data Upload Successfully!!");
                                }

                               // ShowBillReport();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }


                    } else {
                        cd.displayMessage("Please check your internet connection and try again!!");
                    }
                }

                @Override
                public void onException(Exception e) {
                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    if (e != null) {
                        //TODO plz check updateversion collection in app42 that time this toast uncomment
                        //  utils.toast(2, 2, getString(R.string.error));
                    } else {
                        cd.displayMessage("Please check your internet connection and try again!!");
                    }
                }
            });

        } else {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            cd.displayMessage("Please check your internet connection and try again!!");
        }
    }

    public void ShowBillReport(){
            try {

                Cursor cursor = BillingRecovery.dbCon.fetchAlldata(DbHelper.TABLE_GENERATE_BILL);

                billReportDetailsArraylist = new ArrayList<>();
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

                        billReportDetailsArraylist.add(billReportModel);

                    } while (cursor.moveToNext());

                }

                adapter = new BillReportAdapter(context,activity, billReportDetailsArraylist);
                stocklistview.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

        }


}
