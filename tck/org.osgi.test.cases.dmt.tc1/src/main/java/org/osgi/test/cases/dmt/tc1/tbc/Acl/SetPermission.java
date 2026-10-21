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
 * Mar 04, 2005  Andre Assad
 * 23            Updates after changes in the Acl API
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.Acl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.Acl;
import org.osgi.test.cases.dmt.tc1.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;
/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>setPermission</code> method of Acl, 
 * according to MEG specification.
 */
public class SetPermission extends DmtTestControl {
	
	private static final String ACL_DEFAULT = "Add=" + DmtConstants.PRINCIPAL + "&Delete=" + DmtConstants.PRINCIPAL;


	/**
	 * This method asserts that a set of permissions the principal had is overriden by new ones.
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission001() {
		try {		
			System.out.println("#testSetPermission001");
			Acl acl = new Acl(ACL_DEFAULT);
	
			acl = acl.setPermission(DmtConstants.PRINCIPAL, Acl.ADD
					| Acl.EXEC);
	
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL)).as("Asserting Get Permission").isEqualTo(Acl.ADD | Acl.EXEC);
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever an invalid 
	 * permission is set to a principal.
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission002() {
		try {
			System.out.println("#testSetPermission002");
			Acl acl = new Acl(ACL_DEFAULT);
			
			acl = acl.setPermission(DmtConstants.PRINCIPAL,Acl.ADD | 2005);

			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever a valid permission 
	 * is set to an invalid principal.
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission003() {
		try {
			System.out.println("#testSetPermission003");
			Acl acl = new Acl(ACL_DEFAULT);
			acl = acl.setPermission(DmtConstants.INVALID, Acl.ADD | Acl.REPLACE);

			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that all permissions the principal had will be overwritten
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission004() {
		try {
			System.out.println("#testSetPermission004");
			Acl acl = new Acl(
					"Add=" + DmtConstants.PRINCIPAL + "&Delete=" + DmtConstants.PRINCIPAL + "&Get=*");
			
			acl = acl.setPermission(DmtConstants.PRINCIPAL, 
					Acl.EXEC | Acl.REPLACE | Acl.GET);
			
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL)).as("Assert " + DmtConstants.PRINCIPAL + " permission.").isEqualTo(Acl.REPLACE | Acl.GET | Acl.EXEC);
			
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that IllegalArgumentException is thrown if a globally granted 
	 * permission is revoked from the principal
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission005() {
		try {
			System.out.println("#testSetPermission005");
			Acl acl = new Acl(
					"Add=" + DmtConstants.PRINCIPAL + "&Delete=" + DmtConstants.PRINCIPAL + "&Get=*");
			
			acl = acl.setPermission(DmtConstants.PRINCIPAL, Acl.EXEC
					| Acl.REPLACE);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}	
	/**
	 * Passes all permission and check if the permission set is the same as ALL_PERMISSION.
	 * 
	 * @spec Acl.setPermission(String,int) 
	 */
	@Test
	public void testSetPermission006() {
		try {
			System.out.println("#testSetPermission006");
			Acl acl = new Acl(
					"Add=" + DmtConstants.PRINCIPAL + "&Delete=" + DmtConstants.PRINCIPAL + "&Exec=" + DmtConstants.PRINCIPAL );			
			acl = acl.setPermission(DmtConstants.PRINCIPAL, 
					Acl.ADD | Acl.GET | Acl.EXEC | Acl.DELETE | Acl.REPLACE);
			
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL)).as("Assert " + DmtConstants.PRINCIPAL
					+ " all permissions").isEqualTo(Acl.ALL_PERMISSION);

		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}
	/**
	 * Asserts that '*' grants permissions to all principals
	 * 
	 * @spec Acl.setPermission(String,int)
	 */
	@Test
	public void testSetPermission007() {
		try {
			System.out.println("#testSetPermission007");
			Acl acl = new Acl(
					"Add=" + DmtConstants.PRINCIPAL);			
			acl = acl.setPermission("*", Acl.ADD | Acl.GET | Acl.EXEC | 
					Acl.DELETE | Acl.REPLACE);
			
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL)).as("Assert that '*' grants permissions to all principals ["
							+ DmtConstants.PRINCIPAL + "]").isEqualTo(Acl.ALL_PERMISSION);
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL_2)).as("Assert that '*' grants permissions to all principals ["
							+ DmtConstants.PRINCIPAL_2 + "]").isEqualTo(Acl.ALL_PERMISSION);

		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}	

	
}
