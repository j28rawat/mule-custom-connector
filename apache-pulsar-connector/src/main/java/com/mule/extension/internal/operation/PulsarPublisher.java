package com.mule.extension.internal.operation;

import java.io.IOException;

import org.apache.pulsar.client.api.Producer;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

import com.mule.extension.api.connection.PulsarConnection;
import com.mule.extension.api.parameter.PulsarProducerParameter;
import com.mule.extension.api.util.Utility;

public class PulsarPublisher {

	@DisplayName("Publisher")
	public void publish(@Connection PulsarConnection connection, PulsarProducerParameter pulsarProducerParameter)
			throws IOException {

		// Create producer
		Producer<byte[]> producer = connection.createClient().newProducer().topic(pulsarProducerParameter.getTopic())
				.create();

		// You can then send messages to the broker and topic you specified:
		producer.send(Utility.readFully(pulsarProducerParameter.getBody().getValue()));

		// Close producer
		producer.close();
	}

}
