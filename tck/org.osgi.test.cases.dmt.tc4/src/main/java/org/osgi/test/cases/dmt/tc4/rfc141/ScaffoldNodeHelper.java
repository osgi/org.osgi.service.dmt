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

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.osgi.service.dmt.DmtAdmin;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.GenericDataPlugin;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.Node;

public abstract class ScaffoldNodeHelper extends DmtTestSupport {

	DmtAdmin dmtAdmin;
	DmtSession session;
	GenericDataPlugin dataPlugin;
	
	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("setting up");
		dmtAdmin = getService(DmtAdmin.class);
	}


	@AfterEach
	public void tearDown() throws Exception {
		System.out.println( "tearing down");
		if ( session != null && session.getState() == DmtSession.STATE_OPEN )
			session.close();
		unregisterAllServices();
		ungetAllServices();
	}
	

	void prepareScaffoldPlugin() throws Exception {
		String mountRoot = "./A/B";

		Node n3 = new Node(null, "B", "node B");
		@SuppressWarnings("unused")
		Node n4 = new Node(n3, "C", "node C");
		dataPlugin = new GenericDataPlugin("P1", mountRoot, n3);
		
		Dictionary<String,Object> props = new Hashtable<>();
		props.put(DataPlugin.DATA_ROOT_URIS, new String[] {mountRoot});
		
		registerService(DataPlugin.class.getName(), dataPlugin, props );

		// at this point "./A" must be a scaffold node that it maintained by the DmtAdmin
	}
	
	void assertStringArrayEquals( String[] expected, String[] actual ) throws Exception {
		assertThat(expected).isNotNull();
		assertThat(actual).isNotNull();
		assertThat(actual.length).isEqualTo(expected.length);
		for (int i = 0; i < actual.length; i++) {
			assertThat(actual[i]).isEqualTo(expected[i]);
		}
	}

}
