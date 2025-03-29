# Gallagher

![Java Version](https://img.shields.io/badge/Temurin-21-green?style=flat-square&logo=eclipse-adoptium)
![Status](https://img.shields.io/badge/Status-Beta-yellowgreen?style=flat-square)

[![Gradle](https://img.shields.io/badge/Gradle-8.13.0-informational?style=flat-square&logo=gradle)](https://github.com/gradle/gradle)
[![SpringBoot](https://img.shields.io/badge/SpringBoot-3.4.3-informational?style=flat-square&logo=springboot)](https://github.com/spring-projects/spring-boot)
[![Spotless](https://img.shields.io/badge/Spotless-7.0.2-informational?style=flat-square)](https://github.com/diffplug/spotless)

[![Github - Version](https://img.shields.io/github/v/tag/Buried-In-Code/Gallagher?logo=Github&label=Version&style=flat-square)](https://github.com/Buried-In-Code/Gallagher/tags)
[![Github - License](https://img.shields.io/github/license/Buried-In-Code/Gallagher?logo=Github&label=License&style=flat-square)](https://opensource.org/licenses/MIT)

[![Github Action - Tests](https://img.shields.io/github/actions/workflow/status/Buried-In-Code/Gallagher/integration.yaml?branch=main&logo=githubactions&label=Tests&style=flat-square)](https://github.com/Buried-In-Code/Gallagher/actions/workflows/integration.yaml)
[![Github Action - Build](https://img.shields.io/github/actions/workflow/status/Buried-In-Code/Gallagher/deployment.yaml?branch=main&logo=githubactions&label=Build&style=flat-square)](https://github.com/Buried-In-Code/Gallagher/actions/workflows/deployment.yaml)

__TODO:__ Description

## Local development

1. Downstream services are mocked with Wiremock, run: `docker compose -f docker-compose-local.yaml up -d`
2. Start project `SPRING_PROFILES_ACTIVE=local gradle clean bootRun`

## Usage

 - Run with: `docker compose up -d`
