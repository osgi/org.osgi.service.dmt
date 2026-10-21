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
 * Mar 09, 2005  Luiz Felipe Guimaraes
 * 1             Implement MEG TCK
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.MetaNode;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.MetaNode;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * Tests all constants of MetaNode
 */

public class DmtMetaNodeConstants {
	@SuppressWarnings("unused")
	private DmtTestControl tbc;

	public DmtMetaNodeConstants(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testConstants001();
	}
	
	/**
	 * Tests all constants of MetaNode
	 * 
	 * @spec 117.12.8
	 */
	public void testConstants001(){
		System.out.println("#testConstants001");
		assertThat(MetaNode.CMD_ADD).as("Asserts MetaNode.CMD_ADD").isZero();
		assertThat(MetaNode.CMD_DELETE).as("Asserts MetaNode.CMD_DELETE").isOne();
		assertThat(MetaNode.CMD_EXECUTE).as("Asserts MetaNode.CMD_EXECUTE").isEqualTo(2);
		assertThat(MetaNode.CMD_GET).as("Asserts MetaNode.CMD_GET").isEqualTo(4);
		assertThat(MetaNode.CMD_REPLACE).as("Asserts MetaNode.CMD_REPLACE").isEqualTo(3);
		assertThat(MetaNode.AUTOMATIC).as("Asserts MetaNode.AUTOMATIC").isEqualTo(2);
		assertThat(MetaNode.DYNAMIC).as("Asserts MetaNode.DYNAMIC").isOne();
		assertThat(MetaNode.PERMANENT).as("Asserts MetaNode.PERMANENT").isZero();
	}

}
