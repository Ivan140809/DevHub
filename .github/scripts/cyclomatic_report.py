import xml.etree.ElementTree as ET
import re
import os
from pathlib import Path
from datetime import datetime

PMD_XML = "target/pmd.xml"
OUTPUT_HTML = "target/quality-reports/cyclomatic-report.html"


def get_severity(complexity):
    if complexity <= 10:
        return ("LOW", "#155724", "#d4edda", "Baja")

    if complexity <= 20:
        return ("MEDIUM", "#856404", "#fff3cd", "Media")

    if complexity <= 50:
        return ("HIGH", "#721c24", "#f8d7da", "Alta")

    return ("CRITICAL", "#5a0c14", "#f5c6cb", "Crítica")

def parse_cyclomatic_complexity():


    tree = ET.parse(PMD_XML)
    root = tree.getroot()

    namespace = {
        "pmd":
        "http://pmd.sourceforge.net/report/2.0.0"
    }

    metrics = []

    for file_tag in root.findall(
            "pmd:file",
            namespace):

        full_path = file_tag.attrib.get(
            "name", "Unknown"
        )

        file_name = Path(
            full_path
        ).name

        for violation in file_tag.findall(
                "pmd:violation",
                namespace):

            if violation.attrib.get(
                    "rule") != \
                    "CyclomaticComplexity":
                continue

            method = violation.attrib.get(
                "method",
                "Unknown"
            )

            message = (
                violation.text or ""
            ).strip()

            match = re.search(
                r"complexity of (\d+)",
                message
            )

            complexity = (
                int(match.group(1))
                if match
                else 0
            )

            level, fg, bg, label = \
                get_severity(
                    complexity
                )

            metrics.append({
                "file":
                    file_name,

                "method":
                    method,

                "complexity":
                    complexity,

                "severity":
                    label,

                "fg":
                    fg,

                "bg":
                    bg
            })

    return metrics


metrics = parse_cyclomatic_complexity()

total = len(metrics)

low = sum(
    1 for m in metrics
    if m["severity"] == "Baja"
)

medium = sum(
    1 for m in metrics
    if m["severity"] == "Media"
)

high = sum(
    1 for m in metrics
    if m["severity"] == "Alta"
)

critical = sum(
    1 for m in metrics
    if m["severity"] == "Crítica"
)

rows = ""

for metric in metrics:

    rows += f"""
    <tr>
        <td>{metric['file']}</td>

        <td>
            <code>
                {metric['method']}
            </code>
        </td>

        <td style="
            text-align:center;
            font-weight:bold;
        ">
            {metric['complexity']}
        </td>

        <td style="
            background:{metric['bg']};
            text-align:center;
        ">
            <span style="
                color:{metric['fg']};
                font-weight:bold;
            ">
                {metric['severity']}
            </span>
        </td>
    </tr>
    """

if not rows:
    rows = """
    <tr>
        <td colspan="4"
            style="text-align:center">
            No cyclomatic complexity
            issues found.
        </td>
    </tr>
    """

now = datetime.utcnow().strftime(
    "%Y-%m-%d %H:%M UTC"
)

html = f"""
<!DOCTYPE html>
<html lang="es">

<head>

<meta charset="UTF-8">

<title>
Cyclomatic Complexity Report
</title>

<style>

* {{
    box-sizing:border-box;
    margin:0;
    padding:0;
}}

body {{
    font-family:Arial,sans-serif;
    background:#f4f6f9;
    color:#222;
    padding:30px;
}}

h1 {{
    color:#2c3e6b;
    margin-bottom:4px;
}}

.meta {{
    color:#666;
    font-size:.9em;
    margin-bottom:24px;
}}

.summary {{
    display:flex;
    gap:16px;
    margin-bottom:28px;
    flex-wrap:wrap;
}}

.card {{
    border-radius:8px;
    padding:14px 24px;
    min-width:140px;
    text-align:center;
    box-shadow:
        0 2px 6px
        rgba(0,0,0,.1);
}}

.card span {{
    display:block;
    font-size:2em;
    font-weight:bold;
}}

.c-total {{
    background:#dce3f0;
    color:#2c3e6b;
}}

.c-low {{
    background:#d4edda;
    color:#155724;
}}

.c-medium {{
    background:#fff3cd;
    color:#856404;
}}

.c-high {{
    background:#f8d7da;
    color:#721c24;
}}

.c-critical {{
    background:#f5c6cb;
    color:#5a0c14;
}}

table {{
    width:100%;
    border-collapse:collapse;
    background:#fff;
    box-shadow:
        0 2px 8px
        rgba(0,0,0,.08);

    border-radius:8px;
    overflow:hidden;
}}

thead tr {{
    background:#2c3e6b;
    color:#fff;
}}

th {{
    padding:12px 14px;
    text-align:left;
    font-size:.9em;
}}

td {{
    padding:10px 14px;
    border-bottom:
        1px solid #e4e8ef;

    vertical-align:top;
    font-size:.87em;
}}

tr:last-child td {{
    border-bottom:none;
}}

tr:hover td {{
    background:#f0f4ff !important;
}}

code {{
    background:#f4f4f4;
    padding:2px 5px;
    border-radius:3px;
}}

</style>

</head>

<body>

<h1>
Reporte de Complejidad
Ciclomática - DevHub
</h1>

<p class="meta">
Generado: {now}
</p>

<div class="summary">

<div class="card c-total">
<span>{total}</span>
Total
</div>

<div class="card c-low">
<span>{low}</span>
Baja
</div>

<div class="card c-medium">
<span>{medium}</span>
Media
</div>

<div class="card c-high">
<span>{high}</span>
Alta
</div>

<div class="card c-critical">
<span>{critical}</span>
Crítica
</div>

</div>

<table>

<thead>
<tr>
<th>Archivo</th>
<th>Método</th>
<th>Complejidad</th>
<th>Severidad</th>
</tr>
</thead>

<tbody>
{rows}
</tbody>

</table>

</body>
</html>
"""

os.makedirs(
    "target/quality-reports",
    exist_ok=True
)

with open(
        OUTPUT_HTML,
        "w",
        encoding="utf-8") as f:

    f.write(html)

print(
    f"Reporte generado: "
    f"{OUTPUT_HTML}"
)