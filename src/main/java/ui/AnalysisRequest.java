package ui;

import java.util.ArrayList;
import java.util.List;

// embodies a request to scan some files and find duplication in them
public class AnalysisRequest {
    public List<String> pathsToScan = new ArrayList<>();
    public String grammarPath = null;
    public String parseStartRuleName = "compilationUnit";
}
