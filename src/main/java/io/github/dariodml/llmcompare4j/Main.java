package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

public class Main {
    public static void main(String[] args) throws RunnerException {
        // 1. Configureer de benchmark
        Options opt = new OptionsBuilder()
 
                // Include both Chat and Embedding benchmarks
                .include(".*(Chat|Embedding)Benchmark.*")
                .build();

        // 2. Run de benchmark en VANG DE RESULTATEN OP in een variabele
        Collection<RunResult> results = new Runner(opt).run();

        // 3. Genereer de grafiek met de resultaten
        BenchmarkVisualizer.createChart(results);
    }
}
