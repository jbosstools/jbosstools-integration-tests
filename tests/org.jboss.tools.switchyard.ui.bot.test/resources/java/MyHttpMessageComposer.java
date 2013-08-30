package com.example.switchyard.http_bpmn;

import org.switchyard.Exchange;
import org.switchyard.Message;
import org.switchyard.component.http.composer.HttpBindingData;
import org.switchyard.component.http.composer.HttpMessageComposer;

public class MyHttpMessageComposer extends HttpMessageComposer {
	@Override
	public Message compose(HttpBindingData source, Exchange exchange)
			throws Exception {
		final Message message = exchange.createMessage();

		getContextMapper().mapFrom(source, exchange.getContext(message));

		String body = source.getBodyAsString();

		message.setContent(body);

		return message;
	}
}