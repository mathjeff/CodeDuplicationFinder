
package logging;

import logging.HierarchicalLogger;

public class PrintLogger extends HierarchicalLogger {
    protected void log(String message) {
        System.out.println(message);
    }
    public HierarchicalLogger copy() {
        return new PrintLogger();
    }
}
