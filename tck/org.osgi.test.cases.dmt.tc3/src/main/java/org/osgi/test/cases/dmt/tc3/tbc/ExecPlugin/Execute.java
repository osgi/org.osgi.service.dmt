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
 * Feb 25, 2005  Luiz Felipe Guimaraes
 * 11            Implemente MEG TCK
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.ExecPlugin;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates the implementation of <code>execute<code> method, 
 * according to MEG specification
 */
public class Execute {

	private DmtTestControl tbc;

	public Execute(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
	    testExecute001();
		testExecute002();
		testExecute003();
		testExecute004();
		
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of execute method 
	 * to the correct plugin.
	 * 
	 * @spec ExecPlugin.execute(DmtSession,String[],String,String)
	 */
	public void testExecute001() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute001");
			String data = "data";
			String correlator = "correlator";
			session = tbc.getDmtAdmin().getSession(TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.execute(TestExecPluginActivator.ROOT, correlator, data);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestExecPlugin.EXECUTE+" to the correct plugin").isEqualTo(TestExecPlugin.EXECUTE);
			assertThat(DmtConstants.PARAMETER_2).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(correlator);
			assertThat(DmtConstants.PARAMETER_3).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(data);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin
	 * 
	 * @spec ExecPlugin.execute(DmtSession,String[],String,String)
	 */
	public void testExecute002() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute002");
			session = tbc.getDmtAdmin().getSession(TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.execute(TestExecPluginActivator.INTERIOR_NODE_EXCEPTION,"test", "test");
			
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestExecPluginActivator.INTERIOR_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.CONCURRENT_ACCESS);
			assertThat(e
					.getMessage().indexOf(TestExecPlugin.EXECUTE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();	
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
	
	/**
	 * Asserts that DmtAdmin correctly forwards the call of execute method 
	 * to the correct plugin.
	 * 
	 * @spec ExecPlugin.execute(DmtSession,String[],String,String)
	 */
	public void testExecute003() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute003");
			String data = "data";

			session = tbc.getDmtAdmin().getSession(TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.execute(TestExecPluginActivator.ROOT, data);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestExecPlugin.EXECUTE+" to the correct plugin").isEqualTo(TestExecPlugin.EXECUTE);
			assertThat(DmtConstants.PARAMETER_3).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(data);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin
	 * 
	 * @spec ExecPlugin.execute(DmtSession,String[],String,String)
	 */
	public void testExecute004() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute004");
			session = tbc.getDmtAdmin().getSession(TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.execute(TestExecPluginActivator.INTERIOR_NODE_EXCEPTION,"test");
			
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestExecPluginActivator.INTERIOR_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.CONCURRENT_ACCESS);
			assertThat(e
					.getMessage().indexOf(TestExecPlugin.EXECUTE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();	
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
            tbc.cleanUp(session,true);
		}
	}
	
}
