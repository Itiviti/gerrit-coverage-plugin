package com.ullink.gerrit.coverage;

import com.google.gerrit.extensions.restapi.RestApiModule;
import com.google.inject.AbstractModule;
import com.ullink.gerrit.coverage.rest.GetFileCoverage;
import com.ullink.gerrit.coverage.rest.PostPatchCoverage;

import static com.google.gerrit.server.change.FileResource.FILE_KIND;
import static com.google.gerrit.server.change.RevisionResource.REVISION_KIND;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        install(new RestApiModule() {
            @Override
            protected void configure() {
                get(FILE_KIND, "coverage").to(GetFileCoverage.class);
                post(REVISION_KIND, "coverage").to(PostPatchCoverage.class);
            }
        });
    }
}
