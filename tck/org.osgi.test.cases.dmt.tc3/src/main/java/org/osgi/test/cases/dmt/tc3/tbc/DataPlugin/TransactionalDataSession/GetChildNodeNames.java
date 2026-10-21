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

/* REVISION HISTORY:
 *
 * Date         Author(s)
 * CR           Headline
 * ===========  ==============================================================
 * 26/01/2005   Andre Assad
 * 1            Implement TCK
 * ===========  ==============================================================
 * 15/02/2005   Leonardo Barros
 * 1            Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-002)
 * ===========  ==============================================================
 * 01/03/2005   Andre Assad
 * 11           Implement TCK Use Cases
 * ===========  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * 
 * This test case validates the implementation of <code>getChildNodeNames<code> method, 
 * according to MEG specification
 */
public class GetChildNodeNames {

	private DmtTestControl tbc;

	public GetChildNodeNames(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testGetChildNodeNames001();
		testGetChildNodeNames002();

	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of getChildNodeNames 
	 * to the correct plugin.
	 * 
	 * @spec ReadableDataSession.getChildNodeNames(String[])
	 */
	public void testGetChildNodeNames001() {
		DmtSession session = null;
		try {
			System.out.println("#testGetChildNodeNames001");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			String[] child = session.getChildNodeNames(TestDataPluginActivator.ROOT);
			assertThat(child[0]).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.GETCHILDNODENAMES+" to the correct plugin").isEqualTo(TestDataPlugin.GETCHILDNODENAMES);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin
	 * 
	 * @spec ReadableDataSession.getChildNodeNames(String[])
	 */
	public void testGetChildNodeNames002() {
		DmtSession session = null;
		try {
			System.out.println("#testGetChildNodeNames002");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			
			session.getChildNodeNames(TestDataPluginActivator.INTERIOR_NODE_EXCEPTION);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.INTERIOR_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.DATA_STORE_FAILURE);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.GETCHILDNODENAMES)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
}
