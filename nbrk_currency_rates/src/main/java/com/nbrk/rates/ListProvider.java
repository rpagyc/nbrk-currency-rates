package com.nbrk.rates;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.nbrk.rates.model.CurrencyRatesItem;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpagyc on 30.10.2015.
 */
public class ListProvider implements RemoteViewsService.RemoteViewsFactory {

    List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
    Context context;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        populateListItem();
    }

    private void populateListItem() {
        currencyRatesItemList.clear();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        for (CurrencyRatesItem currencyRatesItem : CurrencyRatesService.currencyRatesItems) {
            // apply filter from settings
            if(sharedPref.getBoolean("widget_show_" + currencyRatesItem.getTitle(), true)) {
                Log.d("Currency", currencyRatesItem.getTitle());
                currencyRatesItemList.add(currencyRatesItem);
            }
        }
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        currencyRatesItemList.clear();
        populateListItem();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return currencyRatesItemList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_row);
        CurrencyRatesItem currencyRatesItem = currencyRatesItemList.get(position);
        remoteViews.setTextViewText(R.id.fc, currencyRatesItem.getTitle());
        //price flag price_difference fc_lable
        remoteViews.setTextViewText(R.id.price, currencyRatesItem.getDescription());
        remoteViews.setTextViewText(R.id.fc_label, currencyRatesItem.getQuant() + " " + currencyRatesItem.getFullname().toLowerCase());
        remoteViews.setTextViewText(R.id.price_difference, currencyRatesItem.getChange());
        remoteViews.setImageViewResource(R.id.flag, getDrawable(context, currencyRatesItem.getTitle().toLowerCase()));
        if (currencyRatesItem.getInd().equals("UP")) {
            remoteViews.setTextColor(R.id.price_difference, Color.rgb(90, 150, 55));
        } else if (currencyRatesItem.getInd().equals("DOWN")) {
            remoteViews.setTextColor(R.id.price_difference, Color.RED);
        } else {
            remoteViews.setTextColor(R.id.price_difference, Color.LTGRAY);
        }
        return remoteViews;
    }

    // image resource helper
    private static int getDrawable(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        if (name.equals("try")) {
            name = "ytl";
        }
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
