package com.khubla.pdxreader.listener;

import java.util.List;
import com.khubla.pdxreader.api.PDXTableListener;
import com.khubla.pdxreader.db.DBTableField;
import com.khubla.pdxreader.db.DBTableHeader;
import com.khubla.pdxreader.db.DBTableValue;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * @author tom
 */
public class PDXTableReaderCSVListenerImpl implements PDXTableListener {

    private static final Character DELIMITER = ';';
    private static final Character QUOTE = '"';
    private static final Character ESCAPE = '\\';
    private static final String LINE_SAPARATOR = "\r\n";

    private final File outputFile;
    private final Character delimiter;
    private final Character quote;
    private final Character escape;
    private final String lineSeparator;
    private String processDate;
    
    private CsvWriter writer;

    public PDXTableReaderCSVListenerImpl(File outputFile) {
        this.outputFile = outputFile;
        this.delimiter = DELIMITER;
        this.quote = QUOTE;
        this.escape = ESCAPE;
        this.lineSeparator = LINE_SAPARATOR;
    }

    public PDXTableReaderCSVListenerImpl(File outputFile, Character delimiter, Character quote, Character escape, String lineSeparator) {
        this.outputFile = outputFile;
        this.delimiter = delimiter;
        this.quote = quote;
        this.escape = escape;
        this.lineSeparator = lineSeparator;
    }

    @Override
    public void start() {
        processDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        
        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setNullValue("");
        settings.setMaxCharsPerColumn(-1);
        settings.getFormat().setDelimiter(delimiter);
        settings.getFormat().setQuote(quote);
        settings.getFormat().setQuoteEscape(escape);
        settings.getFormat().setLineSeparator(lineSeparator);

        if (outputFile.exists()) {
            outputFile.delete();
        }

        writer = new CsvWriter(outputFile, settings);
    }

    @Override
    public void header(DBTableHeader tableHeader) {
        List<String> header = tableHeader
                .getFields()
                .stream()
                .map(DBTableField::getName)
                .map(column -> column.toLowerCase())
                .collect(Collectors.toList());

        //Include the load date column.
        header.add("etl_load_date");

        //Write the header.
        writer.writeHeaders(header);
    }

    @Override
    public void record(List<DBTableValue> values) {
        List<String> record = values
                .stream()
                .map(DBTableValue::getValue)
                .map(value -> value.replaceAll("\"|\n|\r|\\\\|;|\\p{C}|\\p{S}", ""))
                .collect(Collectors.toList());
        
        record.add(processDate);
        
        //Write the records.
        writer.writeRow(record);
    }

    @Override
    public void finish() {
        writer.close();
    }
}
