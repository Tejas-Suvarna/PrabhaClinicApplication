package application;

import javafx.beans.property.SimpleStringProperty;

public class BillItems {
	public SimpleStringProperty billNo;
	public SimpleStringProperty name;
	public SimpleStringProperty date;
	public SimpleStringProperty totalAmount;
	public SimpleStringProperty paymentStatus;
	public SimpleStringProperty quantity;
	
	public BillItems(SimpleStringProperty billNo, SimpleStringProperty name, SimpleStringProperty date,
			SimpleStringProperty totalAmount, SimpleStringProperty paymentStatus,SimpleStringProperty quantity) {
		this.billNo = billNo;
		this.name = name;
		this.date = date;
		this.totalAmount = totalAmount;
		this.paymentStatus = paymentStatus;
		this.quantity = quantity;
	}

	public BillItems(String billId, String customerName, String date, String totalAmt, String paymentStatus, String quantity) {
		this.billNo = new SimpleStringProperty(billId);
		this.name = new SimpleStringProperty(customerName);
		this.date = new SimpleStringProperty(date);
		this.totalAmount = new SimpleStringProperty(totalAmt);
		this.paymentStatus = new SimpleStringProperty(paymentStatus);
		this.quantity = new SimpleStringProperty(quantity);
	}
	public String getBillNo() {
		return billNo.get();
	}

	public void setBillNo(SimpleStringProperty billNo) {
		this.billNo = billNo;
	}
	
	public void setQuantity(SimpleStringProperty quantity) {
		this.quantity = quantity;
	}

	public String getName() {
		return name.get();
	}

	public void setName(SimpleStringProperty name) {
		this.name = name;
	}

	public String getDate() {
		return date.get();
	}
	
	public String getQuantity() {
		return quantity.get();
	}

	public void setDate(SimpleStringProperty date) {
		this.date = date;
	}

	public String getTotalAmount() {
		return totalAmount.get();
	}

	public void setTotalAmount(SimpleStringProperty totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getPaymentStatus() {
		return paymentStatus.get();
	}

	public void setPaymentStatus(SimpleStringProperty paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public final SimpleStringProperty billNoProperty() {
		return this.billNo;
	}
	
	public final void setBillNo(final String billNo) {
		this.billNoProperty().set(billNo);
	}
	
	public final SimpleStringProperty nameProperty() {
		return this.name;
	}
	
	public final SimpleStringProperty quantityProperty() {
		return this.quantity;
	}
	
	public final void setName(final String name) {
		this.nameProperty().set(name);
	}
	
	public final SimpleStringProperty dateProperty() {
		return this.date;
	}
	
	public final void setDate(final String date) {
		this.dateProperty().set(date);
	}
	
	public final SimpleStringProperty totalAmountProperty() {
		return this.totalAmount;
	}
	
	public final SimpleStringProperty paymentStatusProperty() {
		return this.paymentStatus;
	}
	
	public final void setTotalAmount(final String totalAmount) {
		this.totalAmountProperty().set(totalAmount);
	}
}