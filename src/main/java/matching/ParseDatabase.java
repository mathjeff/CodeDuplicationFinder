package matching;

import logging.Logger;
import parsing.ParseItem;

import java.util.*;

// holds ParseItems and finds similar ones
public class ParseDatabase {

    public void putParse(Logger logger, ParseItem parseItem) {
        Integer hash = this.hash(logger, parseItem);
        if (!this.parsesByHash.containsKey(hash)) {
            this.parsesByHash.put(hash, new ArrayList<>());
        }
        List<ParseItem> matches = this.parsesByHash.get(hash);
        if (matches.size() > 0) {
            Integer otherHash = this.hash(logger, matches.get(0));
            if (!hash.equals(otherHash) || !(hash.hashCode() == otherHash.hashCode())) {
                throw new InternalError("New item " + parseItem.getText() + " hashes to " + hash +
                        " was matched with existing item " + matches.get(0) + " which hashes to " + otherHash);
            }
        }
        this.parsesByHash.get(hash).add(parseItem);
    }

    public List<ParseItem> getAll(Logger logger) {
        List<ParseItem> parses = new ArrayList<>();
        for (List<ParseItem> parseList : this.parsesByHash.values()) {
            parses.addAll(parseList);
        }
        return parses;
    }
    public List<ParseItem> findSimilar(Logger logger, ParseItem parseItem) {
        int hash = this.hash(logger, parseItem);
        if (!this.parsesByHash.containsKey(hash)) {
            return new ArrayList<>();
        }
        return this.parsesByHash.get(hash);
    }
    public Collection<List<ParseItem>> getMatchGroups(Logger logger) {
        return this.parsesByHash.values();
    }

    private int hash(Logger logger, ParseItem parseItem) {
        return this.hash(logger.push("hash").push("hash"), parseItem, 6);
    }

    private int hash(Logger logger, ParseItem parseItem, int maxDepth) {
        //logger.message("Hashing " + parseItem.getText() + " (" + parseItem.getRuleType() + ")");
        int numChildren = parseItem.getChildCount();
        int hash = parseItem.getRuleType().hashCode();
        //logger.message("Using starting node hash = " + hash + " for '" + parseItem.getText() + "'");
        if (numChildren == 1) {
            ParseItem child = parseItem.getChild(0);
            // this node is probably just an implementation detail that we're not interested in
            //hash = this.hash(logger.push("hash only child"), parseItem.getChild(0));
            hash = this.hash(logger, parseItem.getChild(0), maxDepth);
        } else if (numChildren == 0) {
            hash = 1;
        } else {
            // hash the ruleType of this node
            if (maxDepth > 0) {
                // Hash the text of the child nodes. Note that this could be made more efficient
                for (int i = 0; i < parseItem.getChildCount(); i++) {
                    int childHash = this.hash(logger.push("hash child"), parseItem.getChild(i), maxDepth - 1);
                    hash = hash * this.hashMultiplier + childHash;
                    //logger.message("updated hash to " + hash);
                }
            }
        }
        //logger.message("Hashed '" + parseItem.getText() + "' to " + hash);
        return hash;
    }



    private Map<Integer, List<ParseItem>> parsesByHash = new TreeMap<>();
    // MAX_VALUE^(sqrt(0.5))
    private int hashMultiplier = (int)Math.floor(Math.pow(Integer.MAX_VALUE, Math.pow(0.5, 0.5)));
}
