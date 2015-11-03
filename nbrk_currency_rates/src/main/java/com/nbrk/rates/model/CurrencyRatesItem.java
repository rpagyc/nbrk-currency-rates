package com.nbrk.rates.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item")
public class CurrencyRatesItem implements Comparable<CurrencyRatesItem> {

    private Long _id;
    private String date="";

    @Element
    private String fullname="";
    @Element
    private String title="";
    @Element
    private String description="";
    @Element
    private String quant="";
    @Element(required = false, name = "index")
    private String ind="";
    @Element(required = false)
    private String change="0.00";// set default value to 0.00

    public Long get_id() {
        return _id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setQuant(String quant) {
        this.quant = quant;
    }

    public String getInd() {
        return ind;
    }

    public void setInd(String ind) {
        this.ind = ind;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getFullname() {
        return fullname;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getQuant() {
        return quant;
    }

    @Override
    public String toString(){
        return this.getClass().getSimpleName()+" = "
                +"id: "+_id
                +", date: "+date
                +", fullname: "+getFullname()
                +", title: "+getTitle()
                +", description: "+getDescription()
                +", quant: "+getQuant()
                +", ind: "+getInd()
                +", change: "+getChange();
    }

    @Override
    public int compareTo(CurrencyRatesItem another) {
        int last = ((Float)Float.parseFloat(this.getDescription())).compareTo(Float.parseFloat(another.getDescription()));
        return last;
    }
}
