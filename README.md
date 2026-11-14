# 🚀 Compilador Java (Lexer, Parser e Analisador Semântico)

Projeto de desenvolvimento de um compilador construído 100% do zero em Java (sem uso de bibliotecas externas de parsing como ANTLR ou Yacc). Avaliação AV3 da disciplina de **Compiladores** do curso de **Ciência da Computação** da **UNIJORGE** (Semestre 2026.2).

**Docente:** Prof. Luis Alberto Pires de Oliveira  
**Equipe:** 
- Felipe Matheus (GitHub: [@felipemths12](https://github.com/felipemths12))
- Arthur de Oliveira Silva
- Isaac Santos de Andrade

---

## 📌 Visão Geral do Projeto

Este projeto implementa um pipeline de compilação clássico capaz de ler um arquivo de código-fonte (`.java` ou `.c`), extrair seus tokens, desenhar a Árvore Sintática Abstrata (AST) e realizar validações semânticas de escopo. 

A arquitetura foi dividida em três fases estritamente desacopladas:

### 1. Análise Léxica (Scanner)
- **Tokenização Assertiva:** Varredura caractere por caractere via Autômato Finito para identificação de Palavras Reservadas, Identificadores, Números, Strings Literais e Operadores. Ignora espaços e quebras de linha com controle de contagem de linhas.
- **Tabela de Símbolos Inteligente:** Utiliza `LinkedHashMap` para garantir o tempo de busca $O(1)$ e preservar a ordem de inserção. A tabela armazena e exibe: `ID`, `Lexema`, `Categoria` e `Tipo de Dado` (inferido dinamicamente durante a leitura).

### 2. Análise Sintática Simplificada (Parser & AST)
- **Parser Descendente Recursivo:** Implementação manual baseada em uma Gramática Livre de Contexto (GLC) definida.
- **Backtracking:** Capacidade do compilador de testar rotas gramaticais, abortar em caso de falha (retornando `null` silenciosamente), restaurar o ponteiro de leitura e avançar para o próximo bloco, permitindo a leitura de arquivos Java complexos sem quebras indevidas.
- **AST (Abstract Syntax Tree):** Geração de estruturas aninhadas exibidas hierarquicamente no console através dos nós `DeclaracaoNode`, `AtribuicaoNode`, `ExpressaoNode` e `TermoNode`.

### 3. Análise Semântica
- **Checagem de Escopo (Variável não declarada):** Implementação da Opção 2 do barema avaliativo.
- O `SemanticAnalyzer` desce pelos galhos da AST validando expressões matemáticas. Caso detecte uma variável no lado direito (ou recebendo uma atribuição do lado esquerdo) que não tenha sido previamente registrada na Memória de Escopo (`HashSet`), a compilação é abortada e uma mensagem de erro fatal indicando o nome real da variável fantasma é disparada.

---

## 📜 Gramática Livre de Contexto (BNF)

A análise sintática suporta chamadas de métodos, atribuições diretas e declarações tipadas, operando sobre a seguinte base restrita:

```bnf
<Declaracao> ::= <tipo_dado> <identificador> "=" <Expressao> ";"
<Atribuicao> ::= <identificador> "=" <Expressao> ";"
<Expressao>  ::= <termo> ("+" | "-") <termo> | <termo>
<termo>      ::= <identificador> | <numero> | <chamada_metodo>
