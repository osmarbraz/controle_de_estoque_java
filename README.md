# Sistema de Controle de Estoque & Pedidos (SCEP)

Sistema web desenvolvido em **Java** para gerenciamento de clientes, cidades, categorias, produtos, estoque, pedidos e itens de pedidos.

O projeto utiliza uma arquitetura baseada em **JSP + Servlets + DAO + MySQL**, com o **Apache Maven** para gerenciamento de dependências, compilação e deploy, e o **Apache Tomcat** como servidor web.

---

## Objetivo

O **Sistema de Controle de Estoque & Pedidos (SCEP)** tem como objetivo permitir o gerenciamento de:

* Clientes;
* Cidades;
* Categorias de produtos;
* Produtos;
* Estoque;
* Pedidos;
* Itens dos pedidos;
* Relatórios gerenciais.

O sistema deve possuir uma interface web simples, padronizada e responsiva, utilizando **JSP**, **CSS** e **JavaScript**.

## Comandos Docker para Execução
 - Utilizar o terminal do Windows Powershel em modo administrador.

### Para criar os conteiner e os serviços
 - ```docker compose up --build```

### Parar os serviços
 - ```docker compose down -v```

### Abra o navegador em:
 - http://localhost:8080/

### Remover as imagens
 - ```docker compose down --rmi all```

## Comandos Maven  para Execução
 - Utilizar o terminal do Windows Powershel em modo administrador.

### Construir a aplicação
 - ```mvn clean package```

### Deploy/Rodar a aplicação
 - ```mvn tomcat7:run```

### Undeploy/Remover a aplicação
 - ```mvn tomcat7:undeploy```

### Abra o navegador em:
 - http://localhost:8080/controle-de-estoque/