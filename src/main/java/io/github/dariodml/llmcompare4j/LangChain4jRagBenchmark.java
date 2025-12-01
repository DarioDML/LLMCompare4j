package io.github.dariodml.llmcompare4j;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import org.openjdk.jmh.annotations.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class LangChain4jRagBenchmark extends AbstractRagBenchmark {

    interface Assistant {
        String chat(String userMessage);
    }

    private Assistant assistant;

    @Setup(Level.Trial)
    public void setup() {
        // 1. CHAT MODEL
        ChatLanguageModel chatModel = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .temperature(0.7)
                .build();

        // 2. EMBEDDING MODEL
        EmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(embeddingModelName)
                .build();

        // 3. EMBEDDING STORE
        EmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 4. INGESTOR (Core API)
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();

        List<Document> docs = documents.stream()
                .map(Document::from)
                .collect(Collectors.toList());

        ingestor.ingest(docs);

        // 5. CONTENT RETRIEVER (Core API)
        ContentRetriever contentRetriever = EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(2)
                .minScore(0.5)
                .build();

        // 6. AI SERVICE (No chat memory to match Spring AI - pure RAG with context retrieval)
        this.assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(chatModel)
                .contentRetriever(contentRetriever)
                .build();
    }

    @Benchmark
    public String benchmarkRag() {
        return rag(prompt, modelName);
    }

    @Override
    public String rag(String prompt, String modelName) {
        return assistant.chat(prompt);
    }
}