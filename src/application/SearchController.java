package application;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class SearchController implements Initializable {
	
	@FXML AnchorPane pane;
	@FXML ComboBox<String> dateCombo;
	@FXML DatePicker betweenStartDate, betweenEndDate;
	@FXML TextField billNoTextField, nameTextField;
	@FXML Label notificationLabel, changePaymentStatusLabel, salesLabel, totalLabel;
	@FXML CheckBox searchExactNameCheckBox;
	@FXML Button switchDeleteRestoreButton;
	@FXML Button deleteRestoreItemButton;
	
	@FXML TableView<BillItems> table;
	@FXML TableColumn<BillItems,String> billNo_column; 
	@FXML TableColumn<BillItems,String> name_column; 
	@FXML TableColumn<BillItems,String> date_column;
	@FXML TableColumn<BillItems,String> totalAmount_column;
	@FXML TableColumn<BillItems,String> paymentStatus_column; 
	@FXML TableColumn<BillItems,String> quantity_column;
	
	String shouldWeNotDisplayDeleted = new String("1"); 
	String selectedItem;
	boolean searchCheckboxSelected = false;
	SearchModel model = new SearchModel();
	ObservableList<BillItems> tableItems;
	ObservableList<String> dateComboItems = FXCollections.observableArrayList();
	int displayDeletedFlag = 0;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeTable();
		initializeDateCombo();
		addingFocusListeners();
		setTotalAndSalesLabel();
		System.out.println("SearchController: Search Controller has initialized"); 
	}

	private void setTotalAndSalesLabel() {
		salesLabel.setText("Sales: " + Integer.toString(model.getSales()) + " units");
		totalLabel.setText("Total: Rs." + Integer.toString(model.getTotal()));
	}

	private void addingFocusListeners() {
		addingBillTextFieldFocusListener();
	}

	public void addingBillTextFieldFocusListener() {
		billNoTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(newValue) {
						nameTextField.setText("");
						dateCombo.getSelectionModel().clearSelection();
						betweenStartDate.setValue(null);
						betweenEndDate.setValue(null);
						tableItems = model.getBillItems(shouldWeNotDisplayDeleted);
						table.setItems(tableItems);
						System.out.println("Focus+");
					}
					else {
						billNoTextField.setText("");
						//updateTable();
					}
					
				} catch (NumberFormatException e) {
					System.out.println("Number format exception handled");
				}
			}
			
		});
	}
	
	public void onSearchCheckBoxClicked(ActionEvent ae) {
		if(searchExactNameCheckBox.isSelected()) {
			searchCheckboxSelected = true;
		}
		else searchCheckboxSelected = false;
		updateTable();
		setTotalAndSalesLabel();
	}
	
	private void initializeTable() {
		 table.setRowFactory(tv -> {
	            TableRow<BillItems> row = new TableRow<>();
	            row.setOnMouseClicked(event -> {
	                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
	            		String selectedBillId = null;
	            		try {
	            			selectedBillId = table.getSelectionModel().getSelectedItem().getBillNo();
	            			selectedItem = selectedBillId;
	            		} catch (Exception e1) {
	            			System.out.println(e1.toString());
	            			return;
	            		}
//	            		FXMLLoader Loader = new FXMLLoader();
//	            		Loader.setLocation(getClass().getResource("IndividualBill.fxml"));
//	            		try {
//	            			Loader.load();
//	            		}
//	            		catch(IOException ie) {
//	            			System.out.println(ie.toString());
//	            		}
//	            		IndividualBillController i = Loader.getController();
//	            		i.setBillNo(selectedBillId);
//	            		
//	            		Parent p = Loader.getRoot();
//	            		Stage stage = new Stage();
//	            		stage.setScene(new Scene(p));
//	            		stage.showAndWait();
	            		try {			
	            			Stage primaryStage = new Stage();
	            			FXMLLoader loader = new FXMLLoader();
	            			Pane root = loader.load(getClass().getResource("IndividualBill.fxml").openStream());
	            			IndividualBillController i = (IndividualBillController)loader.getController();
	            			Scene scene = new Scene(root);
	            			primaryStage.setScene(scene);
	            			stageSetSize(primaryStage);
	            			primaryStage.show();
	            			i.setBillNo(selectedBillId);	
	            		} catch (Exception e) {
	            			e.printStackTrace();
	            		}
	                }
	            });
	            return row ;
	        });
		tableItems = FXCollections.observableArrayList();
		billNo_column.setCellValueFactory(cellData -> cellData.getValue().billNoProperty());
		name_column.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		date_column.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		totalAmount_column.setCellValueFactory(cellData -> cellData.getValue().totalAmountProperty());
		paymentStatus_column.setCellValueFactory(cellData -> cellData.getValue().paymentStatusProperty());
		quantity_column.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
		tableItems = model.getBillItems(shouldWeNotDisplayDeleted);
		table.setItems(tableItems);
	}

	private void initializeDateCombo() {
		dateComboItems.add("All");
		dateComboItems.add("Today");
		dateComboItems.add("This Week");
		dateComboItems.add("This Month");
		dateComboItems.add("This Year");
		dateCombo.setItems(dateComboItems);
	}

	public void onBillingButtonClicked(ActionEvent ae) {
		AnchorPane anchorPane;
		try {
			anchorPane = FXMLLoader.load(getClass().getResource("Billing.fxml"));
			pane.getChildren().setAll(anchorPane);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void onStockButtonClick(ActionEvent ae) {
		AnchorPane anchorPane;
		try {
			anchorPane = FXMLLoader.load(getClass().getResource("Stock.fxml"));
			pane.getChildren().setAll(anchorPane);
		} catch (Exception e) {
			
		}
	}
	
	public void onSettingButtonClick(ActionEvent ae) {
		AnchorPane anchorPane;
		try {
			anchorPane = FXMLLoader.load(getClass().getResource("Settings.fxml"));
			pane.getChildren().setAll(anchorPane);
		} catch (Exception e) {
			notificationLabel.setText(e.toString());
			e.printStackTrace();
		}
	}
	
	
	public void onComboBoxDateSelected(ActionEvent ae) {
		if(dateCombo.getSelectionModel().getSelectedIndex() > 0 || dateCombo.getSelectionModel().getSelectedIndex() < 7) {
			updateTable();
			setTotalAndSalesLabel();
		}
	}
	
	public void onComboBoxDateClicked(MouseEvent me) {
		betweenEndDate.setValue(null);
		betweenStartDate.setValue(null);
	}

	
	public void updateTable() {
		notificationLabel.setText("");
		tableItems = model.generateQueryAndGetBills(billNoTextField.getText().trim(), nameTextField.getText().toUpperCase().trim(),
				dateCombo.getSelectionModel().getSelectedItem(),betweenStartDate.getValue(),
				betweenEndDate.getValue(), searchCheckboxSelected);
		if(tableItems != null) {	
			if(tableItems.isEmpty()) {
				notificationLabel.setText("No results");
			    for ( int i = 0; i< table.getItems().size(); i++) {
			    	table.getItems().clear(); 
			    } 
			}
			else table.setItems(tableItems);
		}
	}
	
	public void onBillTyped(KeyEvent ke) {
		billNoTextField.textProperty().addListener(new ChangeListener<String>() {
		    @Override
		    public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        if (!newValue.matches("\\d*")) {
		        	billNoTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
		    }
		});	
	}
	public void onBillTypeReleased(KeyEvent ke) {
		updateTable();
		setTotalAndSalesLabel();
	}
	
	public void onNameTyped(KeyEvent ke) {
		updateTable();
		setTotalAndSalesLabel();
	}
	
	public void onAmountTypeReleased(KeyEvent ke) {
		updateTable();
		setTotalAndSalesLabel();
	}
	
	public void onStartEndDayMouseClick(MouseEvent me) {
		dateCombo.getSelectionModel().clearSelection();
		updateTable();
		setTotalAndSalesLabel();
	}
	
	public void onStartEndDaySelected(ActionEvent ae) {
		if(betweenStartDate == null || betweenEndDate == null)
			return;
		if(betweenStartDate.getValue() == null || betweenEndDate.getValue() == null)
			return;
		if(betweenStartDate.getValue().toString().compareTo(betweenEndDate.getValue().toString()) == -1) {
			updateTable();
			setTotalAndSalesLabel();
		}
		else {
			notificationLabel.setText("End date must be ahead of the start date");
			updateTable();
			setTotalAndSalesLabel();
		}
	}
	
	public void onTableItemClicked(MouseEvent me) {
		changePaymentStatusLabel.setText("Change selected item Payment Status");
//		
//		String selectedBillId = null;
//		try {
//			selectedBillId = table.getSelectionModel().getSelectedItem().getBillNo();
//			selectedItem = selectedBillId;
//		} catch (Exception e1) {
//			System.out.println(e1.toString());
//			return;
//		}
////		FXMLLoader Loader = new FXMLLoader();
////		Loader.setLocation(getClass().getResource("IndividualBill.fxml"));
////		try {
////			Loader.load();
////		}
////		catch(IOException ie) {
////			System.out.println(ie.toString());
////		}
////		IndividualBillController i = Loader.getController();
////		i.setBillNo(selectedBillId);
////		
////		Parent p = Loader.getRoot();
////		Stage stage = new Stage();
////		stage.setScene(new Scene(p));
////		stage.showAndWait();
//		try {			
//			Stage primaryStage = new Stage();
//			FXMLLoader loader = new FXMLLoader();
//			Pane root = loader.load(getClass().getResource("IndividualBill.fxml").openStream());
//			IndividualBillController i = (IndividualBillController)loader.getController();
//			Scene scene = new Scene(root);
//			primaryStage.setScene(scene);
//			stageSetSize(primaryStage);
//			primaryStage.show();
//			i.setBillNo(selectedBillId);	
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
	public void onClickPrintDisplayedButton(ActionEvent ae) throws ParseException {
		try {
			
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Confirmation Dialog");
				alert.setHeaderText(null);
				alert.setContentText("Are you sure to print?");
				Optional <ButtonType> action = alert.showAndWait();
				if(action.get() == ButtonType.OK) {
					model.printReport(nameTextField.getText(),betweenStartDate.getValue(),betweenEndDate.getValue(),dateCombo.getSelectionModel().getSelectedItem(),Integer.toString(model.getSales()),Integer.toString(model.getTotal()));
				}
				else return;
						
				
		} catch (NumberFormatException e) {
			e.printStackTrace();
			notificationLabel.setText("Enter all details ");
		}
	}
	
	public void onChangePaymentMousePressed(MouseEvent me) {
		model.changePayment(table.getSelectionModel().getSelectedItem().getBillNo(),table.getSelectionModel().getSelectedItem().getPaymentStatus());
		tableItems = model.getBillItems(shouldWeNotDisplayDeleted);
		table.setItems(tableItems);
		changePaymentStatusLabel.setText("");
	}
	
	public void stageSetSize(Stage primaryStage) {
		primaryStage.setHeight(550);
		primaryStage.setWidth(650);
		primaryStage.setResizable(false);
	}
	
	public void onClearAllClicked(ActionEvent ae) {
		changePaymentStatusLabel.setText("");
		notificationLabel.setText("");
		billNoTextField.setText("");
		nameTextField.setText("");
		dateCombo.getSelectionModel().clearSelection();
		betweenStartDate.setValue(null);
		betweenEndDate.setValue(null);
		searchExactNameCheckBox.setSelected(false);
		tableItems = model.getBillItems(shouldWeNotDisplayDeleted);
		table.setItems(tableItems);
		model.clearTempBuffer();
	}
	
	public void onClickClearSelectionLabel(MouseEvent me) {
		table.getSelectionModel().clearSelection();
		selectedItem = null;
	}
	
//	public void onClickShowDeletedButton(ActionEvent ae) {
//		if(shouldWeNotDisplayDeleted.equals("1")) {
//			shouldWeNotDisplayDeleted = new String("0");
//			switchDeleteRestoreButton.setText("Show Existing Items");
//			deleteRestoreItemButton.setText("Restore Selected Item");
//		} else {
//			shouldWeNotDisplayDeleted = new String("1");
//			switchDeleteRestoreButton.setText("Show Deleted Items");
//			deleteRestoreItemButton.setText("Delete Selected Item");
//		}
//		changePaymentStatusLabel.setText("");
//		notificationLabel.setText("");
//		billNoTextField.setText("");
//		nameTextField.setText("");
//		dateCombo.getSelectionModel().clearSelection();
//		betweenStartDate.setValue(null);
//		betweenEndDate.setValue(null);
//		searchExactNameCheckBox.setSelected(false);
//		tableItems = model.getBillItems(shouldWeNotDisplayDeleted);
//		table.setItems(tableItems);
//		model.clearTempBuffer();
//		
//	}
	
	public void onClickDeleteSelectedItem(ActionEvent ae) {
		if(table.getSelectionModel().getSelectedItem() == null) {
			return;
		}
		model.changeDeleteFlag(table.getSelectionModel().getSelectedItem().getBillNo());
		table.getSelectionModel().clearSelection();
		AnchorPane anchorPane;
		try {
			anchorPane = FXMLLoader.load(getClass().getResource("Search.fxml"));
			pane.getChildren().setAll(anchorPane);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		
	}
}