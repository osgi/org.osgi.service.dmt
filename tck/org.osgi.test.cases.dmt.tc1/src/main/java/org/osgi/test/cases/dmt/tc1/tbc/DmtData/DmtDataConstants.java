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
 * 26/01/2005   Andre Assad
 * 1         	Implement TCK
 * ===========  ==============================================================
*/

package org.osgi.test.cases.dmt.tc1.tbc.DmtData;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.DmtData;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * This Test Case Validates the values of all constants> of DmtData, according to MEG specification
 */
public class DmtDataConstants extends DmtTestControl {
	/**
	 * This method asserts the constants values
	 * 
	 * @spec 117.12.5
	 */
	@Test
	public void testConstants001() {
		System.out.println("#testConstants001");
		assertThat(DmtData.FORMAT_BASE64).as("Asserting FORMAT_BASE64 value").isEqualTo(128);
		assertThat(DmtData.FORMAT_BINARY).as("Asserting FORMAT_BINARY value").isEqualTo(64);
		assertThat(DmtData.FORMAT_BOOLEAN).as("Asserting FORMAT_BOOLEAN value").isEqualTo(8);
		assertThat(DmtData.FORMAT_DATE).as("Asserting FORMAT_DATE value").isEqualTo(16);
		assertThat(DmtData.FORMAT_FLOAT).as("Asserting FORMAT_FLOAT value").isEqualTo(2);
		assertThat(DmtData.FORMAT_INTEGER).as("Asserting FORMAT_INTEGER value").isOne();
		assertThat(DmtData.FORMAT_NODE).as("Asserting FORMAT_NODE value").isEqualTo(1024);
		assertThat(DmtData.FORMAT_NULL).as("Asserting FORMAT_NULL value").isEqualTo(512);
		assertThat(DmtData.FORMAT_STRING).as("Asserting FORMAT_STRING value").isEqualTo(4);
		assertThat(DmtData.FORMAT_XML).as("Asserting FORMAT_XML value").isEqualTo(256);
		assertThat(DmtData.FORMAT_RAW_BINARY).as("Asserting FORMAT_RAW_BINARY value").isEqualTo(4096);
		assertThat(DmtData.FORMAT_RAW_STRING).as("Asserting FORMAT_RAW_STRING value").isEqualTo(2048);

	}
}
