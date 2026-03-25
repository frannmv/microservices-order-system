FROM python:3.12-slim

COPY --from=ghcr.io/astral-sh/uv:0.10.12 /uv /uvx /bin/

ADD . /app

WORKDIR /app

RUN uv sync --locked --no-dev

EXPOSE 8080

CMD ["uv", "run", "server.py"]