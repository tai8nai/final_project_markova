package ru.example;

import ru.example.csv.CsvLoader;
import ru.example.db.DbManager;
import ru.example.model.SportsObject;

import com.opencsv.exceptions.CsvValidationException;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.chart.JFreeChart;

import java.io.File;
import java.io.IOException; // <--- ВАЖНО: этот импорт нужен для IOException
import java.sql.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        String csvPath = "Объекты спорта.csv";
        try {
            // 1. Load from CSV (обработка возможного CsvValidationException)
            List<SportsObject> objects = CsvLoader.loadSportsObjects(csvPath);

            // 2. Init DB
            DbManager db = new DbManager();
            db.createTables();

            // 3. Save to DB
            Map<String, Integer> regionIdMap = new HashMap<>();
            for (SportsObject obj : objects) {
                String region = obj.getRegion();
                int regionId = regionIdMap.computeIfAbsent(region, r -> {
                    try { return db.getOrInsertRegion(r); }
                    catch (Exception e) { throw new RuntimeException(e); }
                });
                db.insertSportsObject(obj.getId(), obj.getName(), obj.getType(), obj.getAddress(), obj.getDate(), regionId);
            }

            // 4. SQL: Count per region
            Map<String, Integer> regionCounts = new HashMap<>();
            try (Statement st = db.getConnection().createStatement()) {
                ResultSet rs = st.executeQuery(
                        "SELECT region.name, COUNT(sports_object.id) as cnt " +
                                "FROM region LEFT JOIN sports_object ON region.id = sports_object.region_id " +
                                "GROUP BY region.name"
                );
                while (rs.next()) {
                    regionCounts.put(rs.getString("name"), rs.getInt("cnt"));
                }
            }

            // 5. Гистограмма по регионам
            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            for (Map.Entry<String, Integer> entry : regionCounts.entrySet()) {
                dataset.addValue(entry.getValue(), "Объекты спорта", entry.getKey());
            }
            JFreeChart chart = ChartFactory.createBarChart(
                    "Количество объектов спорта по регионам",
                    "Регион",
                    "Количество",
                    dataset
            );
            ChartUtils.saveChartAsPNG(new File("histogram.png"), chart, 1200, 600);

            // 6. Среднее количество по регионам
            double avg = regionCounts.values().stream().mapToInt(x -> x).average().orElse(0.0);
            System.out.println("Среднее количество объектов спорта в регионах: " + avg);

            // 7. Топ-3 регионов
            System.out.println("Топ-3 регионов по количеству объектов спорта:");
            regionCounts.entrySet().stream()
                    .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
                    .limit(3)
                    .forEach(e -> System.out.println("Регион: " + e.getKey() + ", объектов: " + e.getValue()));

            db.close();
            System.out.println("Гистограмма сохранена в файл histogram.png");
        } catch (IOException | CsvValidationException e) {
            System.err.println("Ошибка чтения CSV: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка работы с БД: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}