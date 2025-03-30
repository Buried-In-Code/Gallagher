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

1. Create a `.env` file with your Gallagher details
    ```dotenv
    GALLAGHER__API_KEY='EXAM-PLE-API-KEY'
    GALLAGHER__BASE_URL='https://example.com'
    GALLAGHER__CERT_PATH='./example-cert.pem'
    GALLAGHER__KEY_PATH='./example-key.pem'
    
    CREATE__USER__EMAIL='user@example.com'
    CREATE__USER__FIRST_NAME='Joe'
    CREATE__USER__LAST_NAME='Bloggs'
    CREATE__USER__DIVISION_HREF='https://example.com/api/division/123'
    CREATE__USER__ACCESS_GROUP_HREF='https://example.com/api/access-group/123'
    CREATE__USER__EXTERNAL_ID='AAA-12345-BBB'
    
    READ__USER__EMAIL='user@example.com'
    
    UPDATE__CARD__TYPE_HREF='https://example.com/api/card-type/123'
    UPDATE__CARD__NUMBER='Test Card'
    UPDATE__USER__EMAIL='user@example.com'

    DELETE__USER__EMAIL='user@example.com'
    ```

2. Download or copy the [docker-compose.yaml](./docker-compose.yaml) file
3. Run with: `docker compose up -d`
