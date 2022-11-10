package main

import (
	"fmt"
	"math/rand"
	"time"
)

const (
	Tobacco int = 0
	Paper       = 1
	Matches     = 2
)

func remove(slice []int, s int) []int {
	return append(slice[:s], slice[s+1:]...)
}

type Smoker struct {
	name string
	comp int
}

func NewSmoker(name string, comp int) *Smoker {
	return &Smoker{name: name, comp: comp}
}

func acquire(n chan int) {
	n <- 1
}
func release(n chan int) {
	<-n
}

func agent(semaphore chan int, r1 *rand.Rand, c chan []int) {
	for {
		if len(semaphore) == 0 && len(c) == 0 {
			smokeComps := remove([]int{Tobacco, Paper, Matches}, r1.Intn(3))
			fmt.Println(smokeComps)
			for i := 0; i < 3; i++ {
				c <- smokeComps
			}
			acquire(semaphore)
		}
	}
}

func (smoker Smoker) smoke(semaphore chan int, c chan []int) {
	for {
		if len(semaphore) == 1 {
			comps := <-c
			time.Sleep(1 * time.Millisecond)
			//fmt.Println(comps)
			isSmoking := true
			for i := 0; i < len(comps); i++ {
				if comps[i] == smoker.comp {
					isSmoking = false
				}
			}
			if isSmoking {
				fmt.Println(smoker.name + " is smoking")
				time.Sleep(1 * time.Second)
				release(semaphore)
			}
		}
	}
}

func main() {
	s1 := rand.NewSource(time.Now().UnixNano())
	r1 := rand.New(s1)
	c := make(chan []int, 3)
	semaphore := make(chan int, 1)

	smoker0 := NewSmoker("Smoker0", Tobacco)
	smoker1 := NewSmoker("Smoker1", Paper)
	smoker2 := NewSmoker("Smoker2", Matches)
	go agent(semaphore, r1, c)
	go smoker0.smoke(semaphore, c)
	go smoker1.smoke(semaphore, c)
	go smoker2.smoke(semaphore, c)
	for {

	}
}
