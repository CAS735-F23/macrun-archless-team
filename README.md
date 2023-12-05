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
- Build Test CI/CD ([Actions](https://github.com/features/actions))
- Service Discovery ([Nacos](https://nacos.io/en-us/))
- Message Queue ([RabbitMQ](https://www.rabbitmq.com/))

## How to Build

### Build (Micro-)services

We automatically build and push the image to [DockerHub](https://hub.docker.com/u/macrun) via GitHub Actions, so you
don't need to build it locally. But if you want to build it anyway, please run the following command:

```shell
docker-compose build
```

## How to Deploy

> If you find that our service is a little slow to start, this is normal. We have health check steps to ensure that each
> service is created and started properly before the API Gateway is launched.

### Deploy (Micro-)services

We have our dependent services (e.g. Redis, RabbitMQ, MariaDB) hosted on Google Cloud, so you **only** need to run the
following command to start and deploy our main (micro-)services.

```shell
docker-compose up -d
```

[//]: # (### Deploy Dependent Services)

[//]: # ()

[//]: # (> Dependent services &#40;such as Redis, RabbitMQ&#41; provide basic support for our application services. In general, you **do)

[//]: # (> not need to** deploy these services yourself, as we have already hosted them on **Google Cloud**. However, if you want)

[//]: # (> to fully test the project locally, you can of course start these dependent services by running the following command:)

[//]: # (>)

[//]: # (> NOTE: If you choose to skip our cloud services and do a full local test, you will also need to rebuild our image)

[//]: # (> yourself by first running the global text replacement to replace `34.130.59.222` with the dependent service IP &#40;)

[//]: # (> e.g. `127.0.0.1`&#41;.)

[//]: # ()

[//]: # (```shell)

[//]: # (docker-compose -f docker-compose.dep.yml up -d)

[//]: # (```)

### Deploy Locally

However, if you are experiencing problems with our cloud services (the cloud can sometimes be a problem), or if you want
to run our services completely on-premises, you can of course run the following command, but make sure you *undo* the
last step.

```shell
docker-compose -f docker-compose.local.yml up -d --pull always
```

## How to Use

### With Simulator Script

The script will simulate all actions like player registration, login, start heart rate monitor, start game, etc. It will
also randomly generate heart rate, move random distance and react to game attacks.

- Please make sure you have **Python3.8+** installed.
- Run the following commands in your shell:

```shell
cd ./simulator-script
python3 -m pip install -U requests
python3 ./simulator.py 127.0.0.1:8080
```

> NOTE: If you start the services with a different address or port, you must change the `127.0.0.1:8080` parameter to
> your new service address.

### With Postman

1. Import all postman collection files from [postman-files](./postman-files).
2. Run the service APIs in Postman in the following order:
    - Player Register
    - Player Login
    - HRM Start
    - Game Start
    - Game Action
    - Game Stop
    - HRM Stop
    - Player Logout

> NOTE: If you start the services with a different address or port, you must change the `{{BASE_URL}}` variable in each
> postman collection. The default value is `http://localhost:8080`.

## How to Test

### Auto Test

We automate the testing of our services using GitHub Actions (CI/CD). You can view the test results at:

- <https://github.com/CAS735-F23/macrun-archless-team/actions/workflows/test.yml>

### Manual Test

If you want to run all the tests manually, go to each service folder and run test commands. Java and Golang environments
are required to run the tests.

- Java Services, e.g. player-service:

```shell
cd ./player-service
mvn test
```

- Golang Services, e.g. game-service:

```shell
cd ./game-service
go test ./...
```
