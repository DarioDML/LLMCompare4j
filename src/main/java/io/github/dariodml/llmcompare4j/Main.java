package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                // Regex om alles te runnen dat eindigt op 'ChatBenchmark'
                .include(".*ChatBenchmark.*")
                .build();

        new Runner(opt).run();
    }
}
