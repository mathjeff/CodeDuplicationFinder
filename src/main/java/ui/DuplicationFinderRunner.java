package ui;

import logging.Logger;
import logging.PrintLogger;

import java.io.IOException;

import org.antlr.v4.runtime.tree.ParseTree;
import parsing.FileParser;

public class DuplicationFinderRunner {

    public DuplicationFinderRunner() {

    }

    public AnalysisRequest parse(Logger logger, String[] args) {
        AnalysisRequest request = new AnalysisRequest();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg == "--scan-path"){
                i++;
                String path = args[i];
                request.pathsToScan.add(path);
                break;
            }
            if (arg == "--grammar-path") {
                i++;
                request.grammarPath = args[i];
                break;
            }
        }
        return request;

    }

    public void process(Logger logger, AnalysisRequest request) throws IOException {
        // Note that technically, the question of which logger to use is part of the request
        // However, we're likely to be changing the logger in each function call, so we don't want to put it there and have access to two
        FileParser parser = new FileParser(request.grammarPath, request.parseStartRuleName);
        for (String path: request.pathsToScan) {
            ParseTree parse = parser.parse(logger, path);

        }
    }

    public void execute(String[] args) throws IOException {
        Logger logger = new PrintLogger();
        AnalysisRequest request = this.parse(logger, args);
        this.process(request);
    }


    public static void main(String[] args) throws IOException {
        DuplicationFinderRunner finder = new DuplicationFinderRunner();
        finder.execute(args);

        FileParser parser = new FileParser(args);
        Logger logger = new PrintLogger();
        parser.parse(logger, "test.java", "Java8.g4", "compilationUnit");
    }
}
