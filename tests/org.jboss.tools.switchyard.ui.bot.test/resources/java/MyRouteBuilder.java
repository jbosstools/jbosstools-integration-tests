package com.mycompany.camel.java;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

/**
 * 
 * @author apodhrad
 *
 */
public class MyRouteBuilder extends RouteBuilder {

	public void configure() {
		from("file:in")
				.streamCaching()
				.log("Received message '${body}' from ${header.camelfilename}")
				.process(new Processor() {

					@Override
					public void process(Exchange exchange) throws Exception {
						String body = exchange.getIn().getBody(String.class);
						exchange.getOut().setBody("Hello " + body.trim());
						exchange.getOut().setHeaders(exchange.getIn().getHeaders());
					}
				}).log("Output message '${body}' to ${header.camelfilename}")
				.to("file:out?fileName=${header.camelfilename}");
	}

}
