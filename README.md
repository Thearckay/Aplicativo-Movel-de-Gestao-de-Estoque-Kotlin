# AMGE Mobile

Frontend mobile do projeto **AMGE (Aplicativo Móvel de Gestão de Estoque)** desenvolvido como parte do projeto de imersão profissional da faculdade.

O aplicativo foi construído utilizando **Kotlin** e **Jetpack Compose**, consumindo uma **API RESTful** desenvolvida em Spring Boot. A aplicação se comunica com o backend para realizar operações de consulta e envio de dados relacionados ao estoque.

---

## Tecnologias Utilizadas

- **Kotlin**
- **Jetpack Compose**
- **Retrofit 2**
- **Gson Converter**
- **Kotlin Coroutines**
- **Gradle (Kotlin DSL)**

---

## Arquitetura da Aplicação

A aplicação foi construída utilizando componentes modernos do ecossistema Android, priorizando simplicidade, organização e boas práticas.

### Interface de Usuário

A interface foi desenvolvida utilizando **Jetpack Compose**, adotando o modelo declarativo para criação das telas.

Isso permite:

- Criação de interfaces reativas
- Menos código boilerplate
- Atualização automática da UI quando o estado muda

---

### Gerenciamento de Estado

O estado das telas é gerenciado utilizando:

- `remember`
- `mutableStateOf`

Esses recursos garantem que a interface reflita automaticamente as mudanças nos dados retornados pela API.

---

### Concorrência e Chamadas Assíncronas

Para evitar bloqueio da **Main Thread**, as chamadas de rede são executadas utilizando:

- **Kotlin Coroutines**
- `suspend functions`
- `rememberCoroutineScope`

Isso permite que requisições HTTP sejam feitas de forma assíncrona, mantendo a interface responsiva.

---

## Comunicação com a API

A comunicação com o backend é feita através do **Retrofit 2**, configurado como um **Singleton** responsável por gerenciar todas as requisições HTTP.

### Cliente HTTP

- **Retrofit 2**
- Base URL apontando para o backend Spring Boot
- Integração com Coroutines

---

### Conversão de Dados

A conversão entre JSON e objetos Kotlin é realizada automaticamente utilizando:

- **Gson Converter**

Os dados retornados pela API são mapeados para **data classes Kotlin**, facilitando a manipulação dentro da aplicação.

---

### Interface de Serviços

Os endpoints da API são definidos através de uma **interface de serviço**, contendo:

- Endpoints `GET`
- Endpoints `POST`

---

### Padronização de Respostas

A aplicação utiliza um modelo genérico de resposta:

```kotlin
ApiResponse<T>
