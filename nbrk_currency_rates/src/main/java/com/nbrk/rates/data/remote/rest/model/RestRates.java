package com.nbrk.rates.data.remote.rest.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "rates", strict = false)
public class RestRates {
  @Element
  public String date;
  @ElementList(inline = true)
  public List<RestRatesItem> restRatesItemList;
}
