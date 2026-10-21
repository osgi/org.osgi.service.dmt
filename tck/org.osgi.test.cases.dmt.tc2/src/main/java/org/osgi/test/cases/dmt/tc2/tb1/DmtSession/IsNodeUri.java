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
 * Jan 31, 2005  Andre Assad
 * 1             Implement MEG TCK
 * ============  ==============================================================
 * Feb 16, 2005  Andre Assad
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgi.test.cases.dmt.tc2.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * This test case validates the implementation of <code>isNodeUri</code> method of DmtSession, 
 * according to MEG specification
 */
public class IsNodeUri implements TestInterface {
	private DmtTestControl tbc;

	public IsNodeUri(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
		testIsNodeUri001();
		testIsNodeUri002();
		testIsNodeUri003();
		testIsNodeUri004();
	}
    private void prepare() {
    	tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * Asserts that 'true' is returned if the URI really exists.
	 * 
	 * @spec DmtSession.isNodeUri(String)
	 */
	private void testIsNodeUri001() {
		DmtSession session = null;
		try {
			System.out.println("#testIsNodeUri001");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			assertThat(session
					.isNodeUri(DmtConstants.OSGi_LOG)).as("Assert isNodeUri").isTrue();
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * Asserts that 'false' is returned if the URI really exists.
	 * 
	 * @spec DmtSession.isNodeUri(String)
	 */
	private void testIsNodeUri002() {
		DmtSession session = null;
		try {
			System.out.println("#testIsNodeUri002");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_SHARED);
			assertThat(!session
					.isNodeUri(TestExecPluginActivator.INEXISTENT_NODE)).as("Assert isNodeUri").isTrue();
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts that relative URI works as described. Is also tests if 
	 * only DmtPermission.GET is needed.
	 * 
	 * @spec DmtSession.isNodeUri(String)
	 */
	private void testIsNodeUri003() {
		DmtSession session = null;
		try {
			System.out.println("#testIsNodeUri003");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);
			
			tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtPermission.GET));
			
			session.isNodeUri(TestExecPluginActivator.INTERIOR_NODE_NAME);

			System.out.println("A relative URI can be used with isNodeUri.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
			prepare();
		}
	}
	
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with
	 * 
	 * @spec DmtSession.isNodeUri(String)
	 */
	private void testIsNodeUri004() {
		DmtSession session = null;
		try {
			System.out.println("#testIsNodeUri004");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_ATOMIC);
			
			assertThat(session.isNodeUri("")).as("Asserts that an empty string as relative URI means the root " +
					"URI the session was opened with").isTrue();
			
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
}
