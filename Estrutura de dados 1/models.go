package main

import (
	"fmt"
	"time"
)

type Person struct {
	Ticket      int
	Name        string
	Priority    int
	Description string
	CreatedAt   time.Time
}

type ServiceRecord struct {
	Person     Person
	AttendedAt time.Time
}

func (p Person) ShortLabel() string {
	return fmt.Sprintf("#%03d %s (%s)", p.Ticket, p.Name, priorityText(p.Priority))
}

func (r ServiceRecord) ShortLabel() string {
	return fmt.Sprintf("#%03d %s (%s)", r.Person.Ticket, r.Person.Name, priorityText(r.Person.Priority))
}
