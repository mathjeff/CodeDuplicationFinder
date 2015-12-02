package parsing;

import org.antlr.v4.runtime.tree.ParseTree;

import java.util.ArrayList;
import java.util.Collection;

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
    public Collection<ParseItem> getChildren() {
        return this.children;
    }
    public ParseItem getChild(int index) {
        return this.children.get(index);
    }
    public void addChild(ParseItem child) {
        this.children.add(child);
        this.num_nonLeaf_children += child.num_nonLeaf_children;
        if (child.children.size() > 1) {
            this.num_nonLeaf_children++;
        }
    }

    public int get_num_nonLeaf_children() {
        return this.num_nonLeaf_children;
    }



    int num_nonLeaf_children = 0;
    String sourceDescription; // file name
    String text; // text of the node
    String ruleType; // name of the rule that matches this node
    ArrayList<ParseItem> children = new ArrayList<>();
}
