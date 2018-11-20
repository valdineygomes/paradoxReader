package com.khubla.pdxreader.db;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.khubla.pdxreader.api.PDXReaderException;
import com.khubla.pdxreader.api.PDXTableListener;

/**
 * @author tom
 */
public class DBTableRecord {

    /**
     * read one record
     *
     * @param pdxReaderListener
     * @param fields
     * @param inputStream
     * @throws com.khubla.pdxreader.api.PDXReaderException
     */
    public void read(PDXTableListener pdxReaderListener, List<DBTableField> fields, InputStream inputStream) throws PDXReaderException {
        try {
            final List<DBTableValue> values = new ArrayList<DBTableValue>();
            for (final DBTableField pdxTableField : fields) {
                final DBTableValue pdxTableValue = new DBTableValue();
                pdxTableValue.read(pdxTableField, inputStream);
                values.add(pdxTableValue);
            }
            pdxReaderListener.record(values);
        } catch (final Exception e) {
            throw new PDXReaderException("Exception in read", e);
        }
    }
}
