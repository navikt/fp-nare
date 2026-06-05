# fp-nare (Not A Rule Engine)

Specification-pattern library for business rules used across the foreldrepenger ecosystem.

## Shared context

- Source of truth for shared domain, architecture, and conventions: `navikt/fp-context`
- Copilot Space: `navikt/TeamForeldrepenger`
- Consumer view of team libraries: `fp-context/architecture/team-libraries.md`

## Concept

Reference: Eric Evans and Martin Fowler's Specification pattern, extended here to support n-ary nodes.
Rules are modeled as expression trees. Both the specification and the evaluation trace can be serialized to JSON and persisted for legal and regulatory review.

Key concepts:
- `Spesifikasjon` - the rule definition
- `Evaluering` - the evaluation with outcomes and calculations

## Expression types

| Arity | Expressions |
|---|---|
| Unary| Sequence, Not, Node |
| Binary | And, Or |
| Ternary | If/Else, If/Then/Else |
| Sequences | N evaluated steps |
| N-ary | Conditional If/Or/Else with lazy subtree evaluation |

## Outputs

| Output | Format | Use |
|--------|--------|-----|
| Rule specification tree | JSON | Documentation and visualization  |
| Evaluation trace | JSON | Documentation, reviews and visualization |
| Rule docs | AsciiDoc via Javadoc Doclet | Human-readable rule book |

The same JSON graph shape is used for both specification and trace, so both can be rendered by generic node/edge graph tooling.

## When changing this repo

- Public expression APIs are consumed by several business-rule repos.
- JSON output schema is persisted in consumer applications and must not change lightly.
- Evaluation must stay deterministic and side-effect-free because consumers replay traces.

## Release and use

SemVer release; version not included in `fp-bom`.
Used directly by `fp-uttak`, `fp-inngangsvilkar`, `fp-ytelse-beregn`, `fp-stonadskonto`, `ft-beregning`, and `svp-uttak` to encode Folketrygdloven kap. 14 logic.
