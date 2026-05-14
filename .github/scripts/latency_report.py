import requests
import time
import statistics
import os
from datetime import datetime

BASE_URL = "http://localhost:8080"

ENDPOINTS = [
    {
        "name": "Login",
        "method": "POST",
        "url": "/auth/login",
        "payload": {
            "email": "test@test.com",
            "password": "#TestLatencia1"
        }
    },
    {
        "name": "Register",
        "method": "POST",
        "url": "/auth/register",
        "payload": {
            "firstName": "Test",
            "lastName": "User",
            "username": f"user{int(time.time())}",
            "email": f"test{int(time.time())}@mail.com",
            "password": "#TestLatencia1",
            "phone": f"{int(time.time())}"
        }
    },
    {
        "name": "Get Questions",
        "method": "GET",
        "url": "/questions"
    }
]

results = []

TOTAL_REQUESTS = 10

for endpoint in ENDPOINTS:

    times = []

    for _ in range(TOTAL_REQUESTS):

        try:
            start = time.perf_counter()

            if endpoint["method"] == "POST":
                response = requests.post(
                    BASE_URL + endpoint["url"],
                    json=endpoint["payload"],
                    timeout=10
                )
            else:
                response = requests.get(
                    BASE_URL + endpoint["url"],
                    timeout=10
                )

            elapsed_ms = (
                time.perf_counter() - start
            ) * 1000

            times.append(elapsed_ms)

        except Exception:
            continue

    if not times:
        continue

    avg = round(statistics.mean(times), 2)
    minimum = round(min(times), 2)
    maximum = round(max(times), 2)


    results.append({
        "endpoint": endpoint["name"],
        "avg": avg,
        "min": minimum,
        "max": maximum
    })


rows = ""

for r in results:

    rows += f"""
    <tr>
        <td>{r['endpoint']}</td>
        <td>{r['avg']} ms</td>
        <td>{r['min']} ms</td>
        <td>{r['max']} ms</td>
    </tr>
    """

branch = os.getenv(
    "GITHUB_REF_NAME",
    "local"
)

now = datetime.utcnow().strftime(
    "%Y-%m-%d %H:%M UTC"
)

fastest = min(results, key=lambda x: x["avg"])
slowest = max(results, key=lambda x: x["avg"])

html = f"""
<!DOCTYPE html>
<html lang="es">
<head>
<meta charset="UTF-8">
<title>Reporte de Latencia</title>

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
    flex-wrap:wrap;
    margin-bottom:30px;
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

<h1>Reporte de Latencia de Endpoints - DevHub</h1>

<p class="meta">
Generado: {now}
&nbsp;|&nbsp;
Rama: {branch}
</p>

<div class="summary">

<div class="card">
<span>{len(results)}</span>
Endpoints
</div>

<div class="card">
<span>{fastest["avg"]} ms</span>
Más rápido
</div>

<div class="card">
<span>{slowest["avg"]} ms</span>
Más lento
</div>

<div class="card">
<span>{TOTAL_REQUESTS}</span>
Requests por endpoint
</div>

</div>

<table>
<thead>
<tr>
<th>Endpoint</th>
<th>Promedio</th>
<th>Mínimo</th>
<th>Máximo</th>
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
    "target/quality-reports/latency-report.html",
    "w",
    encoding="utf-8"
) as f:
    f.write(html)

print(
    "Reporte generado:"
    " target/quality-reports/latency-report.html"
)