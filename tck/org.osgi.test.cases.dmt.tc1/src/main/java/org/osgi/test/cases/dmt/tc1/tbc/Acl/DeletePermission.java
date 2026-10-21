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
 * This test case validates the implementation of <code>deletePermission<code> method of Acl, 
 * according to MEG specification
 */
public class DeletePermission extends DmtTestControl {

	private static final String ACL_DEFAULT = "Add=" + DmtConstants.PRINCIPAL
			+ "&Delete=" + DmtConstants.PRINCIPAL + "&Exec="
			+ DmtConstants.PRINCIPAL + "&Get=*";


	/**
	 * This method asserts that a principal permission is correctly deleted from its Acl.
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission001() {
		try {
			System.out.println("#testDeletePermission001");
			Acl acl = new Acl(ACL_DEFAULT);

			acl = acl.deletePermission(DmtConstants.PRINCIPAL,
					Acl.DELETE);

			assertThat(acl
					.getPermissions(DmtConstants.PRINCIPAL)).as("Asserting deleted permission").isEqualTo(Acl.ADD
					| Acl.EXEC | Acl.GET);
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Test that more than one permission can be deleted in the same time from a principal.
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission002() {
		try {
			System.out.println("#testDeletePermission002");
			Acl acl = new Acl(ACL_DEFAULT);

			acl = acl.deletePermission(DmtConstants.PRINCIPAL,
					Acl.DELETE | Acl.EXEC);

			assertThat(acl
					.getPermissions(DmtConstants.PRINCIPAL)).as("Asserting deleted permission").isEqualTo(Acl.ADD
					| Acl.GET);
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever an invalid permission is 
	 * deleted from a principal.
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission003() {
		try {
			System.out.println("#testDeletePermission003");
			Acl acl = new Acl(ACL_DEFAULT);
			acl = acl.deletePermission(DmtConstants.PRINCIPAL, 2005);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown whenever a valid permission is 
	 * deleted from an invalid principal.
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission004() {
		try {
			System.out.println("#testDeletePermission004");
			Acl acl = new Acl(ACL_DEFAULT);
			acl = acl.deletePermission(DmtConstants.INVALID,
					Acl.EXEC);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Tests if this method creates a new instance of Acl without removing 
	 * any permissions from the existing Acl
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission005() {
		try {
			System.out.println("#testDeletePermission005");
			Acl acl = new Acl("Add=" + DmtConstants.PRINCIPAL
					+ "&Delete=" + DmtConstants.PRINCIPAL + "&Exec="
					+ DmtConstants.PRINCIPAL + "&Get=*");

			Acl acl2 = acl.deletePermission(DmtConstants.PRINCIPAL,
					Acl.EXEC);

			assertThat(acl
					.getPermissions(DmtConstants.PRINCIPAL)).as("Assert " + DmtConstants.PRINCIPAL
					+ " permission").isEqualTo(Acl.GET | Acl.ADD | Acl.EXEC
					| Acl.DELETE);

			assertThat(acl2
							.getPermissions(DmtConstants.PRINCIPAL_2)).as("Assert that '*' grants permission to all principals").isEqualTo(Acl.GET);

			assertThat(acl2.getPermissions(DmtConstants.PRINCIPAL)).as("Assert " + DmtConstants.PRINCIPAL
					+ " permission").isEqualTo(Acl.GET | Acl.ADD | Acl.DELETE);

			assertThat(acl
							.getPermissions(DmtConstants.PRINCIPAL_2)).as("Assert that '*' grants permission to all principals").isEqualTo(Acl.GET);
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an IllegalArgumentException is thrown when a globally granted 
	 * permission is revoked from a specific principal
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission006() {
		try {
			System.out.println("#testDeletePermission006");
			Acl acl = new Acl(ACL_DEFAULT);

			acl = acl.deletePermission(DmtConstants.PRINCIPAL,
					Acl.GET);
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts if "*" revokes permissions from all principals.
	 * 
	 * @spec Acl.deletePermission(String,int)
	 */
	@Test
	public void testDeletePermission007() {
		try {
			System.out.println("#testDeletePermission007");
			String[] principal = { DmtConstants.PRINCIPAL,
					DmtConstants.PRINCIPAL_2, "*" };
			int[] perm = { Acl.GET, Acl.EXEC, Acl.ADD };

			Acl acl = new Acl(principal, perm);
			acl = acl.deletePermission("*", Acl.ADD);
			boolean passed = false;

			if (acl.getPermissions(principal[0]) == (Acl.GET)) {
				if (acl.getPermissions(principal[1]) == (Acl.EXEC)) {
					passed = true;
				}
			}
			assertThat(passed).as("Asserts if '*' revokes permissions from all principals.").isTrue();

		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

}
