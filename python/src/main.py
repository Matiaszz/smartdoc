# flake8:noqa
from fastapi import Response
import os
import tempfile
from fastapi import FastAPI, Response
from conctract import Contract, generate_contract_html
from models.document_generator import PdfGenerator
from report import Report, generate_report_html
import pypandoc
import requests
import io

app = FastAPI()


@app.post("/generate-contract-pdf/")
async def generate_contract(contract: Contract):
    html_content = generate_contract_html(contract)
    pdf = PdfGenerator(html_content, 'contract')
    pdf_bytes = pdf.get_raw_bytes()

    if pdf_bytes is None:
        return Response(content="Failed to generate PDF.",
                                media_type="text/plain", status_code=500)

    pdf_file = io.BytesIO(pdf_bytes)

    files = {
        'file': ('contract.pdf', pdf_file, 'application/pdf')
    }

    response = requests.post(
        'http://localhost:8080/api/documents/upload/', files=files)

    print(response.status_code)
    print(response.text)

    return Response(content=pdf_bytes, media_type="application/pdf")


@app.post("/generate-report-docx/")
async def generate_report(report: Report):
    html_content = generate_report_html(report)

    with tempfile.NamedTemporaryFile(delete=False, suffix=".docx") as tmpfile:
        output_path = tmpfile.name

    try:
        pypandoc.convert_text(
            html_content,
            'docx',
            format='html',
            outputfile=output_path
        )

        with open(output_path, 'rb') as f:
            docx_bytes = f.read()

        docx_file = io.BytesIO(docx_bytes)

        files = {
            'file': ('report.docx', docx_file, 'application/vnd.openxmlformats-officedocument.wordprocessingml.document')
        }

        response = requests.post(
            'http://localhost:8080/api/documents/upload/', files=files)

        print(response.status_code)
        print(response.text)

        docx_file.seek(0)
        return Response(
            content=docx_file.read(),
            media_type='application/vnd.openxmlformats-officedocument.wordprocessingml.document'
        )

    finally:
        os.remove(output_path)
