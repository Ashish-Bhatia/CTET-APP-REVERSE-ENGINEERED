# CTET analysis — continuation checkpoint

Current phase: **47**, completed 28 October 2026.

## Source

- APK package `com.edurev.ctet`, observed version `6.1.6_ctet`.
- SHA-256 `2c82aa916519476aef3dc0c70a509baafd9d33d59e05c8cfdd4e18d425034b4c`.
- Repository: `Ashish-Bhatia/CTET-APP-REVERSE-ENGINEERED`, branch `main`.

## Evidence ledger

- Phases 32–37: local getter-to-TextView paths: `i2.G()` (`totalScore`) → `a7.P.f` → `tvMarks`; `i2.x()` (`percentile`) → `a7.y0` → `tvPercentile`. Bounded by modeled DEX control flow.
- Phases 38–43: `i2.a()` (`accuracy`) formatted with `%`; `i2.y()` (`rankinAll`) and `i2.s()` (`maxRank`) concatenated with `/` and styled for rank.
- Phases 44–46: `a7.q0` candidate `tvAccuracy`, `a7.A0` candidate `tvRank`; one final reaching assignment for constructor registers `v72`, `v82`, but full transitive and exception coverage incomplete.
- Phase 47: immediate `sget`→lookup→final move windows rechecked at 2463–2469 and 2713–2719. Getter invocation counts in `p9.success(i2)`: correct `g` 7, incorrect `q` 13, section data `B` 4. Consumers not yet established.

## Exact Phase 48 task

Implement exception-aware transitive reaching definitions through `move-result-object` and ViewBinding lookups; independently establish consumers for `i2.g()` and `i2.q()` and trace `i2.B()` into the section RecyclerView adapter. Update this checkpoint and phase report on `main` after validation.

## Guardrails

Never publish APK, proprietary assets/questions, credentials, raw extracted dumps or unsupported claims. Do not introduce custom GitHub Actions workflows.
