/*
    Mad Kaz Assembler  Copyright 2007 CodeYield Development, Inc.  inquiries@codeyielddevelopment.com

    This file is part of Mad Kaz Assembler.

    Mad Kaz Assembler is free software; you can redistribute it and/or modify 
    it under the terms of the GNU General Public License as published by the 
    Free Software Foundation; either version 3 of the License, or (at your 
    option) any later version.

    Mad Kaz Assembler is distributed in the hope that it will be useful, but 
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
    or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
    more details.

    You should have received a copy of the GNU General Public License along 
    with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Assembler {
    public Assembler() {
    }
    
    public static void main(String[] args) throws Throwable {
        if (args.length != 2) {
            System.out.println("e.g. java -jar Assembler input.txt output.bin");
            System.exit(1);
        }
        
        List inputStrings = new ArrayList();
        
        BufferedReader reader = new BufferedReader(new FileReader(args[0]));
        
        String line = "";
        while ((line = reader.readLine()) != null)
            inputStrings.add(line);
        
        cydASMParser parser = new cydASMParser();
        parser.parse(inputStrings);
        
        cydASMProcessor processor = new cydASMProcessor(parser.getResourceMap(), parser.getFuncMap(), parser.getExtensionSet());
        
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(args[1]));
        writer.write(processor.getFinalData());
        writer.close();
    }
    
}
