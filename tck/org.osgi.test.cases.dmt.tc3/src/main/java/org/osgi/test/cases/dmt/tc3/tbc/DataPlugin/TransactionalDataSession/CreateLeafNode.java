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
 * ===========  ==============================================================
 * 28/02/2005   Andre Assad
 * 11           Implement DMT Use Cases 
 * ===========  ==============================================================
 */
package org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TransactionalDataSession;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;


/**
 * @author Andre Assad
 * 
 * This test case validates the implementation of <code>createLeafNode</code> method, 
 * according to MEG specification
 */
public class CreateLeafNode {
	private DmtTestControl tbc;

	public CreateLeafNode(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
 		testCreateLeafNode001();
		testCreateLeafNode002();
		testCreateLeafNode003();
		testCreateLeafNode004();
		testCreateLeafNode005();
		testCreateLeafNode006();
	}

	
	/**
	 * Asserts that DmtAdmin correctly forwards the call of createLeafNode to the correct plugin,
     * using DmtSession.createLeafNode(String)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode001() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode001");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.CREATELEAFNODE+" to the correct plugin").isEqualTo(TestDataPlugin.CREATELEAFNODE);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}	

	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin,
     * using DmtSession.createLeafNode(String)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode002() {
		DmtSession session = null;
		try {
			System.out.println("#testCreateLeafNode002");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION);
			
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.INVALID_URI);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.CREATELEAFNODE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
	/**
	 * Asserts that DmtAdmin correctly forwards the call of createLeafNode to the correct plugin,
     * using DmtSession.createLeafNode(String,DmtData)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode003() {
		DmtSession session = null;
		DmtData data = new DmtData(8);
		try {
			System.out.println("#testCreateLeafNode003");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE,data);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.CREATELEAFNODE+" to the correct plugin").isEqualTo(TestDataPlugin.CREATELEAFNODE);
			assertThat(DmtConstants.PARAMETER_2).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(data.toString());
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}	

	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin,
     * using DmtSession.createLeafNode(String,DmtData)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode004() {
		DmtSession session = null;
		DmtData data = new DmtData(5);
		try {
			System.out.println("#testCreateLeafNode004");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION,data);
			
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.INVALID_URI);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.CREATELEAFNODE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
	/**
	 * Asserts that DmtAdmin correctly forwards the call of createLeafNode to the correct plugin,
     * using DmtSession.createLeafNode(String,DmtData,String)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode005() {
		DmtSession session = null;
		DmtData data = new DmtData(8);
		String mimeType = "text/xml";
		try {
			System.out.println("#testCreateLeafNode005");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE,data,mimeType);
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin fowarded "+ TestDataPlugin.CREATELEAFNODE+" to the correct plugin").isEqualTo(TestDataPlugin.CREATELEAFNODE);
			assertThat(DmtConstants.PARAMETER_2).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(data.toString());
			assertThat(DmtConstants.PARAMETER_3).as("Asserts that DmtAdmin the parameter was fowarded to the correct plugin without modification").isEqualTo(mimeType);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session,true);
		}	

	}

	/**
	 * Asserts that DmtAdmin correctly forwards the DmtException thrown by the plugin,
     * using DmtSession.createLeafNode(String,DmtData,String)
	 * 
	 * @spec ReadWriteDataSession.createLeafNode(String[],DmtData,String)
	 */
	public void testCreateLeafNode006() {
		DmtSession session = null;
		DmtData data = new DmtData(5);
		String mimeType = "text/xml";
		try {
			System.out.println("#testCreateLeafNode006");
			session = tbc.getDmtAdmin().getSession(TestDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			session.createLeafNode(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION,data,mimeType);
			
			DmtTestControl.failException("#", DmtException.class);
		} catch (DmtException e) {
			assertThat(e
					.getURI()).as("Asserts that DmtAdmin fowarded the DmtException with the correct subtree: ").isEqualTo(TestDataPluginActivator.INEXISTENT_LEAF_NODE_EXCEPTION);			
			assertThat(e
					.getCode()).as("Asserts that DmtAdmin fowarded the DmtException with the correct code: ").isEqualTo(DmtException.INVALID_URI);
			assertThat(e
					.getMessage().indexOf(TestDataPlugin.CREATELEAFNODE)>-1).as("Asserts that DmtAdmin fowarded the DmtException with the correct message. ").isTrue();
		} catch (Exception e) {
			DmtTestControl.failExpectedOtherException(DmtException.class, e);
		} finally {
			tbc.cleanUp(session,true);
		}
	}
}
