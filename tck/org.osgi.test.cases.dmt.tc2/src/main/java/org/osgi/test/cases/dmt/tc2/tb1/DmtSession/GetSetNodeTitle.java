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
 * Feb 15, 2005  Andre Assad
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ============  ==============================================================
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
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.NonAtomic.TestNonAtomicPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ReadOnly.TestReadOnlyPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>getNodeTitle, setNodeTitle</code> method, 
 * according to MEG specification
 */
public class GetSetNodeTitle implements TestInterface {
	private DmtTestControl tbc;

	public GetSetNodeTitle(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
        if (DmtConstants.SUPPORTS_NODE_TITLE) {
    		testGetSetNodeTitle001();
    		testGetSetNodeTitle002();
    		testGetSetNodeTitle003();
    		testGetSetNodeTitle004();
    		testGetSetNodeTitle005();
    		testGetSetNodeTitle006();
    		testGetSetNodeTitle007();
    		testGetSetNodeTitle008();
    		testGetSetNodeTitle009();
    		testGetSetNodeTitle010();
    		testGetSetNodeTitle011();
    		testGetSetNodeTitle012();
    		testGetSetNodeTitle013();
    		testGetSetNodeTitle014();
    		testGetSetNodeTitle015();
        } else {
            testGetSetNodeTitleFeatureNotSupported001();
            testGetSetNodeTitleFeatureNotSupported002();
        }
	}

    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node 
	 * 
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle001() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle001");
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeTitle(TestExecPluginActivator.INEXISTENT_NODE, DmtConstants.TITLE);
            DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_NOT_FOUND").isEqualTo(DmtException.NODE_NOT_FOUND);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node 
	 * 
	 * @spec DmtSession.getNodeTitle(String)
	 */
	private void testGetSetNodeTitle002() {	
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle002");
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);			
			session.getNodeTitle(TestExecPluginActivator.INEXISTENT_NODE);
            DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_NOT_FOUND").isEqualTo(DmtException.NODE_NOT_FOUND);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	
	/**
	 * This method asserts that getNodeTitle is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.getNodeTitle(String)
	 */
	private void testGetSetNodeTitle003() {

		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle003");
            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.INTERIOR_NODE, DmtConstants.PRINCIPAL, Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,TestExecPluginActivator.INTERIOR_NODE,DmtSession.LOCK_TYPE_ATOMIC);
			session.getNodeTitle(TestExecPluginActivator.INTERIOR_NODE);
			System.out.println("getNodeTitle correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		}  finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.INTERIOR_NODE);	
            
		}
	}
	/**
	 * This method asserts that setNodeTitle is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle004() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle004");
            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.INTERIOR_NODE, DmtConstants.PRINCIPAL, Acl.REPLACE | Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,TestExecPluginActivator.INTERIOR_NODE,DmtSession.LOCK_TYPE_ATOMIC);
			session.setNodeTitle(TestExecPluginActivator.INTERIOR_NODE,DmtConstants.TITLE);
			System.out.println("setNodeTitle correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.INTERIOR_NODE);
            
		}

	}

	
	/**
	 * This method asserts that setNodeTitle is executed when the right DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle005() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle005");
            tbc.setPermissions(new PermissionInfo[] {
                    new PermissionInfo(DmtPermission.class.getName(), DmtConstants.OSGi_ROOT, DmtPermission.GET),
                    new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.REPLACE)});
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeTitle(TestExecPluginActivator.INTERIOR_NODE, DmtConstants.TITLE);
			System.out.println("setNodeTitle correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
			
		}
	}
	/**
	 * This method asserts that getNodeTitle is executed when the right DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.getNodeTitle(String)
	 */
	private void testGetSetNodeTitle006() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle006");
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,DmtSession.LOCK_TYPE_EXCLUSIVE);
			tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.GET));
			session.getNodeTitle(TestExecPluginActivator.INTERIOR_NODE);
			System.out.println("getNodeTitle correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
			
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
		}
	}
	
	
	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.getNodeTitle(String)
	 * 
	 */
	private void testGetSetNodeTitle007() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle007");
			
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeTitle(TestExecPluginActivator.LEAF_RELATIVE);

			System.out.println("A relative URI can be used with getNodeTitle.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}	
	
	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle008() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle008");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.setNodeTitle(TestExecPluginActivator.LEAF_RELATIVE, "temp");

			System.out.println("A relative URI can be used with setNodeTitle.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}	
	

	
	/**
	 * This method asserts if DmtIllegalStateException is thrown if this method is called 
	 * when the session is LOCK_TYPE_SHARED
	 * 
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle009() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle009");
			session = tbc.getDmtAdmin().getSession(
				TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_SHARED);
			session.setNodeTitle(TestExecPluginActivator.LEAF_RELATIVE, "temp");
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
	 * This method asserts that DmtException.TRANSACTION_ERROR is thrown 
	 * if the session is atomic and the plugin is read-only
	 * 
	 * @spec DmtSession.setNodeTitle(String,DmtData)
	 */
	private void testGetSetNodeTitle010() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle010");
			session = tbc.getDmtAdmin().getSession(".",
			    DmtSession.LOCK_TYPE_ATOMIC);
			
			session.setNodeTitle(TestReadOnlyPluginActivator.LEAF_NODE,DmtConstants.TITLE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is TRANSACTION_ERROR").isEqualTo(DmtException.TRANSACTION_ERROR);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that DmtException.TRANSACTION_ERROR is thrown 
	 * if the session is atomic and the plugin does not support non-atomic writing
	 * 
	 * @spec DmtSession.setNodeTitle(String,DmtData)
	 */
	private void testGetSetNodeTitle011() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle011");
			session = tbc.getDmtAdmin().getSession(".",
			    DmtSession.LOCK_TYPE_ATOMIC);
			
			session.setNodeTitle(TestNonAtomicPluginActivator.LEAF_NODE,DmtConstants.TITLE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is TRANSACTION_ERROR").isEqualTo(DmtException.TRANSACTION_ERROR);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that DmtException.COMMAND_NOT_ALLOWED is thrown 
	 * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin 
	 * does not support non-atomic writing
	 * 
	 * @spec DmtSession.setNodeTitle(String,DmtData)
	 */
	private void testGetSetNodeTitle012() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle012");
			session = tbc.getDmtAdmin().getSession(".",
			    DmtSession.LOCK_TYPE_EXCLUSIVE);
			
			session.setNodeTitle(TestNonAtomicPluginActivator.LEAF_NODE,DmtConstants.TITLE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is COMMAND_NOT_ALLOWED").isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that DmtException.COMMAND_NOT_ALLOWED is thrown 
	 * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin 
	 * is read-only 
	 * 
	 * @spec DmtSession.setNodeTitle(String,DmtData)
	 */
	private void testGetSetNodeTitle013() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle013");
			session = tbc.getDmtAdmin().getSession(".",
			    DmtSession.LOCK_TYPE_EXCLUSIVE);
			
			session.setNodeTitle(TestReadOnlyPluginActivator.LEAF_NODE,DmtConstants.TITLE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is COMMAND_NOT_ALLOWED").isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with
	 * 
	 * @spec DmtSession.getNodeTitle(String)
	 * 
	 */
	private void testGetSetNodeTitle014() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle014");
			
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeTitle("");

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
	 * @spec DmtSession.setNodeTitle(String,String)
	 */
	private void testGetSetNodeTitle015() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeTitle015");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.setNodeTitle("", "temp");

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
     * @spec DmtSession.getNodeTitle(String)
     */
    private void testGetSetNodeTitleFeatureNotSupported001() { 
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeTitleFeatureNotSupported001");
            session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
                    DmtSession.LOCK_TYPE_EXCLUSIVE);            
            session.getNodeTitle(TestExecPluginActivator.INTERIOR_NODE);
            DmtTestControl.failException("", DmtException.class);
        } catch (DmtException e) {
            assertThat(e.getCode()).as("Asserting that DmtException code is FEATURE_NOT_SUPPORTED").isEqualTo(DmtException.FEATURE_NOT_SUPPORTED);
        } catch (Exception e) {
        	tbc.failExpectedOtherException(DmtException.class, e);
        } finally {
            tbc.closeSession(session);
        }
    }
    
    /**
     * Asserts that if the DmtAdmin service implementation does not support this method,
     * DmtException.FEATURE_NOT_SUPPORTED is thrown
     * 
     * @spec DmtSession.setNodeTitle(String,String)
     */
    private void testGetSetNodeTitleFeatureNotSupported002() { 
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeTitleFeatureNotSupported002");
            session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
                    DmtSession.LOCK_TYPE_EXCLUSIVE);            
            session.setNodeTitle(TestExecPluginActivator.INTERIOR_NODE,DmtConstants.TITLE);
            DmtTestControl.failException("", DmtException.class);
        } catch (DmtException e) {
            assertThat(e.getCode()).as("Asserting that DmtException code is FEATURE_NOT_SUPPORTED").isEqualTo(DmtException.FEATURE_NOT_SUPPORTED);
        } catch (Exception e) {
        	tbc.failExpectedOtherException(DmtException.class, e);
        } finally {
            tbc.closeSession(session);
        }
    }
}
