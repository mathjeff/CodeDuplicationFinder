package parsing;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;

public class ParseItem {
    public ParseItem(String text, String ruleType, String sourceDescription) {
        this.text = text;
        this.ruleType = ruleType;
        this.sourceDescription = sourceDescription;
    }
    public String getSourceDescription() {
        return this.sourceDescription;
    }
    public String getText() {
        return this.text;
    }
    public String getRuleType() {
        return this.ruleType;
    }

    public int getChildCount() {
        return this.children.size();
    }
    public ParseItem getChild(int index) {
        return this.children.get(index);
    }
    public void addChild(ParseItem child) {
        this.children.add(child);
    }



    String sourceDescription; // file name
    public String text; // text of the node
    public String ruleType; // name of the rule that matches this node
    ArrayList<ParseItem> children = new ArrayList<>();
}
