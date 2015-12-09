package parsing;

import logging.Logger;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.Trees;
import org.antlr.v4.tool.Grammar;

import java.io.File;
import java.io.IOException;

import logging.PrintLogger;


public class FileParser {
    public FileParser(String combinedGrammarFilePath, String parseStartRuleName) {
        System.out.println("combined grammar file path = " + combinedGrammarFilePath);
        assert(combinedGrammarFilePath != null);
        File grammarFile = new File(combinedGrammarFilePath);
        assert(grammarFile.canRead());
        this.grammar = Grammar.load(combinedGrammarFilePath);
        this.parseStartRuleName = parseStartRuleName;
    }


    public ParseItem parse(Logger logger, String filePath)
            throws IOException
    {
        Logger subLogger = logger.push("parse file");
        subLogger.message("Parsing " + filePath);
        LexerInterpreter lexEngine = this.grammar.createLexerInterpreter(new ANTLRFileStream(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexEngine);
        ParserInterpreter parser = this.grammar.createParserInterpreter(tokens);
        ParserRuleContext tree = parser.parse(this.grammar.getRule(this.parseStartRuleName).index);
//GALAXYJIM        logger.message("parse tree: " + tree.toStringTree(parser));
        ParseItem item = this.toParseItem(subLogger, filePath, tree, parser);
        return item;
    }

    // converts a ParseTree to a ParseItem by telling each node which file and which rule it came from
    private ParseItem toParseItem(Logger logger, String fileName, ParseTree tree, ParserInterpreter parser) {
        String sourceDescription = fileName;
        //String ruleName = Trees.toStringTree(tree, parser);
        String ruleName = Trees.getNodeText(tree, parser);
        String nodeText = tree.getText();
        ParseItem result = new ParseItem(nodeText, ruleName, sourceDescription);
        for (int i = 0; i < tree.getChildCount(); i++) {
            ParseItem child = this.toParseItem(logger, fileName, tree.getChild(i), parser);
            result.addChild(child);
        }
        return result;
    }

    String parseStartRuleName;
    Grammar grammar;

}