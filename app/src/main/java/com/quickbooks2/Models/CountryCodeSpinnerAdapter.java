package com.quickbooks2.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.quickbooks2.R;

import java.util.ArrayList;

public class CountryCodeSpinnerAdapter extends ArrayAdapter<CountryCodeModel> {

    Context context;
    private final ArrayList<CountryCodeModel> arrayList;

    public CountryCodeSpinnerAdapter(Context context, ArrayList<CountryCodeModel> arrayList) {
        super(context, R.layout.spinner_item, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, null);
        TextView tvCountryName = (TextView) row.findViewById(R.id.tvCountryName);
        TextView tvCountryCode = (TextView) row.findViewById(R.id.tvCountryCode);

        String countryData = arrayList.get(position).getCountryFlag() + "  " + arrayList.get(position).getCountryName();
        tvCountryName.setText(countryData);
        tvCountryCode.setText(arrayList.get(position).getPhoneCode());
        return row;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_item, null);
        TextView tvCountryName = (TextView) row.findViewById(R.id.tvCountryName);
        TextView tvCountryCode = (TextView) row.findViewById(R.id.tvCountryCode);

        String countryData = arrayList.get(position).getCountryFlag() + arrayList.get(position).getCountryName();
        tvCountryName.setText(countryData);
        tvCountryCode.setText(arrayList.get(position).getPhoneCode());
        return row;
    }
}
