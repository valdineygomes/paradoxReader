package com.khubla.pdxreader.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ParadoxDate {

    static public String getDateFromParadoxDate(byte[] data) {
        data[0] = (byte) (data[0] & 0x7f);
        final int days = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getInt();
        final Calendar calendar = Calendar.getInstance();

        /*
       * Jan 1, 1 A.D.
         */
        calendar.set(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        /*
        * add days
         */
        calendar.add(Calendar.DATE, (days + 1));
        /*
        * return date
         */
        return new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
    }
}
