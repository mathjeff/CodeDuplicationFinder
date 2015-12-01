package logging;

import java.util.ArrayList;
import java.util.List;

public abstract class HierarchicalLogger implements Logger {

    HierarchicalLogger parent;
    String description;
    String placeholder = "  ";
    String messagePrefix;
    int maxDepthToLog = -1;
    int depth = 0;
    int numSkippedMessages = 0;
    String latestSkippedMessage = null;
    HierarchicalLogger root = this;
    String skippedPrefix = null;



    protected abstract void log(String message);

    public void message(String message) {
        String transformedMessage = this.getMessagePrefix() + message;
        if (this.isImportant()) {
            this.root.summarizeSkippedMessages();
            this.log(transformedMessage);
        } else {
            this.root.onSkippedMessage(transformedMessage);
        }
    }

    private void onSkippedMessage(String message) {
        this.numSkippedMessages++;
        this.latestSkippedMessage = message;
    }

    public void summarizeSkippedMessages() {
        if (this.numSkippedMessages > 0) {
            if (this.numSkippedMessages == 1) {
                this.log(this.latestSkippedMessage);
            } else {
                this.log(this.skippedPrefix + "Skipped logging " + this.numSkippedMessages + " messages");
            }
        }
        this.numSkippedMessages = 0;
        this.latestSkippedMessage = null;
    }

    public void setMaxDepthToLog(int depth) {
        this.maxDepthToLog = depth;
        String skipPrefix = "";
        for (int i = 0; i < this.maxDepthToLog + 1; i++) {
            skipPrefix += this.placeholder;
        }
        this.skippedPrefix = skipPrefix;
    }

    // Tells whether messages from this logger are interesting enough to log
    protected boolean isImportant() {
        return (this.depth <= this.maxDepthToLog);
    }

    protected String getMessagePrefix() {
        if (this.messagePrefix == null) {
            String prefix = "";
            if (this.parent != null) {
                prefix = this.parent.getMessagePrefix();
                prefix += this.parent.placeholder;
            }
            this.messagePrefix = prefix;
        }
        return this.messagePrefix;
    }

    public abstract HierarchicalLogger copy();

    public Logger push(String description) {
        HierarchicalLogger other = null;
        try {
            other = this.copy();
        } catch (Exception e) {
            other = null;
        }
        other.messagePrefix = this.messagePrefix + this.placeholder;
        other.depth = this.depth + 1;
        other.maxDepthToLog = this.maxDepthToLog;
        other.parent = this;
        other.root = this.root;
        other.description = description;
        return other;
    }
}