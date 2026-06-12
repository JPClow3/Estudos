package main

import (
	"fmt"
	"strings"
)

type stackNode struct {
	value ServiceRecord
	next  *stackNode
}

type Stack struct {
	top  *stackNode
	size int
}

func (s *Stack) Push(record ServiceRecord) {
	newNode := &stackNode{
		value: record,
		next:  s.top,
	}

	s.top = newNode
	s.size++
}

func (s *Stack) Pop() (ServiceRecord, bool) {
	if s.top == nil {
		return ServiceRecord{}, false
	}

	removed := s.top
	s.top = s.top.next
	s.size--

	return removed.value, true
}

func (s *Stack) Peek() (ServiceRecord, bool) {
	if s.top == nil {
		return ServiceRecord{}, false
	}

	return s.top.value, true
}

func (s *Stack) IsEmpty() bool {
	return s.size == 0
}

func (s *Stack) Size() int {
	return s.size
}

func (s *Stack) VisualString() string {
	if s.IsEmpty() {
		return "Pilha vazia"
	}

	var builder strings.Builder
	builder.WriteString("Topo\n")
	builder.WriteString(" |\n")

	for current := s.top; current != nil; current = current.next {
		fmt.Fprintf(&builder, "[%s]", current.value.ShortLabel())

		if current.next != nil {
			builder.WriteString("\n")
		}
	}

	return builder.String()
}

func (s *Stack) ToSlice() []ServiceRecord {
	records := make([]ServiceRecord, 0, s.size)

	for current := s.top; current != nil; current = current.next {
		records = append(records, current.value)
	}

	return records
}
