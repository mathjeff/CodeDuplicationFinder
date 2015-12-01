package matching;

import logging.Logger;
import org.antlr.v4.runtime.tree.ParseTree;
import parsing.ParseItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// holds ParseItems and finds similar ones
public class ParseDatabase {

    public void PutParse(Logger logger, ParseItem parseItem) {
        Integer hash = this.hash(logger, parseItem);
        if (!this.parsesById.containsKey(hash)) {
            this.parsesById.put(hash, new ArrayList<ParseItem>());
        }
        this.parsesById.get(hash).add(parseItem);
    }

    public List<ParseItem> GetAll(Logger logger) {
        List<ParseItem> parses = new ArrayList<ParseItem>();
        for (List<ParseItem> parseList : this.parsesById.values()) {
            parses.addAll(parseList);
        }
        return parses;
    }
    public List<ParseItem> FindSimilar(Logger logger, ParseItem parseItem) {
        int hash = this.hash(logger, parseItem);
        if (!this.parsesById.containsKey(hash)) {
            return new ArrayList<>();
        }
        return this.parsesById.get(hash);
    }

    private int hash(Logger logger, ParseItem parseItem) {
        return this.hash(logger, parseItem, 5);
    }

    private int hash(Logger logger, ParseItem parseItem, int maxDepth) {
        int numChildren = parseItem.getChildCount();
        // If this node has exactly one child then it's likely an implementation detail of the parse that we're not interested in
        if (numChildren == 1) {
            return this.hash(logger, parseItem.getChild(0), maxDepth);
        }
        // hash the ruleType of this node
        int hash = parseItem.getRuleType().hashCode();
        if (maxDepth > 0) {
            // Hash the text of the child nodes. Note that this could be made more efficient
            for (int i = 0; i < parseItem.getChildCount(); i++) {
                int childHash = this.hash(logger, parseItem.getChild(i), maxDepth - 1);
                hash = hash * this.hashMultiplier + childHash;
            }
        }
        return hash;
    }



    private Map<Integer, List<ParseItem>> parsesById = new HashMap<>();
    private int hashMultiplier = (int)Math.floor(Math.pow(Integer.MAX_VALUE, 0.333));
}
