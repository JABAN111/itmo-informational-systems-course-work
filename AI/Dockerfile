#нужен, т.к. без танцев с бубном на маке не запустить пролог
FROM python:3.9-slim

LABEL authors="jaba"

WORKDIR /app

RUN apt-get update -y && \
    apt-get install -y swi-prolog vim && \
    apt-get clean

COPY requirements.txt /app

RUN pip install -r requirements.txt


COPY . /app




EXPOSE 8000

CMD ["uvicorn", "api:app", "--host", "0.0.0.0", "--port", "8000", "--reload"]