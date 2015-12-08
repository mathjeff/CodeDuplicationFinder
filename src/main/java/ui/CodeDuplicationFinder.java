package ui;

import logging.Logger;
import logging.PrintLogger;
import logging.HierarchicalLogger;

import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import matching.ParseDatabase;
import parsing.FileParser;
import parsing.ParseItem;

public class CodeDuplicationFinder {

    public CodeDuplicationFinder() {

    }

    public AnalysisRequest parse(Logger logger, String[] args) {
        logger = logger.push("parsing arguments");
        logger.message("Parsing " + args.length + " arguments: ");
        AnalysisRequest request = new AnalysisRequest();
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            logger.message(arg);
            if ("--scan-path".equals(arg)){
                i++;
                String path = args[i];
                request.pathsToScan.add(path);
            } else if ("--grammar-path".equals(arg)) {
                i++;
                request.grammarPath = args[i];
            } else {
                throw new IllegalArgumentException("Unrecognized argument '" + arg + "'");
            }
        }
        return request;

    }

    public void process(Logger logger, AnalysisRequest request) throws IOException {
        // Note that technically, the question of which logger to use is part of the request
        // However, we're likely to be changing the logger in each function call, so we don't want to put it there and have access to two
        FileParser parser = new FileParser(request.grammarPath, request.parseStartRuleName);
        ParseDatabase db = new ParseDatabase();
        for (String path: request.pathsToScan) {
            ParseItem parse = parser.parse(logger, path);
            this.putParse(logger, db, parse);
        }
        Logger itemLogger = logger.push("match");
        TreeMap<Integer, List<ParseItem>> matchesBySignificance = new TreeMap<>();
        for (List<ParseItem> group : db.getMatchGroups(logger)) {
            // TODO investigate these constants further and determine whether the details make sense
            int numInstances = group.size();
            if (numInstances >= 2) {
                int matchSize = group.get(0).get_num_nonLeaf_children() - 4;
                if (matchSize > 0) {
                    //itemLogger.message("Match size = " + matchSize + " for " + group.get(0).getText());
                    int savingsFromConsolidation = (numInstances - 1) * matchSize - 6;
                    if (savingsFromConsolidation > 0) {
                        matchesBySignificance.put(savingsFromConsolidation, group);
                    }
                }
            }
        }
        logger.message("");
        logger.message("Found " + matchesBySignificance.size() + " groups of matches");
        for (List<ParseItem> group : matchesBySignificance.values()) {
    		logger.message("");
            logger.message("Group:");
            for (ParseItem item : group) {
                itemLogger.message(item.getText() + " (" + item.getRuleType() + " in " + item.getSourceDescription() + ")");
            }
        }
    }

    private void logParseItem(Logger logger, ParseItem parseItem) {
        logger.message(parseItem.getText());
        for (ParseItem child : parseItem.getChildren()) {
            this.logParseItem(logger.push("log child"), child);
        }
    }


    private void dummySimilarMethod(Logger logger, ParseItem parseItem) {
        logger.message(parseItem.getText());
        for (ParseItem child : parseItem.getChildren()) {
                this.dummySimilarMethod(logger.push("log " + child), child);

        }
    }

    private void dummySimilarMethod2(Logger logger, ParseItem parseItem) {
        logger.message(parseItem.getText());
        for (ParseItem child : parseItem.getChildren()) {
            if (parseItem.hashCode() == parseItem.hashCode()) {
                this.dummySimilarMethod2(logger.push("log " + child), child);
            } else {
                logger.message("hmm");
            }
        }
    }


    private void putParse(Logger logger, ParseDatabase db, ParseItem parseItem) {
        int numChildren = parseItem.getChildCount();
        // skip saving any node having exactly 1 child, because those nodes are likely just implementation details of the parse
        if (numChildren != 1) {
            db.putParse(logger, parseItem);
        }
        for (int i = 0; i < numChildren; i++) {
            ParseItem childItem = parseItem.getChild(i);
            this.putParse(logger, db, childItem);
        }

    }
    public void execute(String[] args) throws IOException {
        HierarchicalLogger logger = new PrintLogger();
        logger.setMaxDepthToLog(2);
        AnalysisRequest request = this.parse(logger, args);
        this.process(logger, request);
        logger.summarizeSkippedMessages();
    }


    public static void main(String[] args) throws IOException {
        CodeDuplicationFinder runner = new CodeDuplicationFinder();
        runner.execute(args);
    }
}
