FROM python:3.9-slim

LABEL authors="jaba"

WORKDIR /app

RUN apt-get update -y && \
    apt-get install -y swi-prolog vim && \
    apt-get clean

COPY . /app

RUN pip install dist/AI-1.0.1-py3-none-any.whl


EXPOSE 8000

CMD ["uvicorn", "api:app", "--host", "0.0.0.0", "--port", "8000", "--reload"]