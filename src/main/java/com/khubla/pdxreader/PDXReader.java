package com.khubla.pdxreader;

import java.io.File;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import com.khubla.pdxreader.api.PDXTableListener;
import com.khubla.pdxreader.db.DBTableFile;
import com.khubla.pdxreader.listener.PDXTableReaderCSVListenerImpl;

/**
 * @author tom
 */
public class PDXReader {

    /**
     * file option
     */
    private static final String FILE_OPTION = "file";
    private static final String OUTPUT_OPTION = "output";
    private static final String DELIMITER = "delimiter";
    private static final String QUOTE = "quote";
    private static final String ESCAPE = "escape";
    private static final String LINE_SAPARATOR = "separator";

    public static void main(String[] args) {

        Character outputDelimiter = ';';
        Character outputQuote = '"';
        Character outputEscape = '\\';
        String outputLineSeparator = "\r\n";

        try {
            /*
          * options
             */
            final Options options = new Options();
            final Option inputOption = Option.builder().argName(FILE_OPTION).longOpt(FILE_OPTION).type(String.class).hasArg().required(true).desc("file to read").build();
            final Option outputOption = Option.builder().argName(OUTPUT_OPTION).longOpt(OUTPUT_OPTION).type(String.class).hasArg().required(true).desc("file to write").build();
            final Option delimiter = Option.builder().argName(DELIMITER).longOpt(DELIMITER).type(String.class).hasArg().required(false).desc("column delimiter").build();
            final Option quote = Option.builder().argName(QUOTE).longOpt(QUOTE).type(String.class).hasArg().required(false).desc("quote").build();
            final Option escape = Option.builder().argName(ESCAPE).longOpt(ESCAPE).type(String.class).hasArg().required(false).desc("escape").build();
            final Option lineSeparator = Option.builder().argName(LINE_SAPARATOR).longOpt(LINE_SAPARATOR).type(String.class).hasArg().required(false).desc("line separator").build();

            options.addOption(inputOption);
            options.addOption(outputOption);
            options.addOption(delimiter);
            options.addOption(quote);
            options.addOption(escape);
            options.addOption(lineSeparator);

            /*
          * parse
             */
            final CommandLineParser parser = new DefaultParser();
            CommandLine cmd = null;

            try {
                cmd = parser.parse(options, args);
            } catch (final Exception e) {
                e.printStackTrace();
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("posix", options);
                System.exit(0);
            }

            /*
          * get file
             */
            final String inputFileName = cmd.getOptionValue(FILE_OPTION);
            final String outputFileName = cmd.getOptionValue(OUTPUT_OPTION);

            if (cmd.hasOption(DELIMITER)) {
                outputDelimiter = cmd.getOptionValue(DELIMITER).charAt(0);
            }

            if (cmd.hasOption(QUOTE)) {
                outputQuote = cmd.getOptionValue(QUOTE).charAt(0);
            }

            if (cmd.hasOption(ESCAPE)) {
                outputEscape = cmd.getOptionValue(ESCAPE).charAt(0);
            }

            if (cmd.hasOption(LINE_SAPARATOR)) {
                outputLineSeparator = cmd.getOptionValue(LINE_SAPARATOR);
            }

            if (null != inputFileName) {
                final File inputFile = new File(inputFileName);
                final File outputFile = new File(outputFileName);

                if (inputFile.exists()) {
                    final DBTableFile pdxFile = new DBTableFile();
                    final PDXTableListener pdxReaderListener
                            = new PDXTableReaderCSVListenerImpl(
                                    outputFile,
                                    outputDelimiter,
                                    outputQuote,
                                    outputEscape,
                                    outputLineSeparator);

                    pdxFile.read(inputFile, pdxReaderListener);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }
}
