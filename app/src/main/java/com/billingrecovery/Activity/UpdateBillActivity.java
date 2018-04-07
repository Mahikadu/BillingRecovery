package com.billingrecovery.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.billingrecovery.Model.BillReportModel;
import com.billingrecovery.R;
import com.billingrecovery.app42services.StorageService;
import com.billingrecovery.app42services.UploadService;
import com.billingrecovery.libs.AsyncApp42ServiceApi;
import com.billingrecovery.libs.Config;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.billingrecovery.libs.Utils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadFileType;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.squareup.picasso.Picasso.Priority.HIGH;

/**
 * Created by Mahesh on 3/30/2018.
 */

public class UpdateBillActivity extends Activity implements View.OnClickListener {

    Button Logout, submitbtn, Home, camerabtn;
    TextView username;
    ImageView image;

    EditText custname, billno, edt_date, totalamount, paidamount, remainamount, edtrepaid_amount;

    Context context;
    private SharedPref sharedPref;
    private Utils utils;
    private ConnectionDetector cd;
    DatePickerDialog datePickerDialog;

    private static StorageService storageService;
    private static UploadService uploadService;

    public BillReportModel billReportModel;

    String customer, billnum, currentdate;
    String strtotalamt, strpaidamt, strremainamt, strCustomerImageUrl,
            filePath, strImagename, strusername, strrepaidamt,strstatus,strUpdatedDate;

    private final int requestCode = 20;
    ProgressDialog mProgressDialog;
    Uri outputFileUri;
    String imageurl;
    boolean cameraClick = false;
    TextView headingtitle;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_bill_activity);

        context = getApplicationContext();

        sharedPref = new SharedPref(context);
        cd = new ConnectionDetector(context);
        storageService = new StorageService(context);
        uploadService = new UploadService(context);
        utils = new Utils(context);

        Logout = (Button) findViewById(R.id.btn_logout1);
        Home = (Button) findViewById(R.id.btn_home1);
        submitbtn = (Button) findViewById(R.id.btnsubmit);
        camerabtn = (Button) findViewById(R.id.btncamera);
        headingtitle = (TextView) findViewById(R.id.imageView1);
        headingtitle.setText("Update Bill");

        strusername = sharedPref.getuserName();

       /* ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(fallback)
                .showImageOnFail(fallback)
                .showImageOnLoading(fallback).build();*/

        image = (ImageView) findViewById(R.id.imgupdate);

        custname = (EditText) findViewById(R.id.edtcust_name);
        billno = (EditText) findViewById(R.id.edtbill_no);
        totalamount = (EditText) findViewById(R.id.edttotal_amount);
        paidamount = (EditText) findViewById(R.id.edtpaid_amount);
        remainamount = (EditText) findViewById(R.id.edtremain_amount);
        edt_date = (EditText) findViewById(R.id.edt_date1);
        edtrepaid_amount = (EditText) findViewById(R.id.edtrepaid_amount);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            billReportModel = new BillReportModel();
            billReportModel = (BillReportModel) getIntent().getSerializableExtra("en"); //Obtaining data
        }

        Logout.setOnClickListener(this);
        Home.setOnClickListener(this);
        submitbtn.setOnClickListener(this);
        edt_date.setOnClickListener(this);
        camerabtn.setOnClickListener(this);
        image.setOnClickListener(this);

        edtrepaid_amount.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                edtrepaid_amount.setFocusableInTouchMode(true);
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
                        strrepaidamt = edtrepaid_amount.getText().toString();
                        int total = Integer.parseInt(strtotalamt);
                        int paid = Integer.parseInt(strpaidamt);
                        int repaid = Integer.parseInt(strrepaidamt);
                        int finalpaidamt = paid + repaid;
                        if (total >= finalpaidamt) {
                            if (strtotalamt != null && strpaidamt != null &&
                                    strtotalamt.length() > 0 && strpaidamt.length() > 0) {

                                strpaidamt = String.valueOf(paid + repaid);
                                strremainamt = String.valueOf(total - finalpaidamt);

                                remainamount.setText(strremainamt);

                            }
                        } else {
                            //cd.displayMessage("Please put Paidamt same as Totalamt!!");
                            edtrepaid_amount.setError("Not match");
                            cd.displayMessage("Total amount not match to addition of paid + repaid amount");
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


        if (billReportModel != null) {
            custname.setText(billReportModel.getCustomer_name());
            billno.setText(billReportModel.getBill_number());
            totalamount.setText(billReportModel.getTotal_amount());
            paidamount.setText(billReportModel.getPaid_amount());
            remainamount.setText(billReportModel.getRemaining_amount());
            edt_date.setText(billReportModel.getStrdate());
            imageurl = billReportModel.getImgurl();

            //imageLoader.displayImage(imageurl, image, options);

            try {
                Picasso.with(this)
                        .load(imageurl)
                        .priority(HIGH)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
                        .into(image);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }
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
                view.startAnimation(AnimationUtils.loadAnimation(UpdateBillActivity.this, R.anim.button_click));
                Intent logout = new Intent(getApplicationContext(),
                        MainActivity.class);
                logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(logout);
                break;

            case R.id.btn_home1:
                view.startAnimation(AnimationUtils.loadAnimation(UpdateBillActivity.this, R.anim.button_click));
                Intent home = new Intent(getApplicationContext(),
                        DashboardActivity.class);
                startActivity(home);
                break;

            case R.id.btnsubmit:
                view.startAnimation(AnimationUtils.loadAnimation(UpdateBillActivity.this, R.anim.button_click));
                validationData();
                break;

            case R.id.edt_date1:
                datePickerDialog.show();
                break;

            case R.id.btncamera:
                cameraClick = true;
                view.startAnimation(AnimationUtils.loadAnimation(UpdateBillActivity.this, R.anim.button_click));
                Intent photoCaptureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File file = createFileInternalImage(String.valueOf(new Date().getDate() + "" + new Date().getTime()) + ".png");
                outputFileUri = Uri.fromFile(file);
                if (file != null) {
                    photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                }
                // outputFileUri = Uri.fromFile(new File(getExternalCacheDir().getPath(), System.currentTimeMillis()+".jpeg"));
                strImagename = outputFileUri.getPath();
                photoCaptureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                startActivityForResult(photoCaptureIntent, requestCode);
                break;

            case R.id.imgupdate:
                showImageDialog(outputFileUri, imageurl);
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
                        .priority(HIGH)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
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

    private void showImageDialog(Uri uri, String url) {

        final Dialog dialog = new Dialog(UpdateBillActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog_layout);
        ImageView img = (ImageView) dialog.findViewById(R.id.img);
        Button btnclose = (Button) dialog.findViewById(R.id.image_close);

        if (cameraClick) {
            try {
                Picasso.with(this)
                        .load(uri)
                        .priority(HIGH)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
                        .into(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                Picasso.with(this)
                        .load(url)
                        .priority(HIGH)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE)
                        .error(R.mipmap.ic_launcher)
                        .noFade()
                        .into(img);
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        if (TextUtils.isEmpty(edtrepaid_amount.getText().toString())) {
            edtrepaid_amount.setError("This field is required");
            focusView = edtrepaid_amount;
            focusView.requestFocus();
            edtrepaid_amount.setFocusable(true);
            edtrepaid_amount.requestFocusFromTouch();
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

        if (strImagename != null) {
            try {
                uploadImage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cd.displayMessage("Please Capture UpdateBill Image");
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

                String imagename = customer.substring(0, 4);
                imagename = imagename.concat("_" + strnameimg);

                uploadService.uploadImageCommon(strImagename,
                        imagename, "image_url",
                        customer,
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

    public void uploadData(final String imagepath) {
        try {
            if (cd.isConnectingToInternet()) {

                JSONObject jsonObjectUpdateBill = null;

                try {
                    jsonObjectUpdateBill = new JSONObject();

                    Date mydate = new Date();
                    strUpdatedDate = Utils.readFormat.format(mydate);

                    jsonObjectUpdateBill.put("customer_name", customer);
                    jsonObjectUpdateBill.put("bill_number", billnum);
                    jsonObjectUpdateBill.put("date", currentdate);
                    jsonObjectUpdateBill.put("created_date", billReportModel.getCreated_date());
                    jsonObjectUpdateBill.put("total_amount", strtotalamt);
                    jsonObjectUpdateBill.put("paid_amount", strpaidamt);
                    jsonObjectUpdateBill.put("updated_date", strUpdatedDate);
                    jsonObjectUpdateBill.put("remaining_amount", strremainamt);
                    jsonObjectUpdateBill.put("image_url", imagepath);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                storageService.updateDocs(jsonObjectUpdateBill, billReportModel.getDoc_id(), Config.collectionGenerateBill,
                        new App42CallBack() {
                            @Override
                            public void onSuccess(Object o) {
                                try {
                                    if (o != null) {

                                        if (strtotalamt.equals(strpaidamt)) {
                                            strstatus = "P";
                                        } else {
                                            strstatus = "R";
                                        }

                                        String selection = "object_id = ?";
                                        // WHERE clause arguments
                                        String[] selectionArgs = {billReportModel.getDoc_id()};
                                        //total_amount,created_date,customer_name,strdate,remaining_amount,updated_date,bill_number,image_url,paid_amount

                                        String valuesArray[] = {billReportModel.getDoc_id(),
                                                customer, billnum, currentdate, strtotalamt, strpaidamt, strremainamt,
                                                imagepath, billReportModel.getCreated_date(), strUpdatedDate,
                                                Config.collectionGenerateBill, strstatus};
                                        boolean output = BillingRecovery.dbCon.updateBulk(DbHelper.TABLE_GENERATE_BILL, selection, valuesArray, utils.columnNamesGenerateBill, selectionArgs);
                                        cd.displayMessage("Update Data Succesfully!!");

                                        if (output) {
                                            Intent i = new Intent(UpdateBillActivity.this, DashboardActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                       /* cd.displayMessage("Update Data Succesfully!!");
                                        Intent update = new Intent(UpdateBillActivity.this, DashboardActivity.class);
                                        startActivity(update);
                                        finish();*/
                                    } else {
                                        if (mProgressDialog.isShowing())
                                            mProgressDialog.dismiss();
                                        cd.displayMessage("Please check your internet connection and try again!!");
                                    }
                                } catch (Exception e1) {
                                    cd.displayMessage("Something went wrong. Please try Again!!!");
                                    if (mProgressDialog.isShowing())
                                        mProgressDialog.dismiss();
                                    e1.printStackTrace();
                                }
                            }

                            @Override
                            public void onException(Exception e) {
                                if (mProgressDialog.isShowing())
                                    mProgressDialog.dismiss();
                                try {
                                    if (e != null) {
                                        JSONObject jsonObject = new JSONObject(e.getMessage());
                                        JSONObject jsonObjectError = jsonObject.
                                                getJSONObject("app42Fault");
                                        String strMess = jsonObjectError.getString("details");
                                        cd.displayMessage(strMess);
                                    } else {
                                        cd.displayMessage("Please check your internet connection and try again!!");
                                    }
                                } catch (JSONException e1) {
                                    e1.printStackTrace();
                                    cd.displayMessage("Something went wrong. Please try Again!!!");
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
        }
    }
}
