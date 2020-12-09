
// INSERT OR IGNORE INTO DELETED_BILL SELECT * FROM BILL WHERE PAYMENT_STATUS = 'PAID' WHERE CUSTOMER_NAME IS NOT NULL;
// INSERT OR IGNORE INTO DELETED_BILL SELECT *,(SELECT date('now')) AS DELETED_DATE FROM BILL WHERE PAYMENT_STATUS = 'PAID' AND CUSTOMER_NAME IS NOT NULL;


package application;

import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;




public class BillingController implements Initializable{
	String invoiceNo;
	double grandTotal;
	
	private static final int maxNameTextFieldLength = 30;
	BillingModel model = new BillingModel(); 
	ObservableList<String> itemNames;
	ObservableList<String> listViewNames = null;
	ObservableList<Item> items;
	String currentItemMaxQuantity = "0";
	Boolean firstTimeItemSelect = true, nameFlag = true, qtyFlag=true, discFlag=true;
	int sr = 1, discount;
	
	@FXML AnchorPane anchorPane;
	@FXML ComboBox<String> itemComboBox;
	@FXML Label dateLabel, priceLabel, outOfLabel, maxOfLabel, notificationLabel, invoiceLabel, toLabel, grandTotalLabel, amountInWordsLabel, valueOfSupplyLabel;
	@FXML TextField nameTextField, quantityTextField, discountTextField;
	@FXML ListView<String> listViewForNames;
	@FXML CheckBox paymentDoneCheckBox;
	@FXML TableView<Item> table;

	@FXML	TableColumn<Item,String> sr_column;
	@FXML	TableColumn<Item,String> description_column;
	@FXML	TableColumn<Item,String> qty_column;
	@FXML	TableColumn<Item,String> rate_column;
	@FXML	TableColumn<Item,String> amount_quantity;
	@FXML	TableColumn<Item,String> less_discount_quantity;
	@FXML	TableColumn<Item,String> value_of_supply_column;
	@FXML	TableColumn<Item,String> trade_column;

	
	public void loadItemComboBoxContents() {
		itemNames = model.getItemNameListAndLoadItemDetails();
		itemComboBox.setItems(itemNames);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		dateLabel.setText(dateLabel.getText() + " " + model.getDate());
		quantityTextField.setEditable(false);
		discountTextField.setEditable(false);
		paymentDoneCheckBox.setSelected(true);
		items = FXCollections.observableArrayList() ;
		initializeTable();
		addTypeListenners();
		System.out.println("BillingController: Billing Controller has initialized"); 
	}

	private void addTypeListenners() {
		
		nameTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				toLabel.setText("To: " + nameTextField.getText());
				listViewNames = model.getListViewNames(nameTextField.getText());
				if(listViewNames != null) {
					listViewForNames.setItems(listViewNames);
				}
				if (nameTextField.getText().length() > maxNameTextFieldLength) {
	                String s = nameTextField.getText().substring(0, maxNameTextFieldLength);
	                nameTextField.setText(s);
	          }
			}
			
		});
		
		quantityTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				//discountTextField.setText("" + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
				if(!quantityTextField.getText().equals("")) {
					discountTextField.setEditable(true);
				}
				if(quantityTextField.getText() != null) {
					if (!newValue.matches("\\d*")) {
						quantityTextField.setText(newValue.replaceAll("[^\\d]", ""));
					}
					setValueOfSupply();
					try {							
						if(Integer.parseInt(quantityTextField.getText()) > Integer.parseInt(currentItemMaxQuantity)) {
							while(Integer.parseInt(quantityTextField.getText()) > Integer.parseInt(currentItemMaxQuantity)) {
								quantityTextField.setText(quantityTextField.getText().substring(0, quantityTextField.getText().length() - 1));
							}
						}
						maxOfLabel.setText("/Max " + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
					}
					catch(NumberFormatException nfe) {
						System.out.println("Number Format Exception handled");
					}
					catch(Exception e) {
						notificationLabel.setText(e.toString());
					}
			       }
			}
		});
		
		discountTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					discountTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				setValueOfSupply();
				try {							
					if((Integer.parseInt(discountTextField.getText()) > (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))))) {
						while(Integer.parseInt(discountTextField.getText()) > Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))) {
							discountTextField.setText(discountTextField.getText().substring(0, discountTextField.getText().length() - 1));
							discount = Integer.parseInt(discountTextField.getText());
						}
					}
				}
				catch(NumberFormatException nfe) {
					System.out.println("Number Format Exception handled");
					valueOfSupplyLabel.setText("Value of Supply: 0");
				}
				catch(Exception e) {
					notificationLabel.setText(e.toString());
				}
			}
		});
		
	}

	public void initializeTable() {
		sr_column.setCellValueFactory(cellData -> cellData.getValue().srProperty());
		description_column.setCellValueFactory(cellData -> cellData.getValue().descProperty());
		qty_column.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
		rate_column.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
		amount_quantity.setCellValueFactory(cellData -> cellData.getValue().amtProperty());
		less_discount_quantity.setCellValueFactory(cellData -> cellData.getValue().dstProperty());
		value_of_supply_column.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
		trade_column.setCellValueFactory(cellData -> cellData.getValue().tradeProperty());
		table.setItems(items);
		setInvoiceNumber();
		loadItemComboBoxContents();
	}

	private void setInvoiceNumber() {
		invoiceNo = model.getInvoiceNo();
		if(invoiceNo == null) {
			notificationLabel.setText("Error in fetching Invoice");
		}
		invoiceLabel.setText("Invoice: PAR" + invoiceNo);
		
	}

	public void onPrintClick(ActionEvent ae) {
		

		
		
		try {
			if(nameTextField.getText().trim().length() == 0) {
				notificationLabel.setText("Enter the customer name");
				return;
			}
			if(itemComboBox.getSelectionModel().getSelectedItem() == null) {
				notificationLabel.setText("Select an item first");
				return;
			}
			if(Integer.parseInt(quantityTextField.getText()) == 0 || quantityTextField.getText() == "" ) {
				notificationLabel.setText("Set a quantity for your item");
				return;
			}
			if(items.isEmpty()) {
				notificationLabel.setText("Add at least one item");
				return;
			}
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure to print?");
				Optional <ButtonType> action = alert.showAndWait();
				if(action.get() == ButtonType.OK) {
					model.makeBill(nameTextField.getText().trim().toUpperCase(),table.getItems(),paymentDoneCheckBox.isSelected());
					model.printBill(invoiceNo,nameTextField.getText(),model.getDate(), NumberToWordsConverter.convert((int) grandTotal),Double.toString(grandTotal));
				}
				else return;
				
				AnchorPane pane;
				try {
					pane = FXMLLoader.load(getClass().getResource("Billing.fxml"));
					anchorPane.getChildren().setAll(pane);
				} catch (Exception e) {
					notificationLabel.setText(e.toString());
				}			
				
		} catch (NumberFormatException e) {
			e.printStackTrace();
			notificationLabel.setText("Enter all details ");
		}
	}
	private String addSpace(String s, int size) {
		StringBuffer sb = new StringBuffer();
		for(int i=0;i<size - s.length();i++)
			sb.append("  ");
		sb.append(s);
		return sb.toString();
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void printBillInA5() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Date date = new Date();
		System.out.println(dateFormat.format(date));
		 StringBuffer sb = new StringBuffer("123456789012345678901234567890123456789012345\nInvoice:	 PAR" + //12
				 (Integer.parseInt(model.getInvoiceNo())-1) + "                " + "Date: " + 
				 dateFormat.format(date) + "\n" +
				 "To: " + 
				 nameTextField.getText() + "\n\n\n");
		 StringBuffer sb2 = new StringBuffer();
		 String ss;
		 ObservableList<Item> list = table.getItems();
		 for(int i=0;i<list.size();i++) {
			 if(i == 0)
				 ss = "RM";
			 else 
				 ss= "ABCDEFGHIJKLMNOPQRST";
			 sb2.append(list.get(i).getSr()	 + "    " + addSpace(ss,20) + "        " + addSpace(list.get(i).getQty(),4) + "    " + addSpace(list.get(i).getRate(),4) + "    " + addSpace(String.valueOf((int)Float.parseFloat(list.get(i).getAmt())),5) 
					 + "    " + addSpace(list.get(i).getDst(),5) + "        " + addSpace(String.valueOf((int)Float.parseFloat(list.get(i).getValue())),5) + "\n\n"
					 );
		 }
		 Text text1 = new Text(sb.toString());
		 System.out.println(sb2.toString());
	     text1.setFont(Font.font("Verdana",18));
	     Text text2 = new Text(sb2.toString());
	     text1.setFont(Font.font("Verdana",15));
		 TextFlow printArea = new TextFlow(text1);
		 printArea.getChildren().add(text2);
		    PrinterJob printerJob = PrinterJob.createPrinterJob();

		    if (printerJob != null && printerJob.showPrintDialog(anchorPane.getScene().getWindow())) {
		    	Printer printer = Printer.getDefaultPrinter();
		    	PageLayout pageLayout = printer.createPageLayout(Paper.A5, PageOrientation.PORTRAIT, Printer.MarginType.EQUAL);
		        printArea.setMaxWidth(pageLayout.getPrintableWidth());

		        if (printerJob.printPage(printArea)) {
		            printerJob.endJob();
		            // done printing
		        } else {
		            System.out.println("Failed to print");
		        }
		    } else {
		        System.out.println("Canceled");
		    }
		
	}

	public void onSaveClick(ActionEvent ae) {
		

		try {
			if(nameTextField.getText().trim().length() == 0) {
				notificationLabel.setText("Enter the customer name");
				return;
			}
			if(itemComboBox.getSelectionModel().getSelectedItem() == null) {
				notificationLabel.setText("Select an item first");
				return;
			}
			if(Integer.parseInt(quantityTextField.getText()) == 0 || quantityTextField.getText() == "" ) {
				notificationLabel.setText("Set a quantity for your item");
				return;
			}
			if(items.isEmpty()) {
				notificationLabel.setText("Add at least one item");
				return;
			}
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure to save?");
				Optional <ButtonType> action = alert.showAndWait();
				if(action.get() == ButtonType.OK) {
					model.makeBill(nameTextField.getText().trim().toUpperCase(),table.getItems(),paymentDoneCheckBox.isSelected());
				}
				else return;
				
				AnchorPane pane;
				try {
					pane = FXMLLoader.load(getClass().getResource("Billing.fxml"));
					anchorPane.getChildren().setAll(pane);
				} catch (Exception e) {
					notificationLabel.setText(e.toString());
				}
				
				
		} catch (NumberFormatException e) {
			notificationLabel.setText("Enter all details ");
		}
	}
	
	public void onPaymentDoneCheckBoxClicked(ActionEvent ae) {
		
	}
	
	public void onClickUpdateAdd(ActionEvent ae) {
		try {
			if(nameTextField.getText() == "") {
				notificationLabel.setText("Enter the customer name");
				return;
			}
			if(itemComboBox.getSelectionModel().getSelectedItem() == null) {
				notificationLabel.setText("Select an item first");
				return;
			}
			if(Integer.parseInt(quantityTextField.getText()) == 0 || quantityTextField.getText() == "" ) {
				notificationLabel.setText("Set a quantity for your item");
				return;
			}
//		if(discountTextField.getText() == "") {
//			discountTextField.setText("0");
//			System.out.println("dffdfdf");
//			return;
//		}

			float totAmt = Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))*Integer.parseInt(quantityTextField.getText());
			@SuppressWarnings("unused")
			float valueOfSupply = totAmt;	
			Item item = new Item(Integer.toString(sr++), itemNames.get(itemComboBox.getSelectionModel().getSelectedIndex()), quantityTextField.getText(), model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()), Float.toString(totAmt), Integer.toString((Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex())) - discount)*(Integer.parseInt(quantityTextField.getText()))) ,Float.toString(discount*Integer.parseInt(quantityTextField.getText())) , discountTextField.getText()  );
			if(!items.isEmpty()) {	
				try {			
					Item tempItem;
					for(int i=0;i<items.size();i++) {
						tempItem = items.get(i);
						if(tempItem.getDesc().toString() == itemComboBox.getSelectionModel().getSelectedItem()) {	
							item.setSr(tempItem.getSr());
							items.remove(i);
							items.add(i, item);
							table.setItems(items);
							sr = items.size() + 1;
							notificationLabel.setText("");
							setGrandTotal();
							return;
						}
					}
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}		
			}
			items.add(item);
			sr = items.size() + 1;
			table.setItems(items);
			notificationLabel.setText("");
			setGrandTotal();
		} catch (NumberFormatException e) {
			notificationLabel.setText("Enter all details ");
		}
	}
	
	private void setGrandTotal() {
		try {
			grandTotal = 0;
			for(int i=0;i<items.size();i++) {
				Item item = items.get(i);
				grandTotal += Double.parseDouble(item.getValue()); 
			}
			System.out.println(grandTotal);
			grandTotalLabel.setText("Grand Total: " + Double.toString(grandTotal));
			amountInWordsLabel.setText("Amount in words: " + NumberToWordsConverter.convert((int) grandTotal) + " only");
			model.grandTotal = (int) grandTotal;
			
		} catch (NumberFormatException e) {
			System.out.println(e.getMessage());
		}
	}

	public void onRemoveItemLabelClick(MouseEvent me) {
		table.getSelectionModel().clearSelection();
		Item tempItem;
		for(int i=0;i<items.size();i++) {
			tempItem = items.get(i);
			if(tempItem.getDesc().toString() == itemComboBox.getSelectionModel().getSelectedItem()) {	
				items.remove(i);
				notificationLabel.setText("");
			}
		}
		for(int  i = 1 ; i < items.size() + 1 ; i++ ) {
			tempItem = items.get(i - 1);
			tempItem.setSr(Integer.toString(i));
		}
		sr = items.size() + 1;
		table.setItems(items);
	}
	
	public void addingQuantityTextFieldFocusListener() {
		quantityTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(newValue) {
						//discountTextField.setText("" + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
						discount = 0;
//						if(quantityTextField.getText().equals("") || quantityTextField.getText() == null) {
//							if(Integer.parseInt(quantityTextField.getText()) > 0) {
//								if(Integer.parseInt(quantityTextField.getText()) == 0) {
//									quantityTextField.setText("");
//									discount = 0;
//								}
//							}
//						}
						if(quantityTextField.getText().equals("0") || quantityTextField.getText().equals("")) {
							quantityTextField.setText("");
						}
					}
					else {
//						if(quantityTextField.getText().equals("")) {
//							quantityTextField.setText("0");
//							discount = 0;
//						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Number Format Exception handled"); 
				}
			}
			
		});
	}
	
	public void addingDiscountTextFieldFocusListener() {
		discountTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(itemComboBox.getSelectionModel().getSelectedItem() == null)
						return;
					if(newValue) {
						discountTextField.setText("");
						discount = 0;
						if(quantityTextField.getText().equals("")) {
							discount = 0;
							return;
						}
						if(Integer.parseInt(quantityTextField.getText()) == 0) {
							discount = 0;
							quantityTextField.setText("");
						}
					}
					else {
						if(discountTextField.getText().equals("")) {
							discountTextField.setText("" + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
							discount = 0;
						}
					}
				} catch (NumberFormatException e) {
					System.out.println("Number Format Exception handled");
				}
			}
			
		});
	}
		
	public void onEnterName(KeyEvent ke) {	
		
	}
	
	public void onNameListViewClicked(MouseEvent me) {
		if(listViewForNames.getSelectionModel().getSelectedItem() != null) {
			toLabel.setText("To: " + nameTextField.getText());
			nameTextField.setText(listViewForNames.getSelectionModel().getSelectedItem());
		}
	}
	
	public void onItemSelected(ActionEvent me) {
		if(model.getItemQuantity(itemComboBox.getSelectionModel().getSelectedIndex()).equals("0")) {
			notificationLabel.setText("Stock Empty");
			return;
		}
		maxOfLabel.setText("/Max " + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
		notificationLabel.setText("");
		quantityTextField.setEditable(true);
		discountTextField.setText("" + (Integer.parseInt(model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex()))));
		if(itemComboBox.getSelectionModel().getSelectedItem() != null) {
			String price = model.getItemPrice(itemComboBox.getSelectionModel().getSelectedIndex());
			setPriceLabel(price);
			currentItemMaxQuantity = model.getItemQuantity(itemComboBox.getSelectionModel().getSelectedIndex());
			outOfLabel.setText("/Out of " + currentItemMaxQuantity);
			quantityTextField.setText("0");
			if(firstTimeItemSelect) {
				addingQuantityTextFieldFocusListener();
				addingDiscountTextFieldFocusListener();
				firstTimeItemSelect = false;
			}
			if(table.getSelectionModel().getSelectedItem() != null) {
				Item i = table.getSelectionModel().getSelectedItem();
				quantityTextField.setText(i.getQty());
				discountTextField.setText(i.getDst()); /* OLD ONE WAS discountTextField.setText(i.getRate()); */
			}	
		}
		for(Item i : table.getItems()) {
			if(i.getDesc().equals(itemComboBox.getSelectionModel().getSelectedItem())) {
				quantityTextField.setText(i.getQty());
				discountTextField.setText(i.getDst());
			}
		}
	}
	
	public void onComboBoxClick(MouseEvent me) {
		table.getSelectionModel().clearSelection();
	}
	public void setPriceLabel(String price) {
		priceLabel.setText("Price: " + price);
	}
//	if(quantityTextField.getText() == "") {
//		quantityTextField.setText("0");
//	}TTT
	
	public void onNameTyped(KeyEvent ke) {		
		if(nameFlag == true) {
			nameFlag = false;
			
		}
	}
	
	public void onQuantityTypedListener(KeyEvent ke) {
		if(qtyFlag == false)
			return;
			qtyFlag = false;
			
		
	}
	
	public void setValueOfSupply() {
		try {
			discount  = 0;
			if(quantityTextField.getText().equals("")) {
				valueOfSupplyLabel.setText("Value of Supply: ");
			}
			if(!discountTextField.getText().equals("") || discountTextField.getText() != null || Integer.parseInt(discountTextField.getText()) >= 0)
				discount = Integer.parseInt(discountTextField.getText());
			valueOfSupplyLabel.setText("Value of Supply: " + Integer.toString(Integer.parseInt(quantityTextField.getText()) * (discount)));
		}
		catch(Exception e) {
			System.out.println("Expection " + e.getMessage() + " handled");
		}
	}
	
//	@Deprecated
//	public void onQuantityTyped(KeyEvent ke) {
//		if(itemComboBox.getSelectionModel().getSelectedItem() != null) {
//			if((ke.getCharacter() != "0" || ke.getCharacter() != "1" || ke.getCharacter() != "2" || ke.getCharacter() != "3" || ke.getCharacter() != "4" || 
//					ke.getCharacter() != "5" || ke.getCharacter() != "6" || ke.getCharacter() != "7" || ke.getCharacter() != "8" || ke.getCharacter() != "9")) {
//				quantityTextField.setText(quantityTextField.getText().replaceAll("[^\\d]", ""));
//				quantityTextField.positionCaret(quantityTextField.getText().length());
//			}
//		}
//	}
	public void onDiscountPress(KeyEvent ke) {
		try {
			if (discountTextField.getText().matches("\\d*")) {
				discount = Integer.parseInt(discountTextField.getText());
			}
		} catch (NumberFormatException e) {
			System.out.println("Number Format Exception handled");
		}
	}
	
	public void onDiscountRelease(KeyEvent ke) {
		try {
			if (discountTextField.getText().matches("\\d*")) {
				discount = Integer.parseInt(discountTextField.getText());
			}
		} catch (NumberFormatException e) {
			System.out.println("Number Format Exception handled");
		}
	}
	
	public void onDiscountTyped(KeyEvent ke) {
		if(discFlag == false)
			return;
		discFlag = false;

		
	}
	
	public void onSettingButtonClick(ActionEvent ae) {
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Settings.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (Exception e) {
			notificationLabel.setText(e.toString());
			e.printStackTrace();
		}
	}
	
	public void onStockButtonClick(ActionEvent ae) {
		
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Stock.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (Exception e) {
			notificationLabel.setText(e.toString());
			e.printStackTrace();
		}
	}
	
	public void onSearchButtonClick(ActionEvent ae) {
			
			AnchorPane pane;
			try {
				pane = FXMLLoader.load(getClass().getResource("Search.fxml"));
				anchorPane.getChildren().setAll(pane);
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	public void onTableItemClicked(MouseEvent me) {
		try {
			Item i = table.getSelectionModel().getSelectedItem();
			String selectedItem = i.getDesc();
			itemComboBox.setValue(selectedItem);
		} catch (Exception e) {
			if(e.toString().equals("java.lang.NullPointerException")) {
				System.out.println("The header of table was clicked");
			}
			else {
				notificationLabel.setText(e.toString());
			}
		}
	}
}



/*	To remove item directly from quantity textfield
if(Integer.parseInt(quantityTextField.getText()) == 0) {
							items.remove(i);
							if(items.isEmpty()){
								sr = 1;
								table.setItems(items);
								sr = items.size() + 1;
								return;							
							}
							int index = 0;
							while(index <= items.size()) {
								Item tempItem2 = items.get(index);
								tempItem2.setSr(Integer.toString(index++));								
							}
							table.setItems(items);
							sr = items.size() + 1;
							return;
						}
*/
