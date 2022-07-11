package com.mule.extension.api.parameter;

import java.io.InputStream;

import org.mule.runtime.api.metadata.TypedValue;
import org.mule.runtime.extension.api.annotation.dsl.xml.ParameterDsl;
import org.mule.runtime.extension.api.annotation.param.Content;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Summary;

public class PulsarProducerParameter {

	@Parameter
	@DisplayName("Topic")
	private String topic;

	@Parameter
	@ParameterDsl(allowReferences = false)
	@Content(primary = true)
	@Summary("The body of the Message")
	private TypedValue<InputStream> body;

	public String getTopic() {
		return topic;
	}

	public TypedValue<InputStream> getBody() {
		return body;
	}
}
