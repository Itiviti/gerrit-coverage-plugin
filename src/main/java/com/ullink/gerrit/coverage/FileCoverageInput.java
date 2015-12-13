package com.ullink.gerrit.coverage;

import java.util.Collections;
import java.util.Map;

public class FileCoverageInput {
    public Map<Integer, Integer> hits = Collections.emptyMap();
    public Map<Integer, Integer> conditions = Collections.emptyMap();
    public Map<Integer, Integer> coveredConditions = Collections.emptyMap();
}
