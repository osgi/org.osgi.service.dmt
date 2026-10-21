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
 * 05/07/2005   Luiz Felipe Guimaraes
 * 1            Implement TCK
 * ===========  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.AlertPermission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.test.cases.dmt.tc1.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * 
 * This test case validates the implementation of <code>equals<code> method of AlerPermission, 
 * according to MEG specification
 */
public class Equals extends DmtTestControl {
	/**
	 * Asserts that two AlertPermission instances are equal if they have the same target string
	 * 
	 * @spec AlertPermission.equals(Object)
	 */
	@Test
	public void testEquals001() {
		try {		
			System.out.println("#testEquals001");
			org.osgi.service.dmt.security.AlertPermission permission = new org.osgi.service.dmt.security.AlertPermission(DmtConstants.REMOTE_SERVER);
			org.osgi.service.dmt.security.AlertPermission permission2 = new org.osgi.service.dmt.security.AlertPermission(DmtConstants.REMOTE_SERVER);
			assertThat(permission.equals(permission2)).as("Asserts that two AlertPermission instances are equal if they have the same target string").isTrue();
		} catch(Exception e) {
			failUnexpectedException(e);
		}
			
	}
	
	/**
	 * Asserts that two AlertPermission instances are different if they have different target strings
	 * 
	 * @spec AlertPermission.equals(Object)
	 */
	@Test
	public void testEquals002() {
		try {		
			System.out.println("#testEquals002");
			org.osgi.service.dmt.security.AlertPermission permission = new org.osgi.service.dmt.security.AlertPermission(DmtConstants.REMOTE_SERVER);
			org.osgi.service.dmt.security.AlertPermission permission2 = new org.osgi.service.dmt.security.AlertPermission(DmtConstants.REMOTE_SERVER + "a");
			assertThat(!permission.equals(permission2)).as("Asserts that two AlertPermission instances are different if they have different target strings").isTrue();
		} catch(Exception e) {
			failUnexpectedException(e);
		}
			
	}
	
	
}
