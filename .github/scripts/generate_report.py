import xml.etree.ElementTree as ET
import glob
import json
import os
from datetime import datetime

meta = {}
try:
    with open("target/caso-prueba-meta.jsonl", encoding="utf-8") as f:
        for line in f:
            line = line.strip()
            if line:
                try:
                    entry = json.loads(line)
                    meta[entry["method"]] = entry
                except (json.JSONDecodeError, KeyError) as e:
                    print(f"Warning: JSONL line ignorada ({e}): {repr(line[:80])}")
except FileNotFoundError:
    pass

results = {}
for path in glob.glob("target/surefire-reports/*.xml"):
    try:
        root = ET.parse(path).getroot()
        for tc in root.iter("testcase"):
            method    = tc.get("name", "")
            classname = tc.get("classname", "").split(".")[-1]
            failure   = tc.find("failure")
            error     = tc.find("error")
            skipped   = tc.find("skipped")
            if failure is not None:
                status = "FAILED"
                detail = (failure.get("message") or failure.text or "")[:300]
            elif error is not None:
                status = "ERROR"
                detail = (error.get("message") or error.text or "")[:300]
            elif skipped is not None:
                status = "SKIPPED"
                detail = ""
            else:
                status = "PASSED"
                detail = ""
            results[method] = {"class": classname, "status": status, "detail": detail}
    except Exception as e:
        print(f"Warning: {path}: {e}")

all_methods = list(meta.keys()) + [m for m in results if m not in meta]

total   = len(results)
passed  = sum(1 for r in results.values() if r["status"] == "PASSED")
failed  = sum(1 for r in results.values() if r["status"] in ("FAILED", "ERROR"))
skipped = sum(1 for r in results.values() if r["status"] == "SKIPPED")

STATUS_COLOR = {
    "PASSED":       ("#1a7f3c", "#d4edda", "Paso"),
    "FAILED":       ("#c0392b", "#f8d7da", "Fallo"),
    "ERROR":        ("#c0392b", "#f8d7da", "Error"),
    "SKIPPED":      ("#7f8c8d", "#e9ecef", "Omitido"),
    "NO EJECUTADO": ("#7f8c8d", "#e9ecef", "No ejecutado"),
}

TIPO_BG = {
    "Normal":            "#d1ecf1",
    "Negativa":          "#f8d7da",
    "Borde":             "#fff3cd",
    "Logica de Negocio": "#d4edda",
}

rows = ""
for method in all_methods:
    r  = results.get(method, {"class": "-", "status": "NO EJECUTADO", "detail": ""})
    m  = meta.get(method, {})
    cp_id    = m.get("id",          "-")
    desc     = m.get("descripcion", method)
    entrada  = m.get("entrada",     "-")
    tipo     = m.get("tipo",        "-")
    esperado = m.get("esperado",    "-")

    fg, bg, label = STATUS_COLOR.get(r["status"], ("#000", "#fff", r["status"]))
    detail_html   = f'<br><small style="color:#555">{r["detail"]}</small>' if r["detail"] else ""
    tipo_bg       = TIPO_BG.get(tipo, "#fff")

    rows += f"""
    <tr>
      <td style="text-align:center;font-weight:bold">{cp_id}</td>
      <td>{desc}</td>
      <td><code style="font-size:.82em">{entrada}</code></td>
      <td style="text-align:center;background:{tipo_bg};font-weight:bold">{tipo}</td>
      <td>{esperado}</td>
      <td style="background:{bg};text-align:center">
        <span style="color:{fg};font-weight:bold">{label}</span>{detail_html}
      </td>
    </tr>"""

now = datetime.utcnow().strftime("%Y-%m-%d %H:%M UTC")

html = f"""<!DOCTYPE html>
<html lang="es">
<head>
  <meta charset="UTF-8">
  <title>Informe de Pruebas - DevHub</title>
  <style>
    * {{ box-sizing:border-box; margin:0; padding:0; }}
    body {{ font-family:Arial,sans-serif; background:#f4f6f9; color:#222; padding:30px; }}
    h1   {{ color:#2c3e6b; margin-bottom:4px; }}
    .meta {{ color:#666; font-size:.9em; margin-bottom:24px; }}
    .summary {{ display:flex; gap:16px; margin-bottom:28px; flex-wrap:wrap; }}
    .card {{ border-radius:8px; padding:14px 24px; min-width:120px; text-align:center; box-shadow:0 2px 6px rgba(0,0,0,.1); }}
    .card span {{ display:block; font-size:2em; font-weight:bold; }}
    .c-total   {{ background:#dce3f0; color:#2c3e6b; }}
    .c-passed  {{ background:#d4edda; color:#155724; }}
    .c-failed  {{ background:#f8d7da; color:#721c24; }}
    .c-skipped {{ background:#e9ecef; color:#495057; }}
    table {{ width:100%; border-collapse:collapse; background:#fff; box-shadow:0 2px 8px rgba(0,0,0,.08); border-radius:8px; overflow:hidden; }}
    thead tr {{ background:#2c3e6b; color:#fff; }}
    th {{ padding:12px 14px; text-align:left; font-size:.9em; }}
    th:first-child {{ width:80px; text-align:center; }}
    td {{ padding:10px 14px; border-bottom:1px solid #e4e8ef; vertical-align:top; font-size:.87em; }}
    tr:last-child td {{ border-bottom:none; }}
    tr:hover td {{ background:#f0f4ff !important; }}
    code {{ background:#f4f4f4; padding:2px 5px; border-radius:3px; word-break:break-all; }}
  </style>
</head>
<body>
  <h1>Informe de Pruebas Unitarias - Backend DevHub</h1>
  <p class="meta">Generado: {now} &nbsp;|&nbsp; Rama: develop</p>
  <div class="summary">
    <div class="card c-total">  <span>{total}</span>  Total</div>
    <div class="card c-passed"> <span>{passed}</span> Pasaron</div>
    <div class="card c-failed"> <span>{failed}</span> Fallaron</div>
    <div class="card c-skipped"><span>{skipped}</span> Omitidos</div>
  </div>
  <table>
    <thead>
      <tr>
        <th>Caso de Prueba</th>
        <th>Descripcion</th>
        <th>Entrada de Datos</th>
        <th>Tipo de Prueba</th>
        <th>Resultado Esperado</th>
        <th>Resultado Real</th>
      </tr>
    </thead>
    <tbody>{rows}</tbody>
  </table>
</body>
</html>"""

os.makedirs("target", exist_ok=True)
with open("target/test-report.html", "w", encoding="utf-8") as f:
    f.write(html)

print(f"Reporte generado: target/test-report.html")
print(f"Total: {total} | Passed: {passed} | Failed: {failed} | Skipped: {skipped}")
