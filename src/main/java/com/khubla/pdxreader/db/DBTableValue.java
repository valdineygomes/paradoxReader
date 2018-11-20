package com.khubla.pdxreader.db;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.db.DBTableField.FieldType;
import com.khubla.pdxreader.util.ParadoxDate;
import com.khubla.pdxreader.util.ParadoxDateTime;
import com.khubla.pdxreader.util.ParadoxTime;
import com.khubla.pdxreader.util.StringUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @author tom
 */
public class DBTableValue {

    /**
     * the value
     */
    private String value;
    /**
     * the type
     */
    private DBTableField.FieldType type;

    public DBTableField.FieldType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    /**
     * Read a table field
     *
     * @param pdxTableField
     * @param inputStream
     * @throws com.khubla.pdxreader.api.PDXReaderException
     */
    public void read(DBTableField pdxTableField, InputStream inputStream) throws PDXReaderException {
        try {
            /*
          * get the data
             */
            final byte[] data = new byte[pdxTableField.getLength()];
            final int bytesRead = inputStream.read(data);
            if (bytesRead > 0) {
                /*
             * convert to type
                 */
                final FieldType fieldType = pdxTableField.getFieldType();
                switch (fieldType) {
                    case A:
                        value = StringUtil.readString(data);
                        break;
                    case D:
                        value = ParadoxDate.getDateFromParadoxDate(data);
                        break;
                    case T:
                        value = ParadoxTime.getTimeFromParadoxTime(data);
                        break;
                    case TS:
                        value = ParadoxDateTime.getTimeFromParadoxDateTime(data);
                        break;
                    case S:
                    case Auto:
                        final long s = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN).getShort();
                        value = Long.toString(s);
                        break;
                    case I:
                        data[0] = (data[0] == Byte.MAX_VALUE ? -1 : (data[0] == Byte.MIN_VALUE ? 0 : data[0]));
                        final long i = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getInt();
                        value = Long.toString(i);
                        break;
                    case N:
                    case C:
                        data[0] = (byte) (data[0] & 0x7f);
                        final double n = ByteBuffer.wrap(data).order(ByteOrder.BIG_ENDIAN).getDouble();
                        value = String.valueOf(new BigDecimal(n).setScale(2, RoundingMode.HALF_UP).doubleValue());
                        break;
                    case M:
                    case L:
                    case B:
                    case O:
                    case E:
                    case G:
                    case BCD:
                    case Bytes:
                        value = StringUtil.byteArrayToString(data);
                        break;
                    default:
                        throw new Exception("Unknown field type '" + fieldType.name() + "'");
                }
            }
        } catch (final Exception e) {
            throw new PDXReaderException("Exception in read", e);
        }
    }

    public void setType(DBTableField.FieldType type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
