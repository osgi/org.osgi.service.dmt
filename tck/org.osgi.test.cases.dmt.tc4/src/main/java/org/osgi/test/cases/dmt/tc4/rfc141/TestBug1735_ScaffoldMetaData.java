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
import static org.assertj.core.api.Assertions.within;

import java.util.Dictionary;
import java.util.Hashtable;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.MetaNode;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.ArrayAssert;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.GenericDataPlugin;
import org.osgi.test.cases.dmt.tc4.rfc141.plugins.Node;

public class TestBug1735_ScaffoldMetaData extends ScaffoldNodeHelper {
	
	
	/**
	 * 
	 * Checks metadata of a Scaffold node as described in RFC141
	 * (RFC141, section 6.3.1)
	 */
	@Test
	public void testScaffoldMetaData() throws Exception {

		preparePlugin();
		
		String uri = "./A";
		session = dmtAdmin.getSession( uri, DmtSession.LOCK_TYPE_SHARED);
		MetaNode metaNode = session.getMetaNode(uri);
		
		assertThat(metaNode).as("DmtAdmin must provide MetaData for a scaffold node!").isNotNull();
		
		checkMetaNode(metaNode);

		assertThat(session.getNodeType(uri)).as("The type of a scaffold node must be: " + DmtConstants.DDF_SCAFFOLD).isEqualTo(DmtConstants.DDF_SCAFFOLD);
	}

	/**
	 * Checks for correct node type of scaffold nodes.
	 * (RFC141, section 6.3.2)
	 * 
	 * @throws Exception
	 */
	@Test
	public void testScaffoldNodeType() throws Exception {
		preparePlugin();

		System.out.println("testing node type of scaffold nodes ...");
		String uri = "./A";
		session = dmtAdmin.getSession( uri, DmtSession.LOCK_TYPE_SHARED);
		assertThat(DmtConstants.DDF_SCAFFOLD).as("Scaffold nodes must have node type: '" + DmtConstants.DDF_SCAFFOLD + "'!").isEqualTo(session.getNodeType(uri));
	}

	
	//	- can():  CMD_GET
	//	- isLeaf(): false
	//	- getScope(): permanent (can't be changed via sessions)
	//	- getDescription(): null
	//	- getMaxOccurrence(): 1
	//	- isZeroOccurrenceAllowed(): true
	//	- getDefault(), getValidNames(), getValidValues(): null
	//	- getMax(): Double.MAX_VALUE
	//	- getMin(): Double.MIN_VALUE
	//	- getRawFormatNames(): null
	//	- getMimeTypes(): null
	//	- isValidName(): true
	//	- isValidValue(): false
	private void checkMetaNode( MetaNode metaNode ) {
		assertThat(metaNode.isLeaf()).as("Scaffold nodes must be interior nodes!").isFalse();
		
		assertThat(metaNode.can(MetaNode.CMD_GET)).as("Scaffold nodes must support the GET operation!").isTrue();
		assertThat(metaNode.can(MetaNode.CMD_ADD)).as("Scaffold nodes must not support the ADD operation!").isFalse();
		assertThat(metaNode.can(MetaNode.CMD_DELETE)).as("Scaffold nodes must not support the DELETE operation!").isFalse();
		assertThat(metaNode.can(MetaNode.CMD_EXECUTE)).as("Scaffold nodes must not support the EXECUTE operation!").isFalse();
		assertThat(metaNode.can(MetaNode.CMD_REPLACE)).as("Scaffold nodes must not support the REPLACE operation!").isFalse();

		assertThat(metaNode.getScope()).as("Scaffold nodes must have scope PERMANENT!").isEqualTo(MetaNode.PERMANENT);

		assertThat(metaNode.getDescription()).as("The MetaData of scaffold nodes must not provide a description!").isNull();

		assertThat(metaNode.getMaxOccurrence()).as("The MetaData of scaffold nodes must have max occurence of 1!").isOne();

		assertThat(metaNode.isZeroOccurrenceAllowed()).as("The MetaData of scaffold nodes must allow zero occurrence!").isTrue();

		assertThat(metaNode.getDefault()).as("The MetaData of scaffold nodes must not provide a default!").isNull();
		assertThat(metaNode.getValidNames()).as("The MetaData of scaffold nodes must not provide values for 'getValidNames()'!").isNull();
		assertThat(metaNode.getValidValues()).as("The MetaData of scaffold nodes must not provide values for 'getValidValues()'!").isNull();

		assertThat(metaNode.getMax()).as("The MetaData of scaffold nodes must allow a max value of " + Double.MAX_VALUE).isCloseTo(Double.MAX_VALUE, within(0.0));
		assertThat(metaNode.getMin()).as("The MetaData of scaffold nodes must allow a min value of " + Double.MIN_VALUE).isCloseTo(Double.MIN_VALUE, within(0.0));

		assertThat(metaNode.getRawFormatNames()).as("The MetaData of scaffold nodes must not provide raw format names!").isNull();
		ArrayAssert.assertEquivalenceArrays("The MetaData of scaffold nodes must not provide mime types!", 
				metaNode.getMimeTypes(), new String[]{ DmtConstants.DDF_SCAFFOLD });

		assertThat(metaNode.isValidName("any name")).as("The method 'isValidName()' of a scaffolds MetaData must return 'true' for all params!").isTrue();
		assertThat(metaNode.isValidValue(new DmtData("value"))).as("The method 'isValidValue()' of a scaffolds MetaData must return 'false' for all params!").isFalse();

		assertThat(metaNode.getFormat()).as("Scaffold nodes must have the format DmtData.FORMAT_NODE: " + DmtData.FORMAT_NODE).isEqualTo(DmtData.FORMAT_NODE);
		
	}
	
	private void preparePlugin() throws Exception {
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
}
