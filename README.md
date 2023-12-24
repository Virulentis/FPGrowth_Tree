# Frequent Pattern Growth (FP-Growth) Algorithm


This is a Java implementation of the Frequent Pattern Growth (FP-Growth) algorithm, a popular method for mining frequent patterns in transactional databases. 
The FP-Growth algorithm is known for its efficiency in handling large datasets and is widely used in data mining and association rule learning.


## Usage

Run the FP-Growth algorithm on your dataset with the following command:

`java FPG FILE_PATH MINSUP`

where FILE_PATH is the path to the input text file (ex ./connect.txt) and minsup is the minimum support threshold from 0-100 (ex 98).


## Input Format

The input dataset should be in a text file, where the first line represents the number of transactions and the following lines represent a transaction. Within the transactions line the first number the id of the transaction followed by the amount of items, then the item ids.

Example input file:
```
4
1  3  1 3 4
2  3  2 3 5
3  4  1 2 3 5
4  2  2 5
```

## result

The result will be put into a text file called "MiningResult.txt" where |FPs| is the amount of frequent patterns found and then the following lines are the item ids with the number of finds.

