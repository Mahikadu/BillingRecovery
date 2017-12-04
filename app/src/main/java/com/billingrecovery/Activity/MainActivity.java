package com.billingrecovery.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

import com.billingrecovery.Dbconfig.DataBaseCon;
import com.billingrecovery.Dbconfig.DatabaseCopy;
import com.billingrecovery.Dbconfig.DbHelper;
import com.billingrecovery.R;
import com.billingrecovery.app42services.StorageService;
import com.billingrecovery.app42services.UserService;
import com.billingrecovery.libs.Config;
import com.billingrecovery.libs.ConnectionDetector;
import com.billingrecovery.libs.SharedPref;
import com.billingrecovery.libs.Utils;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.user.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends Activity {

    EditText username, password;
    Button loginButton;
    String user, pass;
    ConnectionDetector cd;
    private ProgressDialog progressDialog;
    private Utils utils;
    private static final int RECORD_REQUEST_CODE = 101;
    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cd = new ConnectionDetector(MainActivity.this);
        progressDialog = new ProgressDialog(MainActivity.this);
        utils = new Utils(MainActivity.this);
        sharedPref = new SharedPref(MainActivity.this);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        loginButton = (Button) findViewById(R.id.loginButton);

        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                username.setFocusableInTouchMode(true);
                return false;
            }
        });

        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                password.setFocusableInTouchMode(true);
                return false;
            }
        });

        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("", "Permission to record denied");

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Permission to access the photos,camera,storage and files is required for this app .")
                        .setTitle("Permission required");

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("","Clicked");
                        makeRequest();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                makeRequest();
            }
        }

        //  Copy Database from Asset folder
        DatabaseCopy databaseCopy = new DatabaseCopy();
        AssetManager assetManager = this.getAssets();
        databaseCopy.copy(assetManager, MainActivity.this);
        BillingRecovery.dbCon = DataBaseCon.getInstance(getApplicationContext());

        exportDB();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.button_click));
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("can't be blank");
                } else if (pass.equals("")) {
                    password.setError("can't be blank");
                } else {
                    if (cd.isConnectingToInternet()) {
                        if (progressDialog != null) {
                            progressDialog.setMessage("Login Please Wait...");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                        }

                        verifyLogin(user, pass);

                    } else {
                        cd.displayMessage("Please check your internet connection and try again!!");
                    }

                }
            }
        });
    }

    private void exportDB() {
        File sd = Environment.getExternalStorageDirectory();
        File data = Environment.getDataDirectory();
        FileChannel source = null;
        FileChannel destination = null;
        String currentDBPath = "/data/" + "com.billingrecovery" + "/databases/" + DbHelper.DATABASE_NAME;
        String backupDBPath = DbHelper.DATABASE_NAME;
        File currentDB = new File(data, currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,Manifest.permission.ACCESS_FINE_LOCATION
                        ,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.READ_CONTACTS,Manifest.permission.WRITE_CONTACTS},
                RECORD_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RECORD_REQUEST_CODE: {

                if (grantResults.length == 0
                        || grantResults[0] !=
                        PackageManager.PERMISSION_GRANTED) {

                    Log.i("", "Permission has been denied by user");
                } else {
                    Log.i("", "Permission has been granted by user");
                }
                return;
            }
        }
    }

    private void verifyLogin(final String username, String password) {

        UserService userService = new UserService(MainActivity.this);

        userService.authenticate(username, password, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {

                if (response != null) {

                    User user = (User) response;

                    if (true) {
                        fetchUser(progressDialog, username);
                    } else {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        cd.displayMessage("Invalid Credentials");
                    }

                } else {
                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    cd.displayMessage("Please check your internet connection and try again!!");
                }
            }

            @Override
            public void onException(Exception e) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
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
                    cd.displayMessage("Something went wrong. Try Again!!!");
                }
            }
        });
    }

    private void fetchUser(final ProgressDialog progressDialog, final String username) {

        if (cd.isConnectingToInternet()) {

            StorageService storageService = new StorageService(MainActivity.this);

            Query q1 = QueryBuilder.build("admin_name", username.toLowerCase(), QueryBuilder.
                    Operator.EQUALS);

            storageService.findDocsByQueryOrderBy(Config.collectionAdmin, q1, 1, 0,
                    "updated_date", 1, new App42CallBack() {

                        @Override
                        public void onSuccess(Object o) {

                            try {
                                Storage storage = (Storage) o;

                                if (storage.isResponseSuccess() && storage.getJsonDocList().
                                        size() > 0) {

                                    Storage.JSONDocument jsonDocument = storage.getJsonDocList().
                                            get(0);

                                    try {

                                        String selection = "object_id = ? AND collection_name = ?";
                                        // WHERE clause arguments
                                        String[] selectionArgs = {jsonDocument.getDocId(),Config.collectionAdmin};

                                        String valuesArray[] = {jsonDocument.getDocId(),
                                                jsonDocument.getUpdatedAt(),
                                                jsonDocument.getJsonDoc(),
                                                Config.collectionAdmin};
                                        boolean output = BillingRecovery.dbCon.updateBulk(DbHelper.TABLE_USER_DETAILS, selection, valuesArray, utils.columnNamesUserDetails, selectionArgs);

                                        if(output){
                                            cd.displayMessage("You have login successfully..");
                                            sharedPref.clearPref();
                                            sharedPref.setLoginInfo(jsonDocument.getDocId(),username);
                                            Intent i = new Intent(MainActivity.this,DashboardActivity.class);
                                            startActivity(i);
                                        }

                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }


                                } else {
                                    if (progressDialog.isShowing())
                                        progressDialog.dismiss();
                                    cd.displayMessage("Invalid Credentials");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                                if (progressDialog.isShowing())
                                    progressDialog.dismiss();
                                cd.displayMessage("Please check your internet connection and try again!!");
                            }
                        }

                        @Override
                        public void onException(Exception e) {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                            try {
                                if (e != null) {
                                    cd.displayMessage("Invalid Credentials");
                                } else {
                                    cd.displayMessage("Please check your internet connection and try again!!");
                                }
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }
                        }
                    });

        } else {
            cd.displayMessage("Please check your internet connection and try again!!");
        }
    }
}
