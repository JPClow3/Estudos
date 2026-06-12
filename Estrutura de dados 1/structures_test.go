package main

import (
	"bufio"
	"strings"
	"testing"
	"time"
)

func TestQueueUsesFIFO(t *testing.T) {
	queue := &Queue{}

	queue.Enqueue(Person{Ticket: 1, Name: "Joao"})
	queue.Enqueue(Person{Ticket: 2, Name: "Maria"})

	first, ok := queue.Dequeue()
	if !ok {
		t.Fatal("expected first person in queue")
	}

	if first.Name != "Joao" {
		t.Fatalf("expected Joao, got %s", first.Name)
	}

	second, ok := queue.Dequeue()
	if !ok {
		t.Fatal("expected second person in queue")
	}

	if second.Name != "Maria" {
		t.Fatalf("expected Maria, got %s", second.Name)
	}
}

func TestQueueCanRemoveHighestPriority(t *testing.T) {
	queue := &Queue{}

	queue.Enqueue(Person{Ticket: 1, Name: "Joao", Priority: 3})
	queue.Enqueue(Person{Ticket: 2, Name: "Maria", Priority: 1})
	queue.Enqueue(Person{Ticket: 3, Name: "Carlos", Priority: 1})

	first, ok := queue.DequeueHighestPriority()
	if !ok {
		t.Fatal("expected highest-priority person in queue")
	}

	if first.Name != "Maria" {
		t.Fatalf("expected Maria because she has the first priority-1 ticket, got %s", first.Name)
	}

	second, ok := queue.DequeueHighestPriority()
	if !ok {
		t.Fatal("expected second highest-priority person in queue")
	}

	if second.Name != "Carlos" {
		t.Fatalf("expected Carlos because priority ties keep arrival order, got %s", second.Name)
	}
}

func TestQueueVisualStringWrapsAfterFourNodes(t *testing.T) {
	queue := &Queue{}

	for ticket := 1; ticket <= 5; ticket++ {
		queue.Enqueue(Person{Ticket: ticket, Name: "Pessoa", Priority: 3})
	}

	if !strings.Contains(queue.VisualString(), "->\n") {
		t.Fatal("expected queue visual output to wrap after four nodes")
	}
}

func TestStackUsesLIFO(t *testing.T) {
	stack := &Stack{}

	stack.Push(recordFor(Person{Ticket: 1, Name: "Joao"}))
	stack.Push(recordFor(Person{Ticket: 2, Name: "Maria"}))

	top, ok := stack.Pop()
	if !ok {
		t.Fatal("expected record on stack")
	}

	if top.Person.Name != "Maria" {
		t.Fatalf("expected Maria, got %s", top.Person.Name)
	}
}

func TestHistorySelectionSortByPriority(t *testing.T) {
	history := &HistoryList{}
	history.Append(recordFor(Person{Ticket: 1, Name: "Joao", Priority: 2}))
	history.Append(recordFor(Person{Ticket: 2, Name: "Maria", Priority: 1}))
	history.Append(recordFor(Person{Ticket: 3, Name: "Carlos", Priority: 3}))

	originalFirstNode := history.head
	originalSecondNode := history.head.next
	originalThirdNode := history.tail

	history.SelectionSortByPriority()
	records := history.ToSlice()

	expected := []string{"Maria", "Joao", "Carlos"}
	for index, name := range expected {
		if records[index].Person.Name != name {
			t.Fatalf("position %d: expected %s, got %s", index, name, records[index].Person.Name)
		}
	}

	if history.head != originalSecondNode {
		t.Fatal("expected selection sort to relink Maria's node to the head")
	}

	if history.head.next != originalFirstNode {
		t.Fatal("expected selection sort to relink Joao's node after Maria")
	}

	if history.tail != originalThirdNode {
		t.Fatal("expected Carlos's node to remain the tail")
	}
}

func TestHistoryVisualStringWrapsAfterFourNodes(t *testing.T) {
	history := &HistoryList{}

	for ticket := 1; ticket <= 5; ticket++ {
		history.Append(recordFor(Person{Ticket: ticket, Name: "Pessoa", Priority: 3}))
	}

	if !strings.Contains(history.VisualString(), "->\n") {
		t.Fatal("expected history visual output to wrap after four nodes")
	}
}

func TestHistorySearchAndRemove(t *testing.T) {
	history := &HistoryList{}
	history.Append(recordFor(Person{Ticket: 1, Name: "Joao Silva", Priority: 2}))
	history.Append(recordFor(Person{Ticket: 2, Name: "Maria Souza", Priority: 1}))

	results := history.SearchByName("silva")
	if len(results) != 1 || results[0].Person.Name != "Joao Silva" {
		t.Fatalf("expected to find Joao Silva, got %#v", results)
	}

	removed := history.RemoveByTicket(1)
	if !removed {
		t.Fatal("expected ticket 1 to be removed")
	}

	if history.Size() != 1 {
		t.Fatalf("expected history size 1, got %d", history.Size())
	}
}

func TestReadRequiredTextStopsOnEOF(t *testing.T) {
	app := NewApp()
	app.reader = bufio.NewReader(strings.NewReader(""))

	value := app.readRequiredText("Nome: ")

	if value != "" {
		t.Fatalf("expected empty value on EOF, got %q", value)
	}

	if !app.inputClosed {
		t.Fatal("expected app to mark input as closed")
	}
}

func recordFor(person Person) ServiceRecord {
	return ServiceRecord{
		Person:     person,
		AttendedAt: time.Now(),
	}
}
