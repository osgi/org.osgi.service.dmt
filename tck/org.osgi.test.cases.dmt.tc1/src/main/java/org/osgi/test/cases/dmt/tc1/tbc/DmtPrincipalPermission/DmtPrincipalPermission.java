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
 * Jan 26, 2005  Leonardo Barros
 * 1             Implement TCK
 * ============  ==============================================================
 * Feb 14, 2005  Luiz Felipe Guimaraes
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-002)
 * ============  ==============================================================
 * Feb 14, 2005  Luiz Felipe Guimaraes
 * 34            [MEGTCK][DMT] CVS update 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtPrincipalPermission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.test.cases.dmt.tc1.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * This class tests DmtPrincipalPermission constructors according with MEG specification
 * 
 */

public class DmtPrincipalPermission extends DmtTestControl {

	/**
	 * It asserts if the principal passed in the constructor is
	 * equals to DmtPrincipalPermission.getName() method.
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String)
	 */
	@Test
	public void testDmtPrincipalPermission001() {
		try {
			System.out.println("#testDmtPrincipalPermission001");
			org.osgi.service.dmt.security.DmtPrincipalPermission dmtPrincipalPermission = new org.osgi.service.dmt.security.DmtPrincipalPermission(
					DmtConstants.PRINCIPAL);
			assertThat(dmtPrincipalPermission
									.getName()).as("Asserts that the principal passed as parameter is equal to the returned value").isEqualTo(DmtConstants.PRINCIPAL);
			assertThat(dmtPrincipalPermission.getActions()).as("Asserts that the actions returned is '*'").isEqualTo("*");
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that '*' is returned when it is passed as principal.
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String)
	 */
	@Test
	public void testDmtPrincipalPermission002() {
		try {
			System.out.println("#testDmtPrincipalPermission002");
			org.osgi.service.dmt.security.DmtPrincipalPermission dmtPrincipalPermission = new org.osgi.service.dmt.security.DmtPrincipalPermission(
					"*");
			assertThat(dmtPrincipalPermission.getName()).as("Asserts that the principal passed as parameter is equal to the returned value.").isEqualTo("*");
			assertThat(dmtPrincipalPermission.getActions()).as("Asserts that the actions returned is '*'.").isEqualTo("*");
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * It asserts if the principal passed in the constructor with 2 arguments is
	 * equals to DmtPrincipalPermission.getName() method.
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission003() {
		try {
			System.out.println("#testDmtPrincipalPermission003");
			org.osgi.service.dmt.security.DmtPrincipalPermission dmtPrincipalPermission = new org.osgi.service.dmt.security.DmtPrincipalPermission(
					DmtConstants.PRINCIPAL, "*");
			assertThat(dmtPrincipalPermission
									.getName()).as("Asserts that the principal passed as parameter is equal to the returned value").isEqualTo(DmtConstants.PRINCIPAL);
			assertThat(dmtPrincipalPermission.getActions()).as("Asserts that the actions passed as parameter is equal to the returned value").isEqualTo("*");
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that NullPointerException is thrown if name is null constructor with 1 parameter
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String)
	 */
	@Test
	public void testDmtPrincipalPermission004() {
		try {
			System.out.println("#testDmtPrincipalPermission004");
			new org.osgi.service.dmt.security.DmtPrincipalPermission(null);
			failException("#", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(NullPointerException.class, e);
		}
	}

	/**
	 * Asserts that IllegalArgumentException is thrown if name is empty constructor with 1 parameter
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String)
	 */
	@Test
	public void testDmtPrincipalPermission005() {
		try {
			System.out.println("#testDmtPrincipalPermission005");
			new org.osgi.service.dmt.security.DmtPrincipalPermission("");
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that NullPointerException is thrown if name is null constructor with 2 parameter
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission006() {
		try {
			System.out.println("#testDmtPrincipalPermission006");
			new org.osgi.service.dmt.security.DmtPrincipalPermission(null, "*");
			failException("#", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(NullPointerException.class, e);
		}
	}

	/**
	 * Asserts that IllegalArgumentException is thrown if name is empty constructor with 2 parameter
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission007() {
		try {
			System.out.println("#testDmtPrincipalPermission007");
			new org.osgi.service.dmt.security.DmtPrincipalPermission("", "*");
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that NullPointerException is thrown if actions is null
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission008() {
		try {
			System.out.println("#testDmtPrincipalPermission008");
			new org.osgi.service.dmt.security.DmtPrincipalPermission(
					DmtConstants.PRINCIPAL, null);
			failException("#", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(NullPointerException.class, e);
		}
	}

	/**
	 * Asserts that IllegalArgumentException is thrown if actions is not "*"
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission009() {
		try {
			System.out.println("#testDmtPrincipalPermission009");
			new org.osgi.service.dmt.security.DmtPrincipalPermission(
					DmtConstants.PRINCIPAL, "");
			failException("#", IllegalArgumentException.class);
		} catch (IllegalArgumentException e) {
			System.out.println("IllegalArgumentException correctly thrown");
		} catch (Exception e) {
			failExpectedOtherException(IllegalArgumentException.class, e);
		}
	}

	/**
	 * Asserts that the name of the principal can end with "*" to match any principal
	 * using the constructor with 1 parameter
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String)
	 */
	@Test
	public void testDmtPrincipalPermission010() {
		try {
			System.out.println("#testDmtPrincipalPermission010");
			String principal = "www.cesar*";
			org.osgi.service.dmt.security.DmtPrincipalPermission dmtPrincipalPermission = new org.osgi.service.dmt.security.DmtPrincipalPermission(
					principal);
			assertThat(dmtPrincipalPermission.getName()).as("Asserts that the principal passed as parameter is equal to the returned value").isEqualTo(principal);
			assertThat(dmtPrincipalPermission.getActions()).as("Asserts that the actions returned is '*'").isEqualTo("*");
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that the name of the principal can end with "*" to match any principal
	 * using the constructor with 2 parameters
	 * 
	 * @spec DmtPrincipalPermission.DmtPrincipalPermission(String,String)
	 */
	@Test
	public void testDmtPrincipalPermission011() {
		try {
			System.out.println("#testDmtPrincipalPermission011");
			String principal = "www.cesar*";
			org.osgi.service.dmt.security.DmtPrincipalPermission dmtPrincipalPermission = new org.osgi.service.dmt.security.DmtPrincipalPermission(
					principal, "*");
			assertThat(dmtPrincipalPermission.getName()).as("Asserts that the principal passed as parameter is equal to the returned value").isEqualTo(principal);
			assertThat(dmtPrincipalPermission.getActions()).as("Asserts that the actions returned is '*'").isEqualTo("*");
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}
}
