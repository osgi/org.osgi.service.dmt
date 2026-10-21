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
 * Feb 11, 2005  Andre Assad
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.Acl;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtIllegalStateException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.service.dmt.security.DmtPrincipalPermission;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;
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
 * This test case validates the implementation of <code>getNodeValue, setNodeValue</code> 
 * method of DmtSession, according to MEG specification.
 */
public class GetSetNodeValue implements TestInterface {
	private DmtTestControl tbc;
	
	private final DmtData dmtData = new DmtData(10);
	
	public GetSetNodeValue(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
		testGetSetNodeValue001();
		testGetSetNodeValue002();
		testGetSetNodeValue003();
		testGetSetNodeValue004();
		testGetSetNodeValue005();
		testGetSetNodeValue006();
		testGetSetNodeValue007();
		testGetSetNodeValue008();
		testGetSetNodeValue009();
		testGetSetNodeValue010();
		testGetSetNodeValue011();
		testGetSetNodeValue012();
        testGetSetNodeValue013();
        testGetSetNodeValue014();
		testGetSetNodeValue015();
		testGetSetNodeValue016();
		testGetSetNodeValue017();
		testGetSetNodeValue018();
		testGetSetNodeValue019();
	}
    
    private void prepare() {
        tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
    }
	/**
	 * This method asserts that DmtException.NODE_NOT_FOUND is thrown
	 * if nodeUri points to a non-existing node 
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue001() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue001");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeValue(TestExecPluginActivator.INEXISTENT_NODE,dmtData);
			DmtTestControl.failException("#", DmtException.class);
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
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue002() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue002");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.getNodeValue(TestExecPluginActivator.INEXISTENT_NODE);
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is NODE_NOT_FOUND").isEqualTo(DmtException.NODE_NOT_FOUND);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that no Exception is thrown if nodeUri is an interior node
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue003() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue003");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeValue(TestExecPluginActivator.INTERIOR_NODE, new DmtData(Integer.valueOf("1")));
			System.out.println("Asserts that no Exception is thrown if nodeUri is an interior node and DmtSession.setNodeValue(String,DmtData) is called");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	/**
	 * This method asserts that no Exception is thrown if nodeUri is an interior node
	 * 
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue004() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue004");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.getNodeValue(TestExecPluginActivator.INTERIOR_NODE);
			System.out.println("Asserts that no Exception is thrown if nodeUri is an interior node and DmtSession.getNodeValue(String) is called");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}

	
	/**
	 * This method asserts that it is successfully executed when the correct Acl is set (Remote)
	 * 
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue005() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue005");

            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.LEAF_NODE, DmtConstants.PRINCIPAL, Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
            
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,TestExecPluginActivator.LEAF_NODE,DmtSession.LOCK_TYPE_ATOMIC);
			session.getNodeValue(TestExecPluginActivator.LEAF_NODE);
			System.out.println("getNodeValue correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, TestExecPluginActivator.LEAF_NODE);
            
		}
	}
	/**
	 * This method asserts that it is successfully executed when the correct Acl is set (Remote)
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue006() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue006");

            tbc.openSessionAndSetNodeAcl(TestExecPluginActivator.LEAF_NODE, DmtConstants.PRINCIPAL, Acl.REPLACE | Acl.GET );
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(),DmtConstants.PRINCIPAL,"*"));
			session = tbc.getDmtAdmin().getSession(DmtConstants.PRINCIPAL,TestExecPluginActivator.LEAF_NODE,DmtSession.LOCK_TYPE_ATOMIC);
			session.setNodeValue(TestExecPluginActivator.LEAF_NODE,dmtData);
			System.out.println("setNodeValue correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session,TestExecPluginActivator.LEAF_NODE);
            
		}
	}
	/**
	 * This method asserts that it is successfully executed when the correct DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue007() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue007");
            tbc.setPermissions(new PermissionInfo[] {
                    new PermissionInfo(DmtPermission.class.getName(), DmtConstants.OSGi_ROOT, DmtPermission.GET),
                    new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.REPLACE)});
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.setNodeValue(TestExecPluginActivator.LEAF_NODE, dmtData);
			System.out.println("setNodeValue correctly executed");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
            tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS));
            tbc.cleanUp(session, null);
            
		}
	}
	/**
	 * This method asserts that it is successfully executed when the correct DmtPermission is set (Local)
	 * 
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue008() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue008");
			tbc.setPermissions(new PermissionInfo(DmtPermission.class.getName(),DmtConstants.ALL_NODES,DmtPermission.GET));
			session = tbc.getDmtAdmin().getSession(DmtConstants.OSGi_ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			
			session.getNodeValue(TestExecPluginActivator.LEAF_NODE);
			System.out.println("getNodeValue correctly executed");
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
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue009() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue009");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeValue(TestExecPluginActivator.LEAF_RELATIVE);

			System.out.println("A relative URI can be used with getNodeValue.");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}	
	
	/**
	 * This method asserts that relative URI works as described.
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue010() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue010");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_ATOMIC);

			session.setNodeValue(TestExecPluginActivator.LEAF_RELATIVE, dmtData);

			System.out.println("A relative URI can be used with setNodeValue.");
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
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue011() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue011");
			session = tbc.getDmtAdmin().getSession(
				TestExecPluginActivator.ROOT, DmtSession.LOCK_TYPE_SHARED);
			session.setNodeValue(TestExecPluginActivator.LEAF_RELATIVE, dmtData);
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
	 * This method asserts that DmtException.COMMAND_NOT_ALLOWED is thrown 
	 * if the session is non-atomic (LOCK_TYPE_EXCLUSIVE) and the plugin is read-only
	 *
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
    private void testGetSetNodeValue012() {
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeValue012");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeValue(TestReadOnlyPluginActivator.LEAF_NODE,dmtData);
			DmtTestControl.failException("#", DmtException.class);
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
	 * does not support non-atomic writing
	 *
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
    private void testGetSetNodeValue013() {
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeValue013");
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_EXCLUSIVE);
			session.setNodeValue(TestNonAtomicPluginActivator.LEAF_NODE,dmtData);
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is COMMAND_NOT_ALLOWED").isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
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
     * @spec DmtSession.setNodeValue(String,DmtData)
     */
    private void testGetSetNodeValue014() {
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeValue014");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.setNodeValue(TestReadOnlyPluginActivator.LEAF_NODE,dmtData);
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
     * @spec DmtSession.setNodeValue(String,DmtData)
     */
    private void testGetSetNodeValue015() {
        DmtSession session = null;
        try {
            System.out.println("#testGetSetNodeValue015");
            session = tbc.getDmtAdmin().getSession(".",
                DmtSession.LOCK_TYPE_ATOMIC);
            
            session.setNodeValue(TestNonAtomicPluginActivator.LEAF_NODE,dmtData);
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
	 * This method asserts that an empty string as relative URI means the root 
	 * URI the session was opened with
	 * 
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue016() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue016");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.getNodeValue("");

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
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue017() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue017");
			
			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.LEAF_NODE, DmtSession.LOCK_TYPE_ATOMIC);

			session.setNodeValue("", dmtData);

			System.out.println("Asserts that an empty string as relative URI means the root " +
				"URI the session was opened with");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
		}
	}
	
	/**
	 * This method asserts that a replaced interior node sends out events for
	 * each of its children in depth first order and node names sorted with
	 * Arrays.sort(String[]).
	 * 
	 * @spec DmtSession.setNodeValue(String,DmtData)
	 */
	private void testGetSetNodeValue018() {
		DmtSession session = null;
		EventTest events = null;
		ServiceRegistration<EventHandler> servReg = null;
		try {
			System.out.println("#testGetSetNodeValue018");
			
			Dictionary<String,Object> ht = new Hashtable<>();
			ht.put(org.osgi.service.event.EventConstants.EVENT_TOPIC,
					new String[] {"org/osgi/service/dmt/DmtEvent/*"});
			ht.put(org.osgi.service.event.EventConstants.EVENT_FILTER, "(nodes="+TestExecPluginActivator.INTERIOR_NODE_WITH_TWO_CHILDREN+ "/*)");
			events = new EventTest(); 
			servReg = tbc.getContext()
					.registerService(EventHandler.class, events, ht);
			
			session = tbc.getDmtAdmin().getSession(".",DmtSession.LOCK_TYPE_ATOMIC);
			session.setNodeValue(TestExecPluginActivator.INTERIOR_NODE_WITH_TWO_CHILDREN, new DmtData(Integer.valueOf("1")));
			
			session.commit();
			synchronized (tbc) {
				tbc.wait(DmtConstants.WAITING_TIME);
			}
			
			assertThat(events.isSorted()).as("Asserts that a replaced interior node sends out events for " +
					"each of its children in depth first order and node names sorted with" +
					" Arrays.sort(String[]). ").isTrue();
			
			
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		} finally {
			tbc.closeSession(session);
			if (servReg!=null) {
				servReg.unregister();
			}
		}
	}
	
	/**
	 * This method asserts that {@code DmtSession.getNodeValue(String)} doesn't
	 * return {@code null} in case of malicious plug-in, which returns
	 * {@code null}.
	 *
	 * @spec DmtSession.getNodeValue(String)
	 */
	private void testGetSetNodeValue019() {
		DmtSession session = null;
		try {
			System.out.println("#testGetSetNodeValue019");
			session = tbc.getDmtAdmin().getSession(".", DmtSession.LOCK_TYPE_SHARED);
			session.getNodeValue(TestExecPluginActivator.INTERIOR_NODE_WITH_NULL_VALUES);
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserting that DmtException code is COMMAND_FAILED").isEqualTo(DmtException.COMMAND_FAILED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.closeSession(session);
		}
	}

	public class EventTest implements EventHandler {
		private int count=0;
		private String[] nodeNames = new String[2];
		
		@Override
		public void handleEvent(Event event) {
			String topic = (String) event.getProperty(DmtConstants.TOPIC);
			String[] nodes = (String[]) event.getProperty(DmtConstants.NODES);
			if (topic.equals(DmtConstants.REPLACED) && nodes.length==2) {
					nodeNames[0] = nodes[0];
					nodeNames[1] = nodes[1];
					count++;
				}

		}

		
		public boolean isSorted() {
			//The node names are must be sorted
			String childA = (TestExecPluginActivator.INTERIOR_NODE_WITH_TWO_CHILDREN + "/" + TestExecPlugin.CHILDREN_NAMES[1]);
			String childB = (TestExecPluginActivator.INTERIOR_NODE_WITH_TWO_CHILDREN + "/" + TestExecPlugin.CHILDREN_NAMES[0]);			
			if (count==1 && childA.equals(nodeNames[0]) && 
					childB.equals(nodeNames[1])) {
				return true;
			} else {
				return false;
			}
		}
		
	}
}
