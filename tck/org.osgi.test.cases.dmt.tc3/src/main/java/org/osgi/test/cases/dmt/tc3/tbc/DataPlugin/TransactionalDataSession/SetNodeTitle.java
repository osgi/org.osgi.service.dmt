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
 * Mar 02, 2005  Luiz Felipe Guimaraes
 * 11            Implement DMT Use Cases 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates the implementation of <code>setNodeTitle</code> method, 
 * according to MEG specification
 */
public class SetNodeTitle {

	private DmtTestControl tbc;

	public SetNodeTitle(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testSetNodeTitle001();
		testSetNodeTitle002();
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of setNodeTitle 
	 * to the correct plugin.
	 * 
	 * @spec ReadWriteDataSession.setNodeTitle(String[],String)
	 */
	public void testSetNodeTitle001() {
		DmtSession session = null;
		try {
			System.out.println("#testSetNodeTitle001");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			String title = "title";
			session.setNodeTitle(TestDataPluginActivator.INTERIOR_NODE,title);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.SETNODETITLE+" to the correct plugin").isEqualTo(TestDataPlugin.SETNODETITLE);
			assertThat(DmtConstants.PARAMETER_2).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(title);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
			DmtConstants.TEMPORARY = "";
		}
		
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin
	 * 
	 * @spec ReadWriteDataSession.setNodeTitle(String[],String)
	 */
	public void testSetNodeTitle002() {
		DmtSession session = null;
		try {
			System.out.println("#testSetNodeTitle002");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.setNodeTitle(TestDataPluginActivator.INTERIOR_NODE_EXCEPTION,"title");
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.INTERIOR_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.CONCURRENT_ACCESS);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.SETNODETITLE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
}
