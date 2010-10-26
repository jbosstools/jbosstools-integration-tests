package org.smooks;

public class OrderItem {
	private long productId;
	private Integer quantity;
	private double price;

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public byte byteValue() {
		return quantity.byteValue();
	}

	public int compareTo(Integer anotherInteger) {
		return quantity.compareTo(anotherInteger);
	}

	public double doubleValue() {
		return quantity.doubleValue();
	}

	public boolean equals(Object obj) {
		return quantity.equals(obj);
	}

	public float floatValue() {
		return quantity.floatValue();
	}

	public int hashCode() {
		return quantity.hashCode();
	}

	public int intValue() {
		return quantity.intValue();
	}

	public long longValue() {
		return quantity.longValue();
	}

	public short shortValue() {
		return quantity.shortValue();
	}

	public String toString() {
		return quantity.toString();
	}

}
