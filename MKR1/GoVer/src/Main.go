package main

import (
	"fmt"
	"golang.org/x/sync/semaphore"
)

type People struct {
	from int
	to   int
}

func NewPeople(from int, to int) *People {
	return &People{from: from, to: to}
}

type Plane struct {
	size                  int
	flightRange           int
	from                  int
	to                    int
	currentNumberOFPeople int
	isStartFlight         bool
}

func (plane *Plane) SetFrom(from int) {
	plane.from = from
}

func (plane *Plane) SetTo(to int) {
	plane.to = to
}

func (plane *Plane) incrementCurrentPeoples() {
	plane.currentNumberOFPeople += 1
}

func (plane *Plane) startFlight() {
	plane.isStartFlight = true
}

func NewPlane(size int, flightRange int) *Plane {
	return &Plane{size: size, flightRange: flightRange, from: -1, to: -1, currentNumberOFPeople: 0, isStartFlight: false}
}

var numberOfPassPeople = 0

var sem *semaphore.Weighted

var planes = []Plane{*NewPlane(2, 10), *NewPlane(5, 5)}

var graphOfDestinations = [][]int{{0, 2, 3, 5, 10},
	{2, 0, 4, 2, 4},
	{3, 4, 0, 1, 0},
	{5, 2, 1, 0, 1},
	{10, 4, 0, 1, 0}}

func (plane *Plane) planeRun() {
	for plane.currentNumberOFPeople < plane.size {
		if plane.isStartFlight {
			break
		}
	}
	if plane.currentNumberOFPeople == plane.size {
		fmt.Println("Plane is full and start flight")
	} else {
		fmt.Println("Plane isn`t full but start flight (run out of people)")
	}
}

func (people People) peopleRun() {
	for !sem.TryAcquire(1) {
	}
	isTake := false
	for i := 0; i < len(planes); i++ {
		if planes[i].flightRange >= graphOfDestinations[people.from][people.to] && planes[i].currentNumberOFPeople < planes[i].size {
			if planes[i].from == -1 {
				planes[i].SetFrom(people.from)
				planes[i].SetTo(people.to)
				planes[i].currentNumberOFPeople += 1
				isTake = true
				fmt.Println("People got on the plane")
				break
			} else if planes[i].from == people.from && planes[i].to == people.to {
				planes[i].incrementCurrentPeoples()
				isTake = true
				fmt.Println("People got on the plane")
				break
			}
		}
	}
	if !isTake {
		fmt.Println("No one plane can take this people")
	}
	numberOfPassPeople++
	sem.Release(1)
}

func main() {
	sem = semaphore.NewWeighted(1)
	for i := 0; i < len(planes); i++ {
		go planes[i].planeRun()
	}
	peoples := []People{
		*NewPeople(0, 2),
		*NewPeople(1, 4),
		*NewPeople(0, 2),
		*NewPeople(1, 4),
		*NewPeople(1, 4),
		*NewPeople(1, 4),
		*NewPeople(3, 4),
	}
	for i := 0; i < len(peoples); i++ {
		go peoples[i].peopleRun()
	}
	isFirst := true
	for {
		if numberOfPassPeople == len(peoples) && isFirst {
			for i := 0; i < len(planes); i++ {
				planes[i].startFlight()
			}
			isFirst = false
		}
	}
}
