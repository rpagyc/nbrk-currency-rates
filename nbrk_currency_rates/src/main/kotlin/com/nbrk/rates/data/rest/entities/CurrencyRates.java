package com.nbrk.rates.data.rest.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "dataSource")
public class CurrencyRates {
  @Element
  public String date = "";
  @ElementList(inline = true)
  public List<CurrencyRatesItem> currencyRatesItemList;
  @Element
  String generator = "";
  @Element
  String title = "";
  @Element
  String link = "";
  @Element
  String description = "";
  @Element
  String copyright = "";
}
