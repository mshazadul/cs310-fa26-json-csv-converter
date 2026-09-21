package edu.jsu.mcis.cs310;

import com.github.cliftonlabs.json_simple.*;
import com.opencsv.*;
import java.io.*;
import java.util.*;

public class Converter {
    
    /*
        
        Consider the following CSV data, a portion of a database of episodes of
        the classic "Star Trek" television series:
        
        "ProdNum","Title","Season","Episode","Stardate","OriginalAirdate","RemasteredAirdate"
        "6149-02","Where No Man Has Gone Before","1","01","1312.4 - 1313.8","9/22/1966","1/20/2007"
        "6149-03","The Corbomite Maneuver","1","02","1512.2 - 1514.1","11/10/1966","12/9/2006"
        
        (For brevity, only the header row plus the first two episodes are shown
        in this sample.)
    
        The corresponding JSON data would be similar to the following; tabs and
        other whitespace have been added for clarity.  Note the curly braces,
        square brackets, and double-quotes!  These indicate which values should
        be encoded as strings and which values should be encoded as integers, as
        well as the overall structure of the data:
        
        {
            "ProdNums": [
                "6149-02",
                "6149-03"
            ],
            "ColHeadings": [
                "ProdNum",
                "Title",
                "Season",
                "Episode",
                "Stardate",
                "OriginalAirdate",
                "RemasteredAirdate"
            ],
            "Data": [
                [
                    "Where No Man Has Gone Before",
                    1,
                    1,
                    "1312.4 - 1313.8",
                    "9/22/1966",
                    "1/20/2007"
                ],
                [
                    "The Corbomite Maneuver",
                    1,
                    2,
                    "1512.2 - 1514.1",
                    "11/10/1966",
                    "12/9/2006"
                ]
            ]
        }
        
        Your task for this program is to complete the two conversion methods in
        this class, "csvToJson()" and "jsonToCsv()", so that the CSV data shown
        above can be converted to JSON format, and vice-versa.  Both methods
        should return the converted data as strings, but the strings do not need
        to include the newlines and whitespace shown in the examples; again,
        this whitespace has been added only for clarity.
        
        NOTE: YOU SHOULD NOT WRITE ANY CODE WHICH MANUALLY COMPOSES THE OUTPUT
        STRINGS!!!  Leave ALL string conversion to the two data conversion
        libraries we have discussed, OpenCSV and json-simple.  See the "Data
        Exchange" lecture notes for more details, including examples.
        
    */
    
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String result = "{}"; // default return value; replace later!
        
        try {
        
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JsonObject json = new JsonObject();
            JsonArray prodNums = new JsonArray();
            JsonArray colHeadings = new JsonArray();
            JsonArray data = new JsonArray();
            
            String[] headings = iterator.next();
            for (String heading : headings) {
                colHeadings.add(heading);
                }
             while (iterator.hasNext()) {
                    String[] row = iterator.next();

                    prodNums.add(row[0]);

                    JsonArray record = new JsonArray();
                    record.add(row[1]);
                    record.add(Integer.parseInt(row[2]));
                    record.add(Integer.parseInt(row[3]));
                    record.add(row[4]);
                    record.add(row[5]);
                    record.add(row[6]);

                    data.add(record);
                }

                json.put("ProdNums", prodNums);
                json.put("ColHeadings", colHeadings);
                json.put("Data", data);

                result = Jsoner.serialize(json);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
    @SuppressWarnings("unchecked")
    public static String jsonToCsv(String jsonString) {
        
        String result = ""; // default return value; replace later!
        
        try {
            JsonObject json = (JsonObject) Jsoner.deserialize(jsonString);
            JsonArray prodNums = (JsonArray) json.get("ProdNums");
            JsonArray colHeadings = (JsonArray) json.get("ColHeadings");
            JsonArray data = (JsonArray) json.get("Data");

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer);

            String[] headings = new String[colHeadings.size()];
            for (int i = 0; i < colHeadings.size(); i++) {
                headings[i] = colHeadings.get(i).toString();
            }
            csvWriter.writeNext(headings);

            for (int i = 0; i < data.size(); i++) {
                JsonArray record = (JsonArray) data.get(i);

                String[] row = new String[7];
                row[0] = prodNums.get(i).toString();
                row[1] = record.get(0).toString();
                row[2] = record.get(1).toString();
                int episode = Integer.parseInt(record.get(2).toString());
                row[3] = String.format("%02d", episode);
                row[4] = record.get(3).toString();
                row[5] = record.get(4).toString();
                row[6] = record.get(5).toString();

                csvWriter.writeNext(row);
            }

            result = writer.toString();
            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return result.trim();
        
    }
    
}
