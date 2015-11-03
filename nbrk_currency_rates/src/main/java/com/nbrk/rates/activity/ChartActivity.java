package com.nbrk.rates.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.nbrk.rates.CurrencyRatesService;
import com.nbrk.rates.R;
import com.nbrk.rates.SpinnerAdapter;
import com.nbrk.rates.model.CurrencyRatesItem;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

/**
 * Created by rpagyc on 29.12.2014.
 */
public class ChartActivity extends ActionBarActivity{

    private List<CurrencyRatesItem> rates = new ArrayList<>();
    String[] data = {"Неделя", "Месяц", "Год"};
    Spinner currency_spinner;
    Spinner period_spinner;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ChartActivity.this);
            }
        });
        setSupportActionBar(toolbar);

        progress = new ProgressDialog(this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(getString(R.string.loading));

        currency_spinner = (Spinner) findViewById(R.id.spinner1);
        rates = EventBus.getDefault().getStickyEvent(ArrayList.class);
        SpinnerAdapter spinnerAdapter = new SpinnerAdapter(this, rates);
        currency_spinner.setAdapter(spinnerAdapter);
        currency_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progress.show();
                Intent intent = new Intent(getApplicationContext(), CurrencyRatesService.class);
                intent.putExtra("period", period_spinner.getSelectedItem().toString());
                intent.putExtra("currency", rates.get(position).getTitle());
                startService(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_period, data);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        period_spinner = (Spinner) findViewById(R.id.period);
        period_spinner.setAdapter(adapter);
        period_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                progress.show();
                Intent intent = new Intent(getApplicationContext(), CurrencyRatesService.class);
                intent.putExtra("period", parent.getItemAtPosition(position).toString());
                intent.putExtra("currency", rates.get(currency_spinner.getSelectedItemPosition()).getTitle());
                startService(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //public void onEventMainThread(List<CurrencyRatesItem> currencyRatesItemList) {
    @Subscribe(sticky = true, threadMode = ThreadMode.MainThread)
    public void handle(List<CurrencyRatesItem> currencyRatesItemList) {
        progress.dismiss();
        //Log.d("Chart Activity", "size: " + currencyRatesItemList.size());
        PlaceholderFragment placeholderFragment = new PlaceholderFragment();
        placeholderFragment.currencyRatesItemList = currencyRatesItemList;
        getSupportFragmentManager().beginTransaction().replace(R.id.container, placeholderFragment).commit();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        //EventBus.getDefault().registerSticky(this);
        EventBus.getDefault().register(this);
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;

        public List<CurrencyRatesItem> currencyRatesItemList = new ArrayList<>();
        public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun"};

        float minY, maxY = 0f;

        public PlaceholderFragment() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setRetainInstance(true);
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);

            generateData();

            resetViewport();

            return rootView;
        }

        private void resetViewport() {
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = minY;
            v.top = maxY;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        private void generateData() {

            List<Line> lines = new ArrayList<>();
            List<AxisValue> axisXValues = new ArrayList<>();

            List<PointValue> values = new ArrayList<>();

            minY = (float)Math.floor(Float.parseFloat(Collections.min(currencyRatesItemList).getDescription()));
            maxY = (float)Math.ceil(Float.parseFloat(Collections.max(currencyRatesItemList).getDescription()));

            for (int j = 0; j < currencyRatesItemList.size(); ++j) {
                values.add(new PointValue(j, Float.parseFloat(currencyRatesItemList.get(j).getDescription())));
            }

            if (currencyRatesItemList.size() == 7) {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE");
                Calendar end = Calendar.getInstance();
                Calendar start = (Calendar) end.clone();
                start.add(Calendar.DATE, -6);

                //start.add(Calendar.DATE, -6);
                int i = 0;
                while (!start.after(end)) {
                    String d = sdf.format(start.getTime());
                    //Log.d("Week day", d);
                    axisXValues.add(new AxisValue(i, d.toCharArray()));
                    start.add(Calendar.DATE, 1);
                    i++;
                }
            }

            if (currencyRatesItemList.size() == 30) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM");
                Calendar end = Calendar.getInstance();
                Calendar start = (Calendar) end.clone();
                start.add(Calendar.DATE, -29);

                int i = 0;
                while (!start.after(end)) {
                    String d = sdf.format(start.getTime());
                    //Log.d("Month day", d);
                    axisXValues.add(new AxisValue(i, d.toCharArray()));
                    start.add(Calendar.DATE, 5);
                    i += 5;
                }
            }

            if (currencyRatesItemList.size() == 365) {
                SimpleDateFormat sdf = new SimpleDateFormat("LLL");
                Calendar end = Calendar.getInstance();
                Calendar start = (Calendar) end.clone();
                start.add(Calendar.DATE, -364);

                int i = 0;
                while (!start.after(end)) {
                    String d = sdf.format(start.getTime());
                    //Log.d("Year month", d);
                    axisXValues.add(new AxisValue(i, d.toLowerCase().substring(0,3).toCharArray()));
                    start.add(Calendar.MONTH, 1);
                    i += 30;
                }
            }

            Line line = new Line(values);
            line.setColor(Color.parseColor("#8033B5E5"));
            line.setFilled(true);
            line.setHasPoints(false);
            lines.add(line);

            data = new LineChartData(lines);

            Axis axisX = new Axis(axisXValues);
            Axis axisY = new Axis();
            axisX.setHasLines(true);
            axisY.setHasLines(true);
            //axisY.setValues(axisYValues);
            axisY.setAutoGenerated(true);
            axisY.setName(getString(R.string.y_axis_name));
            axisX.setName(getString(R.string.x_axis_name));
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);

            //data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);

        }
    }
}