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
 * CR 1          Implement MEG TCK
 * ============  ==============================================================
 * Feb 14, 2005  Alexandre Santos
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ===========  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.Acl;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtIllegalStateException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.service.dmt.security.DmtPrincipalPermission;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgi.test.cases.dmt.tc2.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ReadOnly.TestReadOnlyPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>execute<code> method of DmtSession, 
 * according to MEG specification.
 * 
 */
public class Execute implements TestInterface {
	private DmtTestControl tbc;
	private final String DATA = "data";
	private final String CORRELATOR = "correlator";
	
	public Execute(DmtTestControl tbc) {
		this.tbc = tbc;

	}

	@Override
	public void run() {
        prepare();
		testExecute001();
		testExecute002();
		testExecute003();
		testExecute004();
		testExecute005();
		testExecute006();
		testExecute007();
		testExecute008();
		testExecute009();
		testExecute010();
		testExecute011();
		testExecute012();
		testExecute013();
		testExecute014();
		testExecute015();
		testExecute016();
		
	}
    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node
	 *  
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute001() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute001");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestExecPluginActivator.INEXISTENT_NODE, DATA);

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
	 * This method asserts that DmtException.COMMAND_FAILED is thrown 
	 * if no DmtExecPlugin is associated with the node
	 * 
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute002() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute002");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestReadOnlyPluginActivator.INTERIOR_NODE, DATA);
			
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException's code is COMMAND_FAILED").isEqualTo(DmtException.COMMAND_FAILED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	
	/**
	 * This method asserts that execute is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute003() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute003");
            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.INTERIOR_NODE, DmtConstants.PRINCIPAL, Acl.EXEC | Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestExecPluginActivator.INTERIOR_NODE, DATA);
			
			System.out.println("execute was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, TestExecPluginActivator.INTERIOR_NODE);
            
            
		}

	}
	/**
	 * This method asserts that execute is executed when the right DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute004() {
		DmtSession session = null;	
		try {
			System.out.println("#testExecute004");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			
			tbc.setPermissions(new PermissionInfo(DmtPermission.class
					.getName(), DmtConstants.ALL_NODES, DmtPermission.EXEC));
			
			session.execute(TestExecPluginActivator.INTERIOR_NODE, DATA);
			
			System.out.println("execute was successfully executed");
		} catch (SecurityException e) {
			System.out.println("The Exception was SecurityException");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
			
		}

	}
	

	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute005() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute005");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute(TestExecPluginActivator.INTERIOR_NODE_NAME, DATA);

			System.out.println("A relative URI can be used with execute.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}	
	
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute006() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute006");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestExecPluginActivator.INEXISTENT_NODE, CORRELATOR,null);

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
	 * This method asserts that DmtException.COMMAND_FAILED is thrown 
	 * if no DmtExecPlugin is associated with the node
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute007() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute007");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestReadOnlyPluginActivator.INTERIOR_NODE,CORRELATOR, null);
			
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException's code is COMMAND_FAILED").isEqualTo(DmtException.COMMAND_FAILED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	
	/**
	 * This method asserts that execute is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute008() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute008");


            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.INTERIOR_NODE, DmtConstants.PRINCIPAL, Acl.EXEC | Acl.GET );

			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.execute(TestExecPluginActivator.INTERIOR_NODE,CORRELATOR, null);
			
			System.out.println("execute was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.INTERIOR_NODE);
            
		}

	}
	/**
	 * This method asserts that execute is executed when the right DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute009() {
		DmtSession session = null;	
		try {
			System.out.println("#testExecute009");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			
			tbc.setPermissions(new PermissionInfo(DmtPermission.class
					.getName(), DmtConstants.ALL_NODES, DmtPermission.EXEC));
			
			session.execute(TestExecPluginActivator.INTERIOR_NODE,CORRELATOR, null);
			
			System.out.println("execute was successfully executed");
		} catch (SecurityException e) {
			System.out.println("The Exception was SecurityException");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.closeSession(session);
			
		}

	}
	

	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute010() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute010");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute(TestExecPluginActivator.INTERIOR_NODE_NAME,CORRELATOR, null);

			System.out.println("A relative URI can be used with execute.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}	
	
	/**
	 * This method asserts that null can be passed on data parameter
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute011() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute011");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute(TestExecPluginActivator.INTERIOR_NODE_NAME,null);

			System.out.println("Null can be passed on data parameter");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that null can be passed on correlator parameter
	 * 
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute012() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute012");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute(TestExecPluginActivator.INTERIOR_NODE_NAME,null, DATA);

			System.out.println("Null can be passed on correlator parameter");
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
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute013() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute013");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute("", DATA);

			System.out.println("Asserts that an empty string as relative URI means the root " +
					"URI the session was opened with");
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
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute014() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute014");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.execute("", CORRELATOR,null);

			System.out.println("Asserts that an empty string as relative URI means the root " +
					"URI the session was opened with");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that DmtIllegalStateException is thrown
	 * if the session was opened using the LOCK_TYPE_SHARED lock type
	 *  
	 * @spec DmtSession.execute(String,String)
	 */
	private void testExecute015() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute015");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_SHARED);

			session.execute(TestExecPluginActivator.INTERIOR_NODE, DATA);

			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtIllegalStateException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that DmtIllegalStateException is thrown
	 * if the session was opened using the LOCK_TYPE_SHARED lock type
	 *  
	 * @spec DmtSession.execute(String,String,String)
	 */
	private void testExecute016() {
		DmtSession session = null;
		try {
			System.out.println("#testExecute016");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_SHARED);

			session.execute(TestExecPluginActivator.INTERIOR_NODE_NAME,CORRELATOR, DATA);

			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtIllegalStateException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
}
