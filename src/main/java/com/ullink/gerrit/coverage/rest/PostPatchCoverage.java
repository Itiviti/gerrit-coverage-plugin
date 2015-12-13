package com.ullink.gerrit.coverage.rest;

import com.google.gerrit.extensions.restapi.*;
import com.google.gerrit.server.change.RevisionResource;
import com.google.gwtorm.server.OrmException;
import com.google.inject.Inject;
import com.ullink.gerrit.coverage.db.CoverageDb;
import com.ullink.gerrit.coverage.PatchCoverageInput;

import java.io.IOException;

public class PostPatchCoverage implements RestModifyView<RevisionResource, PatchCoverageInput> {
    private final CoverageDb coverageDb;

    @Inject
    PostPatchCoverage(CoverageDb coverageDb) {
        this.coverageDb = coverageDb;
    }

    @Override
    public Void apply(RevisionResource revision, PatchCoverageInput input)
            throws AuthException, BadRequestException, ResourceConflictException,
            UnprocessableEntityException, OrmException, IOException {
        coverageDb.registerCoverage(revision, input);
        return null;
    }
}
