package com.billingrecovery.libs;

import android.content.Context;

import java.util.Arrays;

/**
 * Created by Admin on 29-11-2017.
 */

public class Utils {

    public String[] columnNamesUserDetails = new String[50];

    private Context context;

    public Utils(Context context){
        this.context = context;

        String[] userdetailsArray = {"object_id","updated_date","document","collection_name"};
        columnNamesUserDetails = Arrays.copyOf(userdetailsArray,userdetailsArray.length);
    }
}
