package com.global.travel.telecom.app.ui.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.global.travel.telecom.app.R;
import com.global.travel.telecom.app.model.TransactionDetailsActivationExtentionVoIPModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TransactionDetails_ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<TransactionDetailsActivationExtentionVoIPModel>> _listDataChild;

    public TransactionDetails_ExpandableListAdapter(Context context, List<String> listDataHeader,
                                                    HashMap<String, List<TransactionDetailsActivationExtentionVoIPModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition))).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.listview_transaction_details, null);
        }
        TextView amount = convertView.findViewById(R.id.amount);
        TextView remarks = convertView.findViewById(R.id.remarks);
        TextView transactionid = convertView.findViewById(R.id.transactionid);
        TextView PaymentType = convertView.findViewById(R.id.PaymentType);
        TextView DateOfPayment = convertView.findViewById(R.id.DateOfPayment);
        TextView payment = convertView.findViewById(R.id.payment);
        ImageView paymentStatusImage = convertView.findViewById(R.id.paymentStatusImage);


        amount.setText("Amount : $" + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getAmount().toString());
        remarks.setText("Remarks :" + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getRemarks());
        transactionid.setText("Transaction ID: " + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getTxnRefNo());
        PaymentType.setText("Payment Type: " + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getPaymentType());
        DateOfPayment.setText("Date of Payment: " + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getPaymentDtTm());
        payment.setText("Payment: " + _listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getPaymentStatus());

        if(!_listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition).getPaymentStatus().toLowerCase().trim().equals("success")){
            paymentStatusImage.setImageResource(R.drawable.payment_failed);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(this._listDataChild.get(this._listDataHeader.get(groupPosition))).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert infalInflater != null;
            convertView = infalInflater.inflate(R.layout.listview_group_transaction_details, null);
        }

        TextView lblListHeader = convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
