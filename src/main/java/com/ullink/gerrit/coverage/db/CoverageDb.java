package com.ullink.gerrit.coverage.db;

import com.google.gerrit.extensions.annotations.PluginData;
import com.google.gerrit.reviewdb.client.Patch;
import com.google.gerrit.reviewdb.client.PatchSet;
import com.google.gerrit.server.change.FileResource;
import com.google.gerrit.server.change.RevisionResource;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.ullink.gerrit.coverage.FileCoverageInfo;
import com.ullink.gerrit.coverage.PatchCoverageInput;
import com.ullink.gerrit.coverage.FileCoverageInput;
import org.mapdb.DB;
import org.mapdb.DBMaker;

import java.io.File;
import java.util.Collections;
import java.util.Map;

@Singleton
public class CoverageDb {
    private final Map<Patch.Key, Map<Integer, Integer>> hits;
    private final Map<Patch.Key, Map<Integer, Integer>> conditions;
    private final Map<Patch.Key, Map<Integer, Integer>> coveredConditions;
    private final DB db;

    @Inject
    public CoverageDb(@PluginData File dataDir) {
        db = DBMaker.newFileDB(new File(dataDir, "coverage.db"))
                .closeOnJvmShutdown()
                .make();
        hits = db.createHashMap("hits").makeOrGet();
        conditions = db.createHashMap("conditions").makeOrGet();
        coveredConditions = db.createHashMap("coveredConditions").makeOrGet();
    }

    public void registerCoverage(RevisionResource revision, PatchCoverageInput input) {
        PatchSet.Id patchSetId = revision.getPatchSet().getId();
        for (Map.Entry<String, FileCoverageInput> coverageEntry : input.coverage.entrySet()) {
            Patch.Key patchKey = new Patch.Key(patchSetId, coverageEntry.getKey());
            FileCoverageInput coverage = coverageEntry.getValue();
            hits.put(patchKey, coverage.hits);
            if (!coverage.conditions.isEmpty()) {
                conditions.put(patchKey, coverage.conditions);
            }
            if (!coverage.coveredConditions.isEmpty()) {
                coveredConditions.put(patchKey, coverage.coveredConditions);
            }
        }
        db.commit();
    }

    public FileCoverageInfo getCoverage(FileResource resource) {
        FileCoverageInfo fileCoverageInfo = new FileCoverageInfo();
        fileCoverageInfo.hits = getOrEmpty(hits, resource);
        fileCoverageInfo.conditions = getOrEmpty(conditions, resource);
        fileCoverageInfo.coveredConditions = getOrEmpty(coveredConditions, resource);
        return fileCoverageInfo;
    }

    private Map<Integer, Integer> getOrEmpty(Map<Patch.Key, Map<Integer, Integer>> map, FileResource resource) {
        Map<Integer, Integer> coverage = map.get(resource.getPatchKey());
        if (coverage == null) {
            return Collections.emptyMap();
        }
        return coverage;
    }
}