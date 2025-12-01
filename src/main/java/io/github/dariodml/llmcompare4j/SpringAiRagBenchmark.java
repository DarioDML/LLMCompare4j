package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;
import org.springframework.ai.document.Document;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class SpringAiRagBenchmark extends AbstractRagBenchmark {

    private OllamaChatModel chatModel;
    private SimpleVectorStore vectorStore;

    @Setup(Level.Trial)
    public void setup() {
        // 1. Initialize API
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl("http://localhost:11434")
                .build();

        // 2. Chat Model
        OllamaOptions chatOptions = OllamaOptions.builder()
                .model(modelName)
                .temperature(0.7)
                .build();

        this.chatModel = OllamaChatModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(chatOptions)
                .build();

        // 3. Embedding Model
        OllamaOptions embeddingOptions = OllamaOptions.builder()
                .model(embeddingModelName)
                .build();

        OllamaEmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(embeddingOptions)
                .build();

        // 4. Vector Store (In-Memory)
        // FIX: Use Builder pattern instead of constructor (which is now protected)
        this.vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        // 5. Ingest Documents
        List<Document> docs = documents.stream()
                .map(Document::new)
                .collect(Collectors.toList());

        this.vectorStore.add(docs);
    }

    @Benchmark
    public String benchmarkRag() {
        return rag(prompt, modelName);
    }

    @Override
    public String rag(String prompt, String modelName) {
        // 1. Retrieve relevant documents (Limit to Top 2 to match LangChain4j config)
        List<Document> similarDocs = vectorStore.similaritySearch(
                SearchRequest.builder()
                        .query(prompt)
                        .topK(2)
                        .build()
        );

        // 2. Combine context
        String context = similarDocs.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n"));

        // 3. Stuff Prompt
        String finalPrompt = "Context:\n" + context + "\n\nQuestion:\n" + prompt;

        // 4. Call Model
        return chatModel.call(finalPrompt);
    }
}