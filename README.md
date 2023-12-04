# MacRun Services

- [Project Assignment Description](.project-docs/Project_Assignment_Description.pdf)
- [Case Study (Fall 23): ACME Run](.project-docs/Case_Study_Fall2023.pdf)

## Architecture Report

- [MVP Report](https://docs.google.com/document/d/1autqAB21GcHH2TUhu9ez9Kf1AKQdTmIThb3qxiyk7p8/edit?usp=sharing)
- [Final Report](https://docs.google.com/document/d/10VK-EgGRhk5Q-xbG0QR4D0luVF6JTTd3wDxl1OF0oBA/edit?usp=sharing)

## Features

- API Gateway ([Caddy](https://caddyserver.com/))
- Health Check ([Docker](https://www.docker.com/))
- Session Cache ([Redis](https://redis.io/))
- Service Discovery ([Nacos](https://nacos.io/en-us/))
- Message Queue ([RabbitMQ](https://www.rabbitmq.com/))

## How to Build

### Build (Micro-)services

> We automatically build and push the image to [DockerHub](https://hub.docker.com/u/macrun) via GitHub Actions, so you
> don't need to build it locally. But if you want to build it anyway, please run the following command:

```shell
docker-compose build
```

## How to Deploy

### Deploy (Micro-)services

> If you find that our service is a little slow to start, this is normal. We have health check steps to ensure that each
> service is created and started properly before the API Gateway is launched.

```shell
docker-compose up -d
````

### Deploy Dependent Services

> Dependent services (such as Redis, RabbitMQ) provide basic support for our application services. In general, you **do
> not need to** deploy these services yourself, as we have already hosted them on **Google Cloud**. However, if you want
> to fully test the project locally, you can of course start these dependent services by running the following command:
>
> NOTE: If you choose to skip our cloud services and do a full local test, you will also need to rebuild our image
> yourself by first running the global text replacement to replace `34.130.59.222` with the dependent service IP (
> e.g. `127.0.0.1`).

```shell
docker-compose -f docker-compose.dep.yml up -d
```

## How to Test

TBA
