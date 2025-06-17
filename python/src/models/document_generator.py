import weasyprint


class PdfGenerator:
    def __init__(self, html, name) -> None:
        self.html = html
        self.name = name
        self.file = None
        self.bytes = None

    def get_raw_bytes(self):
        if self.bytes is not None:
            return self.bytes

        pdf_bytes = weasyprint.HTML(string=self.html).write_pdf()

        self.bytes = pdf_bytes
        return self.bytes
