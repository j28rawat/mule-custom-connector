package com.mule.extension.internal;

import org.mule.runtime.extension.api.annotation.Extension;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.Sources;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.dsl.xml.Xml;

import com.mule.extension.internal.connection.PulsarConnectionProvider;
import com.mule.extension.internal.operation.PulsarPublisher;
import com.mule.extension.internal.source.PulsarListener;

@Extension(name = "Apache Pulsar")
@Xml(prefix = "pulsar")
@ConnectionProviders(PulsarConnectionProvider.class)
@Sources(PulsarListener.class)
@Operations(PulsarPublisher.class)
public class PulsarConnector {

}
