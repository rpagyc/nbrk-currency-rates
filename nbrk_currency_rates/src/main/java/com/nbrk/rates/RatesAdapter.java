package com.nbrk.rates;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.nbrk.rates.model.CurrencyRatesItem;

import java.util.ArrayList;
import java.util.List;

public class RatesAdapter extends BaseAdapter {
    Context context;
    List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();

    public RatesAdapter(Context context, List<CurrencyRatesItem> currencyRatesItemList) {
        this.context = context;
        setCurrencyRatesList(currencyRatesItemList);
    }

    public void setCurrencyRatesList(List<CurrencyRatesItem> currencyRatesItemList) {
        this.currencyRatesItemList.clear();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        for (CurrencyRatesItem currencyRatesItem : currencyRatesItemList) {
            // apply filter from settings
            if(sharedPref.getBoolean("pref_key_show_" + currencyRatesItem.getTitle(), true)) {
                this.currencyRatesItemList.add(currencyRatesItem);
            }
        }
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

        CurrencyRatesItemView currencyRatesItemView = (CurrencyRatesItemView) convertView;
        if (currencyRatesItemView == null) {
            currencyRatesItemView = CurrencyRatesItemView.inflate(parent);
        }
        currencyRatesItemView.bind(getItem(position));
        return currencyRatesItemView;
    }

}
