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
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * This class tests DmtExpcetion constants according to MEG specification
 */

public class DmtExceptionConstants extends DmtTestControl {
	/**
	 * Tests if constants contains the specified value
	 * 
	 * @spec 117.12.6 
	 */
	@Test
	public void testConstants001(){
		System.out.println("#testConstants001");
		assertThat(org.osgi.service.dmt.DmtException.ALERT_NOT_ROUTED).as("Asserts DmtException.ALERT_NOT_ROUTED").isEqualTo(5);
		assertThat(org.osgi.service.dmt.DmtException.COMMAND_FAILED).as("Asserts DmtException.COMMAND_FAILED").isEqualTo(500);
		assertThat(org.osgi.service.dmt.DmtException.COMMAND_NOT_ALLOWED).as("Asserts DmtException.COMMAND_NOT_ALLOWED").isEqualTo(405);
		assertThat(org.osgi.service.dmt.DmtException.CONCURRENT_ACCESS).as("Asserts DmtException.CONCURRENT_ACCESS").isEqualTo(4);
		assertThat(org.osgi.service.dmt.DmtException.DATA_STORE_FAILURE).as("Asserts DmtException.DATA_STORE_FAILURE").isEqualTo(510);
		assertThat(org.osgi.service.dmt.DmtException.FEATURE_NOT_SUPPORTED).as("Asserts DmtException.FEATURE_NOT_SUPPORTED").isEqualTo(406);
		assertThat(org.osgi.service.dmt.DmtException.INVALID_URI).as("Asserts DmtException.INVALID_URI").isEqualTo(3);
		assertThat(org.osgi.service.dmt.DmtException.METADATA_MISMATCH).as("Asserts DmtException.METADATA_MISMATCH").isEqualTo(2);
		assertThat(org.osgi.service.dmt.DmtException.NODE_ALREADY_EXISTS).as("Asserts DmtException.NODE_ALREADY_EXISTS").isEqualTo(418);
		assertThat(org.osgi.service.dmt.DmtException.NODE_NOT_FOUND).as("Asserts DmtException.NODE_NOT_FOUND").isEqualTo(404);
		assertThat(org.osgi.service.dmt.DmtException.PERMISSION_DENIED).as("Asserts DmtException.PERMISSION_DENIED").isEqualTo(425);
		assertThat(org.osgi.service.dmt.DmtException.REMOTE_ERROR).as("Asserts DmtException.REMOTE_ERROR").isEqualTo(1);
		assertThat(org.osgi.service.dmt.DmtException.ROLLBACK_FAILED).as("Asserts DmtException.ROLLBACK_FAILED").isEqualTo(516);
		assertThat(org.osgi.service.dmt.DmtException.SESSION_CREATION_TIMEOUT).as("Asserts DmtException.SESSION_CREATION_TIMEOUT").isEqualTo(7);
		assertThat(org.osgi.service.dmt.DmtException.TRANSACTION_ERROR).as("Asserts DmtException.TRANSACTION_ERROR").isEqualTo(6);
		assertThat(org.osgi.service.dmt.DmtException.URI_TOO_LONG).as("Asserts DmtException.URI_TOO_LONG").isEqualTo(414);
		assertThat(org.osgi.service.dmt.DmtException.UNAUTHORIZED).as("Asserts DmtException.UNAUTHORIZED").isEqualTo(401);
	}

}
