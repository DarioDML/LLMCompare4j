package io.github.dariodml.llmcompare4j;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.openjdk.jmh.results.RunResult;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class BenchmarkVisualizer {

    public static void createChart(Collection<RunResult> results) {
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
        }
    }
}