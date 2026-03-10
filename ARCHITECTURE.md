# Arquitetura da Bank API

## Diagrama de Camadas

```
┌─────────────────────────────────────────────────────────────┐
│                     CLIENTE (Browser/Postman)               │
└──────────────────────────┬──────────────────────────────────┘
                           │
                    HTTP / REST
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                    SPRING WEB MVC (Camada de Apresentação)  │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Controllers                                             │ │
│  │ ├── UsuarioController                                  │ │
│  │ ├── ContaBancariaController                            │ │
│  │ ├── CartaoController                                   │ │
│  │ ├── TransferenciaController                            │ │
│  │ └── EmprestimoController                               │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────┘
                           │
                    Chamadas de Serviço
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                  SPRING SERVICES (Camada de Negócio)        │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Services - Lógica de Negócio                           │ │
│  │ ├── UsuarioService                                     │ │
│  │ ├── ContaBancariaService                               │ │
│  │ ├── CartaoService                                      │ │
│  │ ├── TransferenciaService                               │ │
│  │ └── EmprestimoService                                  │ │
│  └────────────────────────────────────────────────────────┘ │
│                                                              │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Validações e Tratamento de Erros                       │ │
│  │ ├── GlobalExceptionHandler                             │ │
│  │ ├── Validações de Negócio                              │ │
│  │ └── CorsConfig                                         │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────┘
                           │
                    Acesso a Dados
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                    JPA/HIBERNATE (ORM)                      │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Repositories (Spring Data JPA)                         │ │
│  │ ├── UsuarioRepository                                  │ │
│  │ ├── ContaBancariaRepository                            │ │
│  │ ├── CartaoRepository                                   │ │
│  │ ├── TransferenciaRepository                            │ │
│  │ └── EmprestimoRepository                               │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────┬──────────────────────────────────┘
                           │
                    SQL / Queries
                           │
┌──────────────────────────▼──────────────────────────────────┐
│                   BANCO DE DADOS (MySQL)                    │
│  ┌────────────────────────────────────────────────────────┐ │
│  │ Tables                                                  │ │
│  │ ├── usuarios                                            │ │
│  │ ├── contas_bancarias                                    │ │
│  │ ├── cartoes                                             │ │
│  │ ├── transferencias                                      │ │
│  │ └── emprestimos                                         │ │
│  └────────────────────────────────────────────────────────┘ │
└──────────────────────────────────────────────────────────────┘
```

## Fluxo de Requisição

```
1. Cliente envia HTTP Request
                    │
                    ▼
2. Controller recebe e valida input
                    │
                    ▼
3. Controller chama Service
                    │
                    ▼
4. Service valida regras de negócio
                    │
                    ▼
5. Service chama Repository
                    │
                    ▼
6. Repository (JPA) faz query ao banco
                    │
                    ▼
7. Banco retorna dados
                    │
                    ▼
8. Repository converte para Objects
                    │
                    ▼
9. Service processa resultado
                    │
                    ▼
10. Controller converte para DTO
                    │
                    ▼
11. Spring serializa para JSON
                    │
                    ▼
12. HTTP Response retorna ao Cliente
```

## Relacionamentos de Dados

```
USUARIO (1) ────────────── (1) CONTA_BANCARIA
│ │
│ ├─────────── (N) CARTAO
│ │
│ ├─────────── (N) TRANSFERENCIA (origem)
│ │
│ ├─────────── (N) TRANSFERENCIA (destino)
│ │
│ └─────────── (N) EMPRESTIMO
│
└─ Herança: Validação, Timestamps (dataCriacao, dataAtualizacao)


CARTAO
├── StatusCartao: ATIVO, BLOQUEADO, CANCELADO
└── TipoCartao: CREDITO, DEBITO, PRE_PAGO


TRANSFERENCIA
├── TipoTransferencia: ENTRADA, SAIDA, PIX, DOC, TED
└── StatusTransferencia: PENDENTE, CONCLUIDA, CANCELADA, FALHA


EMPRESTIMO
└── StatusEmprestimo: APROVADO, EM_ANALISE, REJEITADO, QUITADO, INADIMPLENTE
```

## Estrutura de Pacotes

```
com.bank.api/
├── BankApiApplication.java (Main)
│
├── model/
│   ├── Usuario.java
│   ├── ContaBancaria.java
│   ├── Cartao.java
│   ├── Transferencia.java
│   ├── Emprestimo.java
│   ├── StatusCartao.java (enum)
│   ├── TipoCartao.java (enum)
│   ├── StatusTransferencia.java (enum)
│   ├── TipoTransferencia.java (enum)
│   └── StatusEmprestimo.java (enum)
│
├── repository/
│   ├── UsuarioRepository.java
│   ├── ContaBancariaRepository.java
│   ├── CartaoRepository.java
│   ├── TransferenciaRepository.java
│   └── EmprestimoRepository.java
│
├── service/
│   ├── UsuarioService.java
│   ├── ContaBancariaService.java
│   ├── CartaoService.java
│   ├── TransferenciaService.java
│   └── EmprestimoService.java
│
├── controller/
│   ├── UsuarioController.java
│   ├── ContaBancariaController.java
│   ├── CartaoController.java
│   ├── TransferenciaController.java
│   └── EmprestimoController.java
│
├── dto/
│   ├── UsuarioDTO.java
│   ├── UsuarioCriacaoDTO.java
│   ├── ContaBancariaDTO.java
│   ├── CartaoDTO.java
│   ├── TransferenciaDTO.java
│   └── EmprestimoDTO.java
│
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ErrorResponse.java
│
└── config/
    └── CorsConfig.java
```

## Fluxo de Transferência (Exemplo)

```
┌─────────────────────────────────────────────────────────────┐
│ 1. Cliente solicita transferência de João para Maria        │
└────────────────────────────┬────────────────────────────────┘
                             │
                             ▼
      ┌─────────────────────────────────────────┐
      │ 2. TransferenciaController recebe:      │
      │ - usuarioOrigem: João (ID: 1)           │
      │ - usuarioDestino: Maria (ID: 2)         │
      │ - valor: R$ 250,00                      │
      └────────────────┬────────────────────────┘
                       │
                       ▼
      ┌──────────────────────────────────────────┐
      │ 3. TransferenciaService valida:          │
      │ - Ambos usuários existem                 │
      │ - Não é a mesma conta                    │
      │ - Saldo disponível >= 250                │
      └────────────────┬─────────────────────────┘
                       │
                       ▼
      ┌──────────────────────────────────────────┐
      │ 4. Débito na conta de João:              │
      │ Saldo anterior: R$ 1000                  │
      │ Débito: R$ 250                           │
      │ Novo saldo: R$ 750                       │
      └────────────────┬─────────────────────────┘
                       │
                       ▼
      ┌──────────────────────────────────────────┐
      │ 5. Crédito na conta de Maria:            │
      │ Saldo anterior: R$ 500                   │
      │ Crédito: R$ 250                          │
      │ Novo saldo: R$ 750                       │
      └────────────────┬─────────────────────────┘
                       │
                       ▼
      ┌──────────────────────────────────────────┐
      │ 6. Registra transferência no histórico:  │
      │ - Status: CONCLUIDA                      │
      │ - Data/Hora: registrada                  │
      │ - Tipo: PIX                              │
      └────────────────┬─────────────────────────┘
                       │
                       ▼
      ┌──────────────────────────────────────────┐
      │ 7. Retorna DTO com sucesso               │
      │ HTTP 201 Created                         │
      └─────────────────────────────────────────┘
```

## Fluxo de Empréstimo (Exemplo)

```
┌──────────────────────────────────────────────────────┐
│ 1. Usuário solicita empréstimo de R$ 10.000,00       │
│    Taxa: 5.5% a.m.                                    │
│    Parcelas: 12                                        │
└───────────────────┬──────────────────────────────────┘
                    │
                    ▼
    ┌──────────────────────────────────────┐
    │ 2. EmprestimoService cria:           │
    │ Status: EM_ANALISE                    │
    │ Valor: R$ 10.000                      │
    │ Juros: 5.5% ao mês                    │
    │ Vencimento: +1 mês                    │
    └──────────────┬───────────────────────┘
                   │
                   ▼
    ┌──────────────────────────────────────┐
    │ 3. Gerente aprova empréstimo:        │
    │ Status muda para: APROVADO            │
    └──────────────┬───────────────────────┘
                   │
                   ▼
    ┌──────────────────────────────────────┐
    │ 4. Saldo creditado na conta:         │
    │ + R$ 10.000 ao saldo                  │
    └──────────────┬───────────────────────┘
                   │
                   ▼
    ┌──────────────────────────────────────┐
    │ 5. Usuário recebe 12 cobranças de:   │
    │ R$ 833,33 (aprox) cada                │
    └──────────────┬───────────────────────┘
                   │
                   ▼
    ┌──────────────────────────────────────┐
    │ 6. Após 12 pagamentos:               │
    │ Status: QUITADO                       │
    │ Saldo devedor: R$ 0                   │
    │ Parcelas quitadas: 12/12              │
    └──────────────────────────────────────┘
```

## Segurança (Pontos de Melhoria para Produção)

```
Para produção, adicionar:

1. Spring Security
   - JWT Authentication
   - OAuth2
   - Role-based access control

2. Validações
   - Limite máximo de transferência
   - Limite máximo de empréstimo
   - Limite de acesso (IP whitelist)

3. Auditoria
   - Logging de todas operações
   - Rastreamento de IP
   - Tentativas de acesso não autorizado

4. Encriptação
   - Senhas com BCrypt
   - SSL/TLS em produção
   - Dados sensíveis criptografados
```

## Performance (Recomendações)

```
1. Índices de Banco de Dados
   - usuario_id em cartoes
   - usuario_id em emprestimos
   - Índices em chaves estrangeiras

2. Cache
   - Redis para cache de usuários
   - Cache de contas bancárias

3. Paginação
   - Implementar paginação em listagens
   - Page e Pageable do Spring Data

4. Query Optimization
   - Usar @Query customizadas
   - Lazy loading onde apropriado
   - N+1 query prevention
```

