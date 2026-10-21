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
 * Mar 01, 2005  Luiz Felipe Guimaraes
 * 1             Updates 
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
 * This test case validates the implementation of <code>addPermission<code> method of Acl, 
 * according to MEG specification
 */
public class AddPermission extends DmtTestControl {
	
	private static final String ACL_DEFAULT = "Add=" + DmtConstants.PRINCIPAL + "&Get=*";

	
	/**
	 * This method creates a new Acl and asserts that a new permission is correctly added to 
	 * the Acl principal and that "*" grants permissions to all principals.
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission001() {
		try {
			System.out.println("#testAddPermission001");
			Acl acl = new Acl(
					ACL_DEFAULT);
	
			acl =  acl.addPermission(DmtConstants.PRINCIPAL, Acl.DELETE);
	
			assertThat(acl
							.getPermissions(DmtConstants.PRINCIPAL)).as("Asserting added permission").isEqualTo(Acl.ADD | Acl.GET | Acl.DELETE);
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL_2)).as("Asserting that '*' grants permissions to all principals").isEqualTo(Acl.GET);			
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Test that more than one permission is added at the same time to a principal.
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission002() {
		try {
			System.out.println("#testAddPermission002");
			Acl acl = new Acl(
					ACL_DEFAULT);
	
			acl = acl.addPermission(DmtConstants.PRINCIPAL, Acl.DELETE | Acl.EXEC | Acl.REPLACE);
	
			assertThat(acl
							.getPermissions(DmtConstants.PRINCIPAL)).as("Asserting principal permissions").isEqualTo(Acl.ALL_PERMISSION);
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL_2)).as("Asserting that '*' grants permissions to all principals").isEqualTo(Acl.GET);			
			
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever an invalid permission is added 
	 * to a principal.
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission003() {
		try {
			System.out.println("#testAddPermission003");
			Acl acl = new Acl(ACL_DEFAULT);
			acl = acl.addPermission(DmtConstants.PRINCIPAL, 2005);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}		
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever a valid permission 
	 * is added to an invalid principal.
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission004() {
		System.out.println("#testAddPermission004");
		Acl acl = new Acl(ACL_DEFAULT);
		try {
			acl = acl.addPermission(DmtConstants.INVALID, Acl.DELETE);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}
	/**
	 * Test if two different principals has different permissions using the same Acl
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission005() {
		try {
			System.out.println("#testAddPermission005");
			Acl acl = new Acl(
					"Add=" + DmtConstants.PRINCIPAL + "&Delete=" + DmtConstants.PRINCIPAL + "&Exec="
					+ DmtConstants.PRINCIPAL + "&Replace=" + DmtConstants.PRINCIPAL + "&Get=*");
			
			acl = acl.addPermission(DmtConstants.PRINCIPAL_2, Acl.GET
					| Acl.EXEC);
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL_2)).as("Assert " + DmtConstants.PRINCIPAL_2 + " permission").isEqualTo(Acl.GET
							| Acl.EXEC);
			assertThat(acl.getPermissions(DmtConstants.PRINCIPAL)).as("Assert " + DmtConstants.PRINCIPAL + " permission").isEqualTo(Acl.ALL_PERMISSION);
			
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}
	
	/**
	 * Asserts if "*" grants permissions to all principals.
	 * 
	 * @spec Acl.addPermission(String,int)
	 */
	@Test
	public void testAddPermission006() {
		try {
			System.out.println("#testAddPermission006");
			String[] principal = { DmtConstants.PRINCIPAL ,DmtConstants.PRINCIPAL_2 };
			int[] perm = { Acl.GET, Acl.EXEC };
			
			Acl acl = new Acl(principal,perm);
			acl = acl.addPermission("*",Acl.ADD);
			boolean passed = false;
			
			if (acl.getPermissions(principal[0]) == (Acl.GET | Acl.ADD) ) {
				if (acl.getPermissions(principal[1]) == (Acl.EXEC | Acl.ADD) ) {
					passed = true;
				}
			}
			assertThat(passed).as("Asserts if '*' grants permissions to all principals.").isTrue();
			
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}	

}
