import os
import glob
import javalang
from datetime import datetime

JAVA_SRC = "src/main/java"

class_rows = ""
method_rows = []

summary = {
    "classes": 0,
    "methods": 0,
    "total_loc": 0
}

for file_path in glob.glob(f"{JAVA_SRC}/**/*.java", recursive=True):

    with open(file_path, "r", encoding="utf-8") as f:
        code = f.read()

    lines = code.splitlines()

    try:
        tree = javalang.parse.parse(code)
    except:
        continue

    for _, class_decl in tree.filter(javalang.tree.ClassDeclaration):

        class_name = class_decl.name

        start_line = class_decl.position.line if class_decl.position else 1
        end_line = len(lines)

        methods = list(class_decl.methods)

        method_sizes = []

        for method in methods:
            if not method.position:
                continue

            method_start = method.position.line

            next_method_lines = [
                m.position.line
                for m in methods
                if m.position and m.position.line > method_start
            ]

            method_end = (
                min(next_method_lines) - 1
                if next_method_lines
                else end_line
            )

            loc = method_end - method_start + 1
            method_sizes.append(loc)

            method_rows.append(f"""
            <tr>
                <td>{class_name}</td>
                <td>{method.name}</td>
                <td>{loc}</td>
            </tr>
            """)

            summary["methods"] += 1

        class_loc = end_line - start_line + 1
        avg_method_loc = (
            round(sum(method_sizes) / len(method_sizes), 2)
            if method_sizes else 0
        )

        class_rows += f"""
        <tr>
            <td>{class_name}</td>
            <td>{class_loc}</td>
            <td>{len(methods)}</td>
            <td>{avg_method_loc}</td>
        </tr>
        """

        summary["classes"] += 1
        summary["total_loc"] += class_loc

now = datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")
branch = os.getenv(
     "GITHUB_REF_NAME",
     "local"
)

html = f"""
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Longitud de Código</title>

<style>
* {{
    box-sizing:border-box;
}}

body {{
    font-family:Arial,sans-serif;
    background:#f4f6f9;
    color:#222;
    padding:30px;
}}

h1 {{
    color:#2c3e6b;
}}

.meta {{
    color:#666;
    margin-bottom:20px;
}}

.summary {{
    display:flex;
    gap:15px;
    margin-bottom:30px;
    flex-wrap:wrap;
}}

.card {{
    background:white;
    padding:20px;
    border-radius:10px;
    box-shadow:0 2px 6px rgba(0,0,0,.1);
    min-width:180px;
    text-align:center;
}}

.card span {{
    display:block;
    font-size:2em;
    font-weight:bold;
}}

table {{
    width:100%;
    border-collapse:collapse;
    background:white;
    margin-bottom:40px;
    border-radius:10px;
    overflow:hidden;
}}

thead {{
    background:#2c3e6b;
    color:white;
}}

th, td {{
    padding:12px;
    border-bottom:1px solid #ddd;
    text-align:left;
}}

tr:hover {{
    background:#f0f4ff;
}}
</style>
</head>

<body>

<h1>Reporte de Longitud de Código - DevHub</h1>

<p class="meta">
Generado: {now}
&nbsp;|&nbsp;
Rama: {branch}
</p>

<div class="summary">
    <div class="card">
        <span>{summary["classes"]}</span>
        Clases
    </div>

    <div class="card">
        <span>{summary["methods"]}</span>
        Métodos
    </div>

    <div class="card">
        <span>{summary["total_loc"]}</span>
        LOC Totales
    </div>
</div>

<h2>Longitud por Clase</h2>

<table>
<thead>
<tr>
    <th>Clase</th>
    <th>LOC</th>
    <th>Métodos</th>
    <th>Promedio LOC/Método</th>
</tr>
</thead>
<tbody>
{class_rows}
</tbody>
</table>

<h2>Longitud por Método</h2>

<table>
<thead>
<tr>
    <th>Clase</th>
    <th>Método</th>
    <th>LOC</th>
</tr>
</thead>
<tbody>
{''.join(method_rows)}
</tbody>
</table>

</body>
</html>
"""

os.makedirs("target/quality-reports", exist_ok=True)

with open(
    "target/quality-reports/loc-report.html",
    "w",
    encoding="utf-8"
) as f:
    f.write(html)

print("Reporte generado: target/quality-reports/loc-report.html")