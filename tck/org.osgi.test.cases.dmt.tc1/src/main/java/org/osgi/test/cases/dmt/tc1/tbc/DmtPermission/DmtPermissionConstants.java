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
 * 34            [MEGTCK][DMT] CVS update 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtPermission;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * @generalDescription This class tests all of <code>constants<code> of DmtPermission
 */

public class DmtPermissionConstants extends DmtTestControl {
	/**
	 * This method asserts the constants values
	 */
	@Test
	public void testConstants001() {
		System.out.println("#testConstants001");
		assertThat(org.osgi.service.dmt.security.DmtPermission.ADD).as("Asserting DmtPermission.ADD").isEqualTo("Add");
		assertThat(org.osgi.service.dmt.security.DmtPermission.DELETE).as("Asserting DmtPermission.DELETE").isEqualTo("Delete");
		assertThat(org.osgi.service.dmt.security.DmtPermission.EXEC).as("Asserting DmtPermission.EXEC").isEqualTo("Exec");
		assertThat(org.osgi.service.dmt.security.DmtPermission.GET).as("Asserting DmtPermission.GET").isEqualTo("Get");
		assertThat(org.osgi.service.dmt.security.DmtPermission.REPLACE).as("Asserting DmtPermission.REPLACE").isEqualTo("Replace");
	}
	

}
