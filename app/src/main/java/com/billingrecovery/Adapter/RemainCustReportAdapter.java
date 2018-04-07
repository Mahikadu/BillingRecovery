package com.billingrecovery.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.billingrecovery.Activity.UpdateBillActivity;
import com.billingrecovery.Model.BillReportModel;
import com.billingrecovery.R;
import com.billingrecovery.libs.ConnectionDetector;

import java.util.List;

/**
 * Created by Mahesh on 3/30/2018.
 */

public class RemainCustReportAdapter extends BaseAdapter {

    Context mContext;
    private Activity mactivity;
    BillReportModel billReportModel;
    private List<BillReportModel> remaincustDetailsArraylist;

    public RemainCustReportAdapter(Context context, Activity activity, List<BillReportModel> remaincustReportModelslist) {

        this.mContext = context;
        this.mactivity = activity;
        this.remaincustDetailsArraylist = remaincustReportModelslist;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return remaincustDetailsArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return remaincustDetailsArraylist.get(position);
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

            convertView = mInflater.inflate(R.layout.remain_customer_report_item, null);

            TextView txtname = (TextView)convertView.findViewById(R.id.txtcustname);
            TextView txtdocno =(TextView)convertView.findViewById(R.id.txtcustdocno);
            TextView txtbillno =(TextView)convertView.findViewById(R.id.txtcustbill);
            TextView txttotalamt = (TextView)convertView.findViewById(R.id.txtcusttotal);
            TextView txtpaidamt = (TextView)convertView.findViewById(R.id.txtcustpaid);
            TextView txtremainamt = (TextView)convertView.findViewById(R.id.txtcustremain);
            TextView txtcreatedate = (TextView)convertView.findViewById(R.id.txtcustcreatedate);
            TextView txtupdatedate = (TextView)convertView.findViewById(R.id.txtcustupdatedate);
            TextView txtupdate =(TextView)convertView.findViewById(R.id.txtupdate);


            billReportModel = remaincustDetailsArraylist.get(position);

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
            txtupdate.setText("Update");


            txtupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        billReportModel = remaincustDetailsArraylist.get(position);
                        Intent intent = new Intent(mactivity, UpdateBillActivity.class);
                        intent.putExtra("en", billReportModel); //second param is Serializable
                        mactivity.startActivity(intent);
                        mactivity.finish();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

        }
        return convertView;

    }

}
