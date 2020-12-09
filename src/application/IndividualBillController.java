package application;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;

public class IndividualBillController implements Initializable{

	IndividualBillModel model;
	
	@FXML Label toLabel, invoiceLabel, dateLabel, grandTotalLabel, amountInWordsLabel, paymentStatusLabel;
	@FXML TableView<Item> table;

	@FXML	TableColumn<Item,String> sr_column;
	@FXML	TableColumn<Item,String> description_column;
	@FXML	TableColumn<Item,String> qty_column;
	@FXML	TableColumn<Item,String> rate_column;
	@FXML	TableColumn<Item,String> amount_quantity;
	@FXML	TableColumn<Item,String> less_discount_quantity;
	@FXML	TableColumn<Item,String> value_of_supply_column;
	@FXML	TableColumn<Item,String> trade_column;

	
	String billNo;
	ObservableList<Item> items;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		items = FXCollections.observableArrayList() ;
		initializeTable();
		System.out.println("IndividualBillController: Bill Controller has initialized");
	}
	
	private void initializeLabels() {
		toLabel.setText(toLabel.getText() + " " + model.getToLabelText());
		invoiceLabel.setText(invoiceLabel.getText() + " PAR" + model.getInvoiceLabelText());
		dateLabel.setText(dateLabel.getText() + " " + model.getDateLabelText());
		grandTotalLabel.setText(grandTotalLabel.getText() + " " + model.getGrandTotalText());
		amountInWordsLabel.setText(amountInWordsLabel.getText() + " " + NumberToWordsConverter.convert(Integer.parseInt(model.getGrandTotalText().trim())));
		paymentStatusLabel.setText(model.getPaymentStatus());
		if(model.paymentStatusText.equals("PAID"))
			paymentStatusLabel.setTextFill(Color.web("#00b111"));
		else paymentStatusLabel.setTextFill(Color.web("#ff0000"));
	}

	private void initializeTable() {
		sr_column.setCellValueFactory(cellData -> cellData.getValue().srProperty());
		description_column.setCellValueFactory(cellData -> cellData.getValue().descProperty());
		qty_column.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
		rate_column.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
		amount_quantity.setCellValueFactory(cellData -> cellData.getValue().amtProperty());
		less_discount_quantity.setCellValueFactory(cellData -> cellData.getValue().dstProperty());
		value_of_supply_column.setCellValueFactory(cellData -> cellData.getValue().valueProperty());
		trade_column.setCellValueFactory(cellData -> cellData.getValue().tradeProperty());
	}

	public void setBillNo(String billNo) {
		this.billNo = billNo;
		model = new IndividualBillModel(billNo);
		items = model.retrieveItems(billNo);
		table.setItems(items);
		initializeLabels();
	}
	
	public void onPrintClick() {
		model.printBill(model.getInvoiceLabelText(), model.getToLabelText(), model.getDateLabelText(), NumberToWordsConverter.convert(Integer.parseInt(model.getGrandTotalText().trim())), model.getGrandTotalText());
	}
	

}

