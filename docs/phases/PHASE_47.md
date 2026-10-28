# Phase 47 — Local factory verification and result-getter inventory

**Date:** 2026-10-28. **APK SHA-256:** `2c82aa916519476aef3dc0c70a509baafd9d33d59e05c8cfdd4e18d425034b4c`.

## Reproduced local DEX sequences

| Widget | Resource field load | ViewBinding lookup | Final register move | Local check |
|---|---:|---:|---|---|
| Accuracy `tvAccuracy` | 2463 | 2465 | `move-object/from16 v72, v3` at 2469 | Pass |
| Rank `tvRank` | 2713 | 2715 | `move-object/from16 v82, v3` at 2719 | Pass |

The main binding constructor call occurs at offset **2947**. These checks reproduce immediate resource lookup sequences, consistent with Phase 46's unique final reaching definitions. They do **not** establish complete transitive provenance on all exceptional and ordinary paths.

## Getter inventory in `p9.success(i2)`

| Getter | Model field | Static calls |
|---|---|---:|
| `i2.g()` | `correct` | 7 |
| `i2.q()` | `incorrect` | 13 |
| `i2.B()` | `sectionRelatedData` | 4 |

Counts are getter invocations, **not** proven text/chart assignments. Twelve decoded instructions after each invocation are retained in the local Phase 47 evidence JSON. No APK bytes or extracted assets are published.

## Limits and Phase 48

No full exception-aware transitive fixed-point analysis was implemented in this phase. Exact answer-count widgets, section adapter transformations and backend scoring formulas remain unresolved.

**Next:** Implement instruction-accurate transitive reaching definitions with exceptional edges, trace correct/incorrect getter values into their actual consumers, and `sectionRelatedData` into the RecyclerView adapter. Publish only verified results.
