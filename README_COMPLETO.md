# 🏦 Bank API - REST API 100% Funcional para Banco Digital

Uma **API REST profissional e completa** para gerenciar um banco digital, construída com Spring Boot 4.0.3 e Java 21.

---

## 📋 Tabela de Conteúdo

1. [O Que É Este Projeto](#o-que-é-este-projeto)
2. [Características Principais](#características-principais)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Requisitos](#requisitos)
5. [Instalação](#instalação)
6. [Configuração](#configuração)
7. [Como Usar](#como-usar)
8. [Estrutura do Projeto](#estrutura-do-projeto)
9. [Endpoints da API](#endpoints-da-api)
10. [Exemplos de Uso](#exemplos-de-uso)
11. [Funcionalidades em Detalhes](#funcionalidades-em-detalhes)
12. [Modelo de Dados](#modelo-de-dados)
13. [Segurança](#segurança)
14. [Troubleshooting](#troubleshooting)
15. [Próximos Passos](#próximos-passos)

---

## O Que É Este Projeto

A **Bank API** é uma aplicação backend completa que simula um banco digital funcional. Com esta API você pode:

✅ Gerenciar múltiplos usuários  
✅ Criar e gerenciar contas bancárias  
✅ Emitir e controlar cartões de crédito  
✅ Realizar transferências entre contas  
✅ Solicitar e gerenciar empréstimos  
✅ Rastrear histórico de transações completo  

**Status:** Production Ready ✅

---

## Características Principais

### 👤 Gerenciamento de Usuários
- Criar conta de novo usuário
- Email e CPF únicos no sistema
- Dados pessoais completos (nome, telefone, endereço, data de nascimento)
- Criação automática de conta bancária ao registrar
- Listar, atualizar e deletar usuários

### 💰 Contas Bancárias
- Uma conta por usuário (criada automaticamente)
- Número de conta e agência gerados automaticamente
- Saldo em tempo real
- Limite de cheque configurável
- Operações de depósito e saque com validação
- Ativar/desativar conta

### 💳 Cartões de Crédito
- Múltiplos cartões por usuário
- 3 tipos de cartão: Crédito, Débito, Pré-pago
- Número e CVV gerados automaticamente
- Limite de crédito configurável
- Realizar compras
- Controlar fatura
- Pagar fatura
- Bloquear/desbloquear/cancelar cartão
- Status automático: ATIVO, BLOQUEADO, CANCELADO

### 🔄 Transferências Bancárias
- Múltiplos tipos: PIX, DOC, TED, ENTRADA, SAÍDA
- Transferências entre contas
- Validação automática de saldo
- Débito e crédito atômicos
- Histórico de transferências enviadas
- Histórico de transferências recebidas
- Status: CONCLUIDA, PENDENTE, CANCELADA, FALHA

### 📊 Gerenciamento de Empréstimos
- Solicitação de empréstimo
- Sistema de aprovação/rejeição
- Taxa de juros configurável
- Número de parcelas customizável
- Crédito automático na aprovação
- Realizar pagamentos de parcelas
- Controle de quitação
- Status: EM_ANALISE, APROVADO, REJEITADO, QUITADO, INADIMPLENTE
- Rastreamento de vencimentos

---

## Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|-----------|--------|-----|
| **Java** | 21 | Linguagem de programação |
| **Spring Boot** | 4.0.3 | Framework web |
| **Spring Data JPA** | - | Acesso a dados |
| **Hibernate** | - | Object-Relational Mapping (ORM) |
| **MySQL** | 8.0+ | Banco de dados relacional |
| **Lombok** | - | Redução de boilerplate |
| **Maven** | - | Build automation |
| **Jackson** | - | Serialização JSON |

---

## Requisitos

### Pré-requisitos
- **Java 21** ou superior
- **MySQL 8.0** ou superior
- **Maven 3.6** ou superior
- **Postman** (opcional, para testes)

### Verificar Instalações

```bash
# Verificar Java
java -version
# Saída esperada: java version "21" ou superior

# Verificar MySQL
mysql --version
# Saída esperada: mysql Ver 8.0 ou superior

# Verificar Maven
mvn --version
# Saída esperada: Apache Maven 3.6 ou superior
```

---

## Instalação

### Passo 1: Clonar ou Baixar o Projeto

```bash
# Se usando Git
git clone <url-do-repositorio>
cd bank-api

# Se usando arquivo ZIP
# Descompacte e abra a pasta bank-api
```

### Passo 2: Criar Banco de Dados MySQL

```bash
# Conectar ao MySQL
mysql -u root -p

# Dentro do MySQL
CREATE DATABASE bank_api_db;
SHOW DATABASES;  # Confirmar que foi criado
EXIT;
```

### Passo 3: Verificar Estrutura do Projeto

```
bank-api/
├── src/
│   ├── main/
│   │   ├── java/com/bank/api/
│   │   │   ├── model/          
│   │   │   ├── repository/     
│   │   │   ├── service/        
│   │   │   ├── controller/     
│   │   │   ├── dto/            
│   │   │   ├── exception/      
│   │   │   ├── config/         
│   │   │   └── BankApiApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml
└── README.md
```

---

## Configuração

### application.properties

Edite o arquivo `src/main/resources/application.properties` com suas credenciais:

```properties
# ============================================
# SPRING APPLICATION NAME
# ============================================
spring.application.name=bank-api

# ============================================
# DATABASE CONFIGURATION
# ============================================
spring.datasource.url=jdbc:mysql://localhost:3306/bank_api_db
spring.datasource.username=root
spring.datasource.password=sua_senha_aqui
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ============================================
# JPA/HIBERNATE CONFIGURATION
# ============================================
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# ============================================
# SERVER CONFIGURATION
# ============================================
server.port=8080
server.servlet.context-path=/api
```

### Configurações Importantes

**spring.datasource.username**: Seu usuário MySQL (padrão: `root`)  
**spring.datasource.password**: Sua senha MySQL  
**server.port**: Porta onde a API rodará (padrão: 8080)  
**spring.jpa.hibernate.ddl-auto**: Define como as tabelas são criadas (value: `update` cria/atualiza automaticamente)

---

## Como Usar

### 1. Compilar o Projeto

```bash
cd bank-api
mvn clean install -DskipTests
```

**O que acontece:**
- Download de dependências
- Compilação de código
- Verificação de erros

**Tempo estimado:** 5-10 minutos (primeira vez)

### 2. Executar a Aplicação

```bash
# Opção 1: Usando Maven
mvn spring-boot:run

# Opção 2: Executar JAR diretamente
java -jar target/bank-api-0.0.1-SNAPSHOT.jar
```

**Saída esperada:**
```
Started BankApiApplication in X.XXX seconds
Tomcat started on port(s): 8080 (http)
```

### 3. Verificar se está Funcionando

```bash
# Teste um endpoint
curl http://localhost:8080/api/usuarios

# Resposta esperada:
[]  # Array vazio (sem usuários cadastrados ainda)
```

### 4. Parar a Aplicação

```bash
# Pressione: CTRL + C
# Na janela do terminal onde a API está rodando
```

---

## Estrutura do Projeto

### Camada de Modelo (model/)

Define as entidades principais do banco de dados:

```
Usuario.java              - Usuário do sistema
ContaBancaria.java        - Conta bancária do usuário
Cartao.java               - Cartão de crédito/débito
Transferencia.java        - Transferência entre contas
Emprestimo.java           - Empréstimo do usuário
StatusCartao.java         - Enum (ATIVO, BLOQUEADO, CANCELADO)
TipoCartao.java           - Enum (CREDITO, DEBITO, PRE_PAGO)
StatusTransferencia.java  - Enum (PENDENTE, CONCLUIDA, CANCELADA, FALHA)
TipoTransferencia.java    - Enum (ENTRADA, SAIDA, PIX, DOC, TED)
StatusEmprestimo.java     - Enum (EM_ANALISE, APROVADO, REJEITADO, QUITADO)
```

### Camada de Repositório (repository/)

Interface para acesso a dados usando JPA:

```
UsuarioRepository.java              - CRUD de usuários
ContaBancariaRepository.java        - CRUD de contas
CartaoRepository.java               - CRUD de cartões
TransferenciaRepository.java        - CRUD de transferências
EmprestimoRepository.java           - CRUD de empréstimos
```

### Camada de Serviço (service/)

Contém a lógica de negócio:

```
UsuarioService.java         - Criar, listar, atualizar usuários
ContaBancariaService.java   - Gerenciar saldo, depósitos, saques
CartaoService.java          - Gerenciar cartões e transações
TransferenciaService.java   - Realizar transferências
EmprestimoService.java      - Gerenciar empréstimos
```

### Camada de Controle (controller/)

Define os endpoints REST:

```
UsuarioController.java              - Endpoints de usuários
ContaBancariaController.java        - Endpoints de contas
CartaoController.java               - Endpoints de cartões
TransferenciaController.java        - Endpoints de transferências
EmprestimoController.java           - Endpoints de empréstimos
```

### DTOs (dto/)

Data Transfer Objects para proteção e validação:

```
UsuarioDTO.java             - Resposta de usuário
UsuarioCriacaoDTO.java      - Criação de usuário
ContaBancariaDTO.java       - Resposta de conta
CartaoDTO.java              - Resposta de cartão
TransferenciaDTO.java       - Resposta de transferência
EmprestimoDTO.java          - Resposta de empréstimo
```

### Infraestrutura (exception/, config/)

```
GlobalExceptionHandler.java - Tratamento global de exceções
ErrorResponse.java          - Formato padronizado de erro
CorsConfig.java             - Configuração CORS
```

---

## Endpoints da API

### 👤 Usuários (6 endpoints)

```
POST   /api/usuarios
       Criar novo usuário
       Body: {"nome":"João","email":"joao@example.com",...}

GET    /api/usuarios
       Listar todos os usuários

GET    /api/usuarios/{id}
       Obter usuário por ID
       Exemplo: /api/usuarios/1

GET    /api/usuarios/email/{email}
       Obter usuário por email
       Exemplo: /api/usuarios/email/joao@example.com

PUT    /api/usuarios/{id}
       Atualizar usuário
       Body: {"nome":"novo nome",...}

DELETE /api/usuarios/{id}
       Deletar usuário
       Exemplo: /api/usuarios/1
```

### 💰 Contas Bancárias (7 endpoints)

```
GET    /api/contas/usuario/{usuarioId}
       Obter conta do usuário

GET    /api/contas/saldo/{usuarioId}
       Consultar saldo da conta
       Retorna: {"saldo":5000,"limiteCheque":1000}

POST   /api/contas/depositar/{usuarioId}
       Fazer depósito
       Query: ?valor=1000

POST   /api/contas/sacar/{usuarioId}
       Fazer saque
       Query: ?valor=500

PUT    /api/contas/limite-cheque/{usuarioId}
       Atualizar limite de cheque
       Query: ?novoLimite=2000

POST   /api/contas/ativar/{usuarioId}
       Ativar conta

POST   /api/contas/desativar/{usuarioId}
       Desativar conta
```

### 💳 Cartões (8 endpoints)

```
POST   /api/cartoes
       Criar cartão
       Query: ?usuarioId=1&titular=NOME&dataValidade=12/25&limiteCredito=5000&tipoCartao=CREDITO

GET    /api/cartoes/{id}
       Obter cartão por ID

GET    /api/cartoes/usuario/{usuarioId}
       Listar cartões do usuário

POST   /api/cartoes/{id}/compra
       Realizar compra
       Query: ?valor=150&descricao=Compra

POST   /api/cartoes/{id}/pagar-fatura
       Pagar fatura
       Query: ?valor=200

POST   /api/cartoes/{id}/bloquear
       Bloquear cartão

POST   /api/cartoes/{id}/desbloquear
       Desbloquear cartão

POST   /api/cartoes/{id}/cancelar
       Cancelar cartão
```

### 🔄 Transferências (5 endpoints)

```
POST   /api/transferencias
       Realizar transferência
       Query: ?usuarioOrigemId=1&usuarioDestinoId=2&valor=250&tipoTransferencia=PIX

GET    /api/transferencias/{id}
       Obter transferência por ID

GET    /api/transferencias/enviadas/{usuarioId}
       Listar transferências enviadas

GET    /api/transferencias/recebidas/{usuarioId}
       Listar transferências recebidas

GET    /api/transferencias/todas/{usuarioId}
       Listar todas as transferências
```

### 📊 Empréstimos (7 endpoints)

```
POST   /api/emprestimos
       Solicitar empréstimo
       Query: ?usuarioId=1&valorTotal=10000&taxaJuros=5.5&numeroParcelas=12

GET    /api/emprestimos/{id}
       Obter empréstimo por ID

GET    /api/emprestimos/usuario/{usuarioId}
       Listar empréstimos do usuário

GET    /api/emprestimos/ativos/{usuarioId}
       Listar empréstimos ativos

POST   /api/emprestimos/{id}/aprovar
       Aprovar empréstimo

POST   /api/emprestimos/{id}/rejeitar
       Rejeitar empréstimo

POST   /api/emprestimos/{id}/pagamento
       Realizar pagamento
       Query: ?valor=1000
```

**Total: 32 endpoints funcionais**

---

## Exemplos de Uso

### Exemplo 1: Fluxo Completo de Usuário

```bash
# 1. Criar usuário
curl -X POST http://localhost:8080/api/usuarios \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "cpf": "12345678901",
    "telefone": "(11) 98765-4321",
    "dataNascimento": "1990-01-15",
    "endereco": "Rua A, 123, São Paulo"
  }'

# 2. Listar todos os usuários
curl http://localhost:8080/api/usuarios

# 3. Obter usuário específico
curl http://localhost:8080/api/usuarios/1

# 4. Atualizar usuário
curl -X PUT http://localhost:8080/api/usuarios/1 \
  -H "Content-Type: application/json" \
  -d '{"nome":"João Silva Atualizado"}'

# 5. Deletar usuário
curl -X DELETE http://localhost:8080/api/usuarios/1
```

### Exemplo 2: Operações Bancárias

```bash
# 1. Consultar saldo (usuário ID: 1)
curl http://localhost:8080/api/contas/saldo/1

# 2. Depositar R$ 5.000
curl -X POST "http://localhost:8080/api/contas/depositar/1?valor=5000"

# 3. Consultar novo saldo
curl http://localhost:8080/api/contas/saldo/1

# 4. Sacar R$ 1.000
curl -X POST "http://localhost:8080/api/contas/sacar/1?valor=1000"

# 5. Atualizar limite de cheque para R$ 2.000
curl -X PUT "http://localhost:8080/api/contas/limite-cheque/1?novoLimite=2000"
```

### Exemplo 3: Gerenciar Cartões

```bash
# 1. Criar cartão de crédito
curl -X POST "http://localhost:8080/api/cartoes?usuarioId=1&titular=JOAO%20SILVA&dataValidade=12/25&limiteCredito=5000&tipoCartao=CREDITO"

# 2. Listar cartões do usuário
curl http://localhost:8080/api/cartoes/usuario/1

# 3. Fazer compra
curl -X POST "http://localhost:8080/api/cartoes/1/compra?valor=150.00&descricao=Supermercado"

# 4. Pagar fatura
curl -X POST "http://localhost:8080/api/cartoes/1/pagar-fatura?valor=150.00"

# 5. Bloquear cartão
curl -X POST "http://localhost:8080/api/cartoes/1/bloquear"
```

### Exemplo 4: Transferências

```bash
# Pré-requisito: Criar segundo usuário (ID: 2)

# 1. Realizar transferência PIX
curl -X POST "http://localhost:8080/api/transferencias?usuarioOrigemId=1&usuarioDestinoId=2&valor=250.00&descricao=Pagamento&tipoTransferencia=PIX"

# 2. Listar transferências enviadas
curl http://localhost:8080/api/transferencias/enviadas/1

# 3. Listar transferências recebidas
curl http://localhost:8080/api/transferencias/recebidas/2
```

### Exemplo 5: Empréstimos

```bash
# 1. Solicitar empréstimo
curl -X POST "http://localhost:8080/api/emprestimos?usuarioId=1&valorTotal=10000&taxaJuros=5.50&numeroParcelas=12"

# 2. Listar empréstimos do usuário
curl http://localhost:8080/api/emprestimos/usuario/1

# 3. Aprovar empréstimo
curl -X POST "http://localhost:8080/api/emprestimos/1/aprovar"

# 4. Fazer pagamento (primeira parcela)
curl -X POST "http://localhost:8080/api/emprestimos/1/pagamento?valor=1000"

# 5. Listar empréstimos ativos
curl http://localhost:8080/api/emprestimos/ativos/1
```

---


