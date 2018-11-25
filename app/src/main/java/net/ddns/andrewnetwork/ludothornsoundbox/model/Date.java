package net.ddns.andrewnetwork.ludothornsoundbox.model;

import android.support.annotation.NonNull;

import com.google.api.client.util.DateTime;

import java.io.Serializable;


public class Date implements Comparable, Serializable {

    private int day;
    private int month;
    private int year;

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Date(int day, int month, int year) {
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public String toExtendedString() {
        String extendedmonth=null;
        switch(month){
            case 1:
                extendedmonth = "Gennaio";
                break;

            case 2:
                extendedmonth = "Febbraio";
                break;

            case 3:
                extendedmonth = "Marzo";
                break;

            case 4:
                extendedmonth = "Aprile";
                break;

            case 5:
                extendedmonth = "Maggio";
                break;

            case 6:
                extendedmonth = "Giugno";
                break;

            case 7:
                extendedmonth = "Luglio";
                break;

            case 8:
                extendedmonth = "Agosto";
                break;

            case 9:
                extendedmonth = "Settembre";
                break;

            case 10:
                extendedmonth = "Ottobre";
                break;

            case 11:
                extendedmonth = "Novembre";
                break;

            case 12:
                extendedmonth = "Dicembre";
                break;
            default:
                break;
        }
        if(extendedmonth!=null)
        return day + " " + extendedmonth + " " +year;
        else return toString();
    }
    @Override
    public String toString() {
        return day+"/"+month+"/"+year;
    }

    public Date(DateTime dateTime) {
        String dateTimeRFC = dateTime.toStringRfc3339();
        String[] date = dateTimeRFC.split("T");
        String[] dateElement = date[0].split("-");
        this.year = Integer.parseInt(dateElement[0]);
        this.month = Integer.parseInt(dateElement[1]);
        this.day = Integer.parseInt(dateElement[2]);

    }

    @Override
    public int compareTo(@NonNull Object o) {
        if(o.getClass() == Date.class){
            Date date2 = (Date) o;
            if(this.year> date2.getYear()) return 1;
            else if(this.year<date2.getYear()) return -1;
            else {
                if(this.month> date2.getMonth()) return 1;
                else if(this.month< date2.getMonth()) return -1;
                else {
                    if(this.day>date2.getDay()) return 1;
                    else if(this.day<date2.getDay()) return -1;
                    else return 0;
                }
            }

        }
        else return -2;
    }
}
