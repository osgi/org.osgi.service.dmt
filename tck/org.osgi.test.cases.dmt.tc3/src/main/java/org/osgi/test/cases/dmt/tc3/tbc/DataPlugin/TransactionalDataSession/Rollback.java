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
 * Mar 02, 2005  Luiz Felipe Guimaraes
 * 11            Implement DMT Use Cases 
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates the implementation of <code>rollback</code> method, 
 * according to MEG specification
 */
public class Rollback {

	private DmtTestControl tbc;

	public Rollback(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testRollback001();
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of rollback  
	 * to the correct plugin. 
	 * 
	 * @spec TransactionalDataSession.rollback()
	 */
	public void testRollback001() {
		DmtSession session = null;
		try {
			System.out.println("#testRollback001");

			session = tbc.getDmtAdmin().getSession(
					TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			TestDataPlugin.setRollbackThrowsException(true);
			session.rollback();
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.ROLLBACK_FAILED);
			if (e.getCause() instanceof DmtException) {
				DmtException exception = (DmtException)e.getCause();
				assertThat(exception.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree (null)").isNull();
				assertThat(exception.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.ALERT_NOT_ROUTED);
				assertThat(exception.getMessage().indexOf(
										TestDataPlugin.ROLLBACK) > -1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
				
			} else {
				DmtTestControl.failExpectedOtherException(DmtException.class, e.getCause());
			}
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
			TestDataPlugin.setRollbackThrowsException(false);
		}

	}
}
