FROM golang:alpine AS builder

WORKDIR /src
COPY . /src

RUN go build -o game-service main.go

FROM alpine:latest

COPY --from=builder /src/game-service /srv/game-service
COPY --from=builder /src/application.yml /srv/application.yml

CMD ["/srv/game-service"]
