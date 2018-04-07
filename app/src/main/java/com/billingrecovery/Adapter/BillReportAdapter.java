package com.billingrecovery.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.billingrecovery.Activity.BillReportsActivity;
import com.billingrecovery.Model.BillReportModel;
import com.billingrecovery.R;
import com.billingrecovery.libs.ConnectionDetector;

import java.util.List;

/**
 * Created by Mahesh on 3/29/2018.
 */

public class BillReportAdapter extends BaseAdapter {

    Context mContext;
    ConnectionDetector cd;
    private Activity mactivity;
    BillReportModel billReportModel;
    private List<BillReportModel> billReportDetailsArraylist;

    //String total_amount,doc_no, created_date,customer_name,remaining_amount,updated_date,bill_number,paid_amount;


    public BillReportAdapter(Context context, Activity activity, List<BillReportModel> billReportModelslist) {

        this.mContext = context;
        this.mactivity = activity;
        this.billReportDetailsArraylist = billReportModelslist;
        cd = new ConnectionDetector(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return billReportDetailsArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return billReportDetailsArraylist.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        convertView = null;
        if(convertView==null){
            LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.billreport_item, null);

            TableRow row = (TableRow) convertView.findViewById(R.id.tr_label_stock);
            TextView txtname = (TextView)convertView.findViewById(R.id.txtname);
            TextView txtdocno =(TextView)convertView.findViewById(R.id.txtdocno);
            TextView txtbillno =(TextView)convertView.findViewById(R.id.txtbill);
            TextView txttotalamt = (TextView)convertView.findViewById(R.id.txttotal);
            TextView txtpaidamt = (TextView)convertView.findViewById(R.id.txtpaid);
            TextView txtremainamt = (TextView)convertView.findViewById(R.id.txtremain);
            TextView txtcreatedate = (TextView)convertView.findViewById(R.id.txtcreatedate);
            TextView txtupdatedate = (TextView)convertView.findViewById(R.id.txtupdatedate);
            TextView txtview =(TextView)convertView.findViewById(R.id.txtview);


            billReportModel = billReportDetailsArraylist.get(position);

            /*customer_name = billReportModel.getCustomer_name();
            doc_no = billReportModel.getDoc_id();
            bill_number = billReportModel.getBill_number();
            total_amount = billReportModel.getTotal_amount();
            paid_amount = billReportModel.getPaid_amount();
            remaining_amount = billReportModel.getRemaining_amount();
            created_date = billReportModel.getCreated_date();
            updated_date = billReportModel.getUpdated_date();*/

            txtname.setText(billReportModel.getCustomer_name());
            txtdocno.setText(billReportModel.getDoc_id());
            txtbillno.setText(billReportModel.getBill_number());
            txttotalamt.setText(billReportModel.getTotal_amount());
            txtpaidamt.setText(billReportModel.getPaid_amount());
            txtremainamt.setText(billReportModel.getRemaining_amount());
            txtcreatedate.setText(billReportModel.getCreated_date());
            txtupdatedate.setText(billReportModel.getUpdated_date());
            txtview.setText("View");


            txtview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        billReportModel = billReportDetailsArraylist.get(position);
                        showDialog(billReportModel.getCustomer_name(), billReportModel.getDoc_id(), billReportModel.getBill_number(),
                                billReportModel.getTotal_amount(), billReportModel.getPaid_amount(), billReportModel.getRemaining_amount(),
                                billReportModel.getCreated_date(), billReportModel.getUpdated_date());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
        return convertView;

    }

    public void showDialog(String name, String docno, String billno, String totalamt,
                           String paidamt,String remainamt,String credate,String updatedate) {

        final Dialog dialog = new Dialog(mactivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);

        TextView text = (TextView) dialog.findViewById(R.id.str_name);
        TextView text1 = (TextView) dialog.findViewById(R.id.str_docno);
        TextView text2 = (TextView) dialog.findViewById(R.id.str_billno);
        TextView text3 = (TextView) dialog.findViewById(R.id.str_totalamt);
        TextView text4 = (TextView) dialog.findViewById(R.id.str_paidamt);
        TextView text5 = (TextView) dialog.findViewById(R.id.str_remainingamt);
        TextView text6 = (TextView) dialog.findViewById(R.id.str_cretdate);
        TextView text7 = (TextView) dialog.findViewById(R.id.str_updatedate);
        TextView close = (TextView) dialog.findViewById(R.id.done);

        text.setText(name);
        text1.setText(docno);
        text2.setText(billno);
        text3.setText(totalamt);
        text4.setText(paidamt);
        text5.setText(remainamt);
        text6.setText(credate);
        text7.setText(updatedate);

        //Button dialogButton = (Button) dialog.findViewById(R.id.done);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}
