FROM golang:alpine AS builder

WORKDIR /src
COPY . /src

RUN go build -o game-service main.go

FROM alpine:latest

WORKDIR /srv

COPY --from=builder /src/game-service /srv/game-service
COPY --from=builder /src/application.yml /srv/application.yml

RUN apk add --no-cache curl

CMD ["/srv/game-service"]
