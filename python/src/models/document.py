from pydantic import BaseModel
from typing import Literal


class Document(BaseModel):
    title: str
    date: str
    lang: Literal['en', 'pt'] = 'en'  # 'en' = English, 'pt' = Portuguese
