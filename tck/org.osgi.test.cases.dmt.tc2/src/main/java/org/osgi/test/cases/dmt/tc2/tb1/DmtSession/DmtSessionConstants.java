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
 * Feb 15, 2005  Alexandre Santos
 * 1             Implements TCK
 * ============  ==============================================================
 * Feb 28, 2005  Luiz Felipe Guimaraes
 * 1             Updates 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.DmtSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * This class tests all constants of DmtSession
 *                     
 * @author Alexandre Santos
 */

public class DmtSessionConstants implements TestInterface {
    
	@SuppressWarnings("unused")
	private DmtTestControl tbc;

    public DmtSessionConstants(DmtTestControl tbc) {
        this.tbc = tbc;
    }

    @Override
	public void run() {
        testConstants001();
    }
    
    /**
     * This method asserts the constants values 
     * 
     * @spec 118.12.16
     */    
    private void testConstants001() {
		System.out.println("#testConstants001");
		assertThat(DmtSession.LOCK_TYPE_SHARED).as("Asserting LOCK_TYPE_SHARED value").isZero();
		assertThat(DmtSession.LOCK_TYPE_EXCLUSIVE).as("Asserting LOCK_TYPE_EXCLUSIVE value").isOne();
        assertThat(DmtSession.LOCK_TYPE_ATOMIC).as("Asserting LOCK_TYPE_ATOMIC value").isEqualTo(2);
		assertThat(DmtSession.STATE_OPEN).as("Asserting STATE_OPEN value").isZero();
		assertThat(DmtSession.STATE_CLOSED).as("Asserting STATE_CLOSED value").isOne();
        assertThat(DmtSession.STATE_INVALID).as("Asserting STATE_INVALID value").isEqualTo(2);
    }   
    

}
