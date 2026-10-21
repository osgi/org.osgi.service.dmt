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
 * Feb 16, 2005  Andre Assad
 * 11		     Implement DMT Use Cases
 * ============  =================================================================
 */
package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * This test case validates the implementation of <code>close</code> method, 
 * according to MEG specification
 */
public class Close {
	private DmtTestControl tbc;
	
	public Close(DmtTestControl tbc) {
		this.tbc = tbc;
	}
	public void run() {
		testClose001();
		testClose002();
	}
	
	/**
	 * Asserts that COMMAND_FAILED is thrown if an underlying plugin failed to close
	 * 
	 * @spec ReadableDataSession.close()
	 */
	public void testClose001() {
		DmtSession session = null;
		try {
			System.out.println("#testClose001");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			TestDataPlugin.setCloseThrowsException(true);
			session.close();
			DmtTestControl.failException("",DmtException.class);

		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that COMMAND_FAILED is thrown if an underlying plugin failed to close:").isEqualTo(DmtException.COMMAND_FAILED);
		
			if (e.getCause() instanceof DmtException) {
				DmtException exception = (DmtException)e.getCause();
				assertThat(exception.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree").isNull();
				assertThat(exception.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.CONCURRENT_ACCESS);
				assertThat(exception.getMessage().indexOf(
								    TestDataPlugin.CLOSE) > -1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
			}	else {
				DmtTestControl.failExpectedOtherException(DmtException.class,e.getCause());
			}		
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);	
		} finally {
		    TestDataPlugin.setCloseThrowsException(false);
		}
	}	
	/**
	 * Asserts that TRANSACTION_ERROR is thrown if an underlying plugin failed to commit
	 * 
	 * @spec ReadableDataSession.close()
	 */
	public void testClose002() {
		DmtSession session = null;
		try {
			System.out.println("#testClose002");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			TestDataPlugin.setCommitThrowsException(true);
			session.close();
			DmtTestControl.failException("",DmtException.class);

		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that TRANSACTION_ERROR is thrown if an underlying plugin failed to commit:").isEqualTo(DmtException.TRANSACTION_ERROR);
			
			if (e.getCause() instanceof DmtException) {
				DmtException exception = (DmtException)e.getCause();
				assertThat(exception.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree").isNull();
				assertThat(exception.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.COMMAND_FAILED);
				assertThat(exception.getMessage().indexOf(
								    TestDataPlugin.COMMIT) > -1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
				
			} else {
				DmtTestControl.failExpectedOtherException(DmtException.class, e.getCause());
			}			
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
		    TestDataPlugin.setCommitThrowsException(false);
		}
	}


}
