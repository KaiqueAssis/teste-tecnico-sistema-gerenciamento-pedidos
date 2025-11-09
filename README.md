
**API REST** desenvolvida em **Spring Boot 3.5.7** para gerenciamento de pedidos de usuários e criação de produtos por administradores.  
O sistema implementa autenticação e autorização com **JWT**, persistência de dados com **Spring Data JPA** e documentação de endpoints via **Swagger UI**.

---

## 🚀 Tecnologias Utilizadas

- ☕ **Java 17**  
- 🌱 **Spring Boot 3.5.7**
  - Spring Web
  - Spring Data JPA
  - Spring Security
- 🗄️ **MySQL**
- 🧩 **JWT (JSON Web Token)** para autenticação
- 🧪 **JUnit 5** para testes unitários
- 📘 **Swagger / OpenAPI** para documentação

---

### ⚠️ **Importante**

- Para realizar a autenticação com os **usuários já existentes no banco**, o **login e a senha são iguais**.  
  > Exemplo: se o usuário for `admin`, então a senha também será `admin`.  
- Após autenticar, o sistema retornará um **token JWT**, que deve ser informado no campo **"Authorize"** do Swagger para liberar os endpoints protegidos.  
- Esse comportamento foi implementado apenas para **facilitar os testes da API** durante o desenvolvimento.
- Foram criado 7 usuários, admin, user1, user2, user3, user4, user5, user6; 

---

## ⚙️ Como Rodar o Projeto

### ✅ **Pré-requisitos**

Antes de rodar o projeto, certifique-se de ter instalado:
- **Java 17**  
- **MySQL**  
- **Maven**

---

### 🧩 **1️⃣ Criar o banco de dados**

Acesse o MySQL e crie um schema (banco de dados) para o projeto:

```sql
CREATE DATABASE pedidos_db;
```

### 🧩 **2️⃣  Ajuste o application.propeties**

Ajuste o application propeties para seu contexto colocando a senha do seu banco e usuario e se conectando ao banco

```application.properties
spring.datasource.url=jdbc:mysql://localhost:3306/nome_do_seu_banco?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=America/Sao_Paulo&allowPublicKeyRetrieval=true&useSSL=false
No application.yml:
spring.datasource.username=root
spring.datasource.password=suasenhaforte
```

### 🧩 **3️⃣ Rodando o dump.sql**

Agora rode o dump.sql que está dentro da pasta raiz do projeto ao mesmo nivel da pom.xml.

```bash
mysql -u seu_usuario -p sua_senha banco_ou_schema < dump.sql
```

### 🧩 **4️⃣ Rodar o Projeto**

Após importar o banco e configurar o application.properties, rode o projeto em uma eclipse ou Intellij ou execute:

````bash
mvn spring-boot:run

````
O projeto vai rodar na porta 8080 ou outra porta caso você tenha configurado.

###  **📘Documentação da API (Swagger)**

Para acessar a documentação da Api pelo swagger você precisa apenas acessar a url:
http://localhost:8080/swagger-ui/index.html
quando estiver rodando o projeto

### **🧪 Testes Unitários**

Para rodar os teste você pode usar a interface gráfica ou o camando:

```bash
mvn test
```

### **👥 Perfis de Usuário**

- Usuário comum: pode criar e listar pedidos.
- Administrador: pode cadastra, gerenciar produtos e pode usar os endpoints de relatório.

### **🧩 Observações / Diferenças na Implementação**

- Não ficou claro para mim porque nos requisitos do teste técnico o endpoint de listar produtos, pq apenas os usuários do tipo USER poderia usar eles. Então eu deixei ele público(Faz mais sentido para mim já que os ADMIN tbm deveria ver os produtos que criaram), creio que tenha sido um engano.
- Sobre a parte de SQL otimizados, eu criei um endpoint de relatório que só poderia ser visto por usuários admin já que todos os feature que pediram interessaria apenas os administradores da empresa.
- Todo o SQL das tabela eu usei o JPA para gerenciar isso e fazer as criações, já que era uma das tecnologias pedidas, tentei usar todo o seu potêncial.
- Importante para fazer o login com qualquer o usuário, o login é igual a senha!!
