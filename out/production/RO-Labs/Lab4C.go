package main

import (
	"fmt"
	"strconv"
	"sync"
)

var costsGraph = [][]int{
	{0, 0, 2, 0},
	{0, 0, 1, 5},
	{2, 1, 0, 0},
	{0, 5, 0, 0},
}
var lock sync.RWMutex
var wg sync.WaitGroup

const INT_MAX = int(^uint(0) >> 1)

func changePrice(city1 int, city2 int, price int) {
	lock.Lock()
	if costsGraph[city1][city2] != 0 {
		costsGraph[city1][city2] = price
		costsGraph[city2][city1] = price
	}
	lock.Unlock()
	fmt.Println("Price between city " + strconv.Itoa(city1) + " and " +
		strconv.Itoa(city2) + " has become " + strconv.Itoa(price))
	graphPrint()
	defer wg.Done()
}
func addTrip(city1 int, city2 int, price int) {
	lock.Lock()
	if costsGraph[city1][city2] == 0 {
		costsGraph[city1][city2] = price
		costsGraph[city2][city1] = price
	}
	lock.Unlock()
	fmt.Println("Trip between " + strconv.Itoa(city1) + " and " +
		strconv.Itoa(city2) + " was added with price " + strconv.Itoa(price))
	graphPrint()
	defer wg.Done()

}
func deleteTrip(city1 int, city2 int) {
	lock.Lock()
	if costsGraph[city1][city2] != 0 {
		costsGraph[city1][city2] = 0
		costsGraph[city2][city1] = 0
	}
	lock.Unlock()
	fmt.Println("Trip between " + strconv.Itoa(city1) + " and " +
		strconv.Itoa(city2) + " was deleted")
	graphPrint()
	defer wg.Done()
}
func addNewCity() {
	lock.Lock()
	newSize := len(costsGraph) + 1
	newGraph := make([][]int, newSize)
	for i := 0; i < newSize; i++ {
		newGraph[i] = make([]int, newSize)
	}
	for i := 0; i < newSize; i++ {
		for j := 0; j < newSize; j++ {
			if i != newSize-1 && j != newSize-1 {
				newGraph[i][j] = costsGraph[i][j]
			} else {
				newGraph[i][j] = 0
			}
		}
	}
	costsGraph = newGraph
	lock.Unlock()
	fmt.Println("Added new city " + strconv.Itoa(len(costsGraph)-1))
	graphPrint()
	defer wg.Done()
}
func deleteCity(cityToDelete int) {
	lock.Lock()
	newSize := len(costsGraph) - 1
	newGraph := make([][]int, newSize)
	for i := 0; i < newSize; i++ {
		newGraph[i] = make([]int, newSize)
	}
	ni := 0
	nj := 0
	for i := 0; i < len(costsGraph); i++ {
		nj = 0
		for j := 0; j < len(costsGraph); j++ {
			if j != cityToDelete {
				newGraph[ni][nj] = costsGraph[i][j]
				nj++
			}
		}
		if i == cityToDelete {
			continue
		}
		ni++
	}
	costsGraph = newGraph
	lock.Unlock()
	fmt.Println("Deleted city " + strconv.Itoa(cityToDelete))
	graphPrint()
	defer wg.Done()
}
func graphPrint() {
	lock.RLock()
	for i := 0; i < len(costsGraph); i++ {
		for j := 0; j < len(costsGraph); j++ {
			fmt.Print(costsGraph[i][j], " ")
		}
		fmt.Print("\n")
	}
	fmt.Print("\n")
	lock.RUnlock()
}
func minDistance(dist []int, sptSet []bool) int {
	min := INT_MAX
	var min_index int
	for v := 0; v < len(costsGraph); v++ {
		if sptSet[v] == false && dist[v] <= min {
			min = dist[v]
			min_index = v
		}
	}
	return min_index
}
func dijkstra(city1 int, city2 int) {
	lock.RLock()
	costs := make([]int, len(costsGraph))
	sptSet := make([]bool, len(costsGraph))
	for i := 0; i < len(costsGraph); i++ {
		costs[i] = INT_MAX
		sptSet[i] = false
	}
	costs[city1] = 0
	for count := 0; count < len(costsGraph)-1; count++ {
		u := minDistance(costs, sptSet)
		sptSet[u] = true
		for v := 0; v < len(costsGraph); v++ {
			if !sptSet[v] && costsGraph[u][v] != 0 && costs[u] != INT_MAX && costs[u]+costsGraph[u][v] < costs[v] {
				costs[v] = costs[u] + costsGraph[u][v]
			}
		}
	}
	fmt.Println("Shortest trip from " + strconv.Itoa(city1) + " to " + strconv.Itoa(city2) + " costs " +
		strconv.Itoa(costs[city2]))
	lock.RUnlock()
	graphPrint()
	defer wg.Done()
}

func main() {
	var choice int
	graphPrint()
	for {
		fmt.Println("1) Change price \n2) Add new trip \n3) Delete existed trip \n4) Add new city " +
			"\n5) Delete existed city \n6) Trip between two cities")
		fmt.Scan(&choice)
		wg.Add(1)
		switch choice {
		case 1:
			fmt.Println("Enter two cities and new price:")
			var city1, city2, price int
			fmt.Scan(&city1, &city2, &price)
			go changePrice(city1, city2, price)
		case 2:
			fmt.Println("Enter two cities and price of new trip:")
			var city1, city2, price int
			fmt.Scan(&city1, &city2, &price)
			go addTrip(city1, city2, price)
		case 3:
			fmt.Println("Enter two cities:")
			var city1, city2 int
			fmt.Scan(&city1, &city2)
			go deleteTrip(city1, city2)
		case 4:
			go addNewCity()
		case 5:
			fmt.Println("Enter city to delete:")
			var city1 int
			fmt.Scan(&city1)
			go deleteCity(city1)
		case 6:
			fmt.Println("Enter two cities: ")
			var city1, city2 int
			fmt.Scan(&city1, &city2)
			go dijkstra(city1, city2)
		default:
			fmt.Println("Invalid input")
			wg.Done()
		}
		wg.Wait()
	}
}
