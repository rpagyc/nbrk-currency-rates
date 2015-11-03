package com.nbrk.rates.activity;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import com.nbrk.rates.R;
import com.nbrk.rates.SpinnerAdapter;
import com.nbrk.rates.model.CurrencyRatesItem;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class ConverterActivity extends ActionBarActivity implements AdapterView.OnItemSelectedListener {

    EditText editText1;
    EditText editText2;
    Spinner spinner1;
    Spinner spinner2;
    SpinnerAdapter spinnerAdapter;

    private List<CurrencyRatesItem> rates = new ArrayList<>();
    private CurrencyRatesItem kzt;

    //MoPubView adview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_converter);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_awesome_toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ConverterActivity.this);
            }
        });
        setSupportActionBar(toolbar);

        rates.clear();
        rates.addAll(EventBus.getDefault().getStickyEvent(ArrayList.class));
        //Log.d("List size", rates.size()+"");
        kzt = new CurrencyRatesItem();
        kzt.setTitle("KZT");
        kzt.setDescription("1");
        kzt.setQuant("1");
        rates.add(rates.size(), kzt);

        spinnerAdapter = new SpinnerAdapter(this, rates);
        spinner1.setAdapter(spinnerAdapter);
        spinner1.setOnItemSelectedListener(this);
        spinner1.setSelection(0);
        spinner2.setAdapter(spinnerAdapter);
        spinner2.setOnItemSelectedListener(this);
        spinner2.setSelection(rates.size() - 1);

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                convert();
            }
        });
        editText1.setText("1.00");

    }

    //public void onEventMainThread(List<CurrencyRatesItem> currencyRatesItemList) {
    @Subscribe(sticky = true, threadMode = ThreadMode.MainThread)
    public void handle(List<CurrencyRatesItem> currencyRatesItemList) {
    }

    public void convert() {
        if (rates != null) {
            Double price1 = Double.parseDouble(rates.get(spinner1.getSelectedItemPosition()).getDescription());
            Double quant1 = Double.parseDouble(rates.get(spinner1.getSelectedItemPosition()).getQuant());
            Double price2 = Double.parseDouble(rates.get(spinner2.getSelectedItemPosition()).getDescription());
            Double quant2 = Double.parseDouble(rates.get(spinner2.getSelectedItemPosition()).getQuant());
            if (!editText1.getText().toString().equals("")) {
                String amount = editText1.getText().toString();
                editText2.setText((String.format("%.2f", (Double.parseDouble(amount) * (price1/quant1) / (price2/quant2)))).replace(",", "."));
            } else {
                editText2.setText("");
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        convert();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    protected void onDestroy() {
        //adview.destroy();
        super.onDestroy();
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

}
