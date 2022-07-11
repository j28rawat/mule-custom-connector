package com.mule.extension.internal.source;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClientException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.exception.MuleException;
import org.mule.runtime.extension.api.annotation.Alias;
import org.mule.runtime.extension.api.annotation.param.Connection;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.runtime.operation.Result;
import org.mule.runtime.extension.api.runtime.source.Source;
import org.mule.runtime.extension.api.runtime.source.SourceCallback;
import com.mule.extension.api.connection.PulsarConnection;

@Alias("listener")
@DisplayName("Message Listener")
@MediaType(value = ANY, strict = false)
public class PulsarListener extends Source<InputStream, Map<String, String>> {

	// Parameters
	@Parameter
	@DisplayName("Topic")
	private String topic;

	@Parameter
	@DisplayName("Subscription Name")
	private String subscriptionName;

	// Connection
	@Connection
	private ConnectionProvider<PulsarConnection> connectionProvider;

	// Declarations
	private PulsarConnection pulsarConnection;

	private Message msg;

	@Override
	public void onStart(SourceCallback<InputStream, Map<String, String>> sourceCallback) throws MuleException {

		// Get connection
		pulsarConnection = connectionProvider.connect();

		Consumer consumer = null;

		try {

			consumer = pulsarConnection.getClient().newConsumer().topic(topic).subscriptionName(subscriptionName)
					.subscribe();
		} catch (PulsarClientException pulsarClientException) {

			pulsarClientException.printStackTrace();
		}

		while (true) {

			try {

				// Wait for a message
				msg = consumer.receive();

				// Create attributes
				Map<String, String> attributes = new HashMap<String, String>();
				String msgIdStr = new String(msg.getMessageId().toByteArray());
				attributes.put("messageId", msgIdStr);
				attributes.put("producerName", msg.getProducerName());
				attributes.put("publishTime", String.valueOf(msg.getPublishTime()));
				attributes.put("topicName", msg.getTopicName());

				// Create payload
				ByteArrayInputStream inputStream = new ByteArrayInputStream(msg.getData());

				// Create Result object
				Result<InputStream, Map<String, String>> result = Result.<InputStream, Map<String, String>>builder()
						.output(inputStream).attributes(attributes)
						.mediaType(org.mule.runtime.api.metadata.MediaType.ANY).build();

				// Push result to flow
				sourceCallback.handle(result);

				// Acknowledge the message if successful
				consumer.acknowledge(msg);

			} catch (Exception e) {

				// Negative acknowledgement on failure
				consumer.negativeAcknowledge(msg);
			}
		}
	}

	@Override
	public void onStop() {

		if (pulsarConnection != null) {
			try {

				pulsarConnection.getClient().close();
			} catch (PulsarClientException e) {

				e.printStackTrace();
			}
		}

	}

}
