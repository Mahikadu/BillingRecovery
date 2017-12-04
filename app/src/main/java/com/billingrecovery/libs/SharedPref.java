package com.billingrecovery.libs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Admin on 6/19/2017.
 */
public class SharedPref {

    private Context context;
    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    private static final String KEY_DOC_ID = "key_doc_id";
    private static final String KEY_USERNAME = "key_username";


    public SharedPref(Context _ctx) {
        context = _ctx;
        sharedPref = _ctx.getSharedPreferences("BillingRecovery", context.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setLoginInfo(String docId, String baname) {
        editor.putString(KEY_DOC_ID, docId);
        editor.putString(KEY_USERNAME, baname);
        editor.commit();
    }

    public String getDocId() {
        String loginId = sharedPref.getString(KEY_DOC_ID, "");
        return loginId;
    }


    public String getuserName() {
        String baname = sharedPref.getString(KEY_USERNAME, "");
        return baname;
    }

    public void clearPref() {
        try {
            editor.remove(KEY_DOC_ID);
            editor.remove(KEY_USERNAME);
            editor.clear();
            editor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}