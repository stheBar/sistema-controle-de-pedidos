# Sistema de Controle de Pedidos — Documentação

**Última atualização:** 2025-10-27 01:47:59

Este README documenta os principais **controllers**, **fluxos** e a **autenticação/roles** do sistema.

## Visão Geral

Aplicação Spring Boot para gerenciamento de **mesas**, **contas**, **produtos** e **pedidos** em um restaurante/cafeteria.
Configurações importantes (em `application.properties`):

- `spring.application.name` = `sistema-controle-pedidos`
- `server.port` = `8082`
- `server.servlet.context-path` = `/sistema-controle-pedidos`
- `spring.datasource.url` = `jdbc:postgresql://localhost:5432/controlepedidos`
- `jwt.secret` = **definido**
- `jwt.expiration` = `3600000` (ms)

## Modelo de Dados (V1)

Estruturas principais definidas em `V1__criacao-tabela.sql`:

```sql
-- ===============================
-- V1__criar_tabelas_iniciais.sql
-- Sistema de Controle de Pedidos
-- ===============================

-- Usuário
CREATE TABLE IF NOT EXISTS usuario (
                                       id_usuario BIGSERIAL PRIMARY KEY,
                                       nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(72) NOT NULL,
    usuario_tipo VARCHAR(20) NOT NULL
    );

-- Mesa
CREATE TABLE IF NOT EXISTS mesa (
                                    id_mesa BIGSERIAL PRIMARY KEY,
                                    numero INT NOT NULL UNIQUE,
                                    disponivel BOOLEAN NOT NULL DEFAULT TRUE
);

-- Produto
CREATE TABLE IF NOT EXISTS produto (
                                       id_produto BIGSERIAL PRIMARY KEY,
                                       nome VARCHAR(100) NOT NULL,
    descricao TEXT,
    preco NUMERIC(10,2) NOT NULL,
    disponivel BOOLEAN NOT NULL DEFAULT TRUE,
    categoria VARCHAR(20) NOT NULL
    );

-- Conta
CREATE TABLE IF NOT EXISTS conta (
                                     id_conta BIGSERIAL PRIMARY KEY,
                                     id_mesa BIGINT NOT NULL,
                                     cpf_titular VARCHAR(14),
    nome_titular VARCHAR(100),
    conta_status VARCHAR(20) NOT NULL DEFAULT 'ABERTA',
    data_abertura TIMESTAMP NOT NULL DEFAULT NOW(),
    data_fechamento TIMESTAMP,
    valor_total NUMERIC(14,2) NOT NULL DEFAULT 0,
    forma_pagamento VARCHAR(20),
    CONSTRAINT fk_conta_mesa FOREIGN KEY (id_mesa) REFERENCES mesa(id_mesa),
    CONSTRAINT ck_conta_forma_pagamento CHECK (
                                                  forma_pagamento IN ('DINHEIRO','CARTAO','PIX') OR forma_pagamento IS NULL
    )
    );

-- Índice: garante apenas uma conta ABERTA por mesa
CREATE UNIQUE INDEX IF NOT EXISTS uq_conta_aberta_por_mesa
    ON conta (id_mesa)
    WHERE conta_status = 'ABERTA';

-- Pedido
CREATE TABLE IF NOT EXISTS pedido (
                                      id_pedido BIGSERIAL PRIMARY KEY,
                                      id_usuario BIGINT NOT NULL,
                                      id_conta BIGINT NOT NULL,
                                      data_hora_pedido TIMESTAMP NOT NULL DEFAULT NOW(),
    pedido_status VARCHAR(20) NOT NULL,
    CONSTRAINT fk_pedido_usuario FOREIGN KEY (id_usuario) REFERENCES usuario(id_usuario),
    CONSTRAINT fk_pedido_conta FOREIGN KEY (id_conta) REFERENCES conta(id_conta)
    );

-- Itens do pedido
CREATE TABLE IF NOT EXISTS item_pedido (
                                           id_item_pedido BIGSERIAL PRIMARY KEY,
                                           id_pedido BIGINT NOT NULL,
                                           id_produto BIGINT NOT NULL,
                                           quantidade INT NOT NULL,
                                           preco_unitario NUMERIC(10,2) NOT NULL,
    observacao TEXT,
    CONSTRAINT fk_item_pedido_pedido FO
```

_Obs.: SQL completo no arquivo `V1__criacao-tabela.sql`._

## Autenticação e Autorização (JWT)

- **Login:** endpoint no `AuthController` (ex.: `POST /auth/login`) retorna **JWT**.
- **Registro:** (`POST /auth/registrar`) cria um usuário com `usuario_tipo`.
- O cliente deve enviar o token nas requisições protegidas via header: `Authorization: Bearer <token>`.
- Chave e expiração configuráveis por `jwt.secret` e `jwt.expiration`.
- **Perfis (roles) previstos** (ajuste conforme seu código): ADMIN, GARCOM, COZINHA, CAIXA.
  - *ADMIN*: gestão completa (usuários, mesas, produtos, contas).
  - *GARCOM*: abrir conta, lançar pedidos.
  - *COZINHA*: atualizar status de preparo.
  - *CAIXA*: fechar conta e registrar pagamento.

## Controllers e Endpoints

### AuthController

- **Base path:** `/auth`

| Método | Caminho | Caminho completo |
|---|---|---|

| `POST` | `/register` | `/auth/register` |

| `POST` | `/login` | `/auth/login` |



### MesaController

- **Base path:** `/mesa`

| Método | Caminho | Caminho completo |
|---|---|---|

| `GET` | `/{id}` | `/mesa/{id}` |



### ContaController

- **Base path:** `/conta`

| Método | Caminho | Caminho completo |
|---|---|---|

| `GET` | `/{id}` | `/conta/{id}` |

| `POST` | `/{id}/pedido` | `/conta/{id}/pedido` |

| `PUT` | `/{id}/pedir` | `/conta/{id}/pedir` |

| `PUT` | `/{id}/pagar` | `/conta/{id}/pagar` |

| `DELETE` | `/{id}` | `/conta/{id}` |



### PedidoController

- **Base path:** `/pedido`

_Não foi possível extrair endpoints automaticamente; veja o código para detalhes._

```java
package com.csi.sistema_controle_pedidos.controller;

import com.csi.sistema_controle_pedidos.model.Pedido;
import com.csi.sistema_controle_pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedido")
@Tag(name = "Pedidos", description = "Operações relacionadas a pedidos individuais")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @Operation(summary = "Listar pedidos de uma mesa", description = "Retorna uma lista de todos os pedidos associados à conta aberta de uma mesa específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Pedido.class))),
            @ApiResponse(responseCode = "404", description = "Mesa não encontrada ou sem conta aberta", content = @Content)
    })
    @GetMapping
    public List<Pedido> listarPedidosDeUmaMesa(@RequestParam(name = "idMesa") long idMesa) {
        return pedidoService.listarPedidosDeUmaMesa(idMesa);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar um novo pedido", description = "Cria um novo pedido (geralmente associado a uma conta).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso"),
            @ApiResponse(responseCode = "400", de
```

### ProdutoController

- **Base path:** `/produto`

| Método | Caminho | Caminho completo |
|---|---|---|

| `GET` | `/{id}` | `/produto/{id}` |

| `DELETE` | `/{id}` | `/produto/{id}` |



## Fluxos de Negócio

### Criar Mesa

1. Autenticar como ADMIN (ou perfil com permissão de gestão de mesas).
2. POST /mesas com { numero } para cadastrar a mesa.
3. Resposta retorna id_mesa e estado disponivel=true.


### Criar Usuário

1. Autenticar como ADMIN.
2. POST /auth/registrar com nome, email, senha e usuario_tipo (role).
3. Usuário pode então efetuar login em /auth/login para obter o JWT.


### Criar Conta

1. Autenticar como GARCOM ou ADMIN.
2. Garantir que exista uma mesa cadastrada e disponível.
3. POST /contas com { idMesa, cpfTitular?, nomeTitular? } → cria conta com status=ABERTA.
4. Regra: 1 conta ABERTA por mesa (índice único uq_conta_aberta_por_mesa).


### Criar Pedido

1. Autenticar como GARCOM (ou perfil autorizado).
2. POST /pedidos com { idConta, itens:[ { idProduto, quantidade, observacao? } ] }.
3. Sistema cria o registro do pedido e os itens do pedido. O total da conta é atualizado conforme a regra de negócio.
4. Fluxos posteriores: atualizar status do pedido (ex.: EM_PREPARO, PRONTO, ENTREGUE) e fechar a conta com forma_pagamento.


### Fechamento da Conta

1. Autenticar como CAIXA/ADMIN.
2. PUT /contas/{id}/fechar com { formaPagamento } (DINHEIRO, CARTAO, PIX).
3. Atualiza valor_total, data_fechamento e status=FECHADA.


## OpenAPI (sistema-pedidos.yaml)

Um arquivo OpenAPI foi fornecido. Trecho inicial:

```yaml
type: collection.insomnia.rest/5.0
name: Scratch Pad
meta:
  id: wrk_scratchpad
  created: 1718919659119
  modified: 1718919659119
  description: ""
collection:
  - name: Produto
    meta:
      id: fld_0e4510ec35ad4b5582a1fd6e361a8271
      created: 1760925397545
      modified: 1760925397545
      sortKey: -1
      description: CRUD de produtos
    children:
      - url: "{{ api }}/produto"
        name: Listar Produtos
        meta:
          id: req_00586dcb20e048faba18f12b0f366746
          created: 1760925397545
          modified: 1760925397545
          isPrivate: false
          description: ""
          sortKey: -1
        method: GET
        headers:
          - id: hdr_4480ba7d30274
            name: Authorization
            value: Bearer {{ jwt_token }}
          - id: hdr_636a8fcee3b04
            name: Content-Type
            value: application/json
        settings:
          renderRequestBody: true
          encodeUrl: true
          followRedirects: global
          cookies:
            send: true
            store: true
          rebuildPath: true
      - url: "{{ api }}/produto/{{ produto_id }}"
        name: Excluir Produto
        meta:
          id: req_0d060d239576403db5be91565a286e1f
          created: 1760925397545
          modified: 1760925397545
          isPrivate: false
          description: ""
          sortKey: -1
        method: DELETE
        headers:
          - id: hdr_4480ba7d30274
            name: Authorization
            value: Bearer {{ jwt_token }}
          - id: hdr_636a8fcee3b04
            name: Content-Type
            value: application/json
        settings:
          renderRequestBody: true
          encodeUrl: true
          followRedirects: global
          cookies:
            send: true
            store: true
          rebuildPath: true
      - url: "{{ api }}/produto"
        name: Cadastrar Produto
        meta:
          id: req_336a03824eb84feba724fb22da79b3d0
          created: 1760925397545
          modified: 1760925397545
          isPrivate: false
          description: ""
          sortKey: -1
        method: POST
        body:
          mimeType: application/json
          text: >-
            {
              "nome": "X-Burger Duplo com Bacon",
              "descricao": "Dois hambúrgueres 150g, queijo cheddar, bacon crocante e pão brioche.",
              "preco": 32.5,
              "disponivel": true,
              "produtoCategoria": "COMIDA"
            }
        headers:
          - id: hdr_4480ba7d30274
            name: Authorization
            value: Bearer {{ jwt_token }}
          - id: hdr_636a8fcee3b04
            name: Content-Type
            value: application/json
        settings:
          renderRequestBody: true
          encodeUrl: true
          followRedirects: global
          cookies:
            send: true
            store: true
          rebuildPath: true
      - url: "{{ api }}/produto/{{ produto_id }}"
        name: Buscar Produto por ID
        meta:
          id: req_3c20d0c76b72417a910f7dd3b437d4c7
          created: 1760925397545
          modified: 1760925397545
          isPrivate: false
          description: ""
          sortKey: -1
        method: GET
        headers:
          - id: hdr_4480ba7d30274
            name: Authorization
            value: Bearer {{ jwt_token }}
          - id: hdr_636a8fcee3b04
            name: Content-Type
            value: application/json
        settings:
          renderRequestBody: true
          encodeUrl: true
          followRedirects: global
          cookies:
            send: true
            store: true
          rebuildPath: true
      - url: "{{ api }}/produto"
        name: Atualizar Produto
        meta:
          id: req_d670ca80cd2a4dc4b64146454755f729
          created: 1760925397545
          modified: 1760925397545
          isPrivate: false
          description: ""
          sortKey: -1
        method: PUT
        body:
          mimeType: application/json
      
```

_Use esta especificação para gerar clientes/SDKs e explorar via Swagger UI / ReDoc._

## Exemplos de Requisição (cURL)

**Login**

```bash
curl -X POST "http://localhost:8082/sistema-controle-pedidos/auth/login" \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@local","senha":"123456"}'
```

**Criar Mesa (ADMIN)**

```bash
curl -X POST "http://localhost:8082/sistema-controle-pedidos/mesas" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"numero": 10}'
```

**Abrir Conta (GARÇOM/ADMIN)**

```bash
curl -X POST "http://localhost:8082/sistema-controle-pedidos/contas" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"idMesa": 1, "cpfTitular":"123.456.789-00", "nomeTitular":"Cliente"}'
```

**Criar Pedido (GARÇOM)**

```bash
curl -X POST "http://localhost:8082/sistema-controle-pedidos/pedidos" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"idConta":1,"itens":[{"idProduto":2,"quantidade":1,"observacao":"Sem cebola"}]}'
```

**Fechar Conta (CAIXA/ADMIN)**

```bash
curl -X PUT "http://localhost:8082/sistema-controle-pedidos/contas/1/fechar" \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"formaPagamento":"PIX"}'
```

## Observações de Segurança

- **Proteja `jwt.secret`** via variáveis de ambiente/secret manager; não deixe hardcoded em produção.
- Utilize **HTTPS** em produção para proteger o token em trânsito.
- Considere políticas de **refresh token** e **revogação** conforme sua necessidade.
- Valide **roles** em cada endpoint sensível (ex.: `@PreAuthorize`/`@RolesAllowed`).
