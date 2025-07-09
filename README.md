# Sistema Bancário Robusto

## Visão Geral
Sistema bancário completo desenvolvido com Java 17 e Spring Boot 3., seguindo os princípios SOLID, GRASP e Clean Code.

## Tecnologias Utilizadas
- **Java 17** - Linguagem de programação
- **Spring Boot 3.2** - Framework principal
- **Spring Data JPA** - Persistência de dados
- **Spring Security** - Segurança e autenticação
- **PostgreSQL** - Banco de dados principal
- **H2 Database** - Banco para desenvolvimento e testes
- **Docker & Docker Compose** - Containerização
- **Swagger/OpenAPI** - Documentação da API
- **JUnit 5 & Mockito** - Testes unitários

## Funcionalidades

### Gerenciamento de Usuários
- Cadastro de usuários com validação de CPF e email únicos
- Autenticação e autorização
- Criptografia de senhas com BCrypt
- Ativação/desativação de contas

### Gerenciamento de Contas Bancárias
- Criação de contas (Corrente, Poupança, Salário)
- Geração automática de números de conta
- Limite de 5 contas por usuário
- Validações de negócio específicas por tipo de conta

### Operações Bancárias
- **Depósitos** - Com validação de valores
- **Saques** - Com verificação de saldo
- **Transferências** - Entre contas com validações completas
- **PIX** - Transferências instantâneas com limite de R$ 20.000
- **Extratos** - Consulta paginada e por período

### Auditoria e Logs
- Rastreamento completo de todas as operações
- Log de transações bem-sucedidas e falhadas
- Auditoria de alterações em entidades

## Arquitetura

O sistema segue os princípios de Clean Architecture e está organizado em camadas:

```
src/main/java/com/banco/
├── SistemaBancarioApplication.java     # Classe principal
├── application/                        # Camada de aplicação
│   ├── dto/                           # Data Transfer Objects
│   └── services/                      # Interfaces e implementações de serviços
├── domain/                            # Camada de domínio
│   ├── entities/                      # Entidades de negócio
│   └── enums/                         # Enumerações
├── infrastructure/                    # Camada de infraestrutura
│   ├── config/                        # Configurações
│   ├── exception/                     # Tratamento de exceções
│   └── repositories/                  # Repositórios JPA
└── presentation/                      # Camada de apresentação
    └── controllers/                   # Controladores REST
```

## Princípios SOLID Aplicados

1. **Single Responsibility Principle (SRP)** - Cada classe tem uma única responsabilidade
2. **Open/Closed Principle (OCP)** - Extensível via interfaces e abstrações
3. **Liskov Substitution Principle (LSP)** - Implementações intercambiáveis
4. **Interface Segregation Principle (ISP)** - Interfaces específicas e coesas
5. **Dependency Inversion Principle (DIP)** - Dependências de abstrações, não concretizações

## Princípios GRASP Aplicados

- **Information Expert** - Entidades contêm lógica de negócio relevante
- **Creator** - Factory methods para criação de objetos
- **Controller** - Controladores REST como coordenadores
- **Low Coupling** - Baixo acoplamento entre camadas
- **High Cohesion** - Alta coesão dentro das classes

## Como Executar

### Desenvolvimento (H2 Database)
```bash
mvn spring-boot:run
```

### Produção com Docker
```bash
docker-compose up -d
```

## Endpoints da API

### Usuários
- `POST /api/usuarios` - Criar usuário
- `GET /api/usuarios/{id}` - Buscar por ID
- `GET /api/usuarios` - Listar todos
- `PUT /api/usuarios/{id}` - Atualizar
- `DELETE /api/usuarios/{id}` - Desativar

### Contas
- `POST /api/contas` - Criar conta
- `GET /api/contas/{id}` - Buscar por ID
- `GET /api/contas/numero/{numero}` - Buscar por número
- `GET /api/contas/usuario/{usuarioId}` - Listar por usuário

### Transações
- `POST /api/transacoes/deposito` - Realizar depósito
- `POST /api/transacoes/saque` - Realizar saque
- `POST /api/transacoes/transferencia` - Transferir
- `POST /api/transacoes/pix` - PIX
- `GET /api/transacoes/extrato/{numeroConta}` - Extrato

## Documentação
Acesse `http://localhost:8080/swagger-ui.html` para documentação interativa da API.

## Testes
Execute os testes com:
```bash
mvn test
```

## Características de Robustez

- **Tratamento de Exceções** - GlobalExceptionHandler para todas as exceções
- **Validação de Dados** - Bean Validation em todos os DTOs
- **Transações** - Operações atômicas com @Transactional
- **Auditoria** - Log completo de operações
- **Segurança** - Spring Security com BCrypt
- **Monitoramento** - Spring Boot Actuator
- **Containerização** - Docker ready
- **Testes** - Cobertura de testes unitários
