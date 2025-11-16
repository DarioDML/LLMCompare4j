package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;

public abstract class AbstractChatBenchmark {

//    @Param({
//            "Schrijf een Java functie die een lijst sorteert.",
//            "Wat zijn de voor- en nadelen van virtuele threads in Java?"
//    })
    public String prompt;

//    @Param({"llama3", "mistral", "phi3", "codellama"})
    public String modelName;

    public abstract String chat(String prompt, String modelName);
}
