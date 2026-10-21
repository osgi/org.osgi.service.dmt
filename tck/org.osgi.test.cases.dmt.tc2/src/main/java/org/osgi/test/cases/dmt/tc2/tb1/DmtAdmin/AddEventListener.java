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
 * Jul 12, 2005  Luiz Felipe Guimaraes
 * 118           Implement MEG TCK
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtAdmin;

import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.service.dmt.security.DmtPrincipalPermission;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgi.test.cases.dmt.tc2.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;

/**
 * @author Luiz Felipe Guimaraes
 * This test case validates the implementation of <code>addEventListener</code> method of DmtAdmin, 
 * according to MEG specification
 * 
 */

public class AddEventListener implements TestInterface {
	private DmtTestControl tbc;
	
	public AddEventListener(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
        prepare();
        testAddEventListener001();
        testAddEventListener002();
        testAddEventListener003();
        testAddEventListener004();
        testAddEventListener005();
        testAddEventListener006();
        testAddEventListener007();
        testAddEventListener008();
        testAddEventListener009();
	}
	
    private void prepare() {
        tbc.setPermissions(
                new PermissionInfo[] {
                new PermissionInfo(DmtPermission.class.getName(), DmtConstants.ALL_NODES,DmtConstants.ALL_ACTIONS),
                new PermissionInfo(DmtPrincipalPermission.class.getName(), DmtConstants.PRINCIPAL, "*") }
                );
    }
	/**
	 * Asserts that IllegalArgumentException is thrown if the type parameter contains 
	 * invalid bits (not corresponding to any event type defined in DmtEvent) 
	 * 
	 * @spec DmtAdmin.addEventListener(int,String,DmtEventListener) 

	 */
	private void testAddEventListener001() {
		try {
			System.out.println("#testAddEventListener001");
			// Does not compile anymore, do we need this one anymore?
			// tbc.getDmtAdmin().addEventListener(INVALID_CODE, TestExecPluginActivator.ROOT,event);
			DmtTestControl.failException("", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}
	
	/**
	 * Asserts that IllegalArgumentException is thrown if the type parameter contains 
	 * invalid bits (not corresponding to any event type defined in DmtEvent) 
	 * 
	 * @spec DmtAdmin.addEventListener(String,int,String,DmtEventListener) 

	 */
	private void testAddEventListener002() {
		try {
			System.out.println("#testAddEventListener002");
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(DmtConstants.PRINCIPAL,INVALID_CODE, TestExecPluginActivator.ROOT, event);
			DmtTestControl.failException("", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}
	
	/**
	 * Asserts that NullPointerException is thrown if the uri parameter is null  
	 * 
	 * @spec DmtAdmin.addEventListener(int,String,DmtEventListener) 

	 */
	private void testAddEventListener003() {
		try {
			System.out.println("#testAddEventListener003");
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(DmtEvent.ADDED, null,event);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class, e);
		}
	}
	
	/**
	 * Asserts that NullPointerException is thrown if the events parameter is null  
	 * 
	 * @spec DmtAdmin.addEventListener(int,String,DmtEventListener) 

	 */
	private void testAddEventListener004() {
		try {
			System.out.println("#testAddEventListener004");
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(DmtEvent.ADDED, TestExecPluginActivator.ROOT,null);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class, e);
		}
	}
	
	/**
	 * Asserts that NullPointerException is thrown if the principal parameter is null  
	 * 
	 * @spec DmtAdmin.addEventListener(String,int,String,DmtEventListener) 

	 */
	private void testAddEventListener005() {
		try {
			System.out.println("#testAddEventListener005");
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(null,DmtEvent.ADDED, TestExecPluginActivator.ROOT,event);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class, e);
		}
	}
	
	/**
	 * Asserts that NullPointerException is thrown if the uri parameter is null  
	 * 
	 * @spec DmtAdmin.addEventListener(String,int,String,DmtEventListener) 

	 */
	private void testAddEventListener006() {
		try {
			System.out.println("#testAddEventListener006");
			// Does not compile anymore
			//tbc.getDmtAdmin().addEventListener(DmtConstants.PRINCIPAL,DmtEvent.ADDED, null,event);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class, e);
		}
	}
	
	/**
	 * Asserts that NullPointerException is thrown if the events parameter is null  
	 * 
	 * @spec DmtAdmin.addEventListener(String,int,String,DmtEventListener) 

	 */
	private void testAddEventListener007() {
		try {
			System.out.println("#testAddEventListener007");
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(DmtConstants.PRINCIPAL,DmtEvent.ADDED, TestExecPluginActivator.ROOT,null);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class, e);
		}
	}
	
	/**
	 * Asserts that SecurityException is thrown if the caller doesn't 
	 * have the necessary GET DmtPermission for the given URI 
	 * 
	 * @spec DmtAdmin.addEventListener(int,String,DmtEventListener) 

	 */
	private void testAddEventListener008() {
		try {
			System.out.println("#testAddEventListener008");
	        //DmtPermission is not present, so SecurityException must be thrown 
	        tbc.setPermissions(new PermissionInfo[0]);
			// Does not compile anymore
			// tbc.getDmtAdmin().addEventListener(DmtEvent.ADDED, TestExecPluginActivator.ROOT,event);
			DmtTestControl.failException("", SecurityException.class);
		} catch (SecurityException e) {
			System.out.println("SecurityException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
			prepare();
		}
	}
	
	/**
	 * Asserts that SecurityException is thrown if the caller doesn't have the necessary 
	 * DmtPrincipalPermission to use the specified principal 
	 * 
	 * @spec DmtAdmin.addEventListener(String,int,String,DmtEventListener) 

	 */
	private void testAddEventListener009() {
		try {
			System.out.println("#testAddEventListener009");
			tbc.setPermissions(new PermissionInfo(DmtPrincipalPermission.class.getName(), 
					DmtConstants.PRINCIPAL_2, "*"));
			// Does not compile anymore
			//tbc.getDmtAdmin().addEventListener(DmtConstants.PRINCIPAL,DmtEvent.ADDED, TestExecPluginActivator.ROOT,event);
			DmtTestControl.failException("", SecurityException.class);
		} catch (SecurityException e) {
			System.out.println("SecurityException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
			prepare();
		}
	}

}

