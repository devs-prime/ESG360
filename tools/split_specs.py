#!/usr/bin/env python3
"""Split ESG360 master FDD/SDD/TDD document into Claude Code-ready spec files."""
import os, re, shutil

SRC = "full_doc.md"
OUT = "esg360-project"

with open(SRC) as f:
    lines = f.readlines()

# ---- 1. Parse into top-level sections ----
sections = []  # (title, [lines])
current_title, current = None, []
for ln in lines:
    m = re.match(r"^# (.+)$", ln)
    if m:
        if current_title is not None:
            sections.append((current_title, current))
        current_title, current = m.group(1).strip(), []
    else:
        current.append(ln)
if current_title is not None:
    sections.append((current_title, current))

def find(prefix):
    for t, body in sections:
        if t.startswith(prefix):
            return t, body
    return None, None

def numbered(n):
    return find(f"{n}. ")

# ---- 2. Directory layout ----
if os.path.exists(OUT):
    shutil.rmtree(OUT)
for d in ["specs/modules", "specs/architecture", "specs/technical", "specs/testing", "specs/delivery"]:
    os.makedirs(os.path.join(OUT, d))

# ---- 3. Module definitions: (section_no, slug, release, test_modules) ----
MODULES = [
    (2,  "tenant-organisation-boundary",      "R0", ["Tenant", "Organisation"]),
    (3,  "identity-access-sod",               "R0", ["Security"]),
    (4,  "metric-datapoint-catalogue",        "R0", ["Catalogue"]),
    (5,  "reporting-cycles-collection",       "R1", ["Collection"]),
    (6,  "evidence-lineage-assurance",        "R0 (core) / R2 (assurance rooms)", ["Evidence"]),
    (7,  "emission-factor-library",           "R1", ["Factor"]),
    (8,  "ghg-inventory-scope1-2",            "R1", ["Carbon"]),
    (9,  "scope3-value-chain",                "R1 (core)", ["Scope 3"]),
    (10, "energy-water-waste-environment",    "R1", ["Environment"]),
    (11, "social-human-capital",              "R3", ["Social"]),
    (12, "governance-ethics-risk-compliance", "R3", ["Governance"]),
    (13, "materiality-stakeholder",           "R3", ["Materiality"]),
    (14, "targets-transition-initiatives",    "R4", ["Targets"]),
    (15, "climate-nature-risk-scenarios",     "R4", ["Climate Risk"]),
    (16, "supplier-esg-due-diligence",        "R3", ["Supplier"]),
    (17, "carbon-credits-instruments",        "R2 (indicative)", ["Instruments"]),
    (18, "disclosure-framework-packs",        "R1 (basic) / R2 (full)", ["Disclosure"]),
    (19, "analytics-dashboards-benchmarking", "R1 (basic) / R4 (advanced)", []),
    (20, "ai-copilots-automation",            "R4", ["AI"]),
    (21, "workflow-notifications-cases",      "R0", []),
    (22, "integration-data-exchange",         "R0 (framework) / per-release adapters", ["Integration"]),
    (23, "subscription-configuration-admin",  "R0", ["Administration"]),
    (24, "cross-functional-business-rules",   "ALL — applies to every module", []),
    (25, "functional-reports-outputs",        "R2 onwards", []),
]

# ---- 4. Parse master test catalogue (section 48) into module buckets ----
_, tc_body = numbered(48)
tc_rows = {}   # module_name -> list of row strings
tc_header = None
for ln in tc_body:
    if ln.startswith("| **Test ID**"):
        tc_header = ln.rstrip("\n")
        continue
    m = re.match(r"^\| ([A-Z0-9]+-\d+)\s*\|", ln)
    if m:
        cols = [c.strip() for c in ln.strip().strip("|").split("|")]
        if len(cols) >= 3:
            tc_rows.setdefault(cols[2], []).append(ln.rstrip("\n"))
SEP = "|" + "|".join(["---"] * 8) + "|"

def tests_for(names):
    rows = []
    for n in names:
        rows.extend(tc_rows.get(n, []))
    return rows

# ---- 5. Write module spec files ----
for n, slug, release, tmods in MODULES:
    title, body = numbered(n)
    path = os.path.join(OUT, "specs/modules", f"{n:02d}-{slug}.md")
    with open(path, "w") as f:
        f.write(f"# {title}\n\n")
        f.write(f"> **Source:** PSSTEC ESG360 FDD v1.0, section {n}. "
                f"**Indicative release:** {release}.\n"
                f"> Read `CLAUDE.md` (root) before implementing. "
                f"Cross-functional rules BR-001–BR-020 (spec 24) apply to this module.\n\n")
        f.writelines(body)
        rows = tests_for(tmods)
        if rows:
            f.write("\n\n## Acceptance test cases (from Master Test Catalogue, spec 48)\n\n")
            f.write("Implementation of this module is not complete until these pass.\n\n")
            f.write(tc_header + "\n" + SEP + "\n")
            f.write("\n".join(rows) + "\n")

# ---- 6. Overview file (front matter + section 1) ----
front_parts = []
for prefix in ["Purpose and Scope", "Normative and Reference Frameworks",
               "Product Vision and Business Outcomes", "Personas and Roles"]:
    t, b = find(prefix)
    if t:
        front_parts.append((t, b))
t1, b1 = numbered(1)
with open(os.path.join(OUT, "specs/00-product-overview.md"), "w") as f:
    f.write("# ESG360 Product Overview\n\n> Consolidated from FDD front matter and section 1.\n\n")
    for t, b in front_parts:
        f.write(f"## {t}\n\n"); f.writelines(b); f.write("\n")
    f.write(f"## {t1}\n\n"); f.writelines(b1)

# ---- 7. Architecture (26-35), Technical (36-45), Testing (46-50), Delivery (51-54 + appendices) ----
def dump(rng, folder, note):
    for n in rng:
        t, b = numbered(n)
        if t is None:
            continue
        slug = re.sub(r"[^a-z0-9]+", "-", t.split(". ", 1)[1].lower()).strip("-")
        with open(os.path.join(OUT, folder, f"{n:02d}-{slug}.md"), "w") as f:
            f.write(f"# {t}\n\n> **Source:** PSSTEC ESG360 v1.0, section {n} ({note}).\n\n")
            f.writelines(b)

dump(range(26, 36), "specs/architecture", "SDD")
dump(range(36, 46), "specs/technical", "TDD")
dump(range(46, 51), "specs/testing", "Master Test Specification")
dump(range(51, 55), "specs/delivery", "Delivery planning")

for prefix, fname in [("Appendix A", "appendix-a-references.md"),
                      ("Appendix B", "appendix-b-glossary.md"),
                      ("Appendix C", "appendix-c-traceability.md")]:
    t, b = find(prefix)
    if t:
        with open(os.path.join(OUT, "specs/delivery", fname), "w") as f:
            f.write(f"# {t}\n\n"); f.writelines(b)

# Copy full source doc for reference
shutil.copy(SRC, os.path.join(OUT, "specs/SOURCE-full-document.md"))

print("Done. Files created:")
for root, _, files in os.walk(OUT):
    for fn in sorted(files):
        print(os.path.join(root, fn))
print(f"\nTest case buckets found: { {k: len(v) for k, v in sorted(tc_rows.items())} }")
