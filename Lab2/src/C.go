package main

import (
	"fmt"
	"math/rand"
	"time"
)

type Monk struct {
	energy int
	temple string
}

func NewMonk(energy int, temple string) *Monk {
	return &Monk{energy: energy, temple: temple}
}

func battle(a Monk, b Monk, c chan Monk) {
	if a.energy > b.energy {
		c <- a
	} else {
		c <- b
	}
}

func main() {
	s1 := rand.NewSource(time.Now().UnixNano())
	r1 := rand.New(s1)
	var participants []Monk
	numberOfParticipants := 3
	var temple string
	for i := 0; i < numberOfParticipants; i++ {
		if i%2 == 0 {
			temple = "Гуань-Інь"
		} else {
			temple = "Гуань-Янь"
		}
		m := NewMonk(r1.Intn(100), temple)
		participants = append(participants, *m)
	}

	for i := 0; i < len(participants); i++ {
		fmt.Print("Монастирь: " + participants[i].temple + ". Сила: ")
		fmt.Println(participants[i].energy)
	}

	c := make(chan Monk)
	for len(participants) > 1 {
		for i := 0; i < len(participants); i += 2 {
			if i+1 < len(participants) {
				go battle(participants[i], participants[i+1], c)
			} else {
				go battle(participants[i], participants[i], c)
			}
		}
		numberOfParticipants = numberOfParticipants/2 + numberOfParticipants%2
		participants = nil
		for i := 0; i < numberOfParticipants; i++ {
			participants = append(participants, <-c)
		}
	}

	fmt.Println("Переміг монастирь: " + participants[0].temple + ". Сила чинця: ")
	fmt.Println(participants[0].energy)
}
