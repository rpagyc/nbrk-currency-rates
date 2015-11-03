package com.nbrk.rates;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.nbrk.rates.model.CurrencyRatesItem;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by rpagyc on 29.10.2015.
 */
public class RatesWidget extends AppWidgetProvider {

    Calendar date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
    public static final String DATA_FETCHED = "com.nbrk.rates.DATA_FETCHED";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        EventBus.getDefault().register(this);
        date = Calendar.getInstance();

        //Check connection
        if (isOnline(context)) {
            //Call service
            Intent intent = new Intent(context, CurrencyRatesService.class);
            intent.putExtra("date", sdf.format(date.getTime()));
            intent.putExtra("class_name", this.getClass().getName());
            context.startService(intent);
        } else {
            Toast.makeText(context, R.string.no_network_connection, Toast.LENGTH_LONG).show();
        }

//        for (int appWidgetId : appWidgetIds) {
//            RemoteViews remViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//            Log.d("Widget", "1");
//            Intent svcIntent = new Intent(context, WidgetService.class);
//            remViews.setRemoteAdapter(appWidgetId, R.id.lv_widget_rates, svcIntent);
//            appWidgetManager.updateAppWidget(appWidgetId, remViews);
//            Log.d("Widget", "2");
//        }
//        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget_rates);
//        Log.d("Widget", "3");
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        //svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(appWidgetId, R.id.lv_widget_rates, svcIntent);
        //remoteViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
        return remoteViews;
    }

    public boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    @Subscribe
    public void handle(List<CurrencyRatesItem> currencyRatesItemList) {
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(DATA_FETCHED)) {
            Log.d("Widget", "onReceive");
            //int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            ComponentName widget = new ComponentName(context, getClass().getName());
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = mgr.getAppWidgetIds(widget);
            for (int appWidgetId : appWidgetIds) {
                RemoteViews remViews = updateWidgetListView(context, 0);
                //remViews.setEmptyView(R.id.listViewWidget, R.id.empty_view);
                mgr.updateAppWidget(appWidgetId, remViews);
            }
        }
    }

}
