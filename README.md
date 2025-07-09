# Sistema de Gerenciamento de Estoque
Sistema de gerenciamento de estoque desenvolvido em Java, com interface gráfica construída em JavaFX (Scene Builder) e integração com banco de dados MySQL.
Projetado para oferecer controle eficiente sobre entradas e saídas de produtos, histórico de ações, geração de relatórios e segurança através de autenticação de usuários.

Autenticação
Login com usuário e senha criptografada (armazenamento seguro no banco de dados).

Recuperação e alteração de senha com verificação da senha atual.

Tela Principal
A tela principal conta com um menu lateral que dá acesso rápido às seguintes funcionalidades:

Atualização de Produtos

Entrada de Produtos

Saída de Produtos

Consulta de Estoque

Geração de Relatórios

Histórico de Ações

Funcionalidades
Cadastro e Atualização de Produtos
Registro de produtos com os seguintes campos:

Nome

Código

Quantidade

Preço Unitário

Fornecedor

Categoria

Atualização simplificada com busca por nome ou código.

Entrada de Produtos
Registro de novos lotes de produtos no estoque.

Atualização automática da quantidade existente.

Saída de Produtos
Controle de vendas ou retirada de itens do estoque.

Duas abas distintas:

Venda: seleção de itens e cálculo automático do valor total.

Excluir Produto: remoção definitiva do produto do sistema.

Consulta de Estoque
Filtros por nome, categoria ou fornecedor.

Exibição das informações completas do produto, incluindo quantidade e preço.

Geração de Relatórios
Escolha do tipo de relatório:

Produtos com baixo estoque

Histórico de entradas e saídas

Resumo geral

Visualização direta ou exportação em PDF.

Histórico de Ações
Registro completo das ações realizadas por usuários.

Filtros por nome e data, permitindo auditoria precisa.

Segurança
Senhas armazenadas de forma criptografada.

Alteração de senha condicionada à verificação da senha atual.

Registro de todas as ações relevantes na tabela de histórico.

Níveis de permissão para controle de acesso às funcionalidades.

Tecnologias Utilizadas
Java 11 ou superior

JavaFX (FXML com Scene Builder)

MySQL (banco de dados relacional)

JDBC (conexão com banco de dados)

Maven (gerenciador de dependências)

Capturas de Tela
Tela de Login

Tela Principal

Cadastro de Produto

Entrada de Produtos

Saída de Produtos

Exclusão de Produto

Consulta de Estoque

Geração de Relatórios

Histórico de Ações

Requisitos para Execução
Java 11 ou superior

MySQL Server 8.0 ou superior

IDE (IntelliJ IDEA, Eclipse)

Scene Builder (para edição visual das interfaces FXML)

Autor
Gabriel Oliveira Braga
Desenvolvedor Full Stack

© 2025 – Todos os direitos reservados.
