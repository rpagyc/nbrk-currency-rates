package com.nbrk.rates.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name="rates")
public class CurrencyRates {
    @Element
    private String generator="";
    @Element
    private String title="";
    @Element
    private String link="";
    @Element
    private String description="";
    @Element
    private String copyright="";
    @Element
    private String date="";

    @ElementList(inline = true)
    private List<CurrencyRatesItem> currencyRatesItemList;

    public List<CurrencyRatesItem> getCurrencyRatesItemList() {
        return currencyRatesItemList;
    }

    public void setCurrencyRatesItemList(List<CurrencyRatesItem> currencyRatesItemList) {
        this.currencyRatesItemList = currencyRatesItemList;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public CurrencyRatesItem getCurrencyRatesItem(String currency) {
        for (CurrencyRatesItem currencyRatesItem : currencyRatesItemList) {
            if (currencyRatesItem.getTitle().equals(currency)) {
                return currencyRatesItem;
            }
        }
        return null;
    }

}
