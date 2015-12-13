package com.ullink.gerrit.coverage.rest;

import com.google.gerrit.extensions.restapi.ResourceConflictException;
import com.google.gerrit.extensions.restapi.RestReadView;
import com.google.gerrit.server.change.FileResource;
import com.google.inject.Inject;
import com.ullink.gerrit.coverage.db.CoverageDb;
import com.ullink.gerrit.coverage.FileCoverageInfo;

import java.io.IOException;

public class GetFileCoverage implements RestReadView<FileResource>
{
    private final CoverageDb coverageDb;

    @Inject
    public GetFileCoverage(CoverageDb coverageDb) {
        this.coverageDb = coverageDb;
    }

    @Override
    public FileCoverageInfo apply(FileResource resource)
            throws ResourceConflictException, IOException
    {
        return coverageDb.getCoverage(resource);
    }
}
