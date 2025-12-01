package io.github.dariodml.llmcompare4j;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Collection;
import java.awt.*; // for Font
import java.util.HashMap;
import java.util.Map;

public class BenchmarkVisualizer {

    public static void createChart(Collection<RunResult> results) {
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
            String shortPrompt = (prompt != null && prompt.length() > 25) ? prompt.substring(0, 25) + "..." : prompt;
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
                valueLabel = "Time (seconds) - Lower is better";
            }

            JFreeChart barChart = ChartFactory.createBarChart(
                    "LLM " + type + " Benchmark",
                    "Model & Input",
                    valueLabel,
                    dataset,
                    PlotOrientation.VERTICAL,
                    true, true, false);

            // 4. OPMAAK: Labels draaien en waardes tonen
            CategoryPlot plot = (CategoryPlot) barChart.getPlot();

            // X-As labels schuin (45 graden)
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);

            // Waardes BOVEN op de balken tonen
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setDefaultItemLabelsVisible(true);

            // Formattering instellen (bijv. 2 decimalen voor Memory, 3 voor tijd)
            DecimalFormat format = type.equals("Memory") ? new DecimalFormat("0.00") : new DecimalFormat("0.000");
            renderer.setDefaultItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", format));

            // Zorg dat de label ook een beetje leesbaar lettertype heeft
            renderer.setDefaultItemLabelFont(new Font("SansSerif", Font.PLAIN, 10));

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