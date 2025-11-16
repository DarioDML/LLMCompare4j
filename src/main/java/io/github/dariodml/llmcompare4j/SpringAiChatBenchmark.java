package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.annotations.*;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaChatOptions;

public class SpringAiChatBenchmark extends AbstractChatBenchmark {

	private OllamaChatModel model;
	private String activeModelName;

	private OllamaChatModel createModel(String modelName) {
		OllamaApi ollamaApi = OllamaApi.builder()
				.baseUrl("http://localhost:11434")
				.build();

		OllamaChatOptions defaultOptions = OllamaChatOptions.builder()
				.model(modelName)
				.temperature(0.7)
				.build();

		return OllamaChatModel.builder()
				.ollamaApi(ollamaApi)
				.defaultOptions(defaultOptions)
				.build();
	}

	@Setup(Level.Invocation)
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
			model = createModel(modelName);
			activeModelName = modelName;
		}
		return model.call(prompt);
	}
}
