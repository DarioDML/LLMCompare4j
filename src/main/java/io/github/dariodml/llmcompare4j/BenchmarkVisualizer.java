package io.github.dariodml.llmcompare4j;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.Result;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.awt.*; // for Font
import java.util.HashMap;
import java.util.Map;

public class BenchmarkVisualizer {

    public static void createChart(Collection<RunResult> results) {
        // Separate datasets for Chat and Embedding because their time scales differ significantly
        // Maak datasets aan voor alle categorieën
        Map<String, DefaultCategoryDataset> datasets = new HashMap<>();
        datasets.put("Chat", new DefaultCategoryDataset());
        datasets.put("Embedding", new DefaultCategoryDataset());
        datasets.put("RAG", new DefaultCategoryDataset());
        datasets.put("Memory", new DefaultCategoryDataset());

        for (RunResult result : results) {
            // 1. Data Verzamelen
            double score = result.getPrimaryResult().getScore();
            String benchmarkClass = result.getParams().getBenchmark();

            String modelName = result.getParams().getParam("modelName");
            String prompt = result.getParams().getParam("prompt");

            // Framework bepalen (LangChain4j vs Spring AI)
            String framework = benchmarkClass.contains("LangChain") ? "LangChain4j" : "Spring AI";

            // Prompt inkorten voor leesbaarheid op de as
            String shortPrompt = (prompt != null && prompt.length() > 15) ? prompt.substring(0, 15) + "..." : prompt;
            String category = modelName + " (" + shortPrompt + ")";

            // 2. Type Bepalen (Chat, Embedding, of RAG)
            String type;
            if (benchmarkClass.contains("Embedding")) {
                type = "Embedding";
            } else if (benchmarkClass.contains("Rag")) {
                type = "RAG";
            } else {
                type = "Chat";
            }

            // Voeg score toe aan de juiste dataset (Chat/Embedding/RAG)
            datasets.get(type).addValue(score, framework, category);

            // 3. Memory Usage toevoegen
            // Dit komt in de 4e grafiek terecht, ongeacht of het Chat/Rag/Embedding was
            Result memoryResult = result.getSecondaryResults().get("gc.alloc.rate.norm");
            if (memoryResult != null) {
                double memoryInMB = memoryResult.getScore() / 1024.0 / 1024.0;
                String memoryCategory = type + ": " + category;
                datasets.get("Memory").addValue(memoryInMB, framework, memoryCategory);
            }
        }

        // Generate a chart for each type
        for (Map.Entry<String, DefaultCategoryDataset> entry : datasets.entrySet()) {
            String type = entry.getKey();
            DefaultCategoryDataset dataset = entry.getValue();

            if (dataset.getColumnCount() == 0) continue;

            // Labels configured based on type
            String valueLabel;
            if (type.equals("Memory")) {
                valueLabel = "Memory Allocation (MB/op) - Lower is better";
            } else if (type.equals("Embedding")) {
                valueLabel = "Time (milliseconds) - Lower is better";
            } else {
                // Chat en RAG measured in seconds
                valueLabel = "Time (seconds) - Lower is better";
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "LLM " + type + " Benchmark",
                    "Model & Input",
                    valueLabel,
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
        }
    }
}