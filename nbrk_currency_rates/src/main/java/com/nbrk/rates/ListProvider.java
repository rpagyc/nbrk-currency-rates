package com.nbrk.rates;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.nbrk.rates.model.CurrencyRatesItem;

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
        currencyRatesItemList = CurrencyRatesService.currencyRatesItems;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {

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
        remoteViews.setTextViewText(R.id.fc_label, currencyRatesItem.getFullname());
        remoteViews.setTextViewText(R.id.price_difference, currencyRatesItem.getChange());
        return remoteViews;
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
