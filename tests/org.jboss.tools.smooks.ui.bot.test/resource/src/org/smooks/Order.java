package org.smooks;

import java.util.List;

import org.smooks.Header;
import org.smooks.OrderItem;

public class Order {
	private Header header;
	private List<OrderItem> orderItems;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}

}
