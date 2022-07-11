package com.mule.extension.api.connection;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

import com.mule.extension.api.parameter.PulsarConnectionParameter;

public class PulsarConnection {

	private PulsarClient client;

	private PulsarConnectionParameter pulsarConnectionParameter;

	public PulsarConnection(PulsarConnectionParameter pulsarConnectionParameter) throws PulsarClientException {

		this.pulsarConnectionParameter = pulsarConnectionParameter;
		this.client = createClient();
	}

	public PulsarClient createClient() throws PulsarClientException {

		return PulsarClient.builder().serviceUrl(pulsarConnectionParameter.getBootstrapServer()).build();
	}

	public PulsarClient getClient() {

		return this.client;
	}

	public void invalidate() throws PulsarClientException {
		if (this.client != null) {

			((PulsarClient) client).close();
		}
	}

}
