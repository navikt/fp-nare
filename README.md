![](https://github.com/navikt/fp-nare/workflows/Bygg%20og%20deploy/badge.svg) 
[![Sonarcloud Status](https://sonarcloud.io/api/project_badges/measure?project=navikt_fp-nare&metric=alert_status)](https://sonarcloud.io/dashboard?id=navikt_fp-nare) 
[![SonarCloud Coverage](https://sonarcloud.io/api/project_badges/measure?project=navikt_fp-nare&metric=coverage)](https://sonarcloud.io/component_measures/metric/coverage/list?id=navikt_fp-nare)
[![SonarCloud Bugs](https://sonarcloud.io/api/project_badges/measure?project=navikt_fp-nare&metric=bugs)](https://sonarcloud.io/component_measures/metric/reliability_rating/list?id=navikt_fp-nare)
[![SonarCloud Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=navikt_fp-nare&metric=vulnerabilities)](https://sonarcloud.io/component_measures/metric/security_rating/list?id=navikt_fp-nare)
![GitHub release (latest by date)](https://img.shields.io/github/v/release/navikt/fp-nare)
![GitHub](https://img.shields.io/github/license/navikt/fp-nare)
# NARE - Not A Rule Engine

NARE a variation of the Specification pattern, as described by Eric Evans & Martin Fowler here: https://martinfowler.com/apsupp/spec.pdf, adapter to handled more complex cases.  
More generally, it is an implementation of an [Expression Tree](https://en.wikipedia.org/wiki/Binary_expression_tree), but one which can support both unary, binary and ternary nodes expressions.


# Purpose
To model decision trees so they can be visualised, documented, and traced (inputs, outputs, evaluation), for legal and regulatory review. Both the specification of a rule as well as its execution can be documented.

# Features
* Unary expressions:
  - Sequence
  - Not
  - Node:  (node computation supplied by implementor) Allows for arbitrary evaluations to be included.
* Binary expressions:
  - And
  - Or
* Ternary expressions:
  - Conditional If/Else:  Allows for evaluating only specific subtrees based on a provided condition.

# Rule documentation
The rule specification may be output to Asciidoc using the supplied Javadoc Doclet.  This will output names of parameters and rules to a single asciidoc document, while the trees will be output to a json document as a set of nodes and edges.  The output format in json is the same for both the specification and evaluation trees (except a few evaluation specific parameters), making both suitable for long term storage.

The json output for a graph is fairly easy to graph out using any appropriate graphing library (Vue, D3, etc.) that supports a node/edge layout.

# Examples
See tests for examples 
* Usage example (running a rule): [Rule evaluation example (modrekvote)](https://github.com/navikt/fp-nare/blob/master/core/src/test/java/no/nav/fpsak/nare/specification/modrekvote/ModrekvoteTest.java)
* Specification example : [Rule specification example (modrekvote)](https://github.com/navikt/fp-nare/blob/master/core/src/test/java/no/nav/fpsak/nare/specification/modrekvote/Modrekvote.java)
* Generating documentation example (here using a unit test, can be adapted to whatever): [Doclet example](https://github.com/navikt/fp-nare/blob/master/core/src/test/java/no/nav/fpsak/nare/doc/doclet/RegelmodellDocletTest.java)
# Contact 
* **External**: Raise an issue here on GitHub
* **Internal**: Slack channel #teamforeldrepenger.
