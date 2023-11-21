FROM golang:alpine AS builder

WORKDIR /src
COPY ./game-service /src

RUN go build -o game-service main.go

FROM alpine:latest

COPY --from=builder /src/game-service /game-service
COPY --from=builder /src/resources/application.yml /resources/application.yml

CMD ["/game-service"]
