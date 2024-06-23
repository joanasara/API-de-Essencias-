# API de Essências

## Visão Geral

Esta aplicação é uma API para listar e detalhar essências. Inclui:

- Um cache para otimizar as requisições.
- Limitação de taxa para controlar a quantidade de requisições.
- Autenticação e autorização via JWT.
- Configuração de segurança para proteger os endpoints.

## Pré-requisitos

Certifique-se de ter as seguintes ferramentas instaladas:

- Java 11+
- Maven 3.6+
- Docker (opcional, para rodar banco de dados ou outros serviços via contêiner)

## Configuração

### Arquivo `application.yml`

Antes de rodar a aplicação, configure o arquivo `application.yml` com suas credenciais da API e as configurações do JWT. O arquivo deve estar localizado em `src/main/resources`.

```yaml
api:
  url: https://api.dev.grupoboticario.com.br/v1/essences-challenge
  username: xmrGmb7PWFwSzzx6TBxxEGyA7n9zGaC7UWs6GWMruGZMGMNG
  password: 0y1GaVyJ6szGu7O3dR2Ax8ijucQujxkUZs6Y715MiQ5hhLiEi6NAbfMJGHqpad96

jwt:
  secret: mySuperSecretKey
  expiration: 36000000

# Instruções de Instalação e Execução
