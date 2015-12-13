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
