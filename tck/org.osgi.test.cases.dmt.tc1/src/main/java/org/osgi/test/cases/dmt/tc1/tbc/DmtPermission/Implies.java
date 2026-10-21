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
 * Abr 04, 2005  Luiz Felipe Guimaraes
 * 13            [MEGTCK][DMT] CVS update 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtPermission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.security.AlertPermission;
import org.osgi.service.dmt.security.DmtPermission;
import org.osgi.test.cases.dmt.tc1.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * This test case validates the implementation of <code>implies</code> method of DmtPermission, 
 * according to MEG specification
 */
public class Implies extends DmtTestControl {
	/**
	 * This method asserts that an object implies other object using the same uri actions
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies001() {
		try {
			System.out.println("#testImplies001");
			DmtPermission permission = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtConstants.ACTIONS);
			DmtPermission permission2 = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtConstants.ACTIONS);
			assertThat(permission.implies(permission2)).as("Asserts that an object implies other object using the same uri and actions").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an object implies other object using the same uri and the actions it contains
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies002() {
		try {
			System.out.println("#testImplies002");
			DmtPermission permission = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD + ","
							+ DmtPermission.GET);
			DmtPermission permission2 = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD);
			assertThat(permission.implies(permission2)).as("Asserts that an object implies other object using the same uri and the actions it contains").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an object doesn't imply other object using 
	 * the same uri and the actions it doesn't contain
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies003() {
		try {
			System.out.println("#testImplies003");
			DmtPermission permission = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD);
			DmtPermission permission2 = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD + ","
							+ DmtPermission.GET);
			assertThat(!permission.implies(permission2)).as("Asserts that an object doesn't imply other object using the same uri and the actions it doesn't contain").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an object doesn't imply other object using different uris and the same actions
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies004() {
		try {
			System.out.println("#testImplies004");
			DmtPermission permission = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD);
			DmtPermission permission2 = new DmtPermission(
					DmtConstants.OSGi_CONFIGURATION, DmtPermission.ADD);
			assertThat(!permission.implies(permission2)).as("Asserts that an object implies other object using the same uri and the same actions").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}

	/**
	 * Asserts that an object imply other object using the same uri and actions but in a different order
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies005() {
		try {
			System.out.println("#testImplies005");
			DmtPermission permission = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.ADD + ","
							+ DmtPermission.REPLACE);
			DmtPermission permission2 = new DmtPermission(
					DmtConstants.OSGi_LOG, DmtPermission.REPLACE + ","
							+ DmtPermission.ADD);
			assertThat(permission.implies(permission2)).as("Asserts that an object imply other object using the same uri and actions but in a different order").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}
    
    /**
     * Asserts that an object implies other object when using wildcard to match the uri
     * 
     * @spec DmtPermission.implies(Permission)
     */
    @Test
    public void testImplies006() {
        try {
            System.out.println("#testImplies006");
            DmtPermission permission = new DmtPermission(
            		DmtConstants.OSGi_CONFIGURATION + "/a*", DmtPermission.ADD);
            DmtPermission permission2 = new DmtPermission(
            		DmtConstants.OSGi_CONFIGURATION + "/abc", DmtPermission.ADD);
            assertThat(permission.implies(permission2)).as("Asserts that an object implies other object when using wildcard to match the uri").isTrue();
        } catch (Exception e) {
            failUnexpectedException(e);
        }
    }
	/**
	 * Asserts that an object doesn't imply other object if they are from different types
	 * 
	 * @spec DmtPermission.implies(Permission)
	 */
	@Test
	public void testImplies007() {
		try {
			System.out.println("#testImplies007");
			DmtPermission permission = new DmtPermission("./*","*");
			AlertPermission permission2 = new AlertPermission("./*","*");
			assertThat(!permission.implies(permission2)).as("Asserts that an object doesn't imply other object if they are from different types").isTrue();
		} catch (Exception e) {
			failUnexpectedException(e);
		}
	}
    
    /**
     * Asserts that a permission with "*" uri implies a permission with "." uri
     * 
     * @spec DmtPermission.implies(Permission)
     */
    @Test
    public void testImplies008() {
        try {
            System.out.println("#testImplies008");
            DmtPermission permission = new DmtPermission("*",DmtConstants.ALL_ACTIONS);
            DmtPermission permission2 = new DmtPermission(".", "Get");
            assertThat(permission.implies(permission2)).as("Asserts that the \"*\" implies \".\"").isTrue();
        } catch (Exception e) {
            failUnexpectedException(e);
        }
    }
}
