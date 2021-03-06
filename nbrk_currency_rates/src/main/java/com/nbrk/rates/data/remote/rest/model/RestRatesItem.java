package com.nbrk.rates.data.remote.rest.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "item", strict = false)
public class RestRatesItem {
  @Element
  public String fullname = "";
  @Element
  public String title = "";
  @Element
  public String description = "";
  @Element
  public String quant = "";
  @Element(required = false, name = "index")
  public String index = "";
  @Element(required = false)
  public String change = "0";
}
