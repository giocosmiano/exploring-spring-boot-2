package com.giocosmiano.exploration.chapter06;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class MessagingWithSpringBootHealthIndicator implements HealthIndicator {

	/*
	 Implementing the HealthIndicator interface, Spring Boot will include it along with the pre-built
	 health checks when we hit /application/health

	 The name DevToolsForSpringBootHealthIndicator is used to create the indicator. HealthIndicator will be
	 trimmed off, and the remaining text will be formatted with lazy camel style

	 There is but one method in this interface (meaning you could implement it using a Java 8 lambda),
	 health(). It uses some plain old Java APIs to open a connection to a remote URL and fetch a status
	 code. If the status code is good, it will build a Health status code of UP. Otherwise, it will build a
	 Health status code of DOWN while also giving us the failed HTTP status code

	 Finally, if any other exceptions occur, we will also get a Health status code of DOWN but with the
	 information from the exception instead of a commonly coded error path

	 http://localhost:9006/actuator/health
	 */
	@Override
	public Health health() {
		try {
			URL url =
					new URL("http://localhost:9006/");
							HttpURLConnection conn =
							(HttpURLConnection) url.openConnection();
			int statusCode = conn.getResponseCode();
			if (statusCode >= 200 && statusCode < 300) {
				return Health.up().build();
			} else {
				return Health.down()
						.withDetail("HTTP Status Code", statusCode)
						.build();
			}
		} catch (IOException e) {
			return Health.down(e).build();
		}
	}
}
