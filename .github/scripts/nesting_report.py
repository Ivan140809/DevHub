import os
import glob
import javalang
from datetime import datetime

JAVA_SRC = "src/main/java"

rows = ""

summary = {
    "methods": 0,
    "high": 0,
    "moderate": 0,
    "low": 0
}


CONTROL_STRUCTURES = (
    javalang.tree.IfStatement,
    javalang.tree.ForStatement,
    javalang.tree.WhileStatement,
    javalang.tree.DoStatement,
    javalang.tree.SwitchStatement,
    javalang.tree.TryStatement,
)


def calculate_nesting(node, depth=0):
    """
    Calcula profundidad máxima de anidamiento
    """

    if node is None:
        return depth

    max_depth = depth

    if isinstance(node, CONTROL_STRUCTURES):
        depth += 1
        max_depth = depth

    if hasattr(node, "children"):

        for child in node.children:

            if isinstance(child, list):
                for item in child:
                    if isinstance(item, javalang.ast.Node):
                        max_depth = max(
                            max_depth,
                            calculate_nesting(item, depth)
                        )

            elif isinstance(child, javalang.ast.Node):
                max_depth = max(
                    max_depth,
                    calculate_nesting(child, depth)
                )

    return max_depth


for file_path in glob.glob(
    f"{JAVA_SRC}/**/*.java",
    recursive=True
):

    try:
        with open(file_path, "r", encoding="utf-8") as f:
            code = f.read()

        tree = javalang.parse.parse(code)

    except:
        continue

    for _, class_decl in tree.filter(
        javalang.tree.ClassDeclaration
    ):

        class_name = class_decl.name

        methods = list(class_decl.methods)

        for method in methods:
            if not method.position:
                continue

            nesting = calculate_nesting(method)

            summary["methods"] += 1
            if nesting == 0:
                continue

            if nesting >= 5:
                level = "Alto"
                bg = "#f8d7da"
                summary["high"] += 1

            elif nesting >= 3:
                level = "Moderado"
                bg = "#fff3cd"
                summary["moderate"] += 1

            else:
                level = "Bajo"
                bg = "#d4edda"
                summary["low"] += 1

            rows += f"""
            <tr>
                <td>{class_name}</td>
                <td>{method.name}</td>
                <td>{nesting}</td>
                <td style="background:{bg}; font-weight:bold;">
                    {level}
                </td>
            </tr>
            """



branch = os.getenv(
    "GITHUB_REF_NAME",
    "local"
)

now = datetime.utcnow().strftime(
    "%Y-%m-%d %H:%M UTC"
)

html = f"""
<!DOCTYPE html>
<html lang="es">

<head>
<meta charset="UTF-8">
<title>Profundidad de Anidamiento</title>

<style>

body {{
    font-family:Arial,sans-serif;
    background:#f4f6f9;
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
    border-radius:10px;
    padding:20px;
    min-width:180px;
    text-align:center;
    box-shadow:0 2px 6px rgba(0,0,0,.1);
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
    border-radius:10px;
    overflow:hidden;
}}

thead {{
    background:#2c3e6b;
    color:white;
}}

th, td {{
    padding:14px;
    border-bottom:1px solid #ddd;
}}

tr:hover {{
    background:#f0f4ff;
}}

</style>
</head>

<body>

<h1>Reporte de Profundidad de Anidamiento - DevHub </h1>

<p class="meta">
Generado: {now}
&nbsp;|&nbsp;
Rama: {branch}
</p>

<div class="summary">

    <div class="card">
        <span>{summary["methods"]}</span>
        Métodos
    </div>

    <div class="card">
        <span>{summary["low"]}</span>
        Bajo
    </div>

    <div class="card">
        <span>{summary["moderate"]}</span>
        Moderado
    </div>

    <div class="card">
        <span>{summary["high"]}</span>
        Alto
    </div>

</div>

<table>

<thead>
<tr>
    <th>Clase</th>
    <th>Método</th>
    <th>Profundidad</th>
    <th>Estado</th>
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
    "target/quality-reports/nesting-report.html",
    "w",
    encoding="utf-8"
) as f:
    f.write(html)

print(
    "Reporte generado: "
    "target/quality-reports/nesting-report.html",
)