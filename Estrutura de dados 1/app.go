package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
	"time"
)

type App struct {
	queue             *Queue
	undoStack         *Stack
	history           *HistoryList
	nextTicket        int
	reader            *bufio.Reader
	inputClosed       bool
	examplesLoaded    bool
	interactiveInput  bool
	interactiveOutput bool
}

func NewApp() *App {
	return &App{
		queue:             &Queue{},
		undoStack:         &Stack{},
		history:           &HistoryList{},
		nextTicket:        1,
		reader:            bufio.NewReader(os.Stdin),
		interactiveInput:  isTerminal(os.Stdin),
		interactiveOutput: isTerminal(os.Stdout),
	}
}

func (a *App) Run() {
	for {
		a.clearScreen()
		a.printMenu()
		option := a.readLine("Escolha uma opção: ")

		if a.inputClosed {
			a.finishBecauseInputClosed()
			return
		}

		fmt.Println()
		validAction := true

		switch option {
		case "1":
			a.addPerson()
		case "2":
			a.attendNextFIFO()
		case "3":
			a.attendHighestPriority()
		case "4":
			a.showQueue()
		case "5":
			a.undoLastService()
		case "6":
			a.showHistory()
		case "7":
			a.sortHistoryByPriority()
		case "8":
			a.searchHistory()
		case "9":
			a.showAllStructures()
		case "10":
			a.loadExamples()
		case "0":
			fmt.Println("Encerrando simulador. Bons estudos!")
			return
		default:
			validAction = false
			fmt.Println("Opção inválida. Tente novamente.")
		}

		if a.inputClosed {
			a.finishBecauseInputClosed()
			return
		}

		if validAction {
			a.showStateSummary()
		}

		a.pause()
		if a.inputClosed {
			a.finishBecauseInputClosed()
			return
		}
	}
}

func (a *App) printMenu() {
	fmt.Println(title("=== SISTEMA DE ATENDIMENTO ==="))
	fmt.Printf("%s: %d | %s: %d | %s: %d\n",
		coloredLabel("Fila", ansiYellow),
		a.queue.Size(),
		coloredLabel("Pilha de desfazer", ansiMagenta),
		a.undoStack.Size(),
		coloredLabel("Histórico", ansiBlue),
		a.history.Size(),
	)
	fmt.Println()
	fmt.Println("1  - Adicionar pessoa na fila")
	fmt.Println("2  - Atender próxima pessoa da fila (FIFO)")
	fmt.Printf("3  - Atender pessoa mais %s\n", coloredLabel("prioritária", ansiRed))
	fmt.Println("4  - Ver fila atual")
	fmt.Println("5  - Desfazer último atendimento")
	fmt.Println("6  - Ver histórico de atendimentos")
	fmt.Println("7  - Ordenar histórico por prioridade")
	fmt.Println("8  - Buscar no histórico por nome")
	fmt.Println("9  - Ver todas as estruturas")
	fmt.Println("10 - Carregar dados de exemplo")
	fmt.Println("0  - Sair")
	fmt.Println()
}

func (a *App) addPerson() {
	name := a.readRequiredText("Nome da pessoa: ")
	if a.inputClosed {
		return
	}

	priority := a.readIntInRange("Prioridade (1 = alta, 5 = baixa): ", 1, 5)
	if a.inputClosed {
		return
	}

	description := a.readLine("Descrição do chamado: ")
	if a.inputClosed {
		return
	}

	person := Person{
		Ticket:      a.nextTicket,
		Name:        name,
		Priority:    priority,
		Description: description,
		CreatedAt:   time.Now(),
	}

	a.nextTicket++
	a.queue.Enqueue(person)

	fmt.Printf("%s entrou no final da fila.\n", person.ShortLabel())
	fmt.Println("A fila preserva a ordem de chegada para o atendimento FIFO.")
	fmt.Println()
	a.showQueue()
}

func (a *App) attendNextFIFO() {
	person, ok := a.queue.Dequeue()
	if !ok {
		fmt.Println("Não há pessoas na fila para atender.")
		return
	}

	a.registerService(person, "ordem de chegada (FIFO)")
}

func (a *App) attendHighestPriority() {
	person, ok := a.queue.DequeueHighestPriority()
	if !ok {
		fmt.Println("Não há pessoas na fila para atender.")
		return
	}

	a.registerService(person, "menor número de prioridade; empate mantém a ordem de chegada")
}

func (a *App) registerService(person Person, criterion string) {
	record := ServiceRecord{
		Person:     person,
		AttendedAt: time.Now(),
	}

	a.undoStack.Push(record)
	a.history.Append(record)

	fmt.Printf("Atendendo agora: %s\n", person.ShortLabel())
	fmt.Printf("Critério usado: %s.\n", criterion)
	fmt.Println("Operações realizadas:")
	fmt.Println("- Removeu a pessoa da fila")
	fmt.Println("- Empilhou o atendimento no topo da pilha (LIFO)")
	fmt.Println("- Inseriu o atendimento no final da lista encadeada")
}

func (a *App) showQueue() {
	fmt.Println(queueSection("FILA DE ESPERA"))
	fmt.Println("Frente da fila à esquerda; final da fila à direita.")
	fmt.Println(a.queue.VisualString())
}

func (a *App) undoLastService() {
	record, ok := a.undoStack.Pop()
	if !ok {
		fmt.Println("Não há atendimento para desfazer.")
		return
	}

	removedFromHistory := a.history.RemoveByTicket(record.Person.Ticket)
	a.queue.Enqueue(record.Person)

	fmt.Printf("Atendimento desfeito: %s\n", record.ShortLabel())
	fmt.Println("A pessoa voltou para o final da fila.")

	if removedFromHistory {
		fmt.Println("O mesmo registro foi removido da lista encadeada de histórico.")
	} else {
		fmt.Println("Aviso: o registro não estava mais no histórico.")
	}
}

func (a *App) showHistory() {
	fmt.Println(historySection("LISTA ENCADEADA - HISTÓRICO"))
	fmt.Println(a.history.VisualString())

	if a.history.IsEmpty() {
		return
	}

	fmt.Println()
	fmt.Println("Detalhes:")
	for _, record := range a.history.ToSlice() {
		fmt.Printf("- %s | Chamado: %s | Atendido em: %s\n",
			record.ShortLabel(),
			emptyAsDash(record.Person.Description),
			record.AttendedAt.Format("15:04:05"),
		)
	}
}

func (a *App) sortHistoryByPriority() {
	if a.history.IsEmpty() {
		fmt.Println("Não há histórico para ordenar.")
		return
	}

	moves := a.history.SelectionSortByPriority()

	fmt.Println("Histórico ordenado por prioridade com Selection Sort.")
	fmt.Printf("Nós reposicionados: %d\n", moves)
	fmt.Println("A ordenação religa os nós da lista encadeada em vez de trocar apenas os valores.")
	fmt.Println()
	a.showHistory()
}

func (a *App) searchHistory() {
	if a.history.IsEmpty() {
		fmt.Println("Não há histórico para buscar.")
		return
	}

	name := a.readRequiredText("Buscar nome: ")
	if a.inputClosed {
		return
	}

	results := a.history.SearchByName(name)

	if len(results) == 0 {
		fmt.Println("Nenhum atendimento encontrado.")
		return
	}

	fmt.Println("Resultados encontrados:")
	for _, record := range results {
		fmt.Printf("- %s | Chamado: %s | Atendido em: %s\n",
			record.ShortLabel(),
			emptyAsDash(record.Person.Description),
			record.AttendedAt.Format("15:04:05"),
		)
	}
}

func (a *App) showAllStructures() {
	fmt.Println(queueSection("FILA DE ESPERA"))
	fmt.Println(a.queue.VisualString())
	fmt.Println()

	fmt.Println(stackSection("PILHA DE DESFAZER"))
	fmt.Println(a.undoStack.VisualString())
	fmt.Println()

	fmt.Println(historySection("LISTA ENCADEADA - HISTÓRICO"))
	fmt.Println(a.history.VisualString())
}

func (a *App) loadExamples() {
	if a.examplesLoaded {
		confirmed := a.confirm("Os exemplos já foram carregados. Adicionar outro conjunto? (s/N): ")
		if a.inputClosed {
			return
		}

		if !confirmed {
			fmt.Println("Nenhum exemplo novo foi adicionado.")
			return
		}
	}

	examples := []Person{
		{Name: "João", Priority: 2, Description: "Notebook sem internet"},
		{Name: "Maria", Priority: 1, Description: "Sistema acadêmico indisponível"},
		{Name: "Carlos", Priority: 3, Description: "Impressora travando"},
		{Name: "Ana", Priority: 2, Description: "Senha expirada"},
	}

	for _, example := range examples {
		example.Ticket = a.nextTicket
		example.CreatedAt = time.Now()
		a.nextTicket++
		a.queue.Enqueue(example)
	}

	a.examplesLoaded = true

	fmt.Printf("%d pessoas de exemplo foram adicionadas ao final da fila.\n", len(examples))
	a.showQueue()
}

func (a *App) showStateSummary() {
	fmt.Println()
	fmt.Println(statusSection("ESTADO ATUAL"))
	fmt.Printf("%s: %d", coloredLabel("Fila", ansiYellow), a.queue.Size())

	if next, ok := a.queue.Peek(); ok {
		fmt.Printf(" | %s: %s", coloredLabel("Próximo FIFO", ansiYellow), next.ShortLabel())
	}

	if top, ok := a.undoStack.Peek(); ok {
		fmt.Printf(" | %s: %s", coloredLabel("Topo da pilha", ansiMagenta), top.ShortLabel())
	}

	fmt.Printf(" | %s: %d\n", coloredLabel("Histórico", ansiBlue), a.history.Size())
}

func (a *App) readLine(prompt string) string {
	fmt.Print(prompt)

	text, err := a.reader.ReadString('\n')
	if err != nil && len(text) == 0 {
		a.inputClosed = true
		return ""
	}

	return strings.TrimSpace(text)
}

func (a *App) readRequiredText(prompt string) string {
	for !a.inputClosed {
		value := a.readLine(prompt)
		if value != "" {
			return value
		}

		if !a.inputClosed {
			fmt.Println("Este campo não pode ficar vazio.")
		}
	}

	return ""
}

func (a *App) readIntInRange(prompt string, min int, max int) int {
	for !a.inputClosed {
		raw := a.readLine(prompt)
		value, err := strconv.Atoi(raw)

		if err == nil && value >= min && value <= max {
			return value
		}

		if !a.inputClosed {
			fmt.Printf("Digite um número entre %d e %d.\n", min, max)
		}
	}

	return 0
}

func (a *App) confirm(prompt string) bool {
	for !a.inputClosed {
		answer := strings.ToLower(a.readLine(prompt))

		switch answer {
		case "", "n", "nao", "não":
			return false
		case "s", "sim":
			return true
		default:
			if !a.inputClosed {
				fmt.Println("Responda com s para sim ou n para não.")
			}
		}
	}

	return false
}

func (a *App) pause() {
	if !a.interactiveInput {
		return
	}

	fmt.Println()
	_ = a.readLine("Pressione Enter para continuar...")
}

func (a *App) clearScreen() {
	if !a.interactiveOutput {
		return
	}

	fmt.Print("\033[H\033[2J")
}

func (a *App) finishBecauseInputClosed() {
	fmt.Println()
	fmt.Println("Entrada encerrada. Fechando o simulador.")
}

func emptyAsDash(value string) string {
	value = strings.TrimSpace(value)
	if value == "" {
		return "-"
	}

	return value
}

func title(text string) string {
	return color(ansiBoldCyan, text)
}

func queueSection(text string) string {
	return color(ansiBoldYellow, text)
}

func stackSection(text string) string {
	return color(ansiBoldMagenta, text)
}

func historySection(text string) string {
	return color(ansiBoldBlue, text)
}

func statusSection(text string) string {
	return color(ansiBoldGreen, text)
}

func coloredLabel(text string, code string) string {
	return color(code, text)
}

func priorityText(priority int) string {
	label := fmt.Sprintf("P%d", priority)

	switch priority {
	case 1:
		return color(ansiBoldRed, label)
	case 2:
		return color(ansiYellow, label)
	case 3:
		return color(ansiCyan, label)
	default:
		return color(ansiWhite, label)
	}
}

func color(code string, text string) string {
	if os.Getenv("NO_COLOR") != "" || !isTerminal(os.Stdout) {
		return text
	}

	return code + text + "\033[0m"
}

func isTerminal(file *os.File) bool {
	info, err := file.Stat()
	if err != nil {
		return false
	}

	return info.Mode()&os.ModeCharDevice != 0
}

const (
	ansiRed         = "\033[31m"
	ansiYellow      = "\033[33m"
	ansiCyan        = "\033[36m"
	ansiBlue        = "\033[34m"
	ansiMagenta     = "\033[35m"
	ansiWhite       = "\033[37m"
	ansiBoldRed     = "\033[1;31m"
	ansiBoldYellow  = "\033[1;33m"
	ansiBoldGreen   = "\033[1;32m"
	ansiBoldCyan    = "\033[1;36m"
	ansiBoldBlue    = "\033[1;34m"
	ansiBoldMagenta = "\033[1;35m"
)
