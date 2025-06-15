import weasyprint
import requests
import io
from pydantic import BaseModel
from fastapi import FastAPI, Response
from typing import Literal


app = FastAPI()


class Contract(BaseModel):
    title: str
    date: str
    contractor: str
    contracted: str
    key_terms_definitions: str
    terms_and_conditions: str
    payment_terms: str
    start_date: str
    contract_duration: str
    lang: Literal['en', 'pt'] = 'en'  # 'en' = English, 'pt' = Portuguese


def generate_contract_html(contract: Contract) -> str:
    if contract.lang == 'pt':
        labels = {
            "date": "Data",
            "contractor": "Contratante",
            "contracted": "Contratado",
            "key_terms_definitions": "Termos e Definições",
            "terms_and_conditions": "Termos e Condições",
            "payment_terms": "Condições de Pagamento",
            "start_date": "Data de Início",
            "contract_duration": "Duração do Contrato",
            "signature": "Assinatura",
        }
    else:  # English default
        labels = {
            "date": "Date",
            "contractor": "Contractor",
            "contracted": "Contracted",
            "key_terms_definitions": "Key Terms & Definitions",
            "terms_and_conditions": "Terms and Conditions",
            "payment_terms": "Payment Terms",
            "start_date": "Start Date",
            "contract_duration": "Contract Duration",
            "signature": "Signature",
        }

    return f"""
    <html>
    <head>
        <style>
            body {{ font-family: 'Helvetica', sans-serif; margin: 40px; color: #333; }}
            header {{ text-align: center; border-bottom: 2px solid #555; padding-bottom: 10px; margin-bottom: 30px; }}
            h1 {{ font-size: 28px; color: #222; }}
            section {{ margin-bottom: 25px; }}
            .field-label {{ font-weight: bold; display: inline-block; width: 150px; }}
            footer {{ text-align: center; margin-top: 50px; color: #777; font-size: 12px; border-top: 1px solid #ddd; padding-top: 10px; }}
            .signature {{ margin-top: 50px; text-align: center; }}
            .signature-line {{ width: 300px; border-top: 1px solid #000; margin: 0 auto; }}
        </style>
    </head>
    <body>
        <header>
            <h1>{contract.title}</h1>
        </header>
        <section>
            <p><span class="field-label">{labels['date']}:</span> {contract.date}</p>
            <p><span class="field-label">{labels['contractor']}:</span> {contract.contractor}</p>
            <p><span class="field-label">{labels['contracted']}:</span> {contract.contracted}</p>
        </section>
        <section>
            <h2>{labels['key_terms_definitions']}</h2>
            <p>{contract.key_terms_definitions}</p>
        </section>
        <section>
            <h2>{labels['terms_and_conditions']}</h2>
            <p>{contract.terms_and_conditions}</p>
        </section>
        <section>
            <h2>{labels['payment_terms']}</h2>
            <p>{contract.payment_terms}</p>
        </section>
        <section>
            <p><span class="field-label">{labels['start_date']}:</span> {contract.start_date}</p>
            <p><span class="field-label">{labels['contract_duration']}:</span> {contract.contract_duration}</p>
        </section>
        <div class="signature">
            <div class="signature-line"></div>
            <p>{labels['signature']}</p>
        </div>
        <footer>
            <p>Generated automatically by FastAPI Contract Service</p>
        </footer>
    </body>
    </html>
    """


@app.post("/generate-contract-pdf")
async def generate_contract(contract: Contract):
    html_content = generate_contract_html(contract)
    pdf_bytes = weasyprint.HTML(string=html_content).write_pdf()

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
