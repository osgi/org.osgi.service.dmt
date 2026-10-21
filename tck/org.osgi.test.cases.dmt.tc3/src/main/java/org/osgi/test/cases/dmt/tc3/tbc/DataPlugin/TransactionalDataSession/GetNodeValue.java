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

/*
 * REVISION HISTORY:
 *
 * Date          Author(s)
 * CR            Headline
 * ============  ==============================================================
 * Mar 04, 2005  Andre Assad
 * 11		     Implement DMT Use Cases
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>getNodeValue</code> method, 
 * according to MEG specification
 */
public class GetNodeValue {
	private DmtTestControl tbc;

	public GetNodeValue(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
        testGetNodeValue001();
        testGetNodeValue002();
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of getNodeValue 
	 * to the correct plugin.
	 * 
	 * @spec ReadableDataSession.getNodeValue(String[])
	 */
	public void testGetNodeValue001() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeValue001");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			DmtData nodeValue = session.getNodeValue(TestDataPluginActivator.LEAF_NODE);
			assertThat(nodeValue.equals(TestDataPlugin.GETNODEVALUE_VALUE)).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.GETNODEVALUE
					+" to the correct plugin").isTrue();

		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin
	 * 
	 * @spec ReadableDataSession.getNodeValue(String[])
	 */
	public void testGetNodeValue002() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeValue002");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.getNodeValue(TestDataPluginActivator.LEAF_NODE_EXCEPTION);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.LEAF_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.GETNODEVALUE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
    
}
