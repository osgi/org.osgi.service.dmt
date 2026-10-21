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
 * Jan 26, 2005  Andre Assad
 * CR 1          Implement MEG TCK
 * ============  ==============================================================
 * Feb 15, 2005  Alexandre Santos
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ===========  ============================================================== 
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.Acl;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.service.dmt.security.DmtPrincipalPermission;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgi.test.cases.dmt.tc2.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>getNodeSize</code> method of DmtSession, 
 * according to MEG specification
 */
public class GetNodeSize implements TestInterface {
	private DmtTestControl tbc;

	public GetNodeSize(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
        if (DmtConstants.SUPPORTS_NODE_SIZE) {        
    		testGetNodeSize001();
    		testGetNodeSize002();
    		testGetNodeSize003();
    		testGetNodeSize004();
    		testGetNodeSize005();
    		testGetNodeSize006();
        } else {
            testGetNodeSizeFeatureNotSupported001();
        }
	}
    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node 
	 * 
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize001() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize001");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_SHARED);
			session.getNodeSize(TestExecPluginActivator.INEXISTENT_NODE);
			DmtTestControl.failException("", DmtException.class);

		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException's code is NODE_NOT_FOUND").isEqualTo(DmtException.NODE_NOT_FOUND);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts that getNodeSize is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize002() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize002");

            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.ROOT, DmtConstants.PRINCIPAL, Acl.GET );

			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class
					.getName(), DmtConstants.PRINCIPAL, "*"));
			session = tbc.getDmtAdmin().getSession(
					DmtConstants.PRINCIPAL, TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.getNodeSize(TestExecPluginActivator.LEAF_NODE);

			System.out.println("getNodeSize correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.ROOT);
            
		}

	}

	/**
	 * This method asserts that getNodeSize is executed when the right Acl is set (Local)
	 * 
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize003() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize003");
			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			tbc.setPermissions(new PermissionInfo(
					DmtPermission.class.getName(), DmtConstants.ALL_NODES,
					DmtPermission.GET));

			session.getNodeSize(TestExecPluginActivator.LEAF_NODE);

			System.out.println("getNodeSize correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);	
            
		}
	}

	/**
	 * This method asserts that DmtException.COMMAND_NOT_ALLOWED is thrown
	 * if the specified node is not a leaf node
	 *  
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize004() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize004");
			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.getNodeSize(TestExecPluginActivator.INTERIOR_NODE);

			DmtTestControl.failException("#", DmtException.class);

		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is COMMAND_NOT_ALLOWED").isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session, null);			
		}
	}
	
	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize005() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize005");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeSize(TestExecPluginActivator.LEAF_RELATIVE);

			System.out.println("A relative URI can be used with getNodeSize.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with

	 * 
	 * @spec DmtSession.getNodeSize(String)
	 */
	private void testGetNodeSize006() {
		DmtSession session = null;
		try {
			System.out.println("#testGetNodeSize006");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeSize("");

			System.out.println("Asserts that an empty string as relative URI means the root " +
					"URI the session was opened with");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
    /**
     * Asserts that if the DmtAdmin service implementation does not support this method,
     * DmtException.FEATURE_NOT_SUPPORTED is thrown 
     * 
     * @spec DmtSession.getNodeSize(String)
     */
    private void testGetNodeSizeFeatureNotSupported001() {
        DmtSession session = null;
        try {
            System.out.println("#testGetNodeSizeFeatureNotSupported001");

            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_SHARED);
            session.getNodeSize(TestExecPluginActivator.INTERIOR_NODE);
            DmtTestControl.failException("", DmtException.class);

        } catch (DmtException e) {
            assertThat(e.getCode()).as("Asserting that DmtException's code is FEATURE_NOT_SUPPORTED").isEqualTo(DmtException.FEATURE_NOT_SUPPORTED);
        } catch (Exception e) {
        	tbc.failExpectedOtherException(DmtException.class, e);
        } finally {
            tbc.closeSession(session);
        }
    }

}
