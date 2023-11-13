// package test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.Before;
import org.junit.Test;

import controller.ExpenseTrackerController;
import model.ExpenseTrackerModel;
import model.Transaction;
import model.Filter.AmountFilter;
import model.Filter.CategoryFilter;
import view.ExpenseTrackerView;


public class TestExample {
  
  private ExpenseTrackerModel model;
  private ExpenseTrackerView view;
  private ExpenseTrackerController controller;

  @Before
  public void setup() {
    model = new ExpenseTrackerModel();
    view = new ExpenseTrackerView();
    controller = new ExpenseTrackerController(model, view);
  }

    public double getTotalCost() {
        double totalCost = 0.0;
        List<Transaction> allTransactions = model.getTransactions(); // Using the model's getTransactions method
        for (Transaction transaction : allTransactions) {
            totalCost += transaction.getAmount();
        }
        return totalCost;
    }


    public void checkTransaction(double amount, String category, Transaction transaction) {
	assertEquals(amount, transaction.getAmount(), 0.01);
        assertEquals(category, transaction.getCategory());
        String transactionDateString = transaction.getTimestamp();
        Date transactionDate = null;
        try {
            transactionDate = Transaction.dateFormatter.parse(transactionDateString);
        }
        catch (ParseException pe) {
            pe.printStackTrace();
            transactionDate = null;
        }
        Date nowDate = new Date();
        assertNotNull(transactionDate);
        assertNotNull(nowDate);
        // They may differ by 60 ms
        assertTrue(nowDate.getTime() - transactionDate.getTime() < 60000);
    }


    @Test
    public void testAddTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add a transaction
	double amount = 50.0;
	String category = "food";
        assertTrue(controller.addTransaction(amount, category));
    
        // Post-condition: List of transactions contains only
	//                 the added transaction	
        assertEquals(1, model.getTransactions().size());
    
        // Check the contents of the list
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);
	
	// Check the total amount
        assertEquals(amount, getTotalCost(), 0.01);
    }


    @Test
    public void testRemoveTransaction() {
        // Pre-condition: List of transactions is empty
        assertEquals(0, model.getTransactions().size());
    
        // Perform the action: Add and remove a transaction
	double amount = 50.0;
	String category = "food";
        Transaction addedTransaction = new Transaction(amount, category);
        model.addTransaction(addedTransaction);
    
        // Pre-condition: List of transactions contains only
	//                the added transaction
        assertEquals(1, model.getTransactions().size());
	Transaction firstTransaction = model.getTransactions().get(0);
	checkTransaction(amount, category, firstTransaction);

	assertEquals(amount, getTotalCost(), 0.01);
	
	// Perform the action: Remove the transaction
        model.removeTransaction(addedTransaction);
    
        // Post-condition: List of transactions is empty
        List<Transaction> transactions = model.getTransactions();
        assertEquals(0, transactions.size());
    
        // Check the total cost after removing the transaction
        double totalCost = getTotalCost();
        assertEquals(0.00, totalCost, 0.01);
    }
    
    @Test
    public void testAddTransactionView() {
      
    // Pre-condition: List of transactions is empty
     assertEquals(0, model.getTransactions().size());
      
      // Add a transaction
      assertTrue(controller.addTransaction(50.0, "food"));
      
      
      //From the JTable, check the row count and values at individual cells
      assertEquals(2, view.getTransactionsTable().getRowCount());
      assertEquals(50.0, view.getTransactionsTable().getValueAt(0,1));
      assertEquals("food", view.getTransactionsTable().getValueAt(0,2));
      
      //Check for total
      assertEquals(50.0, view.getTransactionsTable().getValueAt(1,3));
 
      //Check for timestamp value to be not null
      assertNotNull(view.getTransactionsTable().getValueAt(0,3));
      
    }
    
    
    @Test
    public void testInvalidInputHandling() {
      assertEquals(0, model.getTransactions().size());

      assertFalse(controller.addTransaction(-10.0, "food"));
      assertFalse(controller.addTransaction(1001.0, "food"));
      assertFalse(controller.addTransaction(800.0, "invalid"));
      assertFalse(controller.addTransaction(2200.0, "invalid"));

      assertEquals(0, model.getTransactions().size());
      assertEquals(0.0, getTotalCost(), 0.01);
    }
    
   
	@Test
    public void testFilterByAmount(){
    	assertEquals(0, model.getTransactions().size());
    	
    	assertTrue(controller.addTransaction(50.0, "food"));
    	assertTrue(controller.addTransaction(150.0, "food"));
    	assertTrue(controller.addTransaction(50.0, "other"));
    	
    	double amountFilterInput = 50.0;
    	
    	AmountFilter amountFilter = new AmountFilter(amountFilterInput);
    	
    	 controller.setFilter(amountFilter);
         
         
         List<Transaction> transactions = model.getTransactions();
         List<Transaction> filteredTransactions = amountFilter.filter(transactions);
         
         assertEquals(2, filteredTransactions.size());
         
         for(Transaction transaction : filteredTransactions){
             
        	 assertEquals(50.0, transaction.getAmount(), 0.01);
         } 	
    	
    }
	
	@Test
    public void testFilterByCategory(){
    	assertEquals(0, model.getTransactions().size());
    	
    	assertTrue(controller.addTransaction(50.0, "food"));
    	assertTrue(controller.addTransaction(150.0, "food"));
    	assertTrue(controller.addTransaction(50.0, "other"));
    	
    	String categoryFilterInput = "food";
        CategoryFilter categoryFilter = new CategoryFilter(categoryFilterInput);
    	
    	 controller.setFilter(categoryFilter);
         
         
         List<Transaction> transactions = model.getTransactions();
         List<Transaction> filteredTransactions = categoryFilter.filter(transactions);
         
         assertEquals(2, filteredTransactions.size());
         
         for(Transaction transaction : filteredTransactions){
             
        	 assertTrue(transaction.getCategory().equalsIgnoreCase(categoryFilterInput));
         } 	
    	
    }
    
    @Test
    public void testUndoDisallowed() {
    	
    	assertEquals(0, model.getTransactions().size());
    	
    	
    	assertTrue(controller.addTransaction(50.0, "food"));
    	
    	JButton button = view.getUndoBtn();
    	
    	assertTrue(button.isEnabled());
    	
    	controller.undoTransaction();
    	
    	assertFalse(button.isEnabled());	
    	
    }
    
    
    @Test
    public void testUndoAllowed() {
    	
    	assertEquals(0, model.getTransactions().size());
    	
    	assertTrue(controller.addTransaction(50.0, "food"));
    	assertTrue(controller.addTransaction(150.0, "food"));
    	
    	
    	assertEquals(3, view.getTransactionsTable().getRowCount());
    	assertEquals(200.0, view.getTransactionsTable().getValueAt(2,3));
    	
    	JButton button = view.getUndoBtn();
    	
    	assertTrue(button.isEnabled());
    	
    	controller.undoTransaction();
    	
    	assertEquals(2, view.getTransactionsTable().getRowCount());
    	assertEquals(50.0, view.getTransactionsTable().getValueAt(1,3));
    	assertEquals(50.0, view.getTransactionsTable().getValueAt(0,1));
        assertEquals("food", view.getTransactionsTable().getValueAt(0,2));
    	
    }
    
}
