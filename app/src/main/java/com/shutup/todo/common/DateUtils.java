package com.shutup.todo.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by shutup on 2017/1/5.
 */

public class DateUtils {
    public static String formatDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }
}
