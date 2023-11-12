package controller;

import view.ExpenseTrackerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javax.swing.JOptionPane;

import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.TransactionFilter;

public class ExpenseTrackerController {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  /** 
   * The Controller is applying the Strategy design pattern.
   * This is the has-a relationship with the Strategy class 
   * being used in the applyFilter method.
   */
  private TransactionFilter filter;
  private Stack<Transaction> removedTransactions = new Stack<>();
  private Stack<Integer> removedTransactionIndices = new Stack<>();

  public ExpenseTrackerController(ExpenseTrackerModel model, ExpenseTrackerView view) {
    this.model = model;
    this.view = view;
  }

  public void setFilter(TransactionFilter filter) {
    // Sets the Strategy class being used in the applyFilter method.
    this.filter = filter;
  }

  public void refresh() {
    List<Transaction> transactions = model.getTransactions();
    view.refreshTable(transactions);
  }

  public boolean addTransaction(double amount, String category) {
    if (!InputValidation.isValidAmount(amount)) {
      return false;
    }
    if (!InputValidation.isValidCategory(category)) {
      return false;
    }
    
    Transaction t = new Transaction(amount, category);
    
    model.removedTransactions.push(t);
    model.removedTransactionIndices.push(model.getTransactions().size());
    
    System.out.println("Transaction Size = ");
    System.out.println(model.getTransactions().size());
    
    model.addTransaction(t);
    view.getTableModel().addRow(new Object[]{t.getAmount(), t.getCategory(), t.getTimestamp()});
    
    
    
    refresh();
    return true;
  }

  public void applyFilter() {
    //null check for filter
    if(filter!=null){
      // Use the Strategy class to perform the desired filtering
      List<Transaction> transactions = model.getTransactions();
      List<Transaction> filteredTransactions = filter.filter(transactions);
      List<Integer> rowIndexes = new ArrayList<>();
      for (Transaction t : filteredTransactions) {
        int rowIndex = transactions.indexOf(t);
        if (rowIndex != -1) {
          rowIndexes.add(rowIndex);
        }
      }
      view.highlightRows(rowIndexes);
    }
    else{
      JOptionPane.showMessageDialog(view, "No filter applied");
      view.toFront();}

  }
  
  public void undoTransaction() {
      try {
          if (!model.removedTransactionIndices.empty()) {
              int removedTransactionIndex = model.removedTransactionIndices.pop();
              System.out.println("RemovedTransaction Index is: ");
              System.out.println(removedTransactionIndex);
              model.undoTransaction(removedTransactionIndex);              
              view.getUndoBtn().setEnabled(!model.removedTransactionIndices.empty());
              refresh();
          }
      } catch (IllegalArgumentException exception) {
          JOptionPane.showMessageDialog(view, exception.getMessage());
          view.toFront();
      }
  }
  
  

}
