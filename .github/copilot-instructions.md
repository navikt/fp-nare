# fp-nare — Not-A-Rule-Engine

Specification-pattern / expression-tree library for business rules.
Used by fp-uttak, fp-inngangsvilkar, fp-ytelse-beregn, fp-stonadskonto,
ft-beregning and svp-uttak to encode Folketrygdloven kap. 14 logic.

## Context

- [fp-context](https://github.com/navikt/fp-context) — team domain,
  architecture, conventions. Source of truth.
- Consumer view:
  [`architecture/team-libraries.md`](https://github.com/navikt/fp-context/blob/main/architecture/team-libraries.md).
- Copilot Space: navikt / **TeamForeldrepenger**.

## Concept

Rules are modelled as expression trees. Both the **specification** (what
the rule is) and the **evaluation trace** (what happened on inputs) can be
serialised to JSON and persisted for legal/regulatory review.

The key concepts are Spesikasjon (rule specification ) and Evaluering (evaluation with outcomes and calculations).

Reference: Eric Evans & Martin Fowler's Specification pattern
(https://martinfowler.com/apsupp/spec.pdf), extended to support n-ary nodes.

## Expression types

| Arity     | Expressions                                          |
|-----------|------------------------------------------------------|
| Unary     | Sequence, Foreach, Not, Node (arbitrary computation) |
| Binary    | And, Or                                              |
| Ternary   | If/Else, If/Then/Else                                |
| Sequences | N steps evaluated                                    |
| N-ary     | Conditional If/Or/Else (lazy subtree evaluation)     |

## Outputs

| Output | Format | Use |
|--------|--------|-----|
| Rule specification tree | JSON (nodes + edges) | Documentation, visualisation |
| Evaluation trace | JSON (same shape + eval results) | Stored with vedtak, legal review |
| Rule docs | AsciiDoc via Javadoc Doclet | Human-readable rule book |

Same JSON DiGraph shape for spec and trace → both renderable with any node/edge
graph library (Vue, D3, etc.).

## When changing this repo

- Public expression API is consumed by multiple business-rule repos —
  breaking changes need major bump and consumer coordination
- JSON output schema is **persisted** in consumer apps — schema-breaking
  changes require migration strategy
- Keep evaluation deterministic and side-effect-free; consumers replay traces

## Release

SemVer; consumed via fp-bom + Dependabot.

## Tech

Java 25, Maven, minimal runtime deps. See `src/test/` for usage examples.
