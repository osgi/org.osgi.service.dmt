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

package org.osgi.test.cases.dmt.tc1.tbc.Acl;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.Acl;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * @author Andre Assad
 *
 * This test case validates all <code>constants</code> of Acl, according to MEG specification
 * 
 */
public class AclConstants extends DmtTestControl {

	/**
	 * Tests all constants values according to the specified values
	 */
	@Test
	public void testConstants001() {
		System.out.println("#testConstants001");
		assertThat(Acl.GET).as("Asserting GET value").isOne();
		assertThat(Acl.ADD).as("Asserting ADD value").isEqualTo(2);
		assertThat(Acl.REPLACE).as("Asserting REPLACE value").isEqualTo(4);
		assertThat(Acl.DELETE).as("Asserting DELETE value").isEqualTo(8);
		assertThat(Acl.EXEC).as("Asserting EXEC value").isEqualTo(16);
		assertThat(Acl.ALL_PERMISSION).as("Asserting ALL_PERMISSION 	 value").isEqualTo(31);
	}
}
