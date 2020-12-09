package application;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class AddOrEditItemScreenController implements Initializable {
	private static final int maxItemNameLength = 50;
	AddOrEditItemScreenModel model = new AddOrEditItemScreenModel();
	Boolean  nameFlag = true, qtyFlag = true, priceFlag = true, focusListenerFirstTimeFlag = true;
	int addOrEditMode;
	@FXML AnchorPane anchorPane;
	@FXML TextField itemNameTextField,itemQuantityTextField,itemPriceTextField;
	@FXML Label cancelButton, addOrEditLabel, notificationLabel;
	@FXML Button addOrUpdateButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setAddOrEditLabelAndButton();
		setTextFieldValuesIfEditMode();
		addingFocusListeners();
	}

	private void setTextFieldValuesIfEditMode() {
		if(model.isAddMode() == 1) 
			return;
		String itemName = model.getItemToEdit();
		itemNameTextField.setText(itemName);
		itemQuantityTextField.setText(model.getEditItemQuantity);
		itemPriceTextField.setText(model.getEditItemPrice);
		
	}

	private void addingFocusListeners() {
		if(focusListenerFirstTimeFlag == true) {
			//addingNameTextFieldFocusListener();
			addingQuantityTextFieldFocusListener();
			addingPriceTextFieldFocusListener();
			focusListenerFirstTimeFlag = false;
		}
		
	}

	public void setAddOrEditLabelAndButton() {
		if(model.isAddMode() == 1) {
			addOrEditMode = 1;
			addOrEditLabel.setText("Add Item");
			addOrUpdateButton.setText("Add");
		}
		else if(model.isAddMode() == 0){
			addOrEditMode = 0;
			addOrEditLabel.setText("Edit Item");
			addOrUpdateButton.setText("Update");
		}
	}
	
	public void onClickAddOrUpdate(ActionEvent ae) {
		if(addOrEditMode == 1) {
			if(itemNameTextField.getText().trim().length() == 0 || itemQuantityTextField.getText().trim().length() == 0 || itemPriceTextField.getText().trim().length() == 0) {
				notificationLabel.setText("Enter the all details");
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure to add the item " + itemNameTextField.getText().toUpperCase() + "?");
			Optional <ButtonType> action = alert.showAndWait();
			if(action.get() == ButtonType.OK) {
				try {
					model.addItemToDatabase(itemNameTextField.getText().trim().toUpperCase(),itemQuantityTextField.getText(),itemPriceTextField.getText());
					loadStockUI();
				} catch (SQLException e) {
					if(e.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: ITEMS.ITEM_NAME)"))
						notificationLabel.setText("The Item " + itemNameTextField.getText() + " already exists");
				}
			}	
		}
		
		else{
			
			if(itemNameTextField.getText().trim().length() == 0 || itemQuantityTextField.getText().trim().length() == 0 || itemPriceTextField.getText().trim().length() == 0) {
				notificationLabel.setText("Enter the all details");
				return;
			}
			
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Are you sure to edit the item " + itemNameTextField.getText().toUpperCase() + "?");
			Optional <ButtonType> action = alert.showAndWait();
			if(action.get() == ButtonType.OK) {
				try {
					model.updateItemDetails(itemNameTextField.getText(),itemQuantityTextField.getText(),itemPriceTextField.getText());
				} catch (SQLException e) {
					if(e.getMessage().equals("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: ITEMS.ITEM_NAME)"))
						notificationLabel.setText("The item of that name already exists");
					return;
				}
				loadStockUI();
			}
			
		}
	}
	
	public void onClickCancelButton(MouseEvent me) {
		loadStockUI();
	}

	public void loadStockUI() {
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Stock.fxml"));
			anchorPane.getChildren().setAll(pane);
			Stage stage = (Stage) pane.getScene().getWindow();
			stage.setResizable(false);
			stage.setHeight(630);
			stage.setWidth(1080);
			stageSetPosition(stage,630,1080);	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void stageSetPosition(Stage primaryStage,int height,int width) {
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
		primaryStage.setX(primaryScreenBounds.getWidth()/2 - width/2);
		primaryStage.setY(primaryScreenBounds.getHeight()/2 - height/2);
		System.out.println();
	}
	
	public void onNameTextFieldTyped(KeyEvent ke) {
		
		if(nameFlag == false)
			return;
		nameFlag = false;
		itemNameTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				 if (itemNameTextField.getText().length() > maxItemNameLength) {
		                String s = itemNameTextField.getText().substring(0, maxItemNameLength);
		                itemNameTextField.setText(s);
		            }
			}
		});
		
	}
	
	public void onQuantityTextFieldTyped(KeyEvent ke) {
		
		if(qtyFlag == false)
			return;
		qtyFlag = false;
		itemQuantityTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itemQuantityTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				
				 if (itemQuantityTextField.getText().length() > 5) {
		                String s = itemQuantityTextField.getText().substring(0, 5);
		                itemQuantityTextField.setText(s);
		            }
			}
		});
		
	}
	
	public void onPriceTextFieldTyped(KeyEvent ke) {
		
		if(priceFlag == false)
			return;
		priceFlag = false;
		itemPriceTextField.textProperty().addListener(new ChangeListener<String>() {

			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if (!newValue.matches("\\d*")) {
					itemPriceTextField.setText(newValue.replaceAll("[^\\d]", ""));
		        }
				
				 if (itemPriceTextField.getText().length() > 5) {
		                String s = itemPriceTextField.getText().substring(0, 5);
		                itemPriceTextField.setText(s);
		            }
			}
		});
		
	}
	
	public void addingNameTextFieldFocusListener() {
		itemNameTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(newValue) {
						itemNameTextField.setText("");
					}
				} catch (NumberFormatException e) {
					// Number Format Exception
				}
			}
			
		});
	}
	
	public void addingQuantityTextFieldFocusListener() {
		itemQuantityTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(newValue) {
						itemQuantityTextField.setText("");
						System.out.println("Focus+");
					}
				} catch (NumberFormatException e) {
					System.out.println("Number format exception handled");
				}
			}
			
		});
	}
	
	public void addingPriceTextFieldFocusListener() {
		itemPriceTextField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				try {
					if(newValue) {
						itemPriceTextField.setText("");
					}
				} catch (NumberFormatException e) {
					// Number Format Exception
				}
			}
			
		});
	}
	
}
