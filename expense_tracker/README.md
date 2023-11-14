# hw1- Manual Review

The homework will be based on this project named "Expense Tracker",where users will be able to add/remove daily transaction. 

## Compile

To compile the code from terminal, use the following command:
```
cd src
javac ExpenseTrackerApp.java
java ExpenseTracker
```

You should be able to view the GUI of the project upon successful compilation. 

## Java Version
This code is compiled with ```openjdk 17.0.7 2023-04-18```. Please update your JDK accordingly if you face any incompatibility issue.

## Usability: Undo Functionality
The undo deletes the latest record in the table. The 'Undo' can be pressed multiple times to delete one/multiple transactions. The button disables when there are no transactions added or when every transaction is deleted.

The ExpenseTrackerApp.java, ExpenseTrackerView.java, ExpenseTrackerController.java, ExpenseTrackerModel.java have been updated to implement the undo functionality. 

## Filters:
The filter can be applied by amount or category. Users can select the amount or the category and the rows which match the filter criteria get highlighted in green. The filter fields have validation to check for appropriate values of category or amount.

## Validation:
The input text fields also have a validation. The amount should be between 1 to 1000 and the category should be from the list of predefined set of categories.

## Testability: Unit Test Suites
The test suites have been updated for the following cases:
1. Add Transaction
2. Invalid Input Handling
3. Filter by amount
4. Filter by category
5. Undo disallowed
6. Undo allowed

