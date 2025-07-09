Sistema de Controle de Estoque -
Sistema de gerenciamento de estoque desenvolvido em Java com JavaFX (interface gráfica construída com Scene Builder) e integração com banco de dados MySQL. Idealizado para controle eficiente de entradas e saídas de produtos, histórico de ações, relatórios e gestão segura por login e senha.

Funcionalidades
1 - Login
Autenticação segura com usuário e senha (armazenada de forma criptografada).

Recuperação e alteração de senha integrada.

2 - Tela Principal
Menu lateral com acesso rápido às funcionalidades:

• Atualização de Produtos

• Entrada de Produtos

• Saída de Produtos

• Consulta de Estoque

• Geração de Relatórios

• Histórico de Ações

3 - Cadastro e Atualização de Produtos
Registro de produtos com:

Nome

Código

Quantidade

Preço Unitário

Fornecedor

Categoria

Atualização facilitada por busca.

• Entrada de Produtos
Entrada de novos lotes no estoque.

Atualização automática da quantidade existente.

• Saída de Produtos
Venda ou retirada de produtos do estoque.

Tela dividida em duas abas:

Venda: Seleção e finalização da saída com cálculo do total.

Excluir Produto: Remoção permanente do produto.

• Consulta de Estoque
Busca filtrada por nome, categoria ou fornecedor.

Visualização da quantidade, preço e demais dados do produto.

• Geração de Relatórios
Seleção de tipo de relatório (baixa de estoque, histórico de entradas e saídas, etc.).

Formatos de visualização e exportação em PDF.

• Histórico de Ações
Registro detalhado das ações dos usuários.

Filtros por nome e data para auditoria.

• Tecnologias Utilizadas
Java 11+

JavaFX (FXML via Scene Builder)

MySQL (banco de dados relacional)

JDBC (conexão com banco de dados)

Maven (gerenciamento de dependências)

• Segurança
Cada ação relevante no sistema é registrada na tabela historico.

O sistema exige nivel de login para acesso às funcionalidades mais avançadas.

Mudança de senha protegida por verificação da senha atual.

• Capturas de Tela
• Login
• Tela Principal
• Entrada de Produtos
• Cadastro de Produto
• Saída de Produtos
• Excluir Produto
• Consultar Estoque
• Gerar Relatórios
• Histórico de Ações


• Requisitos
Java 11 ou superior

MySQL Server 8.0+

IDE (IntelliJ, Eclipse)

Scene Builder (para edição visual de telas FXML)

👨‍💻 Autor
Gabriel Oliveira Braga
Desenvolvedor Full Stack
© 2025 – Todos os direitos reservados.
