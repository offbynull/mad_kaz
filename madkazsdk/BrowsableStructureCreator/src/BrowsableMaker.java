/*
    Mad Kaz Browsable Structure Creator  Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz Browsable Structure Creator.

    Mad Kaz Browsable Structure Creator is free software; you can redistribute 
    it and/or modify it under the terms of the GNU General Public License as 
    published by the Free Software Foundation; either version 3 of the License, 
    or (at your option) any later version.

    Mad Kaz Browsable Structure Creator is distributed in the hope that it will 
    be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General 
    Public License for more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import jargs.gnu.CmdLineParser;
import jargs.gnu.CmdLineParser.Option;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

public class BrowsableMaker {
    public static final String SECTION_ELEMENT_NAME                       = "section";
    public static final String MESSAGE_ELEMENT_NAME                       = "message";
    public static final String LINKS_ELEMENT_NAME                         = "links";
    
    public static final String LINE_ELEMENT_NAME                          = "line";
    public static final String LINK_ELEMENT_NAME                          = "link";
    public static final String LEVEL_ELEMENT_NAME                         = "level";
    
    
    public static final String NAME_ATTRIBUTE_NAME                        = "name";
    public static final String TEXT_ATTRIBUTE_NAME                        = "text";
    
    public static final String OVERRIDE_TEXT_ATTRIBUTE_NAME               = "overridetext";
    
    public static final String COMMENTS_ATTRIBUTE_NAME                    = "comments";
    public static final String AUTHOR_ATTRIBUTE_NAME                      = "author";
    public static final String LONG_NAME_ATTRIBUTE_NAME                   = "longname";
    public static final String SHORT_NAME_ATTRIBUTE_NAME                  = "shortname";
    public static final String CREATE_TIME_ATTRIBUTE_NAME                 = "createtime";
    public static final String LOCATION_ATTRIBUTE_NAME                    = "location";
    
    
    
    public static final String LEVEL_FILENAME_SUFFIX                      = ".ldat";
    public static final String LINK_FILENAME_SUFFIX                       = ".mdat";
    
    public static final String LEVEL_NAME_PREFIX                          = "(L) ";
    public static final String LINK_NAME_PREFIX                           = "(M) ";
    
    
    
    
    public static final int LEVEL_MAGIC_NUM                 = 0xDECADE69;
    public static final int SECTION_MAGIC_NUM               = 0xBABEF00D;
    
    
    
    
    
    public BrowsableMaker() {
    }
    
    private static void printUsage() {
        System.out.println("usage: java -jar BrowsableMaker [--input xmlfilename] [--output filename] [--baseurl url]");
    }
    
    private static class RealOptionsParser extends CmdLineParser {
        public static final Option INPUT = new CmdLineParser.Option.StringOption("input");
        public static final Option OUTPUT = new CmdLineParser.Option.StringOption("output");
        public static final Option BASEURL = new CmdLineParser.Option.StringOption("baseurl");
        
        public RealOptionsParser() {
            super();
            addOption(INPUT);
            addOption(OUTPUT);
            addOption(BASEURL);
        }
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
        String baseURL = (String)myOptions.getOptionValue(RealOptionsParser.BASEURL);
        
        if (inputFileName == null || outputFileName == null) {
            printUsage();
            System.exit(2);
        }
        
        
        File schemaFile = new File("browse_struct.xsd");
        
        
        List sectionList = new ArrayList();
        
        
        List includedFileList = new ArrayList();
        List recursiveIncludeFiles = new ArrayList();
        
        
        Map levelDataOutput = new HashMap();
        Map sectionDataOutput = new HashMap();
        
        
        
        System.out.println("----------------------");
        System.out.println("Converting XML data...");
        System.out.println("----------------------");
        System.out.println("");
        convertXMLData(sectionList, schemaFile, new File(inputFileName), includedFileList, recursiveIncludeFiles);
        
        System.out.println("----------------------");
        System.out.println("Checking section links...");
        System.out.println("----------------------");
        System.out.println("");
        checkSectionLinks(sectionList);
        
        System.out.println("----------------------");
        System.out.println("Compiling levels...");
        System.out.println("----------------------");
        System.out.println("");
        compileLevels(levelDataOutput, sectionList);
        
        System.out.println("----------------------");
        System.out.println("Compiling sections...");
        System.out.println("----------------------");
        System.out.println("");
        compileSections(sectionDataOutput, sectionList, baseURL);
        
        System.out.println("----------------------");
        System.out.println("Archiving data...");
        System.out.println("----------------------");
        System.out.println("");
        archiveDataMaps(sectionDataOutput, levelDataOutput, outputFileName);
    }
    
    private static void archiveDataMaps(Map sectionDataOutput, Map levelDataOutput, String outputFileName) throws Throwable {
        // Create a buffer for reading the files
        byte[] buf = new byte[1024];
        
        // Create the ZIP file
        FileOutputStream fos = new FileOutputStream(outputFileName);
        ZipOutputStream out = new ZipOutputStream(fos);
        
        
        Set sectionKeys = sectionDataOutput.keySet();
        
        Iterator sectionKeyIterator = sectionKeys.iterator();
        
        while (sectionKeyIterator.hasNext()) {
            String filename = (String)sectionKeyIterator.next();
            
            ByteArrayInputStream bais = new ByteArrayInputStream((byte [])sectionDataOutput.get(filename));
            
            out.putNextEntry(new ZipEntry(filename + LINK_FILENAME_SUFFIX));
            
            int len;
            while ((len = bais.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            
            out.closeEntry();
            bais.close();
        }
        
        
        Set levelKeys = levelDataOutput.keySet();
        
        Iterator levelKeyIterator = levelKeys.iterator();
        
        while (levelKeyIterator.hasNext()) {
            String filename = (String)levelKeyIterator.next();
            
            ByteArrayInputStream bais = new ByteArrayInputStream((byte [])levelDataOutput.get(filename));
            
            out.putNextEntry(new ZipEntry(filename + LEVEL_FILENAME_SUFFIX));
            
            int len;
            while ((len = bais.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            
            out.closeEntry();
            bais.close();
        }
        
        out.close();
        fos.close();
    }
    
    private static void compileSections(Map sectionDataOutput, List sectionObjectList, String baseURL) throws Throwable {
        Iterator sectionObjectIterator = sectionObjectList.iterator();
        
        while (sectionObjectIterator.hasNext()) {
            Section section = (Section)sectionObjectIterator.next();
            
            byte []sectionData = getSectionFile(section, sectionObjectList, baseURL);
            
            sectionDataOutput.put(section.getName(), sectionData);
        }
    }
    
    private static byte []getSectionFile(Section section, List sectionObjectList, String baseURL) throws Throwable {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        
        dos.writeInt(SECTION_MAGIC_NUM);
        
        
        
        
        
        List message = section.getMessage();
        
        dos.writeInt(message.size());
        
        Iterator messageIterator = message.iterator();
        
        while (messageIterator.hasNext()) {
            String str = (String)messageIterator.next();
            
            dos.writeUTF(str);
        }
        
        
        
        
        List links = section.getLinks();
        List levels = section.getLevels();
        
        dos.writeInt(links.size() + levels.size());
        
        
        
        Iterator linksIterator = links.iterator();
        
        while (linksIterator.hasNext()) {
            Link link = (Link)linksIterator.next();
            
            String goToName = link.getGoToName();
            String text = link.getOverrideText();
            
            Section foundSection = getSection(sectionObjectList, goToName);
            
            if (text == null)
                text = foundSection.getText();
            
            dos.writeUTF(LINK_NAME_PREFIX + text);
            dos.writeUTF(baseURL + "/" + goToName + LINK_FILENAME_SUFFIX);
        }
        
        
        
        
        Iterator levelsIterator = levels.iterator();
        
        while (levelsIterator.hasNext()) {
            Level level = (Level)levelsIterator.next();
            
            String name = level.getShortName();
            
            dos.writeUTF(LEVEL_NAME_PREFIX + name);
            dos.writeUTF(baseURL + "/" + name + LEVEL_FILENAME_SUFFIX);
        }
        
        return baos.toByteArray();
    }
    
    private static void compileLevels(Map levelDataOutput, List sectionObjectList) throws Throwable {
        Iterator sectionObjectIterator = sectionObjectList.iterator();
        
        while (sectionObjectIterator.hasNext()) {
            Section section = (Section)sectionObjectIterator.next();
            
            List levels = section.getLevels();
            Iterator levelsIterator = levels.iterator();
            
            while (levelsIterator.hasNext()) {
                Level level = (Level)levelsIterator.next();
                
                File location = new File(level.getFileLocation());
                
                if (levelDataOutput.containsKey(level.getShortName())) {
                    System.out.println("");
                    System.out.println("Level already processed: " + location.getAbsolutePath());
                    System.out.println("");
                } else {
                    System.out.println("");
                    System.out.println("Processing " + location.getAbsolutePath() + " ...");
                    System.out.println("");
                    
                    byte []levelData = dumpDownloadableMapFile(location, level.getDate(), level.getShortName(), level.getLongName(), level.getAuthor(), level.getComments());
                    
                    levelDataOutput.put(level.getShortName(), levelData);
                    
                    System.out.println("");
                }
            }
        }
    }
    
    private static void checkSectionLinks(List sectionObjectList) throws Throwable {
        Iterator sectionObjectIterator = sectionObjectList.iterator();
        
        while (sectionObjectIterator.hasNext()) {
            Section section = (Section)sectionObjectIterator.next();
            
            List links = section.getLinks();
            Iterator linksIterator = links.iterator();
            
            while (linksIterator.hasNext()) {
                Link link = (Link)linksIterator.next();
                
                if (getSection(sectionObjectList, link.getGoToName()) == null)
                    throw new RuntimeException("unable to find section " + link.getGoToName() + " -- being referenced from section " + section.getName());
            }
        }
    }
    
    private static Section getSection(List sectionObjectList, String name) throws Throwable {
        Iterator sectionObjectIterator = sectionObjectList.iterator();
        
        while (sectionObjectIterator.hasNext()) {
            Section section = (Section)sectionObjectIterator.next();
            
            if (name.equalsIgnoreCase(section.getName()))
                return section;
        }
        
        return null;
    }
    
    private static void convertXMLData(List sectionObjectList, File schemaFile, File inputFile, List includedFiles, List recursiveIncludeFiles) throws Throwable {
        if (recursiveIncludeFiles.contains(inputFile))
            throw new RuntimeException("recursive include detected on -- " + inputFile.getPath());
        else
            recursiveIncludeFiles.add(inputFile);
        
        if (includedFiles.contains(inputFile))
            System.err.println("dual include detected on -- " + inputFile.getPath());
        else
            includedFiles.add(inputFile);
        
        
        
        
        Document document;
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = sf.newSchema(schemaFile);
        factory.setSchema(schema);
        
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setErrorHandler(new BrowsableErrorHandler());
        
        document = builder.parse(inputFile);
        
        Element browsableElement = document.getDocumentElement();
        
        
        
        
        NodeList sectionList = browsableElement.getElementsByTagName(SECTION_ELEMENT_NAME);
        
        for (int i = 0; i < sectionList.getLength(); i++) {
            Element sectionElement = (Element)sectionList.item(i);
            
            String sectionName = sectionElement.getAttribute(NAME_ATTRIBUTE_NAME);
            String sectionText = sectionElement.getAttribute(TEXT_ATTRIBUTE_NAME);
            
            Section section = new Section(sectionName, sectionText);
            
            
            
            
            NodeList messageList = ((Element)sectionElement.getElementsByTagName(MESSAGE_ELEMENT_NAME).item(0)).getElementsByTagName(LINE_ELEMENT_NAME);
            
            for (int j = 0; j < messageList.getLength(); j++) {
                Element lineElement = (Element)messageList.item(j);
                
                section.appendMessageLine(lineElement.getTextContent());
            }
            
            
            
            
            NodeList linksList = ((Element)sectionElement.getElementsByTagName(LINKS_ELEMENT_NAME).item(0)).getElementsByTagName(LINK_ELEMENT_NAME);
            
            for (int j = 0; j < linksList.getLength(); j++) {
                Element linkElement = (Element)linksList.item(j);
                
                String linkName = linkElement.getAttribute(NAME_ATTRIBUTE_NAME);
                String overrideTextName = null;
                
                if (linkElement.hasAttribute(OVERRIDE_TEXT_ATTRIBUTE_NAME))
                    overrideTextName = linkElement.getAttribute(OVERRIDE_TEXT_ATTRIBUTE_NAME);
                
                section.appendLink(new Link(linkName, overrideTextName));
            }
            
            
            
            
            NodeList levelsList = ((Element)sectionElement.getElementsByTagName(LINKS_ELEMENT_NAME).item(0)).getElementsByTagName(LEVEL_ELEMENT_NAME);
            
            for (int j = 0; j < levelsList.getLength(); j++) {
                Element levelElement = (Element)levelsList.item(j);
                
                String location = levelElement.getAttribute(LOCATION_ATTRIBUTE_NAME);
                Date createTime = null;
                
                if (levelElement.hasAttribute(CREATE_TIME_ATTRIBUTE_NAME))
                    createTime = new SimpleDateFormat("yyyy-MM-dd").parse(levelElement.getAttribute(CREATE_TIME_ATTRIBUTE_NAME));
                else
                    createTime = new Date();
                
                String shortName = levelElement.getAttribute(SHORT_NAME_ATTRIBUTE_NAME);
                String longName = levelElement.getAttribute(LONG_NAME_ATTRIBUTE_NAME);
                String author = levelElement.getAttribute(AUTHOR_ATTRIBUTE_NAME);
                String comments = levelElement.getAttribute(COMMENTS_ATTRIBUTE_NAME);
                
                section.appendLevel(new Level(location, createTime, shortName, longName, author, comments));
            }
            
            
            
            
            if (sectionObjectList.contains(section))
                throw new RuntimeException("duplicate section found -- " + section.getName());
            
            sectionObjectList.add(section);
        }
        
        
        
        
        
        recursiveIncludeFiles.remove(inputFile);
    }
    
    public static byte []dumpDownloadableMapFile(File inputFileName, Date creationTime, String shortName, String longName, String authorName, String comments) throws Throwable {
        FileInputStream fis = new FileInputStream(inputFileName);
        byte []data = getISToByteArray(fis);
        fis.close();
        
        System.out.println("Creation Time: " + creationTime);
        System.out.println("Short Name: " + shortName);
        System.out.println("Long Name: " + longName);
        System.out.println("Author Name: " + authorName);
        System.out.println("Comments: " + comments);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeInt(BrowsableMaker.LEVEL_MAGIC_NUM);
        dos.writeLong(creationTime.getTime());
        dos.writeLong(0L);
        dos.writeUTF(shortName);
        dos.writeUTF(longName);
        dos.writeUTF(authorName);
        dos.writeUTF(comments);
        
        dos.writeInt(data.length);
        dos.write(data);
        
        dos.close();
        
        return baos.toByteArray();
    }
    
    public static byte []getISToByteArray(InputStream is) throws Throwable {
        byte b[] = new byte[1024];
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        int len = -1;
        
        while ((len = is.read(b)) != -1) {
            baos.write(b, 0, len);
        }
        
        return baos.toByteArray();
    }
    
    
    public static class Section {
        private String m_name;
        private String m_text;
        private List m_message;
        private List m_levels;
        private List m_links;
        
        public Section(String name, String text) {
            m_name = name;
            m_text = text;
            
            m_message = new ArrayList();
            m_levels = new ArrayList();
            m_links = new ArrayList();
        }
        
        public String getName() {
            return m_name;
        }
        
        public void setName(String name) {
            this.m_name = name;
        }
        
        public String getText() {
            return m_text;
        }
        
        public void setText(String text) {
            this.m_text = text;
        }
        
        public void appendMessageLine(String text) {
            m_message.add(text);
        }
        
        public void appendLevel(Level level) {
            m_levels.add(level);
        }
        
        public void appendLink(Link link) {
            m_links.add(link);
        }
        
        public List getMessage() {
            return new ArrayList(m_message);
        }
        
        public List getLevels() {
            return new ArrayList(m_levels);
        }
        
        public List getLinks() {
            return new ArrayList(m_links);
        }
        
        public boolean equals(Object o) {
            if (o == null)
                return false;
            
            Section section = (Section)o;
            
            if (section.m_name.equalsIgnoreCase(m_name))
                return true;
            
            return false;
        }
    }
    
    public static class Link {
        private String m_goToName;
        private String m_overrideText;
        
        public Link(String goToName, String overrideText) {
            m_goToName = goToName;
            m_overrideText = overrideText;
        }
        
        public String getGoToName() {
            return m_goToName;
        }
        
        public void setGoToName(String goToName) {
            this.m_goToName = goToName;
        }
        
        public String getOverrideText() {
            return m_overrideText;
        }
        
        public void setOverrideText(String overrideText) {
            this.m_overrideText = overrideText;
        }
    }
    
    public static class Level {
        private String m_fileLocation;
        private Date m_date;
        private String m_shortName;
        private String m_longName;
        private String m_author;
        private String m_comments;
        
        public Level(String fileLocation, Date date, String shortName, String longName, String author, String comments) {
            m_fileLocation = fileLocation;
            m_date = date;
            m_shortName = shortName;
            m_longName = longName;
            m_author = author;
            m_comments = comments;
        }
        
        public String getFileLocation() {
            return m_fileLocation;
        }
        
        public void setFileLocation(String fileLocation) {
            this.m_fileLocation = fileLocation;
        }
        
        public Date getDate() {
            return m_date;
        }
        
        public void setDate(Date m_date) {
            this.m_date = m_date;
        }
        
        public String getShortName() {
            return m_shortName;
        }
        
        public void setShortName(String shortName) {
            this.m_shortName = shortName;
        }
        
        public String getLongName() {
            return m_longName;
        }
        
        public void setLongName(String longName) {
            this.m_longName = longName;
        }
        
        public String getAuthor() {
            return m_author;
        }
        
        public void setAuthor(String author) {
            this.m_author = author;
        }
        
        public String getComments() {
            return m_comments;
        }
        
        public void setComments(String comments) {
            this.m_comments = comments;
        }
    }
    
    public static class BrowsableErrorHandler implements ErrorHandler {
        public void warning(SAXParseException exception) throws SAXException {
            System.err.println(exception);
            System.exit(3);
        }
        
        public void error(SAXParseException exception) throws SAXException {
            System.err.println(exception);
            System.exit(3);
        }
        
        public void fatalError(SAXParseException exception) throws SAXException {
            System.err.println(exception);
            System.exit(3);
        }
    }
    
}
