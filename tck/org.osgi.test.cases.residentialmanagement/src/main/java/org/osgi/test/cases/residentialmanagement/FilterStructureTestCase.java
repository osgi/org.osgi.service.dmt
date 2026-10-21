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
package org.osgi.test.cases.residentialmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.MetaNode;
import org.osgi.service.log.LogService;
/**
 * This test case checks for the correct structure and metadata of the Filter tree.
 * It asserts that all mandatory nodes are there and compares the reported metadata 
 * and node types with the specified values.
 * 
 * @author Steffen Druesedow (Deutsche Telekom Laboratories)
 */
public class FilterStructureTestCase extends RMTTestBase {

	@SuppressWarnings("unused")
	private LogService log;
	
	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		log = getService(LogService.class);
	}

	/**
	 * asserts that the Filter node has correct children
	 * @throws DmtException
	 */
	@Test
	public void testFilterStructure() throws DmtException {
		String[] children;
		HashSet<String> mandatory = new HashSet<String>();
		HashSet<String> unknown = new HashSet<String>();

		session = dmtAdmin.getSession(FILTER_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FILTER_ROOT).isNotNull();
		// create one exemplary filter to initialize creation of the automatic nodes
		String relUri = "example";
		session.createInteriorNode(relUri);
		// TODO: This commit should not be necessary - needs check 
		session.commit();
		
		// 1st descendants
		children = session.getChildNodeNames(FILTER_ROOT + "/" + relUri);
		mandatory.add(FILTER);
		mandatory.add(TARGET);
		mandatory.add(LIMIT);
		mandatory.add(RESULT);
		mandatory.add(RESULT_URI_LIST);
		mandatory.add(INSTANCEID);

		for (String child : children) {
			if ( ! mandatory.contains(child))
				unknown.add(child);
			mandatory.remove(child);
		}
		assertThat(mandatory).as("There are children missing in the Filter node:" + mandatory).isEmpty();
		assertThat(unknown).as("There are unknown children in the Filter node:" + unknown).isEmpty();
		
		session.deleteNode(FILTER_ROOT + "/" + relUri);
		session.commit();
	}

	
	/**
	 * asserts that the Filter tree provides correct metadata and node types
	 * @throws Exception
	 */
	@Test
	public void testMetaDataAndTypes() throws Exception {
		session = dmtAdmin.getSession(FILTER_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FILTER_ROOT).isNotNull();
		String uri = FILTER_ROOT + "/" + "example";

		assertMetaData( FILTER_ROOT, false, "_G__", "0,1", MetaNode.PERMANENT, DmtData.FORMAT_NODE);
		assertMetaData( FILTER_ROOT + "/<>", false, "AG_D", "0..*", MetaNode.DYNAMIC, DmtData.FORMAT_NODE);
		assertMetaData( FILTER_ROOT + "/<>/" + FILTER, true, "_GR_", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_STRING);
		assertMetaData( FILTER_ROOT + "/<>/" + TARGET, true, "_GR_", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_STRING);
		assertMetaData( FILTER_ROOT + "/<>/" + LIMIT,  true, "_GR_", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_INTEGER);
		assertMetaData( FILTER_ROOT + "/<>/" + RESULT, false, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_NODE);
		assertMetaData( FILTER_ROOT + "/<>/" + INSTANCEID, true, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_INTEGER);
		assertMetaData( FILTER_ROOT + "/<>/" + RESULT_URI_LIST, false, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_NODE);
		assertMetaData( FILTER_ROOT + "/<>/" + RESULT_URI_LIST + "/<>", true, "_G__", "0..*", MetaNode.DYNAMIC, DmtData.FORMAT_STRING);

		// create one exemplary filter to initialize creation of the automatic nodes
		session.createInteriorNode(uri);
		// TODO: This commit should not be necessary - needs check 
		session.commit();
		try {
			

			assertThat(session.getNodeType(FILTER_ROOT)).as("The nodeType of the Filter node must be " + DmtConstants.DDF_MAP).isEqualTo(DmtConstants.DDF_MAP);
			assertThat(session.getNodeType(uri + "/" + RESULT_URI_LIST)).as("The nodeType of the ResultUriList node must be " + DmtConstants.DDF_LIST).isEqualTo(DmtConstants.DDF_LIST);
			
		} catch (DmtException de) {
			de.printStackTrace();
			fail("unexpeced DmtException: " + de.getMessage());
		}
		finally {
			session.deleteNode(uri);
			session.commit();
		}
	}

	/**
	 * asserts that the children of the Filter node have the correct defaults
	 * @throws DmtException
	 */
	@Test
	public void testDefaults() throws DmtException {

		session = dmtAdmin.getSession(FILTER_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FILTER_ROOT).isNotNull();
		// create one exemplary filter to initialize creation of the automatic nodes
		String uri = FILTER_ROOT + "/" + "example";
		session.createInteriorNode(uri);
		session.commit(); // TODO: This commit should not be necessary - needs check

		try {
			assertThat(session.getNodeValue(uri + "/" + TARGET).getString()).as("The Default value of the Target node must be an empty String").isEmpty();
			assertThat(session.getNodeValue(uri + "/" + FILTER).getString()).as("The Default value of the Filter node must be \"*\"").isEqualTo("*");
			// Default of Limit is "not set", but cardinality is 1 --> hard to test
		}
		finally {
			session.deleteNode(uri);
			session.commit();
		}
	}

}
