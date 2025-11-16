package io.github.dariodml.llmcompare4j;

import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.chat.ChatModel;


public class LangChain4jChatBenchmark {
    private final ChatModel model;

    public LangChain4jChatBenchmark(String modelName) {
        this.model = OllamaChatModel.builder()
                .baseUrl("http://localhost:11434")
                .modelName(modelName)
                .build();
    }

    public String generateCode(String prompt) {
        return model.chat(prompt);
    }

    public String answerQuestion(String question) {
        return model.chat(question);
    }
}
