# CRUD de Produtos Front-End e Back-End

Protótipo de um CRUD de Produtos feito como parte do processo seletivo da Siteware.

----
## Dependências:

- Docker >= 20.10.00
- Docker Compose >= 1.28.0

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

----
## Rodando

Para rodar o protótipo segue abaixo os comandos.

### Usando o Makefile

- `make build-development` para buildar o Back-End e o Front-End
- `make development` para subir os containers docker

### Sem usar o Makefile

- `docker-compose build --force-rm --no-cache --pull` para buildar o Back-End e o Front-End
- `docker-compose up` para subir os containers docker
