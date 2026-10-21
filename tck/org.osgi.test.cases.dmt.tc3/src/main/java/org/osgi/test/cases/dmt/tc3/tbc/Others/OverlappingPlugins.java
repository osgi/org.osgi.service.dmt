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
 * Feb 25, 2005  Luiz Felipe Guimaraes
 * 11            Implemente MEG TCK
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc3.tbc.Others;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.DmtSession;
import org.osgi.test.cases.dmt.tc3.tbc.DataPlugin.TestDataPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc3.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc3.tbc.ExecPlugin.TestExecPlugin;
import org.osgi.test.cases.dmt.tc3.tbc.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.Plugins.OverlappingDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.Plugins.OverlappingExecPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.Plugins.OverlappingSubtreeDataPluginActivator;
import org.osgi.test.cases.dmt.tc3.tbc.Plugins.ToBeOverlappedDataPlugin;


/**
 * @author Luiz Felipe Guimaraes
 * 
 * This test case validates that overlappings plugins are not allowed. 
 * (Only ExecPlugin is allowed to overlap a DataPlugin)
 */
public class OverlappingPlugins {

	private DmtTestControl	tbc;

	public OverlappingPlugins(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testOverlappingPlugins001();
		testOverlappingPlugins002();
		testOverlappingPlugins003();
		testOverlappingPlugins004();
	}

	/**
	 * Asserts that a DataPlugin cannot be overlapped by another DataPlugin.
	 * 
	 * @spec 117.6 Plugins
	 */
	public void testOverlappingPlugins001() {
		DmtSession session = null;
		try {
			System.out.println("#testOverlappingPlugins001");

			session = tbc.getDmtAdmin().getSession(
					OverlappingDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			String nodeTitle = session.getNodeTitle(OverlappingDataPluginActivator.ROOT);
			assertThat(nodeTitle).as("Asserts that a DataPlugin cannot be overlapped by other DataPlugin").isEqualTo(TestDataPlugin.GETNODETITLE);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session, true);
		}
	}
	/**
	 * Asserts that a it is not possible for a plugin to control the same, 
	 * or part of the same, subtree that another plugins controls.
	 * 
	 * @spec 117.6 Plugins
	 */
	public void testOverlappingPlugins002() {
		DmtSession session = null;
		try {
			System.out.println("#testOverlappingPlugins002");
			session = tbc.getDmtAdmin().getSession(
					OverlappingSubtreeDataPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_ATOMIC);
			String nodeTitle = session.getNodeTitle(OverlappingSubtreeDataPluginActivator.ROOT);
			assertThat(nodeTitle).as("Asserts that a it is not possible for a plugin to control the same, or part of the same, "
									+
					"subtree that another plugins controls.").isEqualTo(TestDataPlugin.GETNODETITLE);
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session, true);

		}
	}
	/**
	 * Asserts that an ExecPlugin can overlap a DataPlugin.
	 * 
	 * @spec 117.6 Plugins
	 */
	public void testOverlappingPlugins003() {
		DmtSession session = null;
		try {
			System.out.println("#testOverlappingPlugins003");

			session = tbc.getDmtAdmin().getSession(
					TestExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			String nodeTitle = session.getNodeTitle(TestExecPluginActivator.ROOT);
			assertThat(nodeTitle).as("Asserts that DmtAdmin allows that an ExecPlugin overlaps a DataPlugin").isEqualTo(ToBeOverlappedDataPlugin.MESSAGE);
			
			session.execute(TestExecPluginActivator.ROOT, "");
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin allows that an ExecPlugin overlaps a DataPlugin").isEqualTo(TestExecPlugin.EXECUTE);
			
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session, true);
		}
	}
	
	/**
	 * Asserts that an ExecPlugin cannot overlap another ExecPlugin.
	 * 
	 * @spec 117.6 Plugins
	 */
	public void testOverlappingPlugins004() {
		DmtSession session = null;
		try {
			System.out.println("#testOverlappingPlugins004");

			session = tbc.getDmtAdmin().getSession(
					OverlappingExecPluginActivator.ROOT,
					DmtSession.LOCK_TYPE_EXCLUSIVE);
			String nodeTitle = session.getNodeTitle(OverlappingExecPluginActivator.ROOT);
			assertThat(nodeTitle).as("Asserts that DmtAdmin does not allow that an ExecPlugin overlaps another ExecPlugin").isEqualTo(ToBeOverlappedDataPlugin.MESSAGE);
			
			session.execute(OverlappingExecPluginActivator.ROOT, "");
			assertThat(DmtConstants.TEMPORARY).as("Asserts that DmtAdmin does not allow that an ExecPlugin overlaps another ExecPlugin").isEqualTo(TestExecPlugin.EXECUTE);
			
		} catch (Exception e) {
			DmtTestControl.failUnexpectedException(e);
		} finally {
			tbc.cleanUp(session, true);
		}
	}
}
