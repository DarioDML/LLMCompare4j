package io.github.dariodml.llmcompare4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class QualityReporter {

    public static void main(String[] args) {
        runQualityCheck();
    }

    public static void runQualityCheck() {
        System.out.println("Starten van Quality Check... Antwoorden worden opgeslagen in 'quality_report.md'");

        try (PrintWriter writer = new PrintWriter(new FileWriter("quality_report.md"))) {
            writer.println("# LLM Kwaliteitsrapport");
            writer.println("Vergelijking van antwoorden tussen Spring AI en LangChain4j voor verschillende modellen.\n");

            List<String> models = List.of("mistral", "llama3.2", "codellama");
            String prompt = "Schrijf een Java functie die een lijst sorteert.";

            LangChain4jChatBenchmark lcBenchmark = new LangChain4jChatBenchmark();
            SpringAiChatBenchmark springBenchmark = new SpringAiChatBenchmark();

            for (String model : models) {
                System.out.println(">>> Testen van model: " + model);

                writer.println("## Model: " + model);
                writer.println("- **Prompt:** " + prompt);
                writer.println("\n---");

                // --- 1. Test LangChain4j ---
                System.out.print("   Generating LangChain4j... ");
                try {
                    String lcResponse = lcBenchmark.chat(prompt, model);

                    writer.println("### 🦜 LangChain4j (" + model + "):");
                    writer.println(lcResponse);

                    System.out.println("OK");
                } catch (Exception e) {
                    writer.println("> Fout bij LangChain4j: " + e.getMessage());
                    System.out.println("FOUT");
                }

                writer.println("\n");

                // --- 2. Test Spring AI ---
                System.out.print("   Generating Spring AI...   ");
                try {
                    String springResponse = springBenchmark.chat(prompt, model);

                    writer.println("### 🌱 Spring AI (" + model + "):");
                    writer.println(springResponse);

                    System.out.println("OK");
                } catch (Exception e) {
                    writer.println("> Fout bij Spring AI: " + e.getMessage());
                    System.out.println("FOUT");
                }

                writer.println("\n---\n");
                writer.flush();
            }

            System.out.println("Klaar! Bekijk 'quality_report.md' voor de resultaten.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}