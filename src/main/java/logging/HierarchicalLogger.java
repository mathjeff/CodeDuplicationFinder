package logging;
import java.util.ArrayList;
import java.util.List;
import logging.Logger;

abstract class HierarchicalLogger implements Logger {

    HierarchicalLogger parent;
    String description;
    String placeholder = "  ";

    protected abstract void log(String message);

    public void message(String message) {
        String prefix = this.determineMessagePrefix();
        this.log(prefix + message);
    }

    protected String determineMessagePrefix() {
        String prefix = "";
        if (this.parent != null) {
            prefix = this.parent.determineMessagePrefix();
            prefix += this.parent.placeholder;
        }
        return prefix;
    }

    public Logger push(String description) {
        HierarchicalLogger other = null;
        try {
            other =(HierarchicalLogger)this.clone();
        } catch (Exception e) {
            other = null;
        }
        other.parent = this;
        return other;
    }
}