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
```
## Instruções de Instalação e Execução
### Clone o repositório para sua máquina local
```bash
git clone https://github.com/seu-usuario/api-essencias.git
cd api-essencias
```
### Compile o projeto usando Maven
```bash
mvn clean install
```
### Rode a aplicação
```bash
mvn spring-boot:run
```
## Utilizando Docker (Opcional)

Se desejar, você pode usar Docker para rodar o banco de dados ou outros serviços necessários. Para rodar a aplicação em um contêiner Docker.
### Crie um Dockerfile no diretório raiz do projeto

```bash
FROM openjdk:11-jre-slim
COPY target/api-essencias-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
```
### Construa a imagem Docker

```bash
docker build -t api-essencias
```
### Rode o contêiner

```bash
docker run -p 8080:8080 api-essencias
```

## Exemplos de Uso
### Listar todas as essências

Faça uma requisição GET para /essences.

Exemplo de comando cURL

```bash
curl -H "Authorization: Bearer <seu_token_jwt>" http://localhost:8080/essences
```
## Detalhar uma essência específica
Faça uma requisição GET para /essences/{id}.

Exemplo de comando cURL

```bash
curl -H "Authorization: Bearer <seu_token_jwt>" http://localhost:8080/essences/{id}
```

## Autenticação
Para obter um token JWT, faça uma requisição POST para /auth/login com suas credenciais de usuário.

Exemplo de comando cURL
```bash
curl -X POST -H "Content-Type: application/json" -d '{"username":"admin", "password":"password"}' http://localhost:8080/auth/login
```
## Limitação de Taxa
Cada cliente tem um limite de 5 requisições por minuto. Caso esse limite seja excedido, será retornada uma resposta com status Limite de requisições excedido. Tente novamente mais tarde.

## Implementação do Cache
Eu criei um mecanismo de cache para armazenar as essências e detalhes de essências para melhorar a eficiência e reduzir a dependência de acessos frequentes à API externa.

### EssenceCache:
- Utilizou `ConcurrentHashMap` para armazenar e gerenciar o cache das essências e dos detalhes das essências (EssenciasResponseDto e EssenciaResponseDto).
 O cache é expirado após 30 minutos para garantir que os dados estejam atualizados.

### Controle de Acesso Volumétrico
Implementei um controle de acesso volumétrico para limitar as requisições à sua API:
LimiteBusca: Utilizou um contador simples (RequestCounter) para contar e limitar as requisições a 5 por minuto.

### Segurança e Autenticação
Implementei segurança avançada com JWT para autenticação de usuários na sua API:

SecurityConfig, JwtRequestFilter, AuthController: Configurei Spring Security para exigir autenticação via JWT para todos os endpoints (/essences/**).
O filtro JwtRequestFilter intercepta as requisições para validar e processar tokens JWT. AuthController gera tokens JWT após autenticação do usuário.

### Gestão de Exceções
Implementei gestão de exceções para lidar com erros específicos:

EssenciaExceptionHandler: Tratamento de exceções personalizado para lidar com limites de requisição excedidos (SearchLimitExceededException) e outros erros gerais.

### Configuração do Spring
Configurei o Spring para injetar dependências e configurar beans necessários:

AppConfig: Configuração do RestTemplate e ObjectMapper.
SecurityConfig: Configuração global de segurança, incluindo autenticação com UserDetailsService e PasswordEncoder.


## Desenho de solução
![Untitled diagram-2024-06-24-012044](https://github.com/joanasara/API-de-Essencias-/assets/66474627/52142ea4-6e22-4a5d-b0c8-57cf226d284b)


