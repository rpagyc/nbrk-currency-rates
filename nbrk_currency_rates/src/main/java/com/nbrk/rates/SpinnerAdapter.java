package com.nbrk.rates;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nbrk.rates.model.CurrencyRatesItem;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    Context context;

    List<CurrencyRatesItem> currencyRatesItemList;

    public void setCurrencyRatesItemList(List<CurrencyRatesItem> currencyRatesItemList) {
        this.currencyRatesItemList = currencyRatesItemList;
    }

    public SpinnerAdapter (Context context, List<CurrencyRatesItem> currencyRatesItemList) {
        this.context = context;
        this.currencyRatesItemList = currencyRatesItemList;
    }

    public int getCount() {
        return currencyRatesItemList.size();
    }

    public CurrencyRatesItem getItem(int position) {
        return currencyRatesItemList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SpinnerEntry spinnerEntry = (SpinnerEntry) convertView;
        if (spinnerEntry == null) {
            spinnerEntry = SpinnerEntry.inflate(parent);
        }
        spinnerEntry.bind(getItem(position));
        return spinnerEntry;
    }
}
