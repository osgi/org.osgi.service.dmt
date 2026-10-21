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
 * Jan 1, 2005   Andre Assad
 * CR 1          Implement MEG TCK
 * ============  ==============================================================
 * Feb 15, 2005  Alexandre Santos
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ===========  ===============================================================
 * Aug 25, 2005  Luiz Felipe Guimaraes
 * 173           [MEGTCK][DMT] Changes on interface names and plugins
 * ============  ==============================================================
 */
package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.Acl;
import org.osgi.service.dmt.DmtData;
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
 * @author Andre Assad
 * 
 * This Test Case Validates the implementation of <code>createLeafNode<code> method of DmtSession, 
 * according to MEG specification
 */
public class CreateLeafNode implements TestInterface {
	private DmtTestControl tbc;
	private DmtData value = new DmtData(10);

	public CreateLeafNode(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
        testCreateLeafNode001();
		testCreateLeafNode002();
		testCreateLeafNode003();
		testCreateLeafNode004();
		testCreateLeafNode005();
		testCreateLeafNode006();
		testCreateLeafNode007();
		testCreateLeafNode008();
		testCreateLeafNode009();
		testCreateLeafNode010();
		testCreateLeafNode011();
		testCreateLeafNode012();
		testCreateLeafNode013();
		testCreateLeafNode014();
		testCreateLeafNode015();
		testCreateLeafNode016();
		testCreateLeafNode017();
		testCreateLeafNode018();
		testCreateLeafNode019();
		testCreateLeafNode020();
		testCreateLeafNode021();
		testCreateLeafNode022();
		testCreateLeafNode023();
		testCreateLeafNode024();
		testCreateLeafNode025();
		testCreateLeafNode026();
		testCreateLeafNode027();
		testCreateLeafNode028();
        testCreateLeafNode029();
        testCreateLeafNode030();
        testCreateLeafNode031();
        testCreateLeafNode032();
        testCreateLeafNode033();
        testCreateLeafNode034();
        testCreateLeafNode035();
        testCreateLeafNode036();
        testCreateLeafNode037();
        testCreateLeafNode038();
        testCreateLeafNode039();
        testCreateLeafNode040();
        testCreateLeafNode041();
        testCreateLeafNode042();
        
    }
        
    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_ALREADY_EXISTS is thrown
	 * if nodeUri points to a node that already exists 
	 * 
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode001() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode001");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(TestExecPluginActivator.LEAF_NODE);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}

	}

	/**
	 * This method asserts that DmtException.NODE_ALREADY_EXISTS is thrown  
	 * if nodeUri points to a node that already exists using the method with two parameters
	 *  
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode002() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode002");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.createLeafNode(TestExecPluginActivator.LEAF_NODE,
					value);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts that DmtException.NODE_ALREADY_EXISTS is thrown  
	 * if nodeUri points to a node that already exists using the method with three parameters
	 *  
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode003() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode003");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(TestExecPluginActivator.LEAF_NODE,
					value, DmtConstants.MIMETYPE);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that createLeafNode is executed when the right DmtPermission is set (Local) 
	 * using the method with one parameter.
	 * 
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode004() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode004");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			tbc.setPermissions(new PermissionInfo(
					DmtPermission.class.getName(), DmtConstants.ALL_NODES,
					DmtPermission.ADD));

			session
					.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
		}
	}

	/**
	 * This method asserts that createLeafNode is executed when the right DmtPermission is set (Local)  
	 * using the method with two parameters.
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode005() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode005");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			tbc.setPermissions(new PermissionInfo(
					DmtPermission.class.getName(), DmtConstants.ALL_NODES,
					DmtPermission.ADD));

			session.createLeafNode(
					TestExecPluginActivator.INEXISTENT_LEAF_NODE, value);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
		}
	}

	/**
	 * This method asserts that createLeafNode is executed when the right DmtPermission is set (Local) 
	 * using the method with three parameters.
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode006() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode006");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			tbc.setPermissions(new PermissionInfo(
					DmtPermission.class.getName(), DmtConstants.ALL_NODES,
					DmtPermission.ADD));

			session.createLeafNode(
					TestExecPluginActivator.INEXISTENT_LEAF_NODE, value, DmtConstants.MIMETYPE);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
		}
	}

	/**
	 * This method asserts that createLeafNode is executed when the right Acl is set (Remote)
	 * using the method with one parameter.
	 * 
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode007() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode007");
			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeAcl(TestExecPluginActivator.ROOT, new Acl(
					new String[] { DmtConstants.PRINCIPAL },
					new int[] { Acl.ADD | Acl.GET }));

			session.close();
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class
					.getName(), DmtConstants.PRINCIPAL, "*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,
					TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session
					.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, TestExecPluginActivator.ROOT);
            
		}

	}

	/**
	 * This method asserts that createLeafNode is executed when the right Acl is set (Remote) 
	 * using the method with two parameters.
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode008() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode008");
			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeAcl(TestExecPluginActivator.ROOT, new Acl(
					new String[] { DmtConstants.PRINCIPAL },
					new int[] { Acl.ADD | Acl.GET }));
			session.close();

			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class
					.getName(), DmtConstants.PRINCIPAL, "*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,
					TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(
					TestExecPluginActivator.INEXISTENT_LEAF_NODE,
					value);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, TestExecPluginActivator.ROOT);
            
		}

	}

	/**
	 * This method asserts that createLeafNode is executed when the right Acl is set (Remote)
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode009() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode009");

			session = tbc.getDmtAdmin().getSession(".",
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeAcl(TestExecPluginActivator.ROOT, new Acl(
					new String[] { DmtConstants.PRINCIPAL },
					new int[] { Acl.ADD | Acl.GET }));

			session.close();

			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class
					.getName(), DmtConstants.PRINCIPAL, "*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,
					TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);

			session.createLeafNode(
					TestExecPluginActivator.INEXISTENT_LEAF_NODE, value, DmtConstants.MIMETYPE);

			System.out.println("createLeafNode was successfully executed");

		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, TestExecPluginActivator.ROOT);
            
		}
	}

	/**
	 * This method asserts that relative URI works as described in this method using the method with one parameter.
	 * 
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode010() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode010");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE_NAME);

			System.out.println("A relative URI can be used with CreateLeafNode.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts that relative URI works as described in this method using the method with two parameters.
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode011() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode011");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE_NAME,
					value);

			System.out.println("A relative URI can be used with CreateLeafNode.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that relative URI works as described in this method 
	 * using the method with three parameters
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode012() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode012");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE_NAME,
					value, DmtConstants.MIMETYPE);

			System.out.println("A relative URI can be used with CreateLeafNode.");
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
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode013() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode013");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_SHARED);
			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE);
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} finally {
			tbc.closeSession(session);
		}
	}

	/**
	 * This method asserts if DmtIllegalStateException is thrown if this method is called 
	 * when the session is LOCK_TYPE_SHARED
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode014() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode014");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_SHARED);
			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE,value);
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts if DmtIllegalStateException is thrown if this method is called 
	 * when the session is LOCK_TYPE_SHARED
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode015() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode015");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_SHARED);
			session.createLeafNode(TestExecPluginActivator.INEXISTENT_LEAF_NODE,value,DmtConstants.MIMETYPE);
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} catch (DmtIllegalStateException e) {
			System.out.println("DmtIllegalStateException correctly thrown");
		} catch (Exception e) {
			DmtTestControl.failException("", DmtIllegalStateException.class);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * Asserts that DmtException with COMMAND_NOT_ALLOWED code is thrown when the parent
	 * is not an interior node.
	 * 
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode016() {
		DmtSession session = null;
		System.out.println("#testCreateLeafNode016");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(TestExecPluginActivator.LEAF_NODE +"/test");
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
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode017() {
		DmtSession session = null;
		System.out.println("#testCreateLeafNode017");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(TestExecPluginActivator.LEAF_NODE +"/test",value);
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
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode018() {
		DmtSession session = null;
		System.out.println("#testCreateLeafNode018");
		try {
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.createLeafNode(TestExecPluginActivator.LEAF_NODE +"/test",value,DmtConstants.MIMETYPE);
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
     * This method asserts that null can be passed as value using the method with two parameters.
     * 
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode019() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode019");

            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_LEAF_NODE, null);

            System.out.println("createLeafNode was successfully executed");

        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
        }
    }
    /**
     * This method asserts that null can be passed as value using the method with three parameters.
     * 
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode020() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode020");

            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_LEAF_NODE, null,DmtConstants.MIMETYPE);

            System.out.println("createLeafNode was successfully executed");

        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
        }
    }
    /**
     * This method asserts that null can be passed as mimetype
     * 
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode021() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode021");

            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_LEAF_NODE, value,null);

            System.out.println("createLeafNode was successfully executed");

        } catch (Exception e) {
        	tbc.failUnexpectedException(e);
        } finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
        }
    }
    /**
     * Asserts that if the parent node does not exist, it is created automatically,  
     * as if createInteriorNode(String) were called for the parent URI using the method with three parameters.
     * 
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode022() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode022");
            TestExecPlugin.resetCount();
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES);

           
            assertThat(TestExecPlugin.getCreateInteriorNodeCount()==2 && TestExecPlugin.getCreateLeafNodeCount()==1).as("Asserts that if the parent node does not exist, it is created automatically, " +
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
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode023() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode023");
            TestExecPlugin.resetCount();
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES, value);

           
            assertThat(TestExecPlugin.getCreateInteriorNodeCount()==2 && TestExecPlugin.getCreateLeafNodeCount()==1).as("Asserts that if the parent node does not exist, it is created automatically, " +
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
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode024() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode024");
            TestExecPlugin.resetCount();
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES, value,null);

           
            assertThat(TestExecPlugin.getCreateInteriorNodeCount()==2 && TestExecPlugin.getCreateLeafNodeCount()==1).as("Asserts that if the parent node does not exist, it is created automatically, " +
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
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode025() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode025");
            TestExecPlugin.setExceptionAtCreateInteriorNode(true);
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES);
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
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode026() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode026");
            TestExecPlugin.setExceptionAtCreateInteriorNode(true);
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES, value);
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
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode027() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode027");
            TestExecPlugin.setExceptionAtCreateInteriorNode(true);
            session = tbc.getDmtAdmin().getSession(".",
                    DmtSession.LOCK_TYPE_EXCLUSIVE);

            session.createLeafNode(
                    TestExecPluginActivator.INEXISTENT_INTERIOR_AND_LEAF_NODES, value,null);
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
     * This method asserts that DmtException.TRANSACTION_ERROR is thrown 
     * if the session is atomic and the plugin does not support non-atomic writing
     * 
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode028() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode028");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE);
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
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode029() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode029");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE,value);
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
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode030() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode030");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE,value, DmtConstants.MIMETYPE);
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
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode031() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode031");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE);
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
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode032() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode032");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE,value);
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
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode033() {
        DmtSession session = null;
        try {
            System.out.println("#testCreateLeafNode033");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE,value, DmtConstants.MIMETYPE);
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
     * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin is read-only
     * 
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode034() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode034");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE,value, DmtConstants.MIMETYPE);
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
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode035() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode035");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE,value);
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
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode036() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode036");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestReadOnlyPluginActivator.INEXISTENT_LEAF_NODE);
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
     * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin 
     * does not support non-atomic writing
     * 
     * @spec DmtSession.createLeafNode(String,DmtData,String)
     */
    private void testCreateLeafNode037() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode037");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE,value, DmtConstants.MIMETYPE);
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
     * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin 
     * does not support non-atomic writing
     * 
     * @spec DmtSession.createLeafNode(String,DmtData)
     */
    private void testCreateLeafNode038() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode038");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE,value);
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
     * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin 
     * does not support non-atomic writing
     * 
     * @spec DmtSession.createLeafNode(String)
     */
    private void testCreateLeafNode039() {
        DmtSession session = null;
        System.out.println("#testCreateLeafNode039");
        try {
            session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
            session.createLeafNode(TestNonAtomicPluginActivator.INEXISTENT_LEAF_NODE);
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
	 * @spec DmtSession.createLeafNode(String)
	 */
	private void testCreateLeafNode040() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode040");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode("");

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with (it throws DmtException.NODE_ALREADY_EXISTS)
	 * using the method with two parameters
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData)
	 */
	private void testCreateLeafNode041() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode041");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode("",value);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with (it throws DmtException.NODE_ALREADY_EXISTS)
	 * using the method with three parameters
	 * 
	 * @spec DmtSession.createLeafNode(String,DmtData,String)
	 */
	private void testCreateLeafNode042() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode042");
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.createLeafNode("",value,DmtConstants.MIMETYPE);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_ALREADY_EXISTS").isEqualTo(DmtException.NODE_ALREADY_EXISTS);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
}
