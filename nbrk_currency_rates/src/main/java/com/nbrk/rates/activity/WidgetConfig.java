package com.nbrk.rates.activity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.nbrk.rates.R;
import com.nbrk.rates.RatesWidget;

/**
 * Created by rpagyc on 09.11.2015.
 */
public class WidgetConfig extends ActionBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(WidgetConfig.this);
            }
        });
        setSupportActionBar(toolbar);

        // display the fragment as the main content
        getFragmentManager().beginTransaction()
                .replace(R.id.content_frame, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle paramBundle) {
            super.onCreate(paramBundle);
            addPreferencesFromResource(R.xml.widget_config);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        populateWidget();
    }

    private void populateWidget() {
        Intent widgetUpdateIntent = new Intent();
        widgetUpdateIntent.setAction(RatesWidget.DATA_FETCHED);
        //widgetUpdateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,appWidgetId);
        sendBroadcast(widgetUpdateIntent);
    }

}
