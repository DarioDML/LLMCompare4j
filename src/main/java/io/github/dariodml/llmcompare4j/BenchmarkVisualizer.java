package io.github.dariodml.llmcompare4j;
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openjdk.jmh.results.RunResult;
<<<<<<< Updated upstream

import java.io.File;
import java.io.IOException;
import java.util.Collection;
=======
import java.awt.*; // Voor Font
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
>>>>>>> Stashed changes

public class BenchmarkVisualizer {

    public static void createChart(Collection<RunResult> results) {
<<<<<<< Updated upstream
        // 1. Dataset voorbereiden
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (RunResult result : results) {
            // Haal de score (tijd in seconden) en parameters op
            double score = result.getPrimaryResult().getScore(); // Score in s/op
            String modelName = result.getParams().getParam("modelName");
            String prompt = result.getParams().getParam("prompt");

            // Bepaal welk framework het is op basis van de klassenaam of methodenaam
            String benchmarkName = result.getParams().getBenchmark();
            String framework = benchmarkName.contains("LangChain") ? "LangChain4j" : "Spring AI";

            // Maak een korte leesbare categorie (Model + kort stukje van prompt)
            String shortPrompt = prompt.length() > 15 ? prompt.substring(0, 15) + "..." : prompt;
            String category = modelName + " (" + shortPrompt + ")";

            // Voeg toe aan dataset: (Waarde, Framework-serie, Model-categorie)
            dataset.addValue(score, framework, category);
        }

        // 2. Grafiek maken
        JFreeChart barChart = ChartFactory.createBarChart(
                "LLM Performance Benchmark", // Titel
                "Model & Prompt",            // X-as label
                "Tijd (seconden) - Lager is beter", // Y-as label
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);

        // 3. Opslaan als afbeelding
        try {
            File chartFile = new File("benchmark_resultaten.png");
            ChartUtils.saveChartAsPNG(chartFile, barChart, 1200, 800);
            System.out.println("Grafiek opgeslagen als: " + chartFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Fout bij opslaan grafiek: " + e.getMessage());
=======
        // Separate datasets for Chat and Embedding because their time scales differ significantly
        Map<String, DefaultCategoryDataset> datasets = new HashMap<>();
        datasets.put("Chat", new DefaultCategoryDataset());
        datasets.put("Embedding", new DefaultCategoryDataset());

        for (RunResult result : results) {
            double score = result.getPrimaryResult().getScore();
            String unit = result.getPrimaryResult().getScoreUnit(); // e.g., s/op or ms/op

            String modelName = result.getParams().getParam("modelName");
            String prompt = result.getParams().getParam("prompt");
            String benchmarkClass = result.getParams().getBenchmark();

            // Determine Type (Chat vs Embedding)
            String type = benchmarkClass.contains("Embedding") ? "Embedding" : "Chat";

            // Determine Framework
            String framework = benchmarkClass.contains("LangChain") ? "LangChain4j" : "Spring AI";

            String shortPrompt = prompt.length() > 15 ? prompt.substring(0, 15) + "..." : prompt;
            String category = modelName + " (" + shortPrompt + ")";

            datasets.get(type).addValue(score, framework, category);
        }

        // Generate a chart for each type
        for (Map.Entry<String, DefaultCategoryDataset> entry : datasets.entrySet()) {
            String type = entry.getKey();
            DefaultCategoryDataset dataset = entry.getValue();

            if (dataset.getColumnCount() == 0) continue;

            String timeLabel = type.equals("Embedding") ? "Time (milliseconds) - Lower is better" : "Time (seconds) - Lower is better";

            JFreeChart barChart = ChartFactory.createBarChart(
                    "LLM " + type + " Benchmark",
                    "Model & Input",
                    timeLabel,
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            try {
                File chartFile = new File("benchmark_resultaten_" + type.toLowerCase() + ".png");
                ChartUtils.saveChartAsPNG(chartFile, barChart, 1200, 800);
                System.out.println(type + " Chart saved as: " + chartFile.getAbsolutePath());
            } catch (IOException e) {
                System.err.println("Error saving " + type + " chart: " + e.getMessage());
            }
>>>>>>> Stashed changes
        }
    }
}