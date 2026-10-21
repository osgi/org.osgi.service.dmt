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
 * Apr 17, 2006  Luiz Felipe Guimaraes
 * 283           [MEGTCK][DMT] DMT API changes
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtEvent;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtEvent;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;




/**
 * @author Luiz Felipe Guimaraes
 * 
 * This Class Validates the implementation of <code>DmtEvent<code> constants, 
 * according to MEG specification
 */
public class DmtEventConstants implements TestInterface {
	private DmtTestControl tbc;
	
	public DmtEventConstants(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
		testDmtEventConstants001();
	}

	/**
	 * Asserts the DmtEvent constants
	 * 
	 * @spec 117.13.5
	 */
	private void testDmtEventConstants001() {

		try {
			System.out.println("#testDmtEventConstants001");
			assertThat(DmtEvent.ADDED).as("Asserting DmtEvent.ADDED constant value").isOne();
			assertThat(DmtEvent.COPIED).as("Asserting DmtEvent.COPIED constant value").isEqualTo(2);
			assertThat(DmtEvent.DELETED).as("Asserting DmtEvent.DELETED constant value").isEqualTo(4);
			assertThat(DmtEvent.RENAMED).as("Asserting DmtEvent.RENAMED constant value").isEqualTo(8);
			assertThat(DmtEvent.REPLACED).as("Asserting DmtEvent.REPLACED constant value").isEqualTo(16);
			assertThat(DmtEvent.SESSION_OPENED).as("Asserting DmtEvent.SESSION_OPENED constant value").isEqualTo(32);
			assertThat(DmtEvent.SESSION_CLOSED).as("Asserting DmtEvent.SESSION_CLOSED constant value").isEqualTo(64);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		}
	
	}
	
		
}

