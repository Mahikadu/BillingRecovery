package com.billingrecovery.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.billingrecovery.Dbconfig.DbHelper;
import com.billingrecovery.R;
import com.billingrecovery.app42services.StorageService;
import com.billingrecovery.app42services.UploadService;
import com.billingrecovery.libs.AsyncApp42ServiceApi;
import com.billingrecovery.libs.Config;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.billingrecovery.libs.Utils;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * Created by Mahesh on 12/19/2017.
 */

public class GenerateBillActivity extends Activity implements View.OnClickListener {

    Button Logout, submitbtn, Home, camerabtn;
    TextView username;
    ImageView image;

    EditText custname, billno, edt_date, totalamount, paidamount, remainamount;

    Context context;
    private SharedPref sharedPref;
    private Utils utils;
    private ConnectionDetector cd;
    DatePickerDialog datePickerDialog;

    String customer,billnum,currentdate;

    String strtotalamt, strpaidamt, strremainamt,strCustomerImageUrl,filePath,strImagename,strusername;
    private final int requestCode = 20;
    ProgressDialog mProgressDialog;
    Uri outputFileUri;
    private static StorageService storageService;
    private static UploadService uploadService;

    String total_amount,created_date,customer_name,strdate,remaining_amount,updated_date,bill_number,image_url,paid_amount,strstatus;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_bill_activity);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);
        storageService = new StorageService(context);
        uploadService = new UploadService(context);
        utils = new Utils(context);

        Logout = (Button) findViewById(R.id.btn_logout1);
        Home = (Button) findViewById(R.id.btn_home1);
        submitbtn = (Button) findViewById(R.id.submitbtn);
        camerabtn = (Button) findViewById(R.id.camerabtn);

        image = (ImageView) findViewById(R.id.imageView);


        //username = (TextView) findViewById(R.id.tv_h_username);
        strusername = sharedPref.getuserName();
        //username.setText(strusername);

        custname = (EditText) findViewById(R.id.cust_name);
        billno = (EditText) findViewById(R.id.bill_no);
        totalamount = (EditText) findViewById(R.id.total_amount);
        paidamount = (EditText) findViewById(R.id.paid_amount);
        remainamount = (EditText) findViewById(R.id.remain_amount);
        edt_date = (EditText) findViewById(R.id.edt_date);


        Logout.setOnClickListener(this);
        Home.setOnClickListener(this);
        submitbtn.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        camerabtn.setOnClickListener(this);
        image.setOnClickListener(this);

        custname.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                custname.setFocusableInTouchMode(true);
                return false;
            }
        });

        billno.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                billno.setFocusableInTouchMode(true);
                return false;
            }
        });

        totalamount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                totalamount.setFocusableInTouchMode(true);
                remainamount.setText("");
                return false;
            }
        });

        paidamount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                paidamount.setFocusableInTouchMode(true);
                remainamount.setText("");
                return false;
            }
        });

        remainamount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                remainamount.setFocusableInTouchMode(true);
                return false;
            }
        });

        remainamount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                try {

                    if (hasfocus) {
                        strtotalamt = totalamount.getText().toString();
                        strpaidamt = paidamount.getText().toString();
                        int total = Integer.parseInt(strtotalamt);
                        int paid = Integer.parseInt(strpaidamt);
                        if (total >= paid) {
                            if (strtotalamt != null && strpaidamt != null &&
                                    strtotalamt.length() > 0 && strpaidamt.length() > 0) {

                                strremainamt = String.valueOf(total - paid);

                                remainamount.setText(strremainamt);

                            }
                        } else {
                            //cd.displayMessage("Please put Paidamt same as Totalamt!!");
                            paidamount.setError("Please put Paidamt same as Totalamt!!");
                        }
                    } else {
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final Calendar newCalendar = Calendar.getInstance();
        final DateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int monthOfYear, int dayOfMonth, int year) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(monthOfYear, dayOfMonth, year);
                edt_date.setText(dateFormatter.format(newDate.getTime()));

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setCalendarViewShown(false);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn_logout1:
                view.startAnimation(AnimationUtils.loadAnimation(GenerateBillActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.btn_home1:
                view.startAnimation(AnimationUtils.loadAnimation(GenerateBillActivity.this, R.anim.button_click));
                Intent home = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(home);
                break;

            case R.id.submitbtn:
                view.startAnimation(AnimationUtils.loadAnimation(GenerateBillActivity.this, R.anim.button_click));
                validationData();
                break;

            case R.id.edt_date:
                datePickerDialog.show();
                break;

            case R.id.camerabtn:
                view.startAnimation(AnimationUtils.loadAnimation(GenerateBillActivity.this, R.anim.button_click));
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = createFileInternalImage(String.valueOf(new Date().getDate() + "" + new Date().getTime())+ ".jpeg");
                outputFileUri = Uri.fromFile(file);
                if (file != null) {
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
               // outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), System.currentTimeMillis()+".jpeg"));
                strImagename = outputFileUri.getPath();
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(photoCaptureIntent, requestCode);
                break;

            case R.id.imageView:
                showImageDialog(outputFileUri);
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.requestCode == requestCode && resultCode == RESULT_OK) {
            try {
                Picasso.with(this)
                        .load(outputFileUri)
                        .fit()
                        .into(image);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public File createFileInternalImage(String strFileName) {

        File file = null;
        try {
            file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), strFileName);
            file.getParentFile().mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return file;
    }

    private void showImageDialog(Uri uri) {

        final Dialog dialog = new Dialog(GenerateBillActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog_layout);
        ImageView img = (ImageView) dialog.findViewById(R.id.img);
        Button btnclose = (Button) dialog.findViewById(R.id.image_close);

        try {
            Picasso.with(this)
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .into(img);
        } catch (Exception e) {
            e.printStackTrace();
        }

        dialog.show();

        btnclose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void validationData() {
        View focusView;

        custname.setError(null);
        billno.setError(null);
        edt_date.setError(null);
        totalamount.setError(null);
        paidamount.setError(null);
        remainamount.setError(null);

        if (TextUtils.isEmpty(custname.getText().toString())) {
            custname.setError("This field is required");
            focusView = custname;
            focusView.requestFocus();
            custname.setFocusable(true);
            custname.requestFocusFromTouch();
            return;
        }

        if (TextUtils.isEmpty(billno.getText().toString())) {
            billno.setError("This field is required");
            focusView = billno;
            focusView.requestFocus();
            billno.setFocusable(true);
            billno.requestFocusFromTouch();
            return;
        }

        if (TextUtils.isEmpty(edt_date.getText().toString())) {
            edt_date.setError("This field is required");
            focusView = edt_date;
            focusView.requestFocus();
            edt_date.setFocusable(true);
            edt_date.requestFocusFromTouch();
            return;
        }

        if (TextUtils.isEmpty(totalamount.getText().toString())) {
            totalamount.setError("This field is required");
            focusView = totalamount;
            focusView.requestFocus();
            totalamount.setFocusable(true);
            totalamount.requestFocusFromTouch();
            return;
        }

        if (TextUtils.isEmpty(paidamount.getText().toString())) {
            paidamount.setError("This field is required");
            focusView = paidamount;
            focusView.requestFocus();
            paidamount.setFocusable(true);
            paidamount.requestFocusFromTouch();
            return;
        }

        if (TextUtils.isEmpty(remainamount.getText().toString())) {
            remainamount.setError("This field is required");
            focusView = remainamount;
            focusView.requestFocus();
            remainamount.setFocusable(true);
            remainamount.requestFocusFromTouch();
            return;
        }

         customer = custname.getText().toString();
         billnum = billno.getText().toString();
         currentdate = edt_date.getText().toString();

        try {
            uploadImage();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void uploadImage() {

        try {

            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            if (cd.isConnectingToInternet()) {

                Calendar calendar = Calendar.getInstance();
                String strnameimg = String.valueOf(calendar.getTimeInMillis());

                String imagename = strusername.substring(0,4);
                imagename = imagename.concat("_"+strnameimg);

                uploadService.uploadImageCommon(strImagename,
                        imagename, "image_url",
                        strusername,
                        UploadFileType.IMAGE, new App42CallBack() {

                            public void onSuccess(Object response) {

                                if (response != null) {
                                    Upload upload = (Upload) response;
                                    ArrayList<Upload.File> fileList = upload.getFileList();

                                    if (fileList.size() > 0) {
                                        strCustomerImageUrl = fileList.get(0).getUrl();
                                        uploadData(strCustomerImageUrl);
                                    } else {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();

                                        cd.displayMessage(((Upload) response).getStrResponse());

                                    }
                                } else {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    cd.displayMessage("Please check your internet connection and try again!!");
                                }
                            }

                            @Override
                            public void onException(Exception e) {

                                if (e != null) {
                                    App42Exception exception = (App42Exception) e;
                                    int appErrorCode = exception.getAppErrorCode();

                                    if (appErrorCode == 2100) {
                                        uploadData(strCustomerImageUrl);

                                    } else {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();
                                        cd.displayMessage("Something went wrong. Try Again!!");
                                    }
                                } else {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    cd.displayMessage("Please check your internet connection and try again!!");
                                }
                            }
                        });
            } else {
                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                cd.displayMessage("Please check your internet connection and try again!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            cd.displayMessage("Something went wrong. Try Again!!");
        }
    }

    public void uploadData(String imagepath){
        try {
            if (cd.isConnectingToInternet()) {

                JSONObject jsonObjectGenerateBill = null;

                try {
                    jsonObjectGenerateBill = new JSONObject();

                    Date mydate = new Date();
                    String strCreateDate = Utils.readFormat.format(mydate);

                    jsonObjectGenerateBill.put("customer_name", customer);
                    jsonObjectGenerateBill.put("bill_number", billnum);
                    jsonObjectGenerateBill.put("date", currentdate);
                    jsonObjectGenerateBill.put("created_date", strCreateDate);
                    jsonObjectGenerateBill.put("total_amount", strtotalamt);
                    jsonObjectGenerateBill.put("paid_amount", strpaidamt);
                    jsonObjectGenerateBill.put("updated_date", "");
                    jsonObjectGenerateBill.put("remaining_amount", strremainamt);
                    jsonObjectGenerateBill.put("image_url", imagepath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                storageService.insertDocs(Config.collectionGenerateBill, jsonObjectGenerateBill,
                        new AsyncApp42ServiceApi.App42StorageServiceListener() {

                            @Override
                            public void onDocumentInserted(Storage response) {
                                try {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    if (response.isResponseSuccess()) {

                                        if (response.getJsonDocList().size() > 0) {

                                            Storage.JSONDocument jsonDocument = response.getJsonDocList().get(0);

                                            JSONObject jsonObject = new JSONObject(response.getJsonDocList().get(0).getJsonDoc());

                                            try {
                                                if(jsonObject !=null){
                                                    if(jsonObject.getString("total_amount") != null &&
                                                            !jsonObject.getString("total_amount").equalsIgnoreCase("")){
                                                        total_amount = jsonObject.optString("total_amount");
                                                    }else{
                                                        total_amount = "";
                                                    }

                                                    if(jsonObject.getString("created_date") != null &&
                                                            !jsonObject.getString("created_date").equalsIgnoreCase("")){
                                                        created_date = jsonObject.optString("created_date");
                                                    }else{
                                                        created_date = "";
                                                    }

                                                    if(jsonObject.getString("customer_name") != null &&
                                                            !jsonObject.getString("customer_name").equalsIgnoreCase("")){
                                                        customer_name = jsonObject.optString("customer_name");
                                                    }else{
                                                        customer_name = "";
                                                    }

                                                    if(jsonObject.getString("date") != null &&
                                                            !jsonObject.getString("date").equalsIgnoreCase("")){
                                                        strdate = jsonObject.optString("date");
                                                    }else{
                                                        strdate = "";
                                                    }

                                                    if(jsonObject.getString("remaining_amount") != null &&
                                                            !jsonObject.getString("remaining_amount").equalsIgnoreCase("")){
                                                        remaining_amount = jsonObject.optString("remaining_amount");
                                                    }else{
                                                        remaining_amount = "";
                                                    }

                                                    if(jsonObject.getString("updated_date") != null &&
                                                            !jsonObject.getString("updated_date").equalsIgnoreCase("")){
                                                        updated_date = jsonObject.optString("updated_date");
                                                    }else{
                                                        updated_date = "";
                                                    }

                                                    if(jsonObject.getString("bill_number") != null &&
                                                            !jsonObject.getString("bill_number").equalsIgnoreCase("")){
                                                        bill_number = jsonObject.optString("bill_number");
                                                    }else{
                                                        bill_number = "";
                                                    }

                                                    if(jsonObject.getString("image_url") != null &&
                                                            !jsonObject.getString("image_url").equalsIgnoreCase("")){
                                                        image_url = jsonObject.optString("image_url");
                                                    }else{
                                                        image_url = "";
                                                    }

                                                    if(jsonObject.getString("paid_amount") != null &&
                                                            !jsonObject.getString("paid_amount").equalsIgnoreCase("")){
                                                        paid_amount = jsonObject.optString("paid_amount");
                                                    }else{
                                                        paid_amount = "";
                                                    }
                                                    if(total_amount.equals(paid_amount)){
                                                        strstatus = "P";
                                                    }else{
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
                                            cd.displayMessage("Data Upload Successfully!!");

                                            if(output){
                                                Intent i = new Intent(GenerateBillActivity.this,DashboardActivity.class);
                                                startActivity(i);
                                            }
                                        }




                                    } else {
                                        cd.displayMessage("Please check your internet connection and try again!!");
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    cd.displayMessage("Please check your internet connection and try again!!");
                                }
                            }

                            @Override
                            public void onUpdateDocSuccess(Storage response) {
                            }

                            @Override
                            public void onFindDocSuccess(Storage response) {
                            }

                            @Override
                            public void onInsertionFailed(App42Exception ex) {
                                try {
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    if (ex != null) {
                                        JSONObject jsonObject = new JSONObject(ex.getMessage());
                                        JSONObject jsonObjectError = jsonObject.
                                                getJSONObject("app42Fault");
                                        String strMess = jsonObjectError.getString("details");
                                        cd.displayMessage(strMess);
                                    } else {
                                        cd.displayMessage("Please check your internet connection and try again!!");
                                    }

                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    cd.displayMessage("Something went wrong. Try Again!!");
                                }
                            }

                            @Override
                            public void onFindDocFailed(App42Exception ex) {
                            }

                            @Override
                            public void onUpdateDocFailed(App42Exception ex) {
                            }
                        });

            }else{
                if(mProgressDialog.isShowing())
                    mProgressDialog.dismiss();
                cd.displayMessage("Please check your internet connection and try again!!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
