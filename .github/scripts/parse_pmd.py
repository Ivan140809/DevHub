import xml.etree.ElementTree as ET
import re
from pathlib import Path


PMD_XML = "target/pmd.xml"
OUTPUT_HTML = "target/quality-report.html"

def get_status(complexity):
    if complexity <= 10:
        return "LOW"

    if complexity <= 20:
        return "MEDIUM"

    if complexity <= 50:
        return "HIGH"

    return "CRITICAL"


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
            "name",
            "Unknown"
        )

        short_name = Path(
            full_path
        ).name

        for violation in file_tag.findall(
                "pmd:violation",
                namespace):

            rule = violation.attrib.get(
                "rule"
            )

            if rule != \
                    "CyclomaticComplexity":
                continue

            method_name = (
                violation.attrib.get(
                    "method",
                    "Unknown"
                )
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

            metrics.append({
                "file":
                    short_name,

                "method":
                    method_name,

                "complexity":
                    complexity,

                "status":
                    get_status(
                        complexity
                    )
            })

    return metrics


def generate_html(metrics):


    rows = ""

    for metric in metrics:

        rows += f"""
        <tr>
            <td>{metric['file']}</td>
            <td>{metric['method']}</td>
            <td>{metric['complexity']}</td>
            <td>{metric['status']}</td>
        </tr>
        """

    if not rows:
        rows = """
        <tr>
            <td colspan="4">
                No cyclomatic complexity
                violations found.
            </td>
        </tr>
        """

    html = f"""
    <!DOCTYPE html>
    <html>
    <head>
        <title>
            Cyclomatic Complexity Report
        </title>

        <style>

            body {{
                font-family: Arial;
                margin: 40px;
            }}

            table {{
                border-collapse: collapse;
                width: 100%;
            }}

            th, td {{
                border: 1px solid #ddd;
                padding: 12px;
                text-align: left;
            }}

            th {{
                background: #f4f4f4;
            }}

        </style>

    </head>

    <body>

        <h1>
            Cyclomatic Complexity Report
        </h1>

        <table>

            <tr>
                <th>File</th>
                <th>Method</th>
                <th>Complexity</th>
                <th>Status</th>
            </tr>

            {rows}

        </table>

    </body>
    </html>
    """

    with open(
        OUTPUT_HTML,
        "w",
        encoding="utf-8"
    ) as file:

        file.write(html)

    print(
        f"Report generated:"
        f" {OUTPUT_HTML}"
    )


if __name__ == "__main__":

    metrics = \
        parse_cyclomatic_complexity()

    print(
        f"Found "
        f"{len(metrics)} "
        f"cyclomatic violations"
    )

    generate_html(metrics)