import logging.Logger;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.LexerInterpreter;
import org.antlr.v4.runtime.ParserInterpreter;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.tool.Grammar;
import org.antlr.v4.tool.LexerGrammar;

import java.io.IOException;

class Parser {
    public Parser() {

    }


    public ParseTree parse(Logger logger, String fileName,
                                  String combinedGrammarFileName,
                                  String startRule)
            throws IOException
    {
        logger = logger.push("parse");
        final Grammar g = Grammar.load(combinedGrammarFileName);
        LexerInterpreter lexEngine = g.createLexerInterpreter(new ANTLRFileStream(fileName));
        CommonTokenStream tokens = new CommonTokenStream(lexEngine);
        ParserInterpreter parser = g.createParserInterpreter(tokens);
        ParseTree t = parser.parse(g.getRule(startRule).index);
        logger.message("parse tree: " + t.toStringTree(parser));
        return t;
    }
}