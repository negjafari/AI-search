# AI-search
A pathfinding algorithm implementation utilizing IDS, BDS, A*, IDA*, and a heuristic-based approach
# Matrix Navigation Project

## Overview

This project involves navigating through a matrix from a specified 'start' point to a 'goal' point, considering various elements in the matrix that affect the score during traversal. The matrix contains specific symbols, each with unique meanings:

- `s`: Start point with an initial score.
- `g`: Goal point that we aim to reach with a specific required score.
- `w`: Wall elements that cannot be crossed.
- `a`: Passing through adds the number in the element to the score.
- `b`: Passing through subtracts the number in the element from the score.
- Mathematical operators: Apply the operator to the current score based on the value in the element.

## Algorithms Implemented

1. **Iterative Deepening Search (IDS):**
   - Implemented using Breadth-First Search (BFS).
   - The cost function:
     - Any allowed house (except walls and start) costs 1.

2. **Bidirectional Search (BDS):**
   - Utilizes BFS for both forward and backward searches.
   - The cost function:
     - Any allowed house (except walls and start) costs 1.

3. **A-star Algorithm:**
   - The cost function is modified:
     - Addition and 'b' houses cost 2.
     - Subtraction and 'a' houses cost 1.
     - Multiplication  houses cost 5.
     - Power houses cost 11.
     - The cost to the target house (goal) is considered 1.

4. **Iterative Deepening A-star (IDA-star):**
   - Implemented using a combination of iterative deepening and A*.
   - The cost function:
     - Any allowed house (except walls and start) costs 1.
  
5. **Heuristic Function:**
   - A heuristic function is implemented to calculate the distance between the current step and the 'goal.'

## Implementation Details

- The project involves traversing the matrix while considering score modifications based on the type of element.
- IDS and BDS are implemented using BFS for efficient exploration.
- A heuristic is utilized in A* and IDA* to guide the search towards the goal efficiently.

## Usage

