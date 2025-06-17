# flake8: noqa
from models.document import Document
from typing import Optional
from datetime import date


class Report(Document):
    purpose_details: str
    author: str
    summary: Optional[str] = None
    reviewed_by: Optional[str] = None
    approved_by: Optional[str] = None
    version: Optional[str] = "1.0"
    creation_date: Optional[date] = None
    last_modified_date: Optional[date] = None


def generate_report_html(report: Report) -> str:
    if report.lang == 'pt':
        labels = {
            "title": "Título",
            "date": "Data",
            "author": "Autor",
            "purpose_details": "Detalhes do Propósito",
            "summary": "Resumo",
            "reviewed_by": "Revisado por",
            "approved_by": "Aprovado por",
            "version": "Versão",
            "creation_date": "Data de Criação",
            "last_modified_date": "Última Modificação",
        }
    else:  # English default
        labels = {
            "title": "Title",
            "date": "Date",
            "author": "Author",
            "purpose_details": "Purpose Details",
            "summary": "Summary",
            "reviewed_by": "Reviewed by",
            "approved_by": "Approved by",
            "version": "Version",
            "creation_date": "Creation Date",
            "last_modified_date": "Last Modified",
        }

    summary_section = (
        f"""<section>
            <h2>{labels['summary']}</h2>
            <p>{report.summary}</p>
        </section>"""
        if report.summary else ""
    )

    final_section = (
        f"""
        <section>
            <p><span class="field-label">{labels['reviewed_by']}:</span> {report.reviewed_by}</p>
            <p><span class="field-label">{labels['approved_by']}:</span> {report.approved_by}</p>
        </section>""" if report.reviewed_by or report.approved_by else ""
    )

    return f"""
    <html>
    <head>
        <style>
            body {{ font-family: 'Helvetica', sans-serif; margin: 40px; color: #333; }}
            header {{ text-align: center; border-bottom: 2px solid #555; padding-bottom: 10px; margin-bottom: 30px; }}
            h1 {{ font-size: 28px; color: #222; }}
            section {{ margin-bottom: 25px; }}
            .field-label {{ font-weight: bold; display: inline-block; width: 150px; vertical-align: top; }}
            footer {{ text-align: center; margin-top: 50px; color: #777; font-size: 12px; border-top: 1px solid #ddd; padding-top: 10px; }}
        </style>
    </head>
    <body>
        <header>
            <h1>{report.title}</h1>
        </header>
        <section>
            <p><span class="field-label">{labels['date']}:</span> {report.date}</p>
            <p><span class="field-label">{labels['author']}:</span> {report.author}</p>
            <p><span class="field-label">{labels['version']}:</span> {report.version}</p>
            <p><span class="field-label">{labels['creation_date']}:</span> {report.creation_date or ''}</p>
            <p><span class="field-label">{labels['last_modified_date']}:</span> {report.last_modified_date or ''}</p>
        </section>
        <section>
            <h2>{labels['purpose_details']}</h2>
            <p>{report.purpose_details}</p>
        </section>
        {summary_section}
        {final_section}
        <footer>
            <p>Generated automatically by SmartDoc Report Service</p>
        </footer>
    </body>
    </html>
    """
