package main

import (
	"fmt"
	"math/rand"
	"os"
	"sync"
	"time"
)

const Size = 10

var wgSum sync.WaitGroup
var wgBarrier sync.WaitGroup
var array1 []int
var array2 []int
var array3 []int

func generateRandomArray() []int {
	array := make([]int, Size)
	for i := 0; i < Size; i++ {
		array[i] = rand.Intn(10)
	}
	return array
}
func sumArray(array []int) int {
	sum := 0
	for i := 0; i < len(array); i++ {
		sum += array[i]
	}
	return sum
}
func sum(array []int, c chan int) {
	c <- sumArray(array)
	defer wgSum.Done()
}
func changeRandomNumber(array []int, avg int) []int {
	sum := sumArray(array)
	if sum-avg > 0 {
		array[rand.Intn(len(array))] -= 1
	} else {
		array[rand.Intn(len(array))] += 1
	}
	return array
}
func barrier(c chan int) {
	wgSum.Wait()
	sum := <-c
	avg := float64(sum)
	areEquals := true
	for len(c) != 0 {
		buf := <-c
		avg += float64(buf)
		if sum != buf {
			areEquals = false
		}
	}
	avg /= 3
	if areEquals {
		os.Exit(0)
	} else {
		array1 = changeRandomNumber(array1, int(avg))
		array2 = changeRandomNumber(array2, int(avg))
		array3 = changeRandomNumber(array3, int(avg))
	}
	wgBarrier.Done()
}
func printAll() {
	fmt.Println(array1, sumArray(array1))
	fmt.Println(array2, sumArray(array2))
	fmt.Println(array3, sumArray(array3))
	fmt.Println("---------------------------------")
}
func createArrays() {
	array1 = generateRandomArray()
	array2 = generateRandomArray()
	array3 = generateRandomArray()
	if (sumArray(array1)-sumArray(array2))%2 != 0 && (sumArray(array2)-sumArray(array3))%2 != 0 {
		array2[0]--
	} else if (sumArray(array1)-sumArray(array3))%2 != 0 && (sumArray(array2)-sumArray(array3))%2 != 0 {
		array3[0]--
	} else if (sumArray(array1)-sumArray(array3))%2 != 0 && (sumArray(array2)-sumArray(array1))%2 != 0 {
		array1[0]--
	}
	printAll()
}
func main() {
	rand.Seed(time.Now().Unix())
	createArrays()
	c := make(chan int, 3)
	for {
		wgSum.Add(3)
		go sum(array1, c)
		go sum(array2, c)
		go sum(array3, c)
		wgBarrier.Add(1)
		go barrier(c)
		wgBarrier.Wait()
		printAll()
	}

}
