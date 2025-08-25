Contributing Guide

Branching & Commits

-   Main branches: main, develop
-   Feature branches: feature/<ticket|HU>-short-desc
-   Commits: feat|fix|docs|test|refactor|chore: message

Before pushing

-   ./gradlew spotlessApply
-   ./gradlew test jacocoTestReport
-   ./gradlew openApiValidateAll

Pull Requests

-   Describe goal and scope
-   Provide evidence (swagger screenshots, tests)
-   Checklist: OpenAPI updated; tests; coverage >= 80%; spotless passes

Microservices from template

-   Adjust .env (PORT, DB\_\*)
-   Rename package com.pragma.powerup → com.pragma.<service>
-   Create docs/openapi/<service>.yaml

Gateway from template
Testing controllers with Security

-   In WebMvcTest slices, Spring Security is enabled by default (auth + CSRF).
-   To focus on controller behavior, disable filters:
    -   `@AutoConfigureMockMvc(addFilters = false)`
-   To test security paths instead, load your SecurityConfiguration and provide headers/CSRF:

    -   `@Import(SecurityConfiguration.class)`
    -   `.with(csrf())` and `X-User-*` headers in requests.
    -   Add test dependency: `org.springframework.security:spring-security-test` to use `csrf()` and `@WithMockUser`.

-   Use springdoc-webflux with /docs/\* routes
-   Configure swagger-ui urls for services
-   Add JWT filter that validates and adds X-User-\* headers
