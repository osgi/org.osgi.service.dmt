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
import org.osgi.test.cases.dmt.tc3.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc3.tbc.Plugins.NewDataPluginActivator;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates the implementation of <code>commit</code> method, 
 * according to MEG specification
 */
public class Commit {

	private DmtTestControl tbc;

	public Commit(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testCommit001();
        testCommit002();
	}

	/**
	 * Asserts that DmtAdmin correctly forwards the call of commit  
	 * to the correct plugin.
	 * 
	 * @spec TransactionalDataSession.commit()
	 */
	public void testCommit001() {
		DmtSession session = null;
		try {
			System.out.println("#testCommit001");
			session = tbc.getDmtAdmin().getSession(
					TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			TestDataPlugin.setCommitThrowsException(true);
			session.commit();
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that TRANSACTION_ERROR is thrown if an underlying plugin failed to commit:").isEqualTo(DmtException.TRANSACTION_ERROR);

			if (e.getCause() instanceof DmtException) {
				DmtException exception = (DmtException)e.getCause();
				assertThat(exception.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree (null)").isNull();
				assertThat(exception.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.COMMAND_FAILED);
				assertThat(exception.getMessage().indexOf(
										TestDataPlugin.COMMIT) > -1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
				
			} else {
				DmtTestControl.failExpectedOtherException(DmtException.class, e.getCause());
			}
				
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
			TestDataPlugin.setCommitThrowsException(false);
		}
	}
    /**
     * Asserts that if plugin A has committed successfully but plugin B failed, the whole session must fail
     * 
     * @spec TransactionalDataSession.commit()
     */
    public void testCommit002() {
        DmtSession session = null;
        try {
            System.out.println("#testCommit002");
            
            session = tbc.getDmtAdmin().getSession(
                    DmtConstants.OSGi_ROOT,
                    DmtSession.LOCK_TYPE_ATOMIC);
            //NewDataPlugin does not throw exceptions at commit, but TestDataPlugin does
            session.getChildNodeNames(TestDataPluginActivator.ROOT);
            session.getChildNodeNames(NewDataPluginActivator.ROOT);
            TestDataPlugin.setCommitThrowsException(true);
            session.commit();
            DmtTestControl.failException("#", DmtException.class);
        } catch (DmtException e) {
            assertThat(e.getCode()).as("Asserts that TRANSACTION_ERROR is thrown if an underlying plugin failed to commit:").isEqualTo(DmtException.TRANSACTION_ERROR);
            assertThat(session.getState()).as("Asserts that if plugin A has committed successfully but plugin B failed, the whole session must fail").isEqualTo(DmtSession.STATE_INVALID);
            
                
        } catch (Exception e) {
        	DmtTestControl.failExpectedOtherException(DmtException.class, e);
        } finally {
            tbc.cleanUp(session,true);
            TestDataPlugin.setCommitThrowsException(false);
        }
    }

}
