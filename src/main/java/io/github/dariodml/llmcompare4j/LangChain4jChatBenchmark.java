package io.github.dariodml.llmcompare4j;

import dev.langchain4j.model.ollama.OllamaChatModel;
import org.openjdk.jmh.annotations.*;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime) // Change Mode to AverageTime (easier to read for slow tasks)
@OutputTimeUnit(TimeUnit.SECONDS) // Output time unit to Seconds
@State(Scope.Thread)
@Fork(value = 1, warmups = 0) // Reduce Forks to 1 to save time

// Reduce iterations. LLMs are slow; we don't need 1000 samples.
@Warmup(iterations = 1, time = 10, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 10, timeUnit = TimeUnit.SECONDS)
public class LangChain4jChatBenchmark extends AbstractChatBenchmark {

    private OllamaChatModel model;
    private String activeModelName;

    private OllamaChatModel createModel(String modelNameToUse) {
        return OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelNameToUse)
                .temperature(0.7)
                .timeout(Duration.ofMinutes(5))
                .build();
    }

    @Setup(Level.Trial)
    public void setupModel() {
        model = createModel(modelName);
        activeModelName = modelName;
    }

    @Benchmark
    public String benchmarkChat() {
        return chat(prompt, modelName);
    }

    @Override
    public String chat(String prompt, String modelName) {
        // Lazily initialize the model when calling from outside of JMH runs
        if (model == null || activeModelName == null || !activeModelName.equals(modelName)) {
            this.model = createModel(modelName);
            this.activeModelName = modelName;
        }
        return model.chat(prompt);
    }
}