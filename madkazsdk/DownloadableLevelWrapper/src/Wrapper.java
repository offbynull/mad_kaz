/*
    Mad Kaz Downloadable Level Wrapper  Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz Downloadable Level Wrapper.

    Mad Kaz Downloadable Level Wrapper is free software; you can redistribute 
    it and/or modify it under the terms of the GNU General Public License as 
    published by the Free Software Foundation; either version 3 of the License, 
    or (at your option) any later version.

    Mad Kaz Downloadable Level Wrapper is distributed in the hope that it will 
    be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
    Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import jargs.gnu.CmdLineParser;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Wrapper {
    public static final int LEVEL_MAGIC_NUM                 = 0xDECADE69;
    
    public Wrapper() {
    }
    
    public static class ShortDateOption extends CmdLineParser.Option {
        public ShortDateOption(char shortForm, String longForm) {
            super(shortForm, longForm, true);
        }
        
        protected Object parseValue(String arg, Locale locale) throws CmdLineParser.IllegalOptionValueException {
            try {
                DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
                return dateFormat.parse(arg);
            } catch (ParseException e) {
                throw new CmdLineParser.IllegalOptionValueException(this, arg);
            }
        }
    }
    
    private static class RealOptionsParser extends CmdLineParser {
        public static final Option INPUT = new CmdLineParser.Option.StringOption("input");
        public static final Option OUTPUT = new CmdLineParser.Option.StringOption("output");
        public static final Option TIME = new Wrapper.ShortDateOption('t',"timeofcreation");
        public static final Option SHORTNAME = new CmdLineParser.Option.StringOption('s',"shortname");
        public static final Option LONGNAME = new CmdLineParser.Option.StringOption('l',"longname");
        public static final Option AUTHORNAME = new CmdLineParser.Option.StringOption('a',"authorname");
        public static final Option COMMENTS = new CmdLineParser.Option.StringOption('c',"comments");

        public RealOptionsParser() {
            super();
            addOption(INPUT);
            addOption(OUTPUT);
            addOption(TIME);
            addOption(SHORTNAME);
            addOption(LONGNAME);
            addOption(AUTHORNAME);
            addOption(COMMENTS);
        }
    }

    private static void printUsage() {
        System.out.println("usage: java -jar Wrapper [--input filename] [--output filename] [{-t,--timeofcreation} short_date] [{-s,--shortname} short_name] [{-l,--longname} long_name] [{-a,--authorname} author_name] [{-c,--comments} comments]");
    }

    public static void main(String[] args) throws Throwable {
        RealOptionsParser myOptions = new RealOptionsParser();

        try {
            myOptions.parse(args);
        } catch (CmdLineParser.UnknownOptionException e) {
            System.out.println(e.getMessage());
            printUsage();
            System.exit(1);
        } catch (CmdLineParser.IllegalOptionValueException e) {
            System.out.println(e.getMessage());
            printUsage();
            System.exit(1);
        }
        
        String inputFileName = (String)myOptions.getOptionValue(RealOptionsParser.INPUT);
        String outputFileName = (String)myOptions.getOptionValue(RealOptionsParser.OUTPUT);
        Date creationTime = (Date)myOptions.getOptionValue(RealOptionsParser.TIME);
        String shortName = (String)myOptions.getOptionValue(RealOptionsParser.SHORTNAME);
        String longName = (String)myOptions.getOptionValue(RealOptionsParser.LONGNAME);
        String authorName = (String)myOptions.getOptionValue(RealOptionsParser.AUTHORNAME);
        String comments = (String)myOptions.getOptionValue(RealOptionsParser.COMMENTS);
        
        if (creationTime == null)
            creationTime = new Date();
        
        if (inputFileName == null || outputFileName == null || shortName == null || longName == null || authorName == null || comments == null) {
            printUsage();
            System.exit(2);
        }
        
        dumpDownloadableMapFile(inputFileName, outputFileName, creationTime, shortName, longName, authorName, comments);
    }
    
    public static void dumpDownloadableMapFile(String inputFileName, String outputFileName, Date creationTime, String shortName, String longName, String authorName, String comments) throws Throwable {
        FileInputStream fis = new FileInputStream(inputFileName);
        byte []data = getISToByteArray(fis);
        fis.close();
        
        System.out.println("Level Information");
        System.out.println("-----------------");
        System.out.println("Creation Time: " + creationTime);
        System.out.println("Short Name: " + shortName);
        System.out.println("Long Name: " + longName);
        System.out.println("Author Name: " + authorName);
        System.out.println("Comments: " + comments);
        
        
        DataOutputStream dos = new DataOutputStream(new FileOutputStream(outputFileName));
        dos.writeInt(Wrapper.LEVEL_MAGIC_NUM);
        dos.writeLong(creationTime.getTime());
        dos.writeLong(0L);
        dos.writeUTF(shortName);
        dos.writeUTF(longName);
        dos.writeUTF(authorName);
        dos.writeUTF(comments);
        
        dos.writeInt(data.length);
        dos.write(data);
        
        dos.close();
        
        System.out.println("Done!");
    }
    
    public static byte []getISToByteArray(InputStream is) {
        byte b[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int len = -1;
        
        try {
            while ((len = is.read(b)) != -1) {
                baos.write(b, 0, len);
            }
        } catch (Throwable t) { }
        
        return baos.toByteArray();
    }
}
