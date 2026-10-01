# Dog-Year Conversion — Portable Spec

Implementation-agnostic spec for converting a dog's age into a human-equivalent
age, adjusted for breed/size. Pair this with `dog-years-data.json` (breed list,
size-class defaults, life-stage chart tables). Everything below is plain math
and table lookups — no runtime dependencies — so it ports directly to Kotlin,
Python, Swift, a shell/CLI tool, etc.

## 1. Data model

```
SizeClass   := "small" | "medium" | "large" | "giant"

Breed := {
  name: string
  lifespanYears: number       // breed-specific average lifespan
  sizeClass: SizeClass
}

SizeClassInfo := {
  label: string
  weightRangeLb: [number, number|null]
  defaultLifespanYears: number     // used when no specific breed is matched
  lifeStageChart: {
    agePoints:  number[]           // dog-age x-axis, years, ascending, starts at 0
    humanYears: number[]           // same length as agePoints
  }
}
```

Load `dog-years-data.json` once; it contains `sizeClasses` (keyed by
`SizeClass`) and `breeds` (array of `Breed`).

## 2. Resolving lifespan + size class for an input

```
function resolve(breedNameOrNull, fallbackSizeClass):
    if breedNameOrNull is not empty:
        match = find breed in data.breeds where name.lowercase() == breedNameOrNull.trim().lowercase()
        if match found:
            return { lifespanYears: match.lifespanYears, sizeClass: match.sizeClass, label: match.name, matched: true }
    // no breed name given, or it didn't match anything in the dataset
    info = data.sizeClasses[fallbackSizeClass]
    return { lifespanYears: info.defaultLifespanYears, sizeClass: fallbackSizeClass, label: info.label + " breed", matched: false }
```

Always let the caller pick a `fallbackSizeClass` (small/medium/large/giant) as
a manual control — it's the graceful path when the breed isn't in the list or
is a mixed breed.

## 3. Method A — Breed-adjusted (epigenetic) formula

Source: Wang et al., *Cell Systems* 2020 — DNA-methylation drift in Labrador
Retrievers mapped against human aging, giving:

```
human_age = 16 × ln(dog_age) + 31          [as originally published, dog_age in "Labrador years"]
```

To generalize across breeds/sizes, rescale `dog_age` against the breed's own
expected lifespan versus the reference lifespan the formula was built on
(12 years, roughly a Labrador's average):

```
REFERENCE_LIFESPAN_YEARS = 12

function breedAdjustedHumanAge(dogAgeYears, breedLifespanYears):
    dogAgeYears = max(dogAgeYears, MIN_AGE_YEARS)     // see §5, guards ln(0)
    adjusted = dogAgeYears * (REFERENCE_LIFESPAN_YEARS / breedLifespanYears)
    result = 16 * ln(adjusted) + 31
    return max(0, result)                              // clamp — see §5
```

This is the headline number: it's the one that actually differs by breed
lifespan (a Great Dane and a Chihuahua at the same calendar age get different
results because their `breedLifespanYears` differ).

## 4. Method B — Size-chart interpolation (traditional reference point)

Veterinary/AVMA-style life-stage tables give human-equivalent age at fixed
checkpoints, per size class (see `lifeStageChart` in the JSON). Interpolate
linearly between checkpoints; extrapolate past the last checkpoint using the
slope of the final segment (giant breeds rarely have data past ~12-15 years).

```
function interpolate(agePoints, humanYears, x):
    n = length(agePoints)
    if x <= agePoints[0]: return humanYears[0]
    if x >= agePoints[n-1]:
        slope = (humanYears[n-1] - humanYears[n-2]) / (agePoints[n-1] - agePoints[n-2])
        return humanYears[n-1] + slope * (x - agePoints[n-1])
    for i in 0..n-2:
        if agePoints[i] <= x <= agePoints[i+1]:
            t = (x - agePoints[i]) / (agePoints[i+1] - agePoints[i])
            return humanYears[i] + t * (humanYears[i+1] - humanYears[i])

function sizeChartHumanAge(dogAgeYears, sizeClass):
    chart = data.sizeClasses[sizeClass].lifeStageChart
    return interpolate(chart.agePoints, chart.humanYears, dogAgeYears)
```

Use this as a secondary/comparison number, not the headline — it doesn't
account for a specific breed's own longevity, only its size bracket.

## 5. Edge cases & guards

- **Minimum age**: clamp `dogAgeYears` to `MIN_AGE_YEARS = 0.08` (~1 month)
  before calling Method A. `ln(x)` is undefined at `x <= 0`, and the formula
  is not validated for newborns anyway.
- **Negative result clamp**: Method A can mathematically go negative for very
  young + short-lived combinations (e.g. a 1-month-old giant breed). Clamp the
  final result to `0`.
- **Unmatched breed**: fall back to the size class's `defaultLifespanYears`
  (see §2) rather than failing — a user typing a mixed-breed or unlisted name
  should still get a reasonable estimate.
- **Units**: accept age in months by converting to years first
  (`years = months / 12`) — don't build a separate month-based formula.

## 6. Life-stage classification (optional, for UI labeling)

```
fraction = dogAgeYears / breedLifespanYears
stage = fraction < 0.15 ? "puppy"
      : fraction < 0.75 ? "adult"
      : "senior"
```

Thresholds are a simplification (roughly: first 15% of expected lifespan =
puppy, last 25% = senior) — reasonable for a UI badge, not a veterinary claim.

## 7. The "×7" myth (only for contrast/education)

```
mythHumanAge = dogAgeYears * 7
```

Include this only to show users *why* a breed-adjusted number differs — never
present it as accurate.

## 8. Suggested function signature (language-agnostic)

```
DogAgeResult := {
  breedAdjustedHumanAge: number   // §3 — headline
  sizeChartHumanAge: number       // §4 — secondary
  lifeStage: "puppy" | "adult" | "senior"
  resolvedBreedLabel: string
  resolvedSizeClass: SizeClass
  resolvedLifespanYears: number
}

function calculateDogAge(dogAgeYears: number, breedName: string?, fallbackSizeClass: SizeClass) -> DogAgeResult
```

### Kotlin/Android port notes
- Bundle `dog-years-data.json` as a raw/assets resource; parse once at startup
  (kotlinx.serialization or Moshi) into `List<Breed>` + `Map<SizeClass, SizeClassInfo>`.
  Follow this project's existing `:data-models` conventions if this lands in
  the android3 codebase — plain `data class` for `Breed`/`SizeClassInfo`, no
  gRPC/network layer needed since this is static reference data.
- `calculateDogAge(...)` is a pure function — put it in a small
  `DogAgeCalculator` object/class with no Android framework dependency, so
  it's trivially unit-testable (MockK/Truth per this repo's conventions
  aren't even needed — it's pure math, plain JUnit `assertEquals` suffices).

### CLI port notes
- Ship `dog-years-data.json` alongside the binary/script; load once at
  startup.
- A minimal CLI: `dogyears --breed "Labrador Retriever" --age 3` →
  resolves via §2, runs §3/§4, prints the `DogAgeResult`.
- No I/O beyond reading the JSON file — same pure function as above.

## 9. Worked example (for verifying a new port)

Input: `breedName = "Great Dane"`, `dogAgeYears = 3`

- Resolved: `lifespanYears = 8`, `sizeClass = "giant"`
- Method A: `adjusted = 3 × (12/8) = 4.5` → `16 × ln(4.5) + 31 ≈ 55.1`
- Method B: interpolate giant chart at `x=3` → exact checkpoint → `31`
- Myth: `3 × 7 = 21`
- Life stage: `3/8 = 0.375` → `"adult"`

If a new implementation doesn't reproduce `55.1` / `31` / `21` / `"adult"` for
this input, something in the port is off.
