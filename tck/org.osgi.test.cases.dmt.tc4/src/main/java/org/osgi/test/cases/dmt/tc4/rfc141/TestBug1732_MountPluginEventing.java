/*******************************************************************************
 * Copyright (c) Contributors to the Eclipse Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0 
 *******************************************************************************/
package org.osgi.test.cases.dmt.tc4.rfc141;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.DmtAdmin;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.service.dmt.spi.MountPlugin;
import org.osgi.service.dmt.spi.MountPoint;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventConstants;
import org.osgi.service.event.EventHandler;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.GenericDataPlugin;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.Node;

public class TestBug1732_MountPluginEventing extends DmtTestSupport {

	DmtAdmin dmtAdmin;
	DmtSession session;
	ServiceRegistration< ? >	pluginRegistration;


	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("setting up");
		dmtAdmin = getService(DmtAdmin.class);
	}

	@AfterEach
	public void tearDown() throws Exception {
		System.out.println("tearing down");
		if (session != null && session.getState() == DmtSession.STATE_OPEN)
			session.close();
		if ( pluginRegistration != null )
			pluginRegistration.unregister();
		unregisterAllServices();
		ungetAllServices();
	}

	@Test
	public void testAddedEvent() throws Exception {
		checkPostEvent(DmtConstants.EVENT_TOPIC_ADDED, false);
	}

	@Test
	public void testDeletedEvent() throws Exception {
		checkPostEvent(DmtConstants.EVENT_TOPIC_DELETED, false);
	}

	@Test
	public void testReplacedEvent() throws Exception {
		checkPostEvent(DmtConstants.EVENT_TOPIC_REPLACED, false);
	}

	@Test
	public void testCopiedEvent() throws Exception {
		checkPostEvent(DmtConstants.EVENT_TOPIC_COPIED, true);
	}

	@Test
	public void testRenamedEvent() throws Exception {
		checkPostEvent(DmtConstants.EVENT_TOPIC_RENAMED, true);
	}

	public void checkPostEvent( String topic, boolean withNewNodes ) throws Exception {

		String[] nodes = new String[] { "X", "Y"};
		String[] newNodes = new String[] { "X1", "Y1"};

		GenericDataPlugin plugin = preparePlugin( "./A" );
		Dictionary<String,Object> props = new Hashtable<>();
		if ( withNewNodes )
			props.put( EventConstants.EVENT_TOPIC, new String[] {
				DmtConstants.EVENT_TOPIC_COPIED,
				DmtConstants.EVENT_TOPIC_RENAMED
			});
		else
			props.put( EventConstants.EVENT_TOPIC, new String[] {
					DmtConstants.EVENT_TOPIC_ADDED,
					DmtConstants.EVENT_TOPIC_DELETED,
					DmtConstants.EVENT_TOPIC_REPLACED
				});

		registerService(EventHandler.class.getName(), plugin, props);

		assertThat(plugin.lastReceivedEvent).isNull();

		MountPoint mountPoint = plugin.lastAddedMountPoints.get(0);
		Dictionary<String,Object> additionalProperties = new Hashtable<>();
		additionalProperties.put( "testKey1", "testValue1" );
		additionalProperties.put( "testKey2", "testValue2" );

		// try to overwrite default properties
		additionalProperties.put( DmtConstants.EVENT_PROPERTY_NODES, "faked nodes" );
		additionalProperties.put( DmtConstants.EVENT_PROPERTY_NEW_NODES, "faked newnodes" );
		additionalProperties.put( DmtConstants.EVENT_PROPERTY_SESSION_ID, "faked session.id" );

		if ( withNewNodes )
			mountPoint.postEvent(topic, nodes, newNodes, additionalProperties );
		else
			mountPoint.postEvent(topic, nodes, additionalProperties );

		Thread.sleep(200);

		Event event = plugin.lastReceivedEvent;
		System.out.println("############# plugin: " + plugin);
		assertThat(event).isNotNull();
		assertThat(event.getTopic()).isEqualTo(topic);

		// check that given props are there (if they have non-conflicting names)
		assertThat(event.getProperty("testKey1")).isEqualTo("testValue1");
		assertThat(event.getProperty("testKey2")).isEqualTo("testValue2");

		// check that protected properties where not overwritten
		String[] eventNodes = (String[]) event.getProperty(DmtConstants.EVENT_PROPERTY_NODES);
		assertThat(Arrays.asList( eventNodes )).isEqualTo(Arrays.asList(new String[] {"./A/X", "./A/Y"}));

		if ( withNewNodes ) {
			String[] eventNewNodes = (String[]) event.getProperty(DmtConstants.EVENT_PROPERTY_NEW_NODES);
			assertThat(Arrays.asList( eventNewNodes )).isEqualTo(Arrays.asList(new String[] {"./A/X1", "./A/Y1"}));
		}
		else
			assertThat(event.getProperty(DmtConstants.EVENT_PROPERTY_NEW_NODES)).isNull();

		// sessionID must be -1 for internal events
		assertThat(event.getProperty(DmtConstants.EVENT_PROPERTY_SESSION_ID)).as("the session.id property must be -1 for internal events").isEqualTo(Integer.valueOf(-1));
	}


	/**
	 * @throws Exception
	 */
	private GenericDataPlugin preparePlugin( String mountRoot )
			throws Exception {
		Node n = new Node(null, "A", "node A");
		GenericDataPlugin mountingPlugin = new GenericDataPlugin(
				"MountingPlugin", mountRoot, n);

		Dictionary<String,Object> props = new Hashtable<>();
		String[] dataRootURIs = new String[] {mountRoot};

		props.put(DataPlugin.DATA_ROOT_URIS, dataRootURIs);

		pluginRegistration = context.registerService(new String[] {DataPlugin.class.getName(), MountPlugin.class.getName() }, mountingPlugin, props);
		return mountingPlugin;
	}
}
