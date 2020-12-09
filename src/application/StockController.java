package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class StockController implements Initializable{
	
	ObservableList<Items> tableItems;
	StockModel model = new StockModel();
	Boolean addFlag = true, subFlag = true, firstTimeAddingFocusListener = true;
	int currentItemQuantity;
	String editBeingEdited;
	
	@FXML AnchorPane anchorPane; 
	@FXML Label notificationLabel, saveEditedItemLabel, clearEditedItemLabel;
	@FXML CheckBox allBatchCheckbox;
	@FXML TextField batchTextField, quantityTextField, mrpTextField, tradePriceTextField; 
	
	@FXML TableView<Items> table;
	@FXML TableColumn<Items,String> batch_column; 
	@FXML TableColumn<Items,String> qty_column; 
	@FXML TableColumn<Items,String> price_column; 
	@FXML TableColumn<Items,String> mrp_column;
	@FXML TableColumn<Items,String> date_column;
	
	public void onBillingButtonClicked(ActionEvent ae) {
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Billing.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	public void onSearchButtonClick(ActionEvent ae) {
		
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Search.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (Exception e) {
			notificationLabel.setText(e.toString());
		}
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
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		initializeTable();
		saveEditedItemLabel.setText("");
//		comboBox.setItems(model.getItemNames());
//		addTextField.setEditable(false);
//		subTextField.setEditable(false);
//		if(firstTimeAddingFocusListener) {
//			addingAddTextFieldFocusListener();
//			addingSubTextFieldFocusListener();
//		}
		System.out.println("StockController: Stock Controller has initialized"); 
	}

	public void initializeTable() {
		tableItems = FXCollections.observableArrayList() ;
		batch_column.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
		qty_column.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
		price_column.setCellValueFactory(cellData -> cellData.getValue().priceProperty());
		mrp_column.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
		date_column.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		tableItems = model.getItems(allBatchCheckbox.isSelected());
		table.setItems(tableItems);
		
	}
	
	public void onAllBatchCheckBoxClicked(ActionEvent ae) {
		tableItems = model.getItems(allBatchCheckbox.isSelected());
		table.setItems(tableItems);
	}
	
//	@Deprecated
//	public void onAddImageClicked(MouseEvent me) {
//		if(comboBox.getSelectionModel().getSelectedItem() == null) {
//			notificationLabel.setText("Select an Item First");
//			return;
//		}
//		if(addTextField.getText().equals("") || addTextField.getText().equals("0")) {
//			notificationLabel.setText("Enter a value");
//			return;
//		}
//		notificationLabel.setText("");
//		String addQuantity = addTextField.getText();
//		String currentItem = comboBox.getSelectionModel().getSelectedItem();
//		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Confirmation Dialog");
//		alert.setHeaderText(null);
//		alert.setContentText("Are you sure to add " + addQuantity + " " + currentItem + "(s)?");
//		Optional <ButtonType> action = alert.showAndWait();
//		if(action.get() == ButtonType.OK) {
//			model.addQuantityToCurrent(addQuantity,currentItem);
//			currentItemQuantity = Integer.parseInt(model.getItemQuantity(comboBox.getSelectionModel().getSelectedIndex()));
//			reloadUI();
//		}
//	}
	
@SuppressWarnings("unused")
	//	@Deprecated
//	public void onSubImageClicked(MouseEvent me) {
//		if(comboBox.getSelectionModel().getSelectedItem() == null) {
//			notificationLabel.setText("Select an Item First");
//			return;
//		}
//		if(subTextField.getText().equals("") || subTextField.getText().equals("0")) {
//			notificationLabel.setText("Enter a value");
//			return;
//		}
//		notificationLabel.setText("");
//		String subQuantity = subTextField.getText();
//		String currentItem = comboBox.getSelectionModel().getSelectedItem();
//		
//		Alert alert = new Alert(AlertType.CONFIRMATION);
//		alert.setTitle("Confirmation Dialog");
//		alert.setHeaderText(null);
//		alert.setContentText("Are you sure to remove " + subQuantity + " " + currentItem + "(s)?");
//		Optional <ButtonType> action = alert.showAndWait();
//		if(action.get() == ButtonType.OK) {
//			model.subQuantityToCurrent(subQuantity,currentItem);
//			currentItemQuantity = Integer.parseInt(model.getItemQuantity(comboBox.getSelectionModel().getSelectedIndex()));
//			reloadUI();
//		}	
//	}
	@Deprecated
	private void reloadUI() {
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Stock.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	public void onAddItemButtonClicked(ActionEvent ae) {
		notificationLabel.setText("");
		if(batchTextField.getText().trim().equals("")) {
			notificationLabel.setText("Enter the Batch");
			return;
		}
		if(quantityTextField.getText().trim().equals("")) {
			notificationLabel.setText("Enter the quantity");
			return;
		}
		if(mrpTextField.getText().trim().equals("")) {
			notificationLabel.setText("Enter the MRP");
			return;
		}
		if(tradePriceTextField.getText().trim().equals("")) {
			tradePriceTextField.setText(mrpTextField.getText());
		}
		if(saveEditedItemLabel.getText().trim().equals("Save")) {
			notificationLabel.setText("Click on the edit button to save changes");
			return;
		}
		Date date = new Date();
		notificationLabel.setText("");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure to add batch " + batchTextField.getText() +  " ?");
		Optional <ButtonType> action = alert.showAndWait();
		if(action.get() == ButtonType.OK) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");  
		    date = new Date();  
		    String dateString = formatter.format(date);
			try {
				model.addItemToDatabase(batchTextField.getText().trim(), quantityTextField.getText().trim(), tradePriceTextField.getText().trim(), mrpTextField.getText().trim(),dateString);
			} catch (SQLException e) {
				if(e.toString().indexOf("[SQLITE_CONSTRAINT_UNIQUE]") > 0) {
					notificationLabel.setText("The name already exists");
					return;
				}
				else
					e.printStackTrace();
			}
			tableItems = model.getItems(allBatchCheckbox.isSelected());
			table.setItems(tableItems);
			notificationLabel.setText("");
			saveEditedItemLabel.setText("");
			batchTextField.setText("");
			quantityTextField.setText("");
			mrpTextField.setText("");
			tradePriceTextField.setText("");
			table.getSelectionModel().clearSelection();
		}	
	}
//		model.setAddOrEditFlag(1);
//		loadAddOrEditScreen();
	
	@SuppressWarnings("unlikely-arg-type")
	public void onEditItemButtonClicked(ActionEvent ae) {
//		if(comboBox.getSelectionModel().getSelectedItem() == null) {
//			notificationLabel.setText("Select an Item from the drop down list to edit");
//			return;
//		}
		notificationLabel.setText("");
		if(table.getSelectionModel().getSelectedItem() == null || table.getSelectionModel().getSelectedItem().equals("")) {
			notificationLabel.setText("Select an Item to edit");
			return;
		}
		editBeingEdited = table.getSelectionModel().getSelectedItem().getDescription();
		model.setItemNameToEdit(table.getSelectionModel().getSelectedItem().getDescription());
		batchTextField.setText(table.getSelectionModel().getSelectedItem().getDescription());
		quantityTextField.setText(table.getSelectionModel().getSelectedItem().getQuantity());
		tradePriceTextField.setText(table.getSelectionModel().getSelectedItem().getPrice());
		mrpTextField.setText(table.getSelectionModel().getSelectedItem().getMrp());
		saveEditedItemLabel.setText("Save");
		clearEditedItemLabel.setText("Clear");
//		model.setAddOrEditFlag(0);
//		model.setItemNameToEdit(comboBox.getSelectionModel().getSelectedItem());
//		loadAddOrEditScreen();
	}
	
	public void onClearEditLabelClicked(MouseEvent me) {
		notificationLabel.setText("");
		saveEditedItemLabel.setText("");
		batchTextField.setText("");
		quantityTextField.setText("");
		mrpTextField.setText("");
		tradePriceTextField.setText("");
		table.getSelectionModel().clearSelection();
	}
	
	public void onSaveEditLabelClicked(MouseEvent me) {
		try {
			model.updateItemDetails(batchTextField.getText(), quantityTextField.getText(), mrpTextField.getText(), tradePriceTextField.getText());
			saveEditedItemLabel.setText("");
			batchTextField.setText("");
			quantityTextField.setText("");
			mrpTextField.setText("");
			tradePriceTextField.setText("");
			table.getSelectionModel().clearSelection();
			tableItems = model.getItems(allBatchCheckbox.isSelected());
			table.setItems(tableItems);
			notificationLabel.setText("");
		} catch (SQLException e) {
			if(e.toString().indexOf("UNIQUE constraint failed") > 0) {
				notificationLabel.setText("The name already exists");
			}
			else
				e.printStackTrace();
		}
	}
	
	public void onDeleteButtonClicked(ActionEvent ae) {
//		if(comboBox.getSelectionModel().getSelectedItem() == null) {
//			notificationLabel.setText("Select an Item from the drop down list to delete");
//			return;
//		}
		notificationLabel.setText("");
		saveEditedItemLabel.setText("");
		batchTextField.setText("");
		quantityTextField.setText("");
		mrpTextField.setText("");
		tradePriceTextField.setText("");
		if(table.getSelectionModel().getSelectedItem() == null) {
			notificationLabel.setText("Select an item to delete");
			return;
		}
		notificationLabel.setText("");
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure to remove " + table.getSelectionModel().getSelectedItem().getDescription() + "?");
		Optional <ButtonType> action = alert.showAndWait();
		if(action.get() == ButtonType.OK) {
			model.deleteItem(table.getSelectionModel().getSelectedItem().getDescription().toString());
			tableItems = model.getItems(allBatchCheckbox.isSelected());
			table.setItems(tableItems);
			table.getSelectionModel().clearSelection();
		}	
	}
	

//	private void loadAddOrEditScreen() {
////		Parent root;
////		try {
////			root = FXMLLoader.load(getClass().getResource("/application/AddOrEditItemScreen.fxml"));
////			Scene scene = new Scene(root);
////			Stage primaryStage = new Stage();
////			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
////			primaryStage.setScene(scene);
////			primaryStage.show();
////			primaryStage.setResizable(false);
////			primaryStageSetSize(primaryStage);
////			stageSetPosition(primaryStage,400,400);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}
//		
//		Parent pane;
//		try {
//			pane = FXMLLoader.load(getClass().getResource("/application/AddOrEditItemScreen.fxml"));
//			anchorPane.getChildren().setAll(pane);
//			Stage stage = (Stage) pane.getScene().getWindow();
//			stage.setResizable(false);
//			stage.setHeight(400);
//			stage.setWidth(400);
//			stageSetPosition(stage,400,400);	
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		
////		AnchorPane pane;
////		try {
////			pane = FXMLLoader.load(getClass().getResource("/application/AddOrEditItemScreen.fxml"));
////			Stage stage = (Stage) pane.getScene().getWindow();
////			stage.setResizable(false);
////			primaryStageSetSize(stage);
////			stage.show();
////			anchorPane.getChildren().setAll(pane);
////		} catch (IOException e) {
////			e.printStackTrace();
////		}	
//		
//	}
	
	public void stageSetPosition(Stage primaryStage,int height,int width) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getWidth()/2 - width/2);
		primaryStage.setY(primaryScreenBounds.getHeight()/2 - height/2);
		System.out.println();
	}
	
	public void onTableItemClicked(MouseEvent me) {

	}
	
	public void onBatchTyped(KeyEvent ke) {
		batchTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(batchTextField.getText().length() > 15) {
					try {
						batchTextField.setText(batchTextField.getText().substring(0, 16));
					}
					catch(Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});
		
	}
	
	public void onQuantityTyped(KeyEvent ke) {
		quantityTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					quantityTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				if(quantityTextField.getText().length() > 6) {
					try {
						quantityTextField.setText(quantityTextField.getText().substring(0, 6));
					}
					catch(Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});
		
	}
	
	public void onMRPTyped(KeyEvent ke) {
		mrpTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					mrpTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				if(mrpTextField.getText().length() > 6) {
					try {
						mrpTextField.setText(mrpTextField.getText().substring(0, 6));
					}
					catch(Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});
		
	}
	
	public void onTradePriceTyped(KeyEvent ke) {
		tradePriceTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					tradePriceTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				if(tradePriceTextField.getText().length() > 6) {
					try {
						tradePriceTextField.setText(tradePriceTextField.getText().substring(0, 6));
					}
					catch(Exception e) {
						System.out.println(e.toString());
					}
				}
			}
		});
		
	}
	
//	@Deprecated
//	public void onAddTextFieldTyped(KeyEvent ke) {
//		
//		if(addFlag == false)
//			return;
//			addFlag = false;
//		addTextField.textProperty().addListener(new ChangeListener<String>() {
//
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if (!newValue.matches("\\d*")) {
//					addTextField.setText(newValue.replaceAll("[^\\d]", ""));
//		        }
//				
//				 if (addTextField.getText().length() > 5) {
//		                String s = addTextField.getText().substring(0, 5);
//		                addTextField.setText(s);
//		            }
//			}
//		});
//		
//	}
	
//	@Deprecated
//	public void onSubTextFieldTyped(KeyEvent ke) {
//		
//		if(subFlag == false)
//			return;
//			subFlag = false;
//			subTextField.textProperty().addListener(new ChangeListener<String>() {
//
//			@Override
//			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
//				if (!newValue.matches("\\d*")) {
//					subTextField.setText(newValue.replaceAll("[^\\d]", ""));
//		        }
//				
//				if (subTextField.getText().length() > 5) {
//		                String s = subTextField.getText().substring(0, 5);
//		                subTextField.setText(s);
//		        }
//				
//				try {		
//					
//					if((Integer.parseInt(subTextField.getText()) > (currentItemQuantity))) {
//						while(Integer.parseInt(subTextField.getText()) > (currentItemQuantity)) {
//							subTextField.setText(subTextField.getText().substring(0, subTextField.getText().length() - 1));
//						}
//					}
//				}
//				catch(NumberFormatException nfe) {
//					System.out.println("Number Format Exception handled");
//				}
//				catch(Exception e) {
//					System.out.println(e.getMessage());
//				}
//				 
//				 
//			}
//		});
//		
//	}
//	
//	@Deprecated
//	public void addingAddTextFieldFocusListener() {
//		addTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				try {
//					if(!addTextField.isEditable())
//						return;
//					if(newValue) {
//						addTextField.setText("");
//						subTextField.setText("");
//					}
//					else {
//						if(addTextField.getText().equals(""))
//							addTextField.setText("0");
//					}
//
//				} catch (NumberFormatException e) {
//					System.out.println("Number Format Exception handled");
//				}
//			}
//			
//		});
//	}
	
//	@Deprecated
//	public void addingSubTextFieldFocusListener() {
//		subTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {
//
//			@Override
//			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//				try {
//					if(!subTextField.isEditable())
//						return;
//					if(newValue) {
//						addTextField.setText("");
//						subTextField.setText("");
//					}
//					else {
//						if(subTextField.getText().equals(""))
//							subTextField.setText("0");
//					}
//
//				} catch (NumberFormatException e) {
//					System.out.println("Number Format Exception handled");
//				}
//			}
//			
//		});
//	}
	
}
