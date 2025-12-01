package io.github.dariodml.llmcompare4j;

import org.openjdk.jmh.profile.GCProfiler;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collection;

public class Main {
    public static void main(String[] args) throws RunnerException {
        // 1. Configure the benchmark
        Options opt = new OptionsBuilder()
 
                // Include both Chat, Embedding and Rag benchmarks
                .include(".*(Chat|Embedding|Rag)Benchmark.*")
                .addProfiler(GCProfiler.class)
                .build();

        // 2. Run the benchmark and CAPTURE THE RESULTS in a variable
        Collection<RunResult> results = new Runner(opt).run();

        // 3. Generate the chart with the results
        BenchmarkVisualizer.createChart(results);
    }
}
