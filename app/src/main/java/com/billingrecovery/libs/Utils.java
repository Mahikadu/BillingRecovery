package com.billingrecovery.libs;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by Admin on 29-11-2017.
 */

public class Utils {

    private static final Locale locale = Locale.ENGLISH;

    public final static SimpleDateFormat readFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", locale);
    /* public final static SimpleDateFormat readFormatLocal =
             new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS", locale);*/
    public final static SimpleDateFormat readFormatLocalDB =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", locale);
    public final static SimpleDateFormat writeFormat = new
            SimpleDateFormat("HH:mm dd MMM yyyy", locale);
    public final static SimpleDateFormat writeFormatDate = new
            SimpleDateFormat("dd-MMM-yyyy", locale);
    public final static SimpleDateFormat writeFormatDateDB = new
            SimpleDateFormat("yyyy-MM-dd", locale);
    public final static SimpleDateFormat writeFormatDateMonth = new
            SimpleDateFormat("M", locale);
    public final static SimpleDateFormat writeFormatDateYear = new
            SimpleDateFormat("yyyy", locale);
    public final static SimpleDateFormat writeFormatDateMY = new
            SimpleDateFormat("dd MMM yyyy", locale);
    public final static SimpleDateFormat queryFormat =
            new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", locale);
    public final static SimpleDateFormat queryFormatday =
            new SimpleDateFormat("yyyyMMddHHmmss", locale);
    public final static SimpleDateFormat writeFormatLocal = new
            SimpleDateFormat("HH:mm dd MMM yyyy", locale);
    private final static SimpleDateFormat writeFormatTime = new
            SimpleDateFormat("HH:mm", locale); //

    //
    public String[] columnNamesUserDetails = new String[50];
    public String[] columnNamesGenerateBill = new String[50];

    private Context context;

    public Utils(Context context){
        this.context = context;

        String[] userdetailsArray = {"object_id","updated_date","document","collection_name"};
        columnNamesUserDetails = Arrays.copyOf(userdetailsArray,userdetailsArray.length);


        String[] generatebillArray = {"object_id","customer_name","bill_number","strdate",
                "total_amount","paid_amount","remaining_amount","image_url","created_date","updated_date","collection_name","status"};
        columnNamesGenerateBill = Arrays.copyOf(generatebillArray,generatebillArray.length);
    }
}
