package application;

import javafx.beans.property.SimpleStringProperty;

public class Item {
	public SimpleStringProperty sr;
	public SimpleStringProperty desc;
	public SimpleStringProperty qty;
	public SimpleStringProperty rate;
	public SimpleStringProperty amt;
	public SimpleStringProperty dst;
	public SimpleStringProperty value;
	public SimpleStringProperty trade;
	public Item(String sr, String desc, String qty, String rate, String amt, String dst,
			String value, String trade) {
		this.sr = new SimpleStringProperty(sr);
		this.desc = new SimpleStringProperty(desc);
		this.qty = new SimpleStringProperty(qty);
		this.rate = new SimpleStringProperty(rate);
		this.amt = new SimpleStringProperty(amt);
		this.dst = new SimpleStringProperty(dst);
		this.value = new SimpleStringProperty(value);
		this.trade = new SimpleStringProperty(trade);
	}
	
	public Item() {
		this.sr = new SimpleStringProperty();
		this.desc = new SimpleStringProperty();
		this.qty = new SimpleStringProperty();
		this.rate = new SimpleStringProperty();
		this.amt = new SimpleStringProperty();
		this.dst = new SimpleStringProperty();
		this.value = new SimpleStringProperty();
		this.trade = new SimpleStringProperty();
	}
	
	public final SimpleStringProperty srProperty() {
		return this.sr;
	}
	
	public final String getSr() {
		return this.srProperty().get();
	}
	
	public final void setSr(final String sr) {
		this.srProperty().set(sr);
	}
	
	public final SimpleStringProperty tradeProperty() {
		return this.trade;
	}
	
	public final String getTrade() {
		return this.tradeProperty().get();
	}
	
	public final void setTrade(final String trade) {
		this.tradeProperty().set(trade);
	}
	
	public final SimpleStringProperty descProperty() {
		return this.desc;
	}
	
	public final String getDesc() {
		return this.descProperty().get();
	}
	
	public final void setDesc(final String desc) {
		this.descProperty().set(desc);
	}
	
	public final SimpleStringProperty qtyProperty() {
		return this.qty;
	}
	
	public final String getQty() {
		return this.qtyProperty().get();
	}
	
	public final void setQty(final String qty) {
		this.qtyProperty().set(qty);
	}
	
	public final SimpleStringProperty rateProperty() {
		return this.rate;
	}
	
	public final String getRate() {
		return this.rateProperty().get();
	}
	
	public final void setRate(final String rate) {
		this.rateProperty().set(rate);
	}
	
	public final SimpleStringProperty amtProperty() {
		return this.amt;
	}
	
	public final String getAmt() {
		return this.amtProperty().get();
	}
	
	public final void setAmt(final String amt) {
		this.amtProperty().set(amt);
	}
	
	public final SimpleStringProperty dstProperty() {
		return this.dst;
	}
	
	public final String getDst() {
		return this.dstProperty().get();
	}
	
	public final void setDst(final String dst) {
		this.dstProperty().set(dst);
	}
	
	public final SimpleStringProperty valueProperty() {
		return this.value;
	}
	
	public final String getValue() {
		return this.valueProperty().get();
	}
	
	public final void setValue(final String value) {
		this.valueProperty().set(value);
	}


	
}
