package io.github.dariodml.llmcompare4j;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello, LLM Compare 4J!");
        LangChain4jChatBenchmark langChainBenchmark = new LangChain4jChatBenchmark();

        String response = langChainBenchmark.chat("Hoeveel is 2 + 2?", "llama3.2");
        System.out.println("Response: " + response);
    }
}
