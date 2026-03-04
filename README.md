# AMGE -  Aplicativo Móvel de Gestão de Estoque

## Descrição do Projeto

O **Armazém AMGE** é uma aplicação completa (Full Stack) desenvolvida para a gestão de estoques e fluxos de armazém.

O sistema permite o controle de entrada de itens, monitoramento de valores totais de inventário e visualização de atividades recentes, garantindo que o operador tenha controle total sobre os produtos sob sua responsabilidade.

---

## Tecnologias Utilizadas

### Backend

- **Java 17+**
- **Spring Boot 3.x**
- **Spring Data JPA** – Persistência de dados e abstração de consultas
- **MySQL** – Banco de dados relacional
- **Jakarta Persistence (JPA)** – Mapeamento Objeto-Relacional (ORM)
- **Maven** – Gerenciamento de dependências

### Frontend (Mobile)

- **Kotlin**
- **Jetpack Compose** – Interface declarativa moderna
- **Retrofit 2** – Comunicação assíncrona com API REST
- **Coroutines** – Execução de tarefas em segundo plano
- **GSON** – Conversão de JSON para objetos Kotlin

---

## Arquitetura do Sistema

O projeto foi estruturado seguindo boas práticas de separação de responsabilidades:

1. **Camada de Entidade**
    - Mapeamento das tabelas `User` e `Item`
    - Relacionamento `ManyToOne`

2. **Camada de Repositório**
    - Interfaces que estendem `JpaRepository`
    - Responsável pelo acesso ao banco de dados

3. **Camada de Serviço (Service Layer)**
    - Implementação das regras de negócio
    - Validações (ex: verificação de código duplicado)
    - Cálculos relacionados ao inventário

4. **Camada de Controle (REST Controllers)**
    - Exposição dos endpoints
    - Uso de **DTOs (Data Transfer Objects)** para segurança e organização

5. **Tratamento Global de Exceções**
    - Implementação com `@RestControllerAdvice`
    - Respostas padronizadas para o frontend mesmo em cenários de erro

---

## Funcionalidades Principais

- **Autenticação de Usuário**
    - Registro
    - Login
    - Retorno de ID de sessão para persistência no mobile

- **Dashboard Dinâmico**
    - Cálculo em tempo real do valor total do inventário
    - Percentual de variação

- **Gestão de Itens**
    - Cadastro de produtos vinculados ao usuário logado
    - Validação de código único (SKU)

- **Consulta de Estoque**
    - Listagem completa de itens por usuário
    - Otimização de tráfego de rede

---

## Autor

Kayck Arcanjo