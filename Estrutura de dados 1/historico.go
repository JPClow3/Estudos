package main

import (
	"fmt"
	"strings"
)

type historyNode struct {
	value ServiceRecord
	next  *historyNode
}

type HistoryList struct {
	head *historyNode
	tail *historyNode
	size int
}

func (h *HistoryList) Append(record ServiceRecord) {
	newNode := &historyNode{value: record}

	if h.head == nil {
		h.head = newNode
		h.tail = newNode
		h.size++
		return
	}

	h.tail.next = newNode
	h.tail = newNode
	h.size++
}

func (h *HistoryList) RemoveByTicket(ticket int) bool {
	var previous *historyNode

	for current := h.head; current != nil; current = current.next {
		if current.value.Person.Ticket == ticket {
			if previous == nil {
				h.head = current.next
			} else {
				previous.next = current.next
			}

			if current == h.tail {
				h.tail = previous
			}

			h.size--
			return true
		}

		previous = current
	}

	return false
}

func (h *HistoryList) SearchByName(name string) []ServiceRecord {
	search := strings.ToLower(strings.TrimSpace(name))
	results := make([]ServiceRecord, 0)

	for current := h.head; current != nil; current = current.next {
		currentName := strings.ToLower(current.value.Person.Name)
		if strings.Contains(currentName, search) {
			results = append(results, current.value)
		}
	}

	return results
}

func (h *HistoryList) SelectionSortByPriority() int {
	if h.size < 2 {
		return 0
	}

	var sortedHead *historyNode
	var sortedTail *historyNode
	unsortedHead := h.head
	moves := 0

	for unsortedHead != nil {
		var minimumPrevious *historyNode
		minimum := unsortedHead
		previous := unsortedHead

		for current := unsortedHead.next; current != nil; current = current.next {
			if historyPriorityLess(current.value, minimum.value) {
				minimum = current
				minimumPrevious = previous
			}

			previous = current
		}

		if minimumPrevious == nil {
			unsortedHead = minimum.next
		} else {
			minimumPrevious.next = minimum.next
			moves++
		}

		minimum.next = nil

		if sortedHead == nil {
			sortedHead = minimum
			sortedTail = minimum
		} else {
			sortedTail.next = minimum
			sortedTail = minimum
		}
	}

	h.head = sortedHead
	h.tail = sortedTail

	return moves
}

func historyPriorityLess(left ServiceRecord, right ServiceRecord) bool {
	if left.Person.Priority != right.Person.Priority {
		return left.Person.Priority < right.Person.Priority
	}

	return left.Person.Ticket < right.Person.Ticket
}

func (h *HistoryList) IsEmpty() bool {
	return h.size == 0
}

func (h *HistoryList) Size() int {
	return h.size
}

func (h *HistoryList) VisualString() string {
	if h.IsEmpty() {
		return "Histórico vazio -> nil"
	}

	var builder strings.Builder
	position := 1

	for current := h.head; current != nil; current = current.next {
		fmt.Fprintf(&builder, "[%s|%s]", current.value.Person.Name, priorityText(current.value.Person.Priority))

		if current.next != nil {
			if position%4 == 0 {
				builder.WriteString(" ->\n")
			} else {
				builder.WriteString(" -> ")
			}
		} else {
			builder.WriteString(" -> nil")
		}

		position++
	}

	return builder.String()
}

func (h *HistoryList) ToSlice() []ServiceRecord {
	records := make([]ServiceRecord, 0, h.size)

	for current := h.head; current != nil; current = current.next {
		records = append(records, current.value)
	}

	return records
}
