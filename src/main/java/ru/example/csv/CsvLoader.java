package ru.example.csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ru.example.model.SportsObject;

import java.io.*;
import java.util.*;

public class CsvLoader {
    public static List<SportsObject> loadSportsObjects(String csvPath) throws IOException, CsvValidationException {
        List<SportsObject> list = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(new FileInputStream(csvPath), "Windows-1251"))) {
            String[] row;
            int lineNum = 0;
            while ((row = reader.readNext()) != null) {
                if (lineNum++ == 0) continue; // skip header
                if (row.length < 5) continue;
                try {
                    int id = Integer.parseInt(row[0].replace("\"", "").trim());
                    String name = row[1].replace("\"", "").trim();
                    String type = row[2].replace("\"", "").trim();
                    String address = row[3].replace("\"", "").trim();
                    String date = row[4].replace("\"", "").trim();

                    String region = extractRegion(address);
                    list.add(new SportsObject(id, name, type, address, date, region));
                } catch (Exception e) {
                    // skip line
                }
            }
        }
        return list;
    }

    private static String extractRegion(String address) {
        if (address == null) return "";
        String[] parts = address.split(",");
        for (int i = parts.length - 1; i >= 0; --i) {
            String p = parts[i].trim();
            if (p.matches(".*(обл|край|республика|Москва|Московская область|Петербург|Санкт-Петербург).*"))
                return combineMoscow(p);
            if (p.matches(".*[а-яА-Я]+ская область.*"))
                return combineMoscow(p);
        }
        if (parts.length > 1) return combineMoscow(parts[parts.length-2].trim());
        return "Неизвестно";
    }

    private static String combineMoscow(String region) {
        String s = region.replaceAll("\"", "");
        if (s.contains("Москва") || s.contains("Московская область"))
            return "Москва и Московская область";
        return s;
    }
}