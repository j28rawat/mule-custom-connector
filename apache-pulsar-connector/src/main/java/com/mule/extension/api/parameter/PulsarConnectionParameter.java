package com.mule.extension.api.parameter;

import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class PulsarConnectionParameter {

	@Parameter
	@DisplayName("Bootstrap Server URL")
	private String bootstrapServer;

	public String getBootstrapServer() {
		return bootstrapServer;
	}
}
