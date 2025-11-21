package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;

@State(Scope.Thread)
public abstract class AbstractEmbeddingBenchmark {

    @Param({
            "Short sentence for embedding.",
            "This is a slightly longer paragraph that needs to be converted into a vector representation to test the performance of the embedding model on larger text chunks."
    })
    public String prompt;

    // 'all-minilm' is a standard small model. 'bge-m3' is a popular large model.
    @Param({"all-minilm", "bge-m3"})
    public String modelName;

    public abstract Object embed(String text, String modelName);
}