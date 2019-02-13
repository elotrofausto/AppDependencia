package com.example.vesprada.appdependencia.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String trasformDate(Date date) {
        String dateString = "";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        dateString = format.format(date);
        return dateString;
    }

    public static String trasformDateToBD(Date date) {
        String dateString = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        dateString = format.format(date);
        return dateString;
    }
}
