package io.github.dariodml.llmcompare4j;

import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2q.AllMiniLmL6V2QuantizedEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import org.openjdk.jmh.annotations.*;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS) // Embeddings are fast, use Milliseconds
@State(Scope.Thread)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
public class LangChain4jEmbeddingBenchmark extends AbstractEmbeddingBenchmark {

    private EmbeddingModel model;
    private String activeModelName;

    @Setup(Level.Trial)
    public void setupModel() {
        model = createModel(modelName);
        activeModelName = modelName;
    }

    private EmbeddingModel createModel(String name) {
        // LangChain4j Special Feature: Run All-MiniLM in-process (Pure Java/ONNX)
        // This avoids the HTTP overhead of Ollama for this specific model.
        if (name.startsWith("all-minilm")) {
            return new AllMiniLmL6V2QuantizedEmbeddingModel();
        }

        // For other models (like bge-m3), use Ollama
        return OllamaEmbeddingModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(name)
                .timeout(Duration.ofMinutes(1))
                .build();
    }

    @Benchmark
    public Object benchmarkEmbedding() {
        return embed(prompt, modelName);
    }

    @Override
    public Object embed(String text, String modelName) {
        if (model == null || !activeModelName.equals(modelName)) {
            this.model = createModel(modelName);
            this.activeModelName = modelName;
        }
        return model.embed(text);
    }
}