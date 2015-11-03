package com.nbrk.rates;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.nbrk.rates.model.CurrencyRatesItem;
import junit.framework.Assert;

import java.math.BigDecimal;

public class CurrencyRatesItemView extends RelativeLayout {
    TextView fc;
    TextView fc_label;
    TextView price;
    TextView price_difference;
    ImageView flag;

    private Context context;

    public static CurrencyRatesItemView inflate(ViewGroup parent) {
        return (CurrencyRatesItemView) LayoutInflater.from(parent.getContext()).inflate(R.layout.row_currency_rates, parent, false);
    }

    public void init() {
        fc = ((TextView) findViewById(R.id.fc));
        price = ((TextView) findViewById(R.id.price));
        flag = ((ImageView) findViewById(R.id.flag));
        price_difference = ((TextView) findViewById(R.id.price_difference));
        fc_label = ((TextView) findViewById(R.id.fc_label));
    }

    public CurrencyRatesItemView(Context context) {
        this(context,null);
    }

    public CurrencyRatesItemView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.row_currency_rates_children, this, true);
        init();
    }

    public CurrencyRatesItemView(Context context, AttributeSet attrs) {
        this(context, null, 0);
    }

    public void bind(CurrencyRatesItem currencyRatesItem) {

        //Log.d("CurrencyRatesItem", currencyRatesItem.toString());

        String currency = currencyRatesItem.getTitle();

        fc.setText(currency);
        fc_label.setText(currencyRatesItem.getQuant() + " " + currencyRatesItem.getFullname().toLowerCase());
        price.setText(currencyRatesItem.getDescription());

        BigDecimal price_diff = new BigDecimal(currencyRatesItem.getChange());

        if (currencyRatesItem.getInd().equals("UP")) {
            price_difference.setTextColor(Color.rgb(90, 150, 55));
            price_difference.setText("+" + String.format("%.2f", price_diff).replace(",", "."));
        } else if (currencyRatesItem.getInd().equals("DOWN")) {
            price_difference.setTextColor(Color.RED);
            price_difference.setText(String.format("%.2f", price_diff).replace(",", "."));
        } else {
            price_difference.setTextColor(Color.LTGRAY);
            price_difference.setText(String.format("%.2f", price_diff).replace(",", "."));
        }


        // TRY fix - reserved word 'try' can't be used as image name
        if (currency.equalsIgnoreCase("TRY")) {
            currency = "YTL";
        }

        flag.setImageResource(getDrawable(context, currency.toLowerCase()));
    }

    // image resource helper
    private static int getDrawable(Context context, String name) {
        Assert.assertNotNull(context);
        Assert.assertNotNull(name);
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
