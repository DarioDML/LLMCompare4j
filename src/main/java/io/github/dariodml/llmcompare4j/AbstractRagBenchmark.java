package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;
import java.util.List;

@State(Scope.Thread)
public abstract class AbstractRagBenchmark {

    @Param({
            "What implies that Java is platform independent?",
            "How does garbage collection work?"
    })
    public String prompt;

    @Param({"llama3.2", "mistral"})
    public String modelName;

    // The knowledge base we will ingest into the Vector Store
    protected final List<String> documents = List.of(
            "Java is a high-level, class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible.",
            "It is a general-purpose programming language intended to let application developers write once, run anywhere (WORA), meaning that compiled Java code can run on all platforms that support Java without the need for recompilation.",
            "Java applications are typically compiled to bytecode that can run on any Java virtual machine (JVM) regardless of the underlying computer architecture.",
            "Garbage collection in Java is the process by which Java programs perform automatic memory management. Java programs compile to bytecode that can be run on a Java Virtual Machine, or JVM.",
            "When Java programs run on the JVM, objects are created on the heap, which is a portion of memory dedicated to the program. Eventually, some objects will no longer be needed. The garbage collector finds these unused objects and deletes them to free up memory."
    );

    public abstract String rag(String prompt, String modelName);
}