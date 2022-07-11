package com.mule.extension.internal.connection;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.api.connection.ConnectionProvider;
import org.mule.runtime.api.connection.ConnectionValidationResult;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;

import com.mule.extension.api.connection.PulsarConnection;
import com.mule.extension.api.parameter.PulsarConnectionParameter;

public class PulsarConnectionProvider implements ConnectionProvider<PulsarConnection> {

	@ParameterGroup(name = "Connection")
	PulsarConnectionParameter pulsarConnectionParameter;

	@Override
	public PulsarConnection connect() throws ConnectionException {

		try {
			return new PulsarConnection(pulsarConnectionParameter);
		} catch (PulsarClientException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void disconnect(PulsarConnection connection) {

		try {
			connection.invalidate();
		} catch (PulsarClientException e) {
			e.printStackTrace();
		}
	}

	@Override
	public ConnectionValidationResult validate(PulsarConnection connection) {

		PulsarClient client = connection.getClient();
		ConnectionValidationResult result = null;

		// Check whether PulsarClient is alive
		result = (!((PulsarClient) client).isClosed()) ? ConnectionValidationResult.success()
				: ConnectionValidationResult.failure("Apache Pulsar Connection Test Failed", new Exception());
		return result;
	}

}
