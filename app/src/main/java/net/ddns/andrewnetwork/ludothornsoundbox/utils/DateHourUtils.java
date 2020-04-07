package net.ddns.andrewnetwork.ludothornsoundbox.utils;


import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public abstract class DateHourUtils {

    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final String TIME_FORMAT = "HH:mm";

    public static boolean isSameDay(Calendar calendar1, Calendar calendar2) {
        int dayOfMonth1 = calendar1.get(Calendar.DAY_OF_MONTH);
        int month1 = calendar1.get(Calendar.MONTH);
        int year1 = calendar1.get(Calendar.YEAR);

        int dayOfMonth2 = calendar2.get(Calendar.DAY_OF_MONTH);
        int month2 = calendar2.get(Calendar.MONTH);
        int year2 = calendar2.get(Calendar.YEAR);

        return dayOfMonth1 == dayOfMonth2 && month1 == month2 && year1 == year2;
    }

    public static List<String> getMonthsNumberList() {
        return new ArrayList<>(Arrays.asList("24", "36", "48", "60", "72", "84", "96", "108", "120"));
    }

    static List<String> getMonthsList() {
        return new ArrayList<>(Arrays.asList( /* "Selezionare...",*/ "GENNAIO", "FEBBRAIO", "MARZO", "APRILE", "MAGGIO", "GIUGNO", "LUGLIO", "AGOSTO", "SETTEMBRE",
                "OTTOBRE", "NOVEMBRE", "DICEMBRE"));
    }

    static List<String> getYears() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        return new ArrayList<>(Arrays.asList( /*"Selezionare...", */ String.valueOf(currentYear), String.valueOf(currentYear + 1)));
    }

    static List<String> getYears(int year) {
        return new ArrayList<>(Arrays.asList( /*"Selezionare...", */ String.valueOf(year), String.valueOf(year + 1)));
    }

    public static String convertToTimestamp(int time) {
        int hour = time / 60;
        int minutes = time % 60;
        int length = String.valueOf(minutes).length();
        if (length == 1) return hour + ":0" + minutes;
        else return hour + ":" + minutes;
    }

    public static Date getDatebyString(String string) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        try {
            return simpleDateFormat.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String convertToTimestamp(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        if (calendar != null)
            return simpleDateFormat.format(calendar.getTime());
        else return null;
    }

    public static String convertToTimestamp(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);

        if (date != null)
            return format.format(date);
        else return null;
    }

    public static String convertToTimestampWithTime(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        return format.format(date);
    }

    public static String convertToTimestamp(int day, int month, int year) {
        month++;
        String dayString = String.valueOf(day);
        String monthString = String.valueOf(month);
        int lengthDay = String.valueOf(day).length();
        int lengthMonth = String.valueOf(month).length();

        if (lengthDay == 1) dayString = "0" + day;
        if (lengthMonth == 1) monthString = "0" + month;
        return dayString + "/" + monthString + "/" + year;
    }

    public static String convertToTimestamp(int day, int month, int year, boolean showDay) {
        String timestamp = convertToTimestamp(day, month, year);
        if (showDay)
            return timestamp;
        else
            return timestamp.substring(timestamp.indexOf("/") + 1);
    }

    public static String convertToTimestamp(int hour, int minutes) {
        int length = String.valueOf(minutes).length();
        if (length == 1) return hour + ":0" + minutes;
        return hour + ":" + minutes;
    }

    public static String convertToHourAndMinutes(int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;
        return convertToTimestamp(hour, minute);
    }

    public static void setMinutes(Calendar calendar, int minutes) {
        int hour = minutes / 60;
        int minute = minutes % 60;
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    public static int getMinutes(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return convertToMinutes(hour, minute);
    }

    public static String getHourAndMinutes(Calendar calendar) {
        return DateHourUtils.convertToHourAndMinutes(getMinutes(calendar));
    }

    /*public static int convertToMinutes(Calendar calendar) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        return hour*60+minutes;
    }*/

    public static int convertToMinutes(int hourOfDay, int minutes) {
        return hourOfDay * 60 + minutes;
    }

    public static long convertToMinutes(Date date) {
        return (date.getTime() + 3600000) / 60000L;
    }

    public static long convertToMinutes(Calendar calendar) {
        return calendar.getTime().getTime() / 60000;
    }


    public static Calendar get15MinutesFromNow() {
        Calendar nowCalendarPlus10 = Calendar.getInstance();
        nowCalendarPlus10.add(Calendar.MINUTE, 15);
        return nowCalendarPlus10;
    }

    public static Calendar get1HourFromNow() {
        Calendar nowCalendarPlus10 = Calendar.getInstance();
        nowCalendarPlus10.add(Calendar.HOUR_OF_DAY, 1);
        return nowCalendarPlus10;
    }

    public static Calendar get15MinutesFromCalendar(Calendar nowCalendarPlus10) {
        nowCalendarPlus10.add(Calendar.MINUTE, 15);
        return nowCalendarPlus10;
    }

    public static Calendar get1HourFromCalendar(Calendar nowCalendarPlus10) {
        nowCalendarPlus10.add(Calendar.HOUR_OF_DAY, 1);
        return nowCalendarPlus10;
    }

    public static int convertToMinutes(String string) {
        if (string.contains(":")) {
            String[] strings = string.split(":");
            return Integer.parseInt(strings[0]) * 60 + Integer.parseInt(strings[1]);
        }
        return 0;
    }

    public static String findDayOfWeek(Calendar calendar) {
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        String dayString;
        switch (dayOfWeek) {
            case 1:
                dayString = "domenica";
                break;
            case 2:
                dayString = "lunedì";
                break;
            case 3:
                dayString = "martedì";
                break;
            case 4:
                dayString = "mercoledì";
                break;
            case 5:
                dayString = "giovedì";
                break;
            case 6:
                dayString = "venerdì";
                break;
            case 7:
                dayString = "sabato";
                break;
            default:
                dayString = "";
                break;
        }
        return dayString.toUpperCase();
    }

    public static String findMonth(Calendar calendar) {
        int month = calendar.get(Calendar.MONTH);
        String monthString;
        switch (month) {
            case 0:
                monthString = "gennaio";
                break;
            case 1:
                monthString = "febbraio";
                break;
            case 2:
                monthString = "marzo";
                break;
            case 3:
                monthString = "aprile";
                break;
            case 4:
                monthString = "maggio";
                break;
            case 5:
                monthString = "giugno";
                break;
            case 6:
                monthString = "luglio";
                break;
            case 7:
                monthString = "agosto";
                break;
            case 8:
                monthString = "settembre";
                break;
            case 9:
                monthString = "ottobre";
                break;
            case 10:
                monthString = "novembre";
                break;
            case 11:
                monthString = "dicembre";
                break;
            default:
                monthString = "";
                break;
        }
        return StringUtils.abbreviate(monthString).toUpperCase();
    }
    static List<String> createNotificationList() {
        List<String> notificationList = new ArrayList<>();
        notificationList.add("10 minuti prima");
        notificationList.add("15 minuti prima");
        notificationList.add("30 minuti prima");
        notificationList.add("Un'ora prima");
        notificationList.add("3 ore prima");
        notificationList.add("1 giorno prima");
        notificationList.add("3 giorni prima");
        notificationList.add("Una settimana prima");
        return notificationList;
    }

    public static String parseToCreditoNetString(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthString = String.valueOf(month);
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }
        return calendar.get(Calendar.YEAR) + monthString;
    }

    public static String convertToTime(int hourOfDay, int minutes) {
        String minuteString = String.valueOf(minutes);

        if (minuteString.length() == 1) {
            minuteString = "0" + minutes;
        }

        return hourOfDay + ":" + minuteString;
    }

    public static String convertToTime(Calendar calendar) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.ITALIAN);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Calendar convertToTime(String string) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(TIME_FORMAT, Locale.ITALIAN);
        Calendar calendar= Calendar.getInstance();
        calendar.setTime(simpleDateFormat.parse(string));

        return calendar;
    }

    public static Date convertToDate(String dataPreventivo) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.ITALIAN);
        try {
            return simpleDateFormat.parse(dataPreventivo);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
