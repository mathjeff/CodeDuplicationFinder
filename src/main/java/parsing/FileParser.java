package parsing;

import logging.Logger;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;

import java.io.IOException;

import logging.PrintLogger;


class
FileParser {
    public FileParser(String combinedGrammarFilePath, String parseStartRule) {
        this.grammar = Grammar.load(combinedGrammarFilePath);
        this.parseStartRule = parseStartRule;
    }


    public ParseTree parse(Logger logger, String filePath)
            throws IOException
    {
        logger = logger.push("parse");
        LexerInterpreter lexEngine = this.grammar.createLexerInterpreter(new ANTLRFileStream(filePath));
        CommonTokenStream tokens = new CommonTokenStream(lexEngine);
        ParserInterpreter parser = this.grammar.createParserInterpreter(tokens);
        ParseTree t = parser.parse(this.grammar.getRule(this.parseStartRule).index);
        logger.message("parse tree: " + t.toStringTree(parser));
        return t;
    }

    String parseStartRule;
    Grammar grammar;


    public static void main(String[] args) throws IOException {
        FileParser parser = new FileParser(args);
        Logger logger = new PrintLogger();
        parser.parse(logger, "test.java", "Java8.g4", "compilationUnit");
    }
}