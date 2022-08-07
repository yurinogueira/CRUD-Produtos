# CRUD de Produtos Front-End e Back-End

Protótipo de um CRUD de Produtos feito como parte do processo seletivo da Siteware.

### Como usar
Após subir o projeto, você pode criar uma conta para você pelo próprio
site, porém essa conta só irá possuir as permissões de usuário,
ela poderá ver o histórico de pedidos dela, ver o catalogo de produtos,
adicionar produtos no carrinho e confirmar o pedido do carrinho.

Para poder adicionar, remover e editar produtos basta acessar utilizando
a conta de administração, credenciais abaixo:
- login: admin@email.com
- senha: admin


### Sobre
- O protótipo conta com um site (rodando na porta 8080) e uma API (rodando na porta 8000)
- Sendo a API uma API Restfull
- Veja as imagens sobre o projeto na pasta [imagens](images/) 

----
## Dependências:

- Docker >= 20.10.00
- Docker Compose >= 1.28.0

----
## Rodando

Para rodar o protótipo segue abaixo os comandos.

### Usando o Makefile

- `make build-development` para buildar o Back-End e o Front-End
- `make development` para subir os containers docker

### Sem usar o Makefile

- `rm -rf spring/target/` para garantir a remoção de qualquer build local do Back-End
- `rm -rf vue/node_modules/` para garantir a remoção de qualquer build local do Front-End
- `docker-compose build --force-rm --no-cache --pull` para buildar o Back-End e o Front-End
- `docker-compose up` para subir os containers docker

----
## Tecnologias Utilizadas

### Back-End
|          Tecnologia       | Versão  |
| :---                      |    ---: |
| Java                      | 17      |
| Spring Boot Framework     | 2.7.1   |
| JJWT                      | 0.11.5  |
| SpringDoc OpenAPI Swagger | 1.6.9   |
| Lombok                    | 1.18.24 |
| H2                        | 2.1.214 |
| PostgreSQL                | 42.4.1  |

### Front-End
|          Tecnologia       | Versão  |
| :---                      |    ---: |
| VueJs                     | 3.2.37  |
| VueRouter                 | 4.1.2   |
| ViteJs                    | 3.0.1   |
| ElementPlus               | 2.1.9   |
| cpf-cnpj-validator        | 1.0.3   |
