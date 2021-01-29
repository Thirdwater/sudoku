# Sudoku

A simple [Sudoku](https://en.wikipedia.org/wiki/Sudoku) game with the following control scheme:

> For each entry in the game grid:
> 1. Press a key corresponding to one of the 9 3x3 subgrids.
> 2. Press a key corresponding to one of the 9 cells within the subgrid selected in step 1.
> 3. Press a key corresponding to one of the 9 numbers for the cell selected in step 2.

By default we use the following mapping:

> Define the "numpad order" as starting from the bottom-left and going from left-to-right, then bottom-to-top.  
> In step 1, each numpad button [Num i] corresponds to the ith 3x3 subgrid in numpad ordering.  
> In step 2, each numpad button [Num i] corresponds to the ith cell in the currently selected subgrid in numpad ordering.  
> In step 3, each numpad button [Num i] corresponds to the number i.  

For example, the key sequence `[Num 6] [Num 1] [Num 3]` will fill in the number 3 in the bottom-left cell of the middle-right subgrid.
