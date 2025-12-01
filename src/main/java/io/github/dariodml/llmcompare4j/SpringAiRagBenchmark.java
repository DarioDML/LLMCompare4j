package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
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

    private ChatClient chatClient;

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

        OllamaChatModel chatModel = OllamaChatModel.builder()
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
        this.vectorStore = SimpleVectorStore.builder(embeddingModel).build();

        // 5. Ingest Documents
        List<Document> docs = documents.stream()
                .map(Document::new)
                .collect(Collectors.toList());

        this.vectorStore.add(docs);

        // 6. ChatClient with QuestionAnswerAdvisor (equivalent to LangChain4j's AiServices)
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(SearchRequest.builder()
                                        .topK(2)
                                        .similarityThreshold(0.5)
                                        .build())
                                .build()
                )
                .build();
    }

    @Benchmark
    public String benchmarkRag() {
        return rag(prompt, modelName);
    }

    @Override
    public String rag(String prompt, String modelName) {
        // Use ChatClient with QuestionAnswerAdvisor (Spring AI's high-level RAG abstraction)
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}