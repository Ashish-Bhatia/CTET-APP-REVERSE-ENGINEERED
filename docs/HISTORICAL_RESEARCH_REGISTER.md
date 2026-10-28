# Historical research register — Phases 1–46

This register preserves the **known findings** from earlier analysis. It is a historical summary, **not** a substitute for the complete original phase scripts and evidence files, which have not all been migrated.

## Source identity
- Package: `com.edurev.ctet`
- Observed version: `6.1.6_ctet`
- APK SHA-256: `2c82aa916519476aef3dc0c70a509baafd9d33d59e05c8cfdd4e18d425034b4c`
- Four DEX files, 5,971 ZIP entries, 659 assets, 5,037 resources (earlier inventory).

## Phase chronology

| Phase(s) | Established focus | Limits |
|---|---|---|
| 1–6 | APK manifest, resources, DEX inventory, Retrofit endpoint and annotation inspection; 358 annotated API methods | No live server access or server implementation verified |
| 7 | StartQuiz, session, question and result DTO field inventories | Names and types derived from local DEX |
| 8–19 | Callback graph, quiz-start branches, payment and test navigation, result transition, StartQuiz getter mapping | Obfuscated control flow and runtime branch outcomes not fully reproduced |
| 20–23 | `QuizScoreFragment.q0()` calls `getTestResult`; `p9.success(i2)` result callback; result getter map | Backend scoring algorithm unknown |
| 24–25 | Result callback text/visibility call-site inventory; generated `a7` ViewBinding, 88 fields and nested bindings | Widget mappings initially provisional |
| 26–29 | `resources.arsc` ID decoding, factory lookup inventory, binary XML validation of score widgets | XML declaration alone does not prove runtime data flow |
| 30–31 | Constructor parameter audit, nearby getter and binding references around `setText` | Proximity alone insufficient |
| 32–37 | Local getter-to-binding traces and control-flow checks: totalScore → nested `rc.f` → `tvMarks`; percentile → `a7.y0` → `tvPercentile` | Path analysis bounded by modeled opcodes and exception edges |
| 38–43 | Accuracy string formatting and rank display `rankinAll/maxRank`; corrected false `v6` conflict due to register decoder bug | Runtime value validation and universal path proof incomplete |
| 44–46 | Accuracy `a7.q0` candidate `tvAccuracy`; rank `a7.A0` candidate `tvRank`; factory constructor reaching definitions | Full transitive and exception-aware provenance incomplete |

## Specific model getters
- `i2.G()` = `totalScore`
- `i2.x()` = `percentile`
- `i2.a()` = `accuracy`
- `i2.y()` = `rankinAll`
- `i2.s()` = `maxRank`
- `i2.g()` = `correct`
- `i2.q()` = `incorrect`
- `i2.B()` = `sectionRelatedData`

## Source files in the working session

The local workspace has named artifacts such as `ctet_phase10_all_calls.json`, `ctet_phase21_trace.py`, `ctet_phase26_trace.py`, `ctet_phase32_trace.py`, `ctet_phase34_audit.py`, `ctet_phase46_audit.py`, and phase-specific `findings.md` / evidence JSON. This register **does not claim** these files have been committed.

## Publication constraints

Do not publish the original APK, proprietary bundled assets, questions, authentication tokens or extracted secrets. Review original analysis outputs before publishing; preserve detailed private evidence in authorized private storage. No custom GitHub Actions workflow.

## Migration gate before Phase 48

1. Inventory every local research file and record checksum/size.
2. Classify files as safe public original research, needs redaction, or third-party/proprietary.
3. Commit reviewed original research files, scripts, and findings from earliest available phase onward.
4. Verify repository contents and update `docs/CONTINUITY.md`.
5. Only then execute Phase 48.
