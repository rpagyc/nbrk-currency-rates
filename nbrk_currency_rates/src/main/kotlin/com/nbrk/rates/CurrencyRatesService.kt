package com.nbrk.rates

import android.app.IntentService
import android.content.Intent
import com.nbrk.rates.extensions.TAG
import com.nbrk.rates.extensions.debug
import com.nbrk.rates.extensions.error
import com.nbrk.rates.extensions.toDateString
import com.nbrk.rates.home.model.RatesModel
import rx.Observable
import java.text.SimpleDateFormat
import java.util.*

class CurrencyRatesService : IntentService(TAG) {
  companion object {
    val KEY_DATE = "$TAG#date"
    val KEY_PERIOD = "$TAG#period"
    val KEY_CURRENCY = "$TAG#currency"
  }

  var date = ""
  var period = 0
  var currency = ""

  override fun onHandleIntent(intent: Intent) {

    date = intent.getStringExtra(KEY_DATE)
    period = intent.getIntExtra(KEY_PERIOD, 0)
    currency = intent.getStringExtra(KEY_CURRENCY)?:""

    if (period > 0) {
      loadRatesForPeriod()
    } else {
      loadRatesForDate()
    }

  }

  fun loadRatesForDate() {
    RatesModel.instance.getRestRates(date)
      .take(1)
      .doOnNext { RatesModel.instance.putRates(it) }
      .subscribe(
        { rates -> debug(rates) },
        { error -> error(error) }
      )
  }

  fun loadRatesForPeriod() {
    val startDate = Calendar.getInstance()
    startDate.add(Calendar.DATE, -period)

    RatesModel.instance.getRatesItems(date, period, currency)
      .take(1)
      .filter { it.size < period }
      .map { it.map { it.date.toDateString(SimpleDateFormat("dd.MM.yyyy")) } }
      .flatMap {
        dbDates ->
        Observable.from(1..period)
          .filter {
            startDate.add(Calendar.DATE, 1)
            startDate.toDateString() !in dbDates
          }
          .flatMap {
            RatesModel.instance.getRestRates(startDate.toDateString())
          }
      }
      .doOnNext { RatesModel.instance.putRates(it) }
      .subscribe(
        { rates -> debug(rates) },
        { error -> error(error) }
      )
  }


  /*
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

                if (!currencyRates.currencyRatesItemList.isEmpty()) {
                    EventBus.getDefault().post(currencyRates.currencyRatesItemList);
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
        CurrencyRates currencyRates = new CurrencyRates();
        NbrkApi nbrkService = new Retrofit.Builder()
                .baseUrl(NbrkApi.API_URL)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build()
                .create(NbrkApi.class);
        Call<CurrencyRates> call = nbrkService.getCurrencyRates(date);

        try {
            currencyRates = call.execute().body();
//          Call<RatesResponse> c = nbrkService.getRates(date);
//          RatesResponse rr = c.execute().body();
//          Log.d("response", rr.toString());
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
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
        String date = currencyRates.date;
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        for (CurrencyRatesItem currencyRatesItem : currencyRates.currencyRatesItemList) {
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
 */
}