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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.MetaNode;
import org.osgi.service.log.LogService;
/**
 * This test case checks for the correct structure and metadata of the Log tree.
 * It asserts that all mandatory nodes are there and compares the reported metadata and node types with the specified values.
 * 
 * @author Steffen Druesedow (Deutsche Telekom Laboratories)
 */
@SuppressWarnings("deprecation")
public class LogStructureTestCase extends RMTTestBase {

	private LogService log;
	
	@BeforeEach
	public void setUp() throws Exception {
		super.setUp();
		log = getService(LogService.class);
	}

	/**
	 * asserts that the Log node has correct children
	 * @throws DmtException
	 */
	@Test
	public void testLogStructure() throws DmtException {
		String[] children;
		HashSet<String> mandatory = new HashSet<String>();
		HashSet<String> unknown = new HashSet<String>();

		session = dmtAdmin.getSession(LOG_ROOT, DmtSession.LOCK_TYPE_SHARED);

		// 1st descendants
		children = session.getChildNodeNames(LOG_ROOT);
		mandatory.add(LOG_ENTRIES);
		for (String child : children) {
			if ( ! mandatory.contains(child))
				unknown.add(child);
			mandatory.remove(child);
		}
		assertThat(mandatory).as("There are children missing in the Log node:" + mandatory).isEmpty();
		assertThat(unknown).as("There are unknown children in the Log node:" + unknown).isEmpty();
		
	}

	/**
	 * asserts that the LogEntry has the right structure
	 * @throws Exception 
	 */
	@Test
	public void testLogEntryStructure() throws Exception {
		String[] children;
		HashSet<String> expected = new HashSet<String>();
		HashSet<String> optional = new HashSet<String>(); 
		
		// ensure that there is at least one log entry
		assertThat(log).as("Null LogService.").isNotNull();
		log.log(LogService.LOG_INFO, "This is a testlog!");

		session = dmtAdmin.getSession(LOG_ROOT, DmtSession.LOCK_TYPE_EXCLUSIVE);
		String[] entries = session.getChildNodeNames(LOG_ROOT + "/" + LOG_ENTRIES );
		assertThat(entries).as("No LogEntries found.").isNotNull();
		assertThat(entries.length == 0).as("No LogEntries found.").isFalse();
		
		optional = new HashSet<String>();
		optional.add(EXCEPTION);

		// children of: Log.xxx.LogEntries
		for (String entry : entries) {
			List<String> undefined = new ArrayList<String>();
			expected = new HashSet<String>();
			expected.add(BUNDLE);
			expected.add(TIME);
			expected.add(LEVEL);
			expected.add(MESSAGE);

			children = session.getChildNodeNames(LOG_ROOT + "/" + LOG_ENTRIES + "/" + entry);
			assertThat(children).as("Log entry children must not be null for: " + entry).isNotNull();
			assertThat(children.length > 0).as("Log entry children must exist for: " + entry).isTrue();
			for (String child : children) {
				if ( ! expected.contains(child) && ! optional.contains(child) )
					undefined.add(child);
				expected.remove(child);
			}

			assertThat(expected).as("Mandatory nodes are missing in Framework.Bundle." + entry).isEmpty();
			
			assertThat(undefined).as("There are un-specified nodes in Framework.Bundle." + entry + ": " + undefined).isEmpty();
		}
		
	}
	
	
	/**
	 * asserts that the Log tree provides correct metadata and node types
	 * @throws Exception
	 */
	@Test
	public void testMetaDataAndType() throws Exception {
		try {
			session = dmtAdmin.getSession(LOG_ROOT, DmtSession.LOCK_TYPE_SHARED);
			assertThat(session).as("Null DMT session.").isNotNull();
			assertMetaData( LOG_ROOT, false, "_G__", "0,1", MetaNode.PERMANENT, DmtData.FORMAT_NODE);
			assertMetaData( LOG_ROOT + "/" + LOG_ENTRIES, false, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_NODE);
			assertThat(session.getNodeType(LOG_ROOT + "/" + LOG_ENTRIES)).as("The nodeType of the Log.LogEntries node must be " + DmtConstants.DDF_LIST).isEqualTo(DmtConstants.DDF_LIST);
	
			String[] logEntries = session.getChildNodeNames(LOG_ROOT + "/" + LOG_ENTRIES);
			for (String entry : logEntries) {
				String uri = LOG_ROOT + "/" + LOG_ENTRIES + "/" + entry;
				assertMetaData( uri, false, "_G__", "0..*", MetaNode.DYNAMIC, DmtData.FORMAT_NODE);
				
				uri+= "/";
				assertMetaData( uri + BUNDLE, 	  	true, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_STRING);
				assertMetaData( uri + TIME, 		true, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_DATE_TIME);
				assertMetaData( uri + LEVEL, 		true, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_INTEGER);
				assertMetaData( uri + MESSAGE,	  	true, "_G__", "1", MetaNode.AUTOMATIC, DmtData.FORMAT_STRING);
				assertMetaData( uri + EXCEPTION,  	true, "_G__", "0,1", MetaNode.AUTOMATIC, DmtData.FORMAT_STRING);
			}
		} catch (DmtException de) {
			de.printStackTrace();
			fail("unexpeced DmtException: " + de.getMessage());
		}
	}

}
