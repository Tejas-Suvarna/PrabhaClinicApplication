package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SettingsController implements Initializable{

	SettingsModel model = new SettingsModel();
	@FXML TextField cgstTextField,sgstTextField;
	@FXML AnchorPane anchorPane;
	protected int maxGSTTextFieldLength=5;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		setGSTTextFields();
		addTextFieldTypeListenners();
	}
	
	private void addTextFieldTypeListenners() {
		cgstTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if (cgstTextField.getText().length() > maxGSTTextFieldLength) {
	                String s = cgstTextField.getText().substring(0, maxGSTTextFieldLength);
	                cgstTextField.setText(s);
	          }
			}
			
		});
		
		sgstTextField.textProperty().addListener(new ChangeListener<String>() {
			
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				if (sgstTextField.getText().length() > maxGSTTextFieldLength) {
	                String s = sgstTextField.getText().substring(0, maxGSTTextFieldLength);
	                sgstTextField.setText(s);
	          }
			}
			
		});
		
	}

	private void setGSTTextFields() {
		cgstTextField.setText(model.getCGST());
		sgstTextField.setText(model.getSGST());
		
	}

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
			e.printStackTrace();
		}
	}
	
	public void onStockButtonClick(ActionEvent ae) {
		
		AnchorPane pane;
		try {
			pane = FXMLLoader.load(getClass().getResource("Stock.fxml"));
			anchorPane.getChildren().setAll(pane);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void onClickSaveButton(ActionEvent ae) {
		model.setGST(cgstTextField.getText(),sgstTextField.getText());
	}
	
	
}
