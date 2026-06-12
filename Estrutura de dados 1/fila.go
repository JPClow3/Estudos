package main

import (
	"fmt"
	"strings"
)

type queueNode struct {
	value Person
	next  *queueNode
}

type Queue struct {
	front *queueNode
	rear  *queueNode
	size  int
}

func (q *Queue) Enqueue(person Person) {
	newNode := &queueNode{value: person}

	if q.rear == nil {
		q.front = newNode
		q.rear = newNode
		q.size++
		return
	}

	q.rear.next = newNode
	q.rear = newNode
	q.size++
}

func (q *Queue) Dequeue() (Person, bool) {
	if q.front == nil {
		return Person{}, false
	}

	removed := q.front
	q.front = q.front.next

	if q.front == nil {
		q.rear = nil
	}

	q.size--
	return removed.value, true
}

func (q *Queue) DequeueHighestPriority() (Person, bool) {
	if q.front == nil {
		return Person{}, false
	}

	var bestPrevious *queueNode
	best := q.front
	var previous *queueNode

	for current := q.front; current != nil; current = current.next {
		if current.value.Priority < best.value.Priority {
			best = current
			bestPrevious = previous
		}

		previous = current
	}

	if bestPrevious == nil {
		q.front = best.next
	} else {
		bestPrevious.next = best.next
	}

	if best == q.rear {
		q.rear = bestPrevious
	}

	if q.front == nil {
		q.rear = nil
	}

	best.next = nil
	q.size--

	return best.value, true
}

func (q *Queue) Peek() (Person, bool) {
	if q.front == nil {
		return Person{}, false
	}

	return q.front.value, true
}

func (q *Queue) IsEmpty() bool {
	return q.size == 0
}

func (q *Queue) Size() int {
	return q.size
}

func (q *Queue) VisualString() string {
	if q.IsEmpty() {
		return "Fila vazia -> nil"
	}

	var builder strings.Builder
	position := 1

	for current := q.front; current != nil; current = current.next {
		fmt.Fprintf(&builder, "[%d] %s", position, current.value.ShortLabel())

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

func (q *Queue) ToSlice() []Person {
	people := make([]Person, 0, q.size)

	for current := q.front; current != nil; current = current.next {
		people = append(people, current.value)
	}

	return people
}
