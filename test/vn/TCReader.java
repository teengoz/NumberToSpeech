/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vn;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author Tee
 */
public class TCReader {
    public String path;
    
    public TCReader() {
        this.path = "";
    }
    
    public TCReader(String path) {
        this.path = path;
    }
    
    public static final String UTF8_BOM = "\uFEFF";

    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
        }
        return s;
    }
    
    public Collection<String[]> getTestData() throws IOException {
        List<String[]> records = new ArrayList<String[]>();
        String record;
        BufferedReader file = new BufferedReader(
		   new InputStreamReader(
                      new FileInputStream(this.path), "UTF8"));
        while ((record = file.readLine()) != null) {
            record = removeUTF8BOM(record);
            String fields[] = record.split(",");
            if (fields.length == 1) {
                fields = new String[]{fields[0], ""};
            }
            if (fields.length == 2) {
                records.add(fields);
            }
        }
        file.close();
        return records;
    }
}
