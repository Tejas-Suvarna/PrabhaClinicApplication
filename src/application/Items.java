package application;

import javafx.beans.property.SimpleStringProperty;

public class Items {
	public SimpleStringProperty description;
	public SimpleStringProperty quantity;
	public SimpleStringProperty price;
	public SimpleStringProperty mrp;
	public SimpleStringProperty date;
	
	public Items(String description, String quantity, String price, String mrp, String date) {
		this.description = new SimpleStringProperty(description);
		this.quantity = new SimpleStringProperty(quantity);
		this.price = new SimpleStringProperty(price);
		this.mrp = new SimpleStringProperty(mrp);
		this.date = new SimpleStringProperty(date);
	}

	public Items() {
		// TODO Auto-generated constructor stub
	}

	public final SimpleStringProperty descriptionProperty() {
		return this.description;
	}
	
	public final String getDescription() {
		return this.descriptionProperty().get();
	}
	
	public final void setDescription(final String description) {
		this.descriptionProperty().set(description);
	}
	
	public final SimpleStringProperty quantityProperty() {
		return this.quantity;
	}
	
	public final String getQuantity() {
		return this.quantityProperty().get();
	}
	
	public final void setQuantity(final String quantity) {
		this.quantityProperty().set(quantity);
	}
	
	public final SimpleStringProperty priceProperty() {
		return this.price;
	}
	
	public final String getPrice() {
		return this.priceProperty().get();
	}
	
	public final void setPrice(final String price) {
		this.priceProperty().set(price);
	}
	
	public final SimpleStringProperty mrpProperty() {
		return this.mrp;
	}
	
	public final String getMrp() {
		return this.mrpProperty().get();
	}
	
	public final void setMrp(final String price) {
		this.mrpProperty().set(price);
	}
	
	public final SimpleStringProperty dateProperty() {
		return this.date;
	}
	
	public final String getDate() {
		return this.dateProperty().get();
	}
	
	public final void setDate(final String price) {
		this.dateProperty().set(price);
	}
	
}
