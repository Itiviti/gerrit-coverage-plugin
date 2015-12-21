[![Build Status](https://img.shields.io/travis/Ullink/gerrit-coverage-plugin.svg?label=build linux)](https://travis-ci.org/Ullink/gerrit-coverage-plugin) [![GitHub license](https://img.shields.io/github/license/Ullink/gerrit-coverage-plugin.svg)](http://www.apache.org/licenses/LICENSE-2.0) 

# gerrit-coverage-plugin
A Gerrit plugin for making coverage information available at review-time.

The ultimate goal is to be able to add coverage information in the gutter
of change diff screen.

## Requirements
It is currently compiled against Gerrit APIs 2.11.5. The information is not
visible yet, only REST endpoints have been added to be able to:
- upload coverage for the files of a patchset
- get coverage for a file in a patchset

A fork of gerrit 2.11 will be necessary to be able to use it.
On a longer term, it would be nice to be able to add the necessary
extension points in Gerrit so that the plugin can customize the diff screen.

And here's a teaser for how it's going to look:

![Preview](https://github.com/Ullink/gerrit-coverage-plugin/raw/master/img/coverage_screen1.png)

### Wait, what ? I *want* that now

Glad you're asking, it's not yet production ready, but maybe you can help !
Here's the status/TODO:

- Issue tracking code coverage support in gerrit is [#3538](https://code.google.com/p/gerrit/issues/detail?id=3538)
- Implementation details are being discussed [here](https://groups.google.com/forum/#!topic/repo-discuss/qQyEkofDFHM), jump in !
- Current direction seems to be to store the coverage data in Gerrit server
- Gerrit UI will be modified to display the data
 
### Thanks but really, I want it *now*

Although that's what we're currently running ourselves (and how we got the screenshot above), it's only a POC right now, and you should probably not use it, but here is how we got it running:

- A gerrit 2.11.5 fork [here](https://github.com/muryoh/gerrit/tree/v2.11.5-coverage) uses this plugin data to display the gutter
- This very plugin stores the code coverage (in a MapDB file), and exposes REST endpoints to upload it
- A sonar-gerrit-plugin fork [here](https://github.com/muryoh/sonar-gerrit-plugin/tree/v2.2.2.1-coverage) now also uploads the code coverage when running sonar analysis on a patchset

## Example of the 2 REST endpoints added
### Upload

```
curl --digest -i --user admin:admin -d@coverage.txt -H "Content-Type: application/json" -X POST "http://localhost:8080/a/changes/some-project~master~CHANGEID/revisions/REVISION_SHA1/coverage"
```

where coverage.txt contains
```
{
    "coverage": {
        "src/main/java/SomeClass.java":{
            hits: {
                0:2,
                1:1,
                3:1,
                2:3,
                4:10
            },
            conditions: {
                0:5,
                1:3
            },
            covered_conditions: {
                0:2,
                1:2
            }
        }
    }
}
```

Multiple files may be specified. hits, conditions and covered_conditions are maps that associate a line
number to the coverage info, namely the *number of hits*, the *number of conditions* and the *number of covered conditions*.

### Getting the coverage
```
curl --digest -i --user admin:admin -X GET "http://localhost:8080/a/changes/some-project~master~CHANGEID/revisions/REVISION_SHA1/files/src%2Fmain%2Fjava%2FSomeClass.java/coverage"
```

which currently would result in
```
{
  "hits": {
    "0": 2,
    "1": 1,
    "3": 1,
    "2": 3,
    "4": 10
  },
  "conditions": {
    "0": 5,
    "1": 3
  },
  "covered_conditions": {
    "0": 2,
    "1": 2
  }
}
```
