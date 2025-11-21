package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

public class Main {
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
<<<<<<< Updated upstream
                // Regex om alles te runnen dat eindigt op 'ChatBenchmark'
                .include(".*ChatBenchmark.*")
=======
                // Include both Chat and Embedding benchmarks
                .include(".*(Chat|Embedding)Benchmark.*")
>>>>>>> Stashed changes
                .build();

        new Runner(opt).run();
    }
}
