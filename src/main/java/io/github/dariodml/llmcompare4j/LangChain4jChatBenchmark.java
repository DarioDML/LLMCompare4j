package io.github.dariodml.llmcompare4j;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.exception.ModelNotFoundException;
import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public class LangChain4jChatBenchmark extends AbstractChatBenchmark {

    private OllamaChatModel model;
    private String activeModelName;

    @Setup(Level.Invocation)
    public void setupModel() {
        model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .temperature(0.7)
                .build();
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
            model = OllamaChatModel.builder()
                    .baseUrl("http://localhost:11434")
                    .modelName(modelName)
                    .temperature(0.7)
                    .build();
            activeModelName = modelName;
        }

        return model.chat(prompt);
    }
}
