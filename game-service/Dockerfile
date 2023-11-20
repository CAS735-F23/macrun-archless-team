FROM golang:alpine AS builder

WORKDIR /src
COPY . /src

RUN go build -o game-service main.go

FROM alpine:latest

COPY --from=builder /src/game-service /usr/bin/game-service

CMD ["game-service"]
