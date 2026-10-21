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
 * 25/01/2005   André Assad
 * 1            Implement TCK
 * ===========  ==============================================================
 * 14/02/2005   Leonardo Barros
 * 1            Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-002)
 * ===========  ==============================================================
 * 28/03/2005   Luiz Felipe Guimaraes
 * 1            Updates 
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
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPlugin;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.NonAtomic.TestNonAtomicPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ReadOnly.TestReadOnlyPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test class validates the implementation of <code>createInteriorNode</code> method of DmtSession, 
 * according to MEG specification
 */
public class CreateInteriorNode implements TestInterface {

	private DmtTestControl tbc;

	public CreateInteriorNode(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
		testCreateInteriorNode001();
		testCreateInteriorNode002();
		testCreateInteriorNode003();
		testCreateInteriorNode004();
		testCreateInteriorNode005();
		testCreateInteriorNode006();
		testCreateInteriorNode007();
		testCreateInteriorNode008();
		testCreateInteriorNode009();
		testCreateInteriorNode010();
		testCreateInteriorNode011();
		testCreateInteriorNode012();
		testCreateInteriorNode013();
		testCreateInteriorNode014();
		testCreateInteriorNode015();
		testCreateInteriorNode016();
		testCreateInteriorNode017();
		testCreateInteriorNode018();
		testCreateInteriorNode019();
        testCreateInteriorNode020();
        testCreateInteriorNode021();
        testCreateInteriorNode022();
        testCreateInteriorNode023();
        testCreateInteriorNode024();
	}
    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_ALREADY_EXISTS is thrown if is tried to create an existing interior node
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode001() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode001");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.INTERIOR_NODE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts DmtException.getCode").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts that createInteriorNode is successfully executed when the correct ACL is assigned
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode002() {

		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode002");
			
            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.ROOT, DmtConstants.PRINCIPAL, Acl.ADD | Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL, TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE);
			System.out.println("createInteriorNode was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.ROOT);
            
		}
	}

	/**
	 * This method asserts that createInteriorNode is successfully executed 
	 * when the correct permission is assigned
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode003() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode003");
			tbc.setPermissions(new PermissionInfo[] {
                new PermissionInfo(DmtPermission.class.getName(), ".", DmtPermission.GET),
                new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.ADD)});
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE);
			System.out.println("createInteriorNode was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
		}
	}

    /**
     * Asserts that if the principal does not have Replace access rights on the 
     * parent of the new node then the session must automatically set the 
     * ACL of the new node so that the creating server has Add, Delete and 
     * Replace rights on the new node.
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode004() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode004");

            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.ROOT, DmtConstants.PRINCIPAL, Acl.ADD | Acl.GET );
            tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
            session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL, TestExecPluginActivator.ROOT,
                    DmtSession.LOCK_TYPE_ATOMIC);
            session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE);
            TestExecPlugin.setAllUriIsExistent(true);
            session.close();
            
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            session = tbc.getDmtAdmin().getSession(TestExecPluginActivator.ROOT,DmtSession.LOCK_TYPE_EXCLUSIVE);
            Acl acl = session.getNodeAcl(TestExecPluginActivator.INEXISTENT_NODE);
            assertThat(acl.isPermitted(DmtConstants.PRINCIPAL,Acl.ADD | Acl.DELETE | Acl.REPLACE) && 
                    !acl.isPermitted(DmtConstants.PRINCIPAL,Acl.GET | Acl.EXEC)).as("Asserts that if the principal does not have Replace access rights on the parent " +
                    "of the new node then the session must automatically set the ACL of the new node so that " +
                    "the creating server has Add, Delete and Replace rights on the new node. ").isTrue();
            
            
        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.ROOT);
            TestExecPlugin.setAllUriIsExistent(false);
            
        }
    }

	/**
	 * This method asserts that DmtException.NODE_ALREADY_EXISTS is thrown if is tried to create an existing interior node
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode005() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode005");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.INTERIOR_NODE,
					DmtConstants.MIMETYPE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts DmtException.getCode").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);			
		}
	}

	

	/**
	 * This method asserts that createInteriorNode is successfully executed when the correct ACL is assigned
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode006() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode006");

            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.ROOT, DmtConstants.PRINCIPAL, Acl.ADD | Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL, TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE,
					DmtConstants.MIMETYPE);
			System.out.println("createInteriorNode was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.ROOT);
            
		}
	}

	

	/**
	 * This method asserts that createInteriorNode is successfully executed when the correct permission is assigned
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode007() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode007");
			tbc.setPermissions(new PermissionInfo[] {
                    new PermissionInfo(DmtPermission.class.getName(), ".", DmtPermission.GET),
                    new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.ADD)});
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE,
					DmtConstants.MIMETYPE);
			System.out.println("createInteriorNode was successfully executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
		}
	}

	
	
	/**
	 * This method asserts that relative URI works as described in this method using the method with only one argument.
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode008() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode008");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);
			
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE_NAME);
			
			System.out.println("A relative URI can be used with CreateInteriorNode.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}		
	}	
	
	/**
	 * This method asserts that relative URI works as described in this method using the method with two arguments.
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode009() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode009");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);
			
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE_NAME, DmtConstants.MIMETYPE);
			
			System.out.println("A relative URI can be used with CreateInteriorNode.");
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
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode010() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode010");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_SHARED);
			session.createInteriorNode(TestExecPluginActivator.INEXISTENT_NODE);
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * Asserts that DmtException with COMMAND_NOT_ALLOWED code is thrown when the parent
	 * is not an interior node.
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode011() {
		DmtSession session = null;
		System.out.println("#testCreateInteriorNode011");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.LEAF_NODE +"/test", null);
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
	 * Asserts that DmtException with COMMAND_NOT_ALLOWED code is thrown when the parent
	 * is not an interior node.
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode012() {
		DmtSession session = null;
		System.out.println("#testCreateInteriorNode012");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestExecPluginActivator.LEAF_NODE +"/test");
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
	 * Asserts that DmtException with COMMAND_NOT_ALLOWED code is thrown  
	 * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin is read-only 
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode013() {
		DmtSession session = null;
		System.out.println("#testCreateInteriorNode013");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createInteriorNode(TestReadOnlyPluginActivator.INEXISTENT_NODE);
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
     * Asserts that if the parent node does not exist, it is created automatically,  
     * as if createInteriorNode(String) were called for the parent URI using the method with three parameters.
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode014() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode014");
            TestExecPlugin.resetCount();
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createInteriorNode(TestExecPluginActivator.INEXISTENT_INTERIOR_NODES);

           
            assertThat(TestExecPlugin.getCreateInteriorNodeCount()==2).as("Asserts that if the parent node does not exist, it is created automatically, " +
                    "as if createInteriorNode(String) were called for the parent URI").isTrue();
        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.cleanUp(session, null);
            
        }
    }
    /**
     * Asserts that if the parent node does not exist, it is created automatically,  
     * as if createInteriorNode(String) were called for the parent URI using the method with three parameters.
     * 
     * @spec DmtSession.createInteriorNode(String,String)
     */
    private void testCreateInteriorNode015() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode015");
            TestExecPlugin.resetCount();
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createInteriorNode(TestExecPluginActivator.INEXISTENT_INTERIOR_NODES,DmtConstants.MIMETYPE);

           
            assertThat(TestExecPlugin.getCreateInteriorNodeCount()==2).as("Asserts that if the parent node does not exist, it is created automatically, " +
                    "as if createInteriorNode(String) were called for the parent URI").isTrue();
        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.cleanUp(session, null);
            
        }
    }
    
    /**
     * Asserts that any exceptions encountered while creating the ancestors are propagated to 
     * the caller of this method
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode016() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode016");
            TestExecPlugin.setExceptionAtCreateInteriorNode(true);
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createInteriorNode(TestExecPluginActivator.INEXISTENT_INTERIOR_NODES);
            
            DmtTestControl.failException("", DmtIllegalStateException.class);
        } catch (DmtIllegalStateException e) {
            System.out.println("Asserts that any exceptions encountered while creating the ancestors are propagated to the caller of createLeafNode");
        } catch (Exception e) {
        	tbc.failExpectedOtherException(DmtIllegalStateException.class, e);
        } finally {
            tbc.cleanUp(session, null);
            TestExecPlugin.setExceptionAtCreateInteriorNode(false);
            
        }
    }
    
    /**
     * Asserts that any exceptions encountered while creating the ancestors are propagated to 
     * the caller of this method
     * 
     * @spec DmtSession.createInteriorNode(String,String)
     */
    private void testCreateInteriorNode017() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode017");
            TestExecPlugin.setExceptionAtCreateInteriorNode(true);
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createInteriorNode(TestExecPluginActivator.INEXISTENT_INTERIOR_NODES,DmtConstants.MIMETYPE);
            
            DmtTestControl.failException("", DmtIllegalStateException.class);
        } catch (DmtIllegalStateException e) {
            System.out.println("Asserts that any exceptions encountered while creating the ancestors are propagated to the caller of createLeafNode");
        } catch (Exception e) {
        	tbc.failExpectedOtherException(DmtIllegalStateException.class, e);
        } finally {
            tbc.cleanUp(session, null);
            TestExecPlugin.setExceptionAtCreateInteriorNode(false);
            
        }
    }
    
    /**
     * This method asserts that DmtException.TRANSACTION_ERROR 
     * if the session is atomic and the plugin is read-only
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode018() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode018");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createInteriorNode(TestReadOnlyPluginActivator.INEXISTENT_NODE);

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
     * if the session is atomic and the plugin is read-only
     * 
     * @spec DmtSession.createInteriorNode(String,String)
     */
    private void testCreateInteriorNode019() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode019");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createInteriorNode(TestReadOnlyPluginActivator.INEXISTENT_NODE,DmtConstants.MIMETYPE);

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
     * This method asserts that DmtException.TRANSACTION_ERROR 
     * if the session is atomic and the plugin does not support non-atomic writing
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode020() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode020");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createInteriorNode(TestNonAtomicPluginActivator.INEXISTENT_NODE);

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
     * @spec DmtSession.createInteriorNode(String,String)
     */
    private void testCreateInteriorNode021() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateInteriorNode021");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createInteriorNode(TestNonAtomicPluginActivator.INEXISTENT_NODE,DmtConstants.MIMETYPE);

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
     * Asserts that DmtException with COMMAND_NOT_ALLOWED code is thrown  
     * if the session is non-atomic (in this case LOCK_TYPE_EXCLUSIVE) and the plugin 
     * does not support non-atomic writing
     * 
     * @spec DmtSession.createInteriorNode(String)
     */
    private void testCreateInteriorNode022() {
        DmtSession session = null;
        System.out.println("#testCreateInteriorNode022");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createInteriorNode(TestNonAtomicPluginActivator.INEXISTENT_NODE);
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
	 * URI the session was opened with (it throws DmtException.NODE_ALREADY_EXISTS)
	 * using the method with one parameter
	 * 
	 * @spec DmtSession.createInteriorNode(String)
	 */
	private void testCreateInteriorNode023() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode023");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_ATOMIC);
			
			session.createInteriorNode("");
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		}  finally {
			tbc.closeSession(session);
		}		
	}	
	
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with (it throws DmtException.NODE_ALREADY_EXISTS)
	 * using the method with two parameters
	 * 
	 * @spec DmtSession.createInteriorNode(String,String)
	 */
	private void testCreateInteriorNode024() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateInteriorNode024");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.INTERIOR_NODE, DmtSession.LOCK_TYPE_ATOMIC);
			
			session.createInteriorNode("",DmtConstants.MIMETYPE);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		}  finally {
			tbc.closeSession(session);
		}		
	}	
    
}
