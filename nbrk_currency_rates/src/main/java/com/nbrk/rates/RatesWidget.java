package com.nbrk.rates;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.nbrk.rates.activity.WidgetConfig;
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

    Calendar date = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat time = new SimpleDateFormat("HH:mm");

    List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();

    public static final String DATA_FETCHED = "com.nbrk.rates.DATA_FETCHED";
    public static final String WIDGET_UPDATE = "com.nbrk.rates.WIDGET_UPDATE";
    public static final String WIDGET_CONFIG = "com.nbrk.rates.WIDGET_CONFIG";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
//        RemoteViews remoteViews;
//        ComponentName componentName;
//
//        Intent config = new Intent(WIDGET_CONFIG);
//        Intent update = new Intent(WIDGET_UPDATE);
//
//        remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
//        componentName = new ComponentName(context, RatesWidget.class);
//
//        remoteViews.setOnClickPendingIntent(R.id.widget_config, PendingIntent.getBroadcast(context, 0, config, 0));
//        remoteViews.setOnClickPendingIntent(R.id.widget_update, PendingIntent.getBroadcast(context, 0, update, 0));
//
//        appWidgetManager.updateAppWidget(componentName, remoteViews);

        updateRates(context);

    }

    public void updateRates(Context context) {
        // Check connection
        if (isOnline(context)) {
            //Call service
            Intent intent = new Intent(context, CurrencyRatesService.class);
            intent.putExtra("date", sdf.format(date.getTime()));
            intent.putExtra("class_name", this.getClass().getName());
            context.startService(intent);
        } else {
            Toast.makeText(context, R.string.no_network_connection, Toast.LENGTH_LONG).show();
        }
    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Intent svcIntent = new Intent(context, WidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        remoteViews.setRemoteAdapter(appWidgetId, R.id.lv_widget_rates, svcIntent);
        Intent config = new Intent(context, WidgetConfig.class);
        Intent update = new Intent(WIDGET_UPDATE);
        remoteViews.setOnClickPendingIntent(R.id.widget_config, PendingIntent.getActivity(context, 0, config, 0));
        remoteViews.setOnClickPendingIntent(R.id.widget_update, PendingIntent.getBroadcast(context, 0, update, 0));
        remoteViews.setTextViewText(R.id.widget_time, time.format(date.getTime()));
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
            //int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
            ComponentName widget = new ComponentName(context, getClass().getName());
            AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = mgr.getAppWidgetIds(widget);
            for (int appWidgetId : appWidgetIds) {
                RemoteViews remViews = updateWidgetListView(context, 0);
                mgr.updateAppWidget(appWidgetId, remViews);
                mgr.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_widget_rates);
            }
        } else if (intent.getAction().equals(WIDGET_UPDATE)) {
            // REFRESH RATES
            updateRates(context);
        }
    }

}
