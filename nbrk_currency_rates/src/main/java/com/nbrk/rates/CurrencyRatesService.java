package com.nbrk.rates;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.nbrk.rates.db.DbOpenHelper;
import com.nbrk.rates.model.CurrencyRates;
import com.nbrk.rates.model.CurrencyRatesItem;
import de.greenrobot.event.EventBus;
import nl.qbusict.cupboard.QueryResultIterable;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static nl.qbusict.cupboard.CupboardFactory.cupboard;

public class CurrencyRatesService extends IntentService {

    private static final String TAG = CurrencyRatesService.class.getName();
    private DbOpenHelper dbOpenHelper;
    static List<CurrencyRatesItem> currencyRatesItems = new ArrayList<>();
    //SQLiteDatabase db;

    public CurrencyRatesService() {
        super(TAG);
        dbOpenHelper = new DbOpenHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // 1. get rates from db
        //db = dbOpenHelper.getWritableDatabase();
        //db.beginTransaction();
        List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
        String date = intent.getStringExtra("date");
        String period = intent.getStringExtra("period");
        String currency = intent.getStringExtra("currency");
        boolean refresh = intent.getBooleanExtra("refresh", false);

        if (date != null) {
            currencyRatesItemList = getCurrencyRatesFromDb(date);
            if (currencyRatesItemList.isEmpty() || refresh) {
                // 2. get rates from network
                CurrencyRates currencyRates = getCurrencyRatesFromNetwork(date);

                if (!currencyRates.getCurrencyRatesItemList().isEmpty()) {
                    EventBus.getDefault().post(currencyRates.getCurrencyRatesItemList());
                    // remove old values
                    deleteCurrencyRates(date);
                    // 3. save rates
                    saveCurrencyRates(currencyRates);
                } else {
                    Log.e(TAG, "Failed to load currency rates from network");
                }

            } else {
                EventBus.getDefault().post(currencyRatesItemList);
            }
            currencyRatesItems = currencyRatesItemList;
        }

        // chart info
        if (period != null && currency != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            Calendar end = Calendar.getInstance();
            Calendar start = (Calendar) end.clone();
            if (period.equals("Неделя")) {
                start.add(Calendar.DATE, -6);
            } else if (period.equals("Месяц")) {
                start.add(Calendar.DATE, -29);
            } else if (period.equals("Год")) {
                start.add(Calendar.DATE, -364);
            }

            while (!start.after(end)) {
                String d = sdf.format(start.getTime());
                //get from db
                CurrencyRatesItem currencyRatesItem = getCurrencyRatesItem(d, currency);
                //get from network
                if (currencyRatesItem == null) {
                    CurrencyRates currencyRates = getCurrencyRatesFromNetwork(d);
                    currencyRatesItem = currencyRates.getCurrencyRatesItem(currency);
                    if (currencyRatesItem != null) {
                        saveCurrencyRates(currencyRates);
                    } else {
                        //no rates, set default values
                        currencyRatesItem = new CurrencyRatesItem();
                        currencyRatesItem.setDate(d);
                        currencyRatesItem.setTitle(currency);
                        currencyRatesItem.setDescription("0.00");
                    }
                }

                //Log.d(TAG, currencyRatesItem.toString());
                currencyRatesItemList.add(currencyRatesItem);
                start.add(Calendar.DATE, 1);
            }
            EventBus.getDefault().post(currencyRatesItemList);
        }

        //db.endTransaction();
        //db.close();
        populateWidget();
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(RatesWidget.DATA_FETCHED);
        //widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        sendBroadcast(widgetUpdateIntent);
    }

    private CurrencyRatesItem getCurrencyRatesItem(String date, String currency) {
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        CurrencyRatesItem currencyRatesItem = cupboard().withDatabase(db).query(CurrencyRatesItem.class).withSelection("date = ? and title = ?", date, currency).get();
        db.close();
        return currencyRatesItem;
    }

    private CurrencyRates getCurrencyRatesFromNetwork(String date) {
        CurrencyRates currencyRates;
        NbrkApi nbrkService = new RestAdapter.Builder()
                .setEndpoint(NbrkApi.API_URL)
                .setConverter(new SimpleXMLConverter())
                .build()
                .create(NbrkApi.class);
        currencyRates = nbrkService.getCurrencyRates(date);
        return currencyRates;
    }

    private List<CurrencyRatesItem> getCurrencyRatesFromDb(String date) {
        List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor = cupboard().withDatabase(db).query(CurrencyRatesItem.class).withSelection("date = ?", date).orderBy("title").getCursor();
        QueryResultIterable<CurrencyRatesItem> itr = cupboard().withCursor(cursor).iterate(CurrencyRatesItem.class);
        for (CurrencyRatesItem currencyRatesItem : itr) {
            currencyRatesItemList.add(currencyRatesItem);
        }
        itr.close();
        db.close();
        return currencyRatesItemList;
    }

    private void saveCurrencyRates(CurrencyRates currencyRates) {
        String date = currencyRates.getDate();
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        for (CurrencyRatesItem currencyRatesItem : currencyRates.getCurrencyRatesItemList()) {
            currencyRatesItem.setDate(date);
            cupboard().withDatabase(db).put(currencyRatesItem);
        }
        db.close();
    }

    public void deleteCurrencyRates(String date) {
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        cupboard().withDatabase(db).delete(CurrencyRatesItem.class, "date = ?", date);
        db.close();
    }

}