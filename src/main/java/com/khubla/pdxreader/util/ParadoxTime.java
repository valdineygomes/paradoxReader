package com.khubla.pdxreader.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ParadoxTime {

    static public String getTimeFromParadoxTime(byte[] data) {
        data[0] = (byte) (data[0] & 0x7f);
        int milisseconds = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getInt();
        final Calendar calendar = Calendar.getInstance();

        /*
       * Jan 1, 1 A.D. 00:00:00 
         */
        calendar.set(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        /*
       * add milisseconds
         */
        calendar.add(Calendar.MILLISECOND, milisseconds);

        return new SimpleDateFormat("HH:mm:ss").format(calendar.getTime());
    }
}
