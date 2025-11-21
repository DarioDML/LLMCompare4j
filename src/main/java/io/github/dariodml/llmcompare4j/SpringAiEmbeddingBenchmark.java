package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;
import org.springframework.ai.ollama.OllamaEmbeddingModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaEmbeddingOptions;

import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 1, warmups = 0)
@Warmup(iterations = 1, time = 5)
@Measurement(iterations = 3, time = 5)
public class SpringAiEmbeddingBenchmark extends AbstractEmbeddingBenchmark {

    private OllamaEmbeddingModel model;

    @Setup(Level.Iteration)
    public void setupModel() {
        model = createModel(modelName);
    }

    private OllamaEmbeddingModel createModel(String name) {
        OllamaApi ollamaApi = OllamaApi.builder()
                .baseUrl("http://localhost:11434")
                .build();

        OllamaEmbeddingOptions options = OllamaEmbeddingOptions.builder()
                .model(name)
                .build();

        return OllamaEmbeddingModel.builder()
                .ollamaApi(ollamaApi)
                .defaultOptions(options)
                .build();
    }

    @Benchmark
    public Object benchmarkEmbedding() {
        return embed(prompt, modelName);
    }

    @Override
    public Object embed(String text, String modelName) {
        // Model is already initialized in setupModel(), ensuring pure benchmarking of the embed call
        return model.embed(text);
    }
}