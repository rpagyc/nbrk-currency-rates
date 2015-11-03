package com.nbrk.rates.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nbrk.rates.CurrencyRatesService;
import com.nbrk.rates.DatePickerDialogFragment;
import com.nbrk.rates.R;
import com.nbrk.rates.RatesAdapter;
import com.nbrk.rates.model.CurrencyRatesItem;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener,
        SwipeRefreshLayout.OnRefreshListener {

    ListView list;
    TextView title;
    //MoPubView adview;

    String sTitle;

    String loading;

    Calendar date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMMM yyyy (HH:mm)");
    List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
    RatesAdapter ratesAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        setSupportActionBar(toolbar);

        list = (ListView) findViewById(R.id.lv_currency_rates);
        title = (TextView) findViewById(R.id.title);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        sTitle = getResources().getString(R.string.title);
        loading = getResources().getString(R.string.loading);

        // set current date
        date = Calendar.getInstance();

        loadRatesAsync(sdf.format(date.getTime()), false);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    void loadRatesAsync(String date, boolean refresh) {
        //Check connection
        if (isOnline()) {
            //Call service
            Intent intent = new Intent(getApplicationContext(), CurrencyRatesService.class);
            intent.putExtra("date", date);
            intent.putExtra("refresh", refresh);
            startService(intent);
        } else {
            Toast.makeText(this, R.string.no_network_connection, Toast.LENGTH_LONG).show();
        }
    }

    //public void onEventMainThread(List<CurrencyRatesItem> currencyRatesItemList) {
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void handle(List<CurrencyRatesItem> currencyRatesItemList) {
        mSwipeRefreshLayout.setRefreshing(false);
        title.setText(sTitle + " " + sdf2.format(date.getTime()));
        this.currencyRatesItemList = currencyRatesItemList;
        if (ratesAdapter == null) {
            ratesAdapter = new RatesAdapter(getApplicationContext(), currencyRatesItemList);
            list.setAdapter(ratesAdapter);
        }
        ratesAdapter.setCurrencyRatesList(currencyRatesItemList);
        ratesAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_refresh:
                date = Calendar.getInstance();
                loadRatesAsync(sdf.format(date.getTime()), true);
                return true;
            case R.id.menu_date:
                showDatePickerDialog();
                return true;
            case R.id.menu_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_converter:
                Intent converter = new Intent(this, ConverterActivity.class);
                EventBus.getDefault().postSticky(currencyRatesItemList);
                startActivity(converter);
                return true;
            case R.id.menu_chart:
                Intent chart = new Intent(this, ChartActivity.class);
                EventBus.getDefault().postSticky(currencyRatesItemList);
                startActivity(chart);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showDatePickerDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerDialogFragment.YEAR, date.get(Calendar.YEAR));
        bundle.putInt(DatePickerDialogFragment.MONTH, date.get(Calendar.MONTH));
        bundle.putInt(DatePickerDialogFragment.DATE, date.get(Calendar.DAY_OF_MONTH));

        DatePickerDialogFragment datePicker = new DatePickerDialogFragment();

        datePicker.setArguments(bundle);
        datePicker.show(getFragmentManager(), "DatePickerDialogFragment");
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        date.set(year, month, day);
        loadRatesAsync(sdf.format(date.getTime()), false);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
        loadRatesAsync(sdf.format(date.getTime()), false);
    }

    @Override
    public void onRefresh() {
        // начинаем показывать прогресс
        mSwipeRefreshLayout.setRefreshing(true);
        date = Calendar.getInstance();
        loadRatesAsync(sdf.format(date.getTime()), true);
    }
}