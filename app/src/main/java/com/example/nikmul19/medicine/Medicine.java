package com.example.nikmul19.medicine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Medicine {
    List<String> names;
    HashMap<String,Object> constituents,schedule;
    String form,manufacturer,name,packageForm;
    int price,units;
    String price1,med_id;
    Medicine(){names=new ArrayList<>();}
    public void addMedicine(String medicine){
        names.add(medicine);
    }
    public String getName(){
        return name;
    }
    public String getPrice(){
        return price1;
    }
    public String getManf(){return manufacturer;}
    public String getMid(){return med_id;}
    Medicine(String name,String price,String manufacturer,String med_id){
        this.name=name;
        this.price1=price;
        this.manufacturer=manufacturer;
        this.med_id=med_id;
    }
    /*Medicine( String form, String manufacturer, String medicine_id, String name, String packageForm, int price, String size, int units){
        this.form=form;
        this.medicine_id=medicine_id;
        this.manufacturer=manufacturer;
        this.name=name;
        this.packageForm=packageForm;
        this.price=price;
        this.units=units;

    }*/
    public void putData(){

            System.out.println(name);
    }
}
