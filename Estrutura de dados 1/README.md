# Simulador de Atendimento com Histórico e Prioridade

Projeto de terminal em Go para demonstrar estruturas de dados em um sistema de atendimento de chamados.

## Estruturas demonstradas

- Fila: pessoas entram no final da fila e podem ser atendidas pela ordem de chegada. Demonstra FIFO, First In, First Out.
- Pilha: cada atendimento realizado entra no topo da pilha de desfazer. Demonstra LIFO, Last In, First Out.
- Lista encadeada: o histórico geral de atendimentos é salvo em nós ligados por ponteiros.
- Selection Sort: o histórico pode ser ordenado por prioridade. A implementação religa os nós da lista, em vez de trocar apenas os valores.
- Prioridade: há uma opção separada para atender o chamado mais urgente, mantendo a opção FIFO pura para a apresentação.

## Como executar

Instale Go e rode:

```bash
go run .
```

Para executar os testes:

```bash
go test ./...
```

## Menu principal

```text
=== SISTEMA DE ATENDIMENTO ===
1  - Adicionar pessoa na fila
2  - Atender próxima pessoa da fila (FIFO)
3  - Atender pessoa mais prioritária
4  - Ver fila atual
5  - Desfazer último atendimento
6  - Ver histórico de atendimentos
7  - Ordenar histórico por prioridade
8  - Buscar no histórico por nome
9  - Ver todas as estruturas
10 - Carregar dados de exemplo
0  - Sair
```

## Fluxo sugerido para apresentação

1. Use a opção 10 para carregar exemplos.
2. Use a opção 4 para mostrar a fila:

```text
[1] #001 João (P2) -> [2] #002 Maria (P1) -> [3] #003 Carlos (P3) -> nil
```

3. Use a opção 2 para atender por FIFO. O primeiro item sai da fila, entra na pilha e entra no histórico.
4. Use a opção 5 para desfazer o atendimento. O topo da pilha é removido, o mesmo ticket sai do histórico e a pessoa volta para o final da fila.
5. Use a opção 3 para atender por prioridade. O sistema procura a menor prioridade na fila; em empate, mantém a ordem de chegada.
6. Use a opção 9 para mostrar fila, pilha e lista encadeada ao mesmo tempo.
7. Use a opção 7 para ordenar o histórico por prioridade com Selection Sort.

## Melhorias de UX incluídas

- O menu limpa a tela em terminais interativos.
- As pausas só aparecem quando a entrada é interativa, então testes com entrada redirecionada não travam.
- O programa encerra com segurança quando a entrada termina.
- A tela mostra um resumo do estado atual depois das ações.
- Carregar exemplos mais de uma vez exige confirmação.
- Filas e históricos longos quebram linha a cada quatro nós para facilitar a leitura.
- Rótulos de fila, pilha, histórico, estado e prioridade usam cores quando o terminal permite ANSI.

## Arquivos

- `fila.go`: implementação da fila com ponteiros `front` e `rear`, FIFO e remoção por prioridade.
- `pilha.go`: implementação da pilha com ponteiro `top`.
- `historico.go`: lista encadeada, busca, remoção e Selection Sort com religação de nós.
- `app.go`: menu, entrada do usuário e regras do simulador.
- `structures_test.go`: testes das estruturas principais.
