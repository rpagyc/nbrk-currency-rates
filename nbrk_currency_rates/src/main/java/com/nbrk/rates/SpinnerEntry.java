package com.nbrk.rates;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.nbrk.rates.model.CurrencyRatesItem;
import junit.framework.Assert;

public class SpinnerEntry extends LinearLayout {

    TextView text;
    ImageView icon;

    private Context context;

    public SpinnerEntry(Context context) {
        this(context, null);
    }

    public SpinnerEntry(Context context, AttributeSet attrs) {
        super(context);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.spinner_entry_children, this, true);
        init();
    }

    private void init() {
        text = (TextView)findViewById(R.id.text);
        icon = (ImageView) findViewById(R.id.icon);
    }

    public static SpinnerEntry inflate(ViewGroup parent) {
        return (SpinnerEntry) LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_entry, parent, false);
    }

    public void bind(CurrencyRatesItem currencyRatesItem) {
        String currency = currencyRatesItem.getTitle();
        if (currency.equalsIgnoreCase("TRY")) {
            currency = "YTL";
        }
        text.setText(currency);
        icon.setImageResource(getDrawable(context, currency.toLowerCase()));
    }

    // image resource helper
    private static int getDrawable(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
