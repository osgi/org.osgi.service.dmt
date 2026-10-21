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
package org.osgi.test.cases.dmt.tc4.ext.junit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.assertj.core.api.Assertions.within;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.Acl;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.MetaNode;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.ArrayAssert;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataMountPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataPlugin;

public class ScaffoldNodeTest extends DmtAdminTestCase {

    @Override
	@BeforeEach
	public void setUp() throws Exception {
        super.setUp();
        getDmtAdmin();
    }

    @Override
	@AfterEach
	public void tearDown() throws Exception {
        unregisterPlugins();
        closeDmtSession();
        super.tearDown();
    }

    @Test
    public void testScaffoldNodeData() throws DmtException {
        TestDataPlugin dataPlugin = new TestDataPlugin();
        setInteriorNode(dataPlugin, "./A/B");

        long pluginRegistrationTime = System.currentTimeMillis();
        registerDataPlugin(dataPlugin, "./A/B");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A/B");

        MetaNode metaNode = session.getMetaNode("./A");
		assertThat(metaNode.can(MetaNode.CMD_ADD)).isFalse();
		assertThat(metaNode.can(MetaNode.CMD_DELETE)).isFalse();
		assertThat(metaNode.can(MetaNode.CMD_EXECUTE)).isFalse();
		assertThat(metaNode.can(MetaNode.CMD_GET)).isTrue();
		assertThat(metaNode.can(MetaNode.CMD_REPLACE)).isFalse();

		assertThat(metaNode.isLeaf()).isFalse();
        assertThat(metaNode.getScope()).isEqualTo(MetaNode.PERMANENT);
		assertThat(metaNode.getDescription()).isNull();
		assertThat(metaNode.getMaxOccurrence()).isOne();
		assertThat(metaNode.isZeroOccurrenceAllowed()).isTrue();
		assertThat(metaNode.getDefault()).isNull();
		assertThat(metaNode.getValidNames()).isNull();
		assertThat(metaNode.getValidValues()).isNull();
        assertThat(metaNode.getMax()).isCloseTo(Double.MAX_VALUE, within(0.0d));
        assertThat(metaNode.getMin()).isCloseTo(Double.MIN_VALUE, within(0.0d));
		assertThat(metaNode.getRawFormatNames()).isNull();
        ArrayAssert.assertEquivalenceArrays(metaNode.getMimeTypes(), new String[]{ DmtConstants.DDF_SCAFFOLD });

		assertThat(metaNode.isValidName("")).isTrue();
		assertThat(metaNode.isValidName("A")).isTrue();
		assertThat(metaNode.isValidName(".")).isTrue();
		assertThat(metaNode.isValidName("/")).isTrue();

		assertThat(metaNode.isValidValue(new DmtData(true))).isFalse();
		assertThat(metaNode.isValidValue(new DmtData(new byte[]{(byte) 0, (byte) 1, (byte) 2}))).isFalse();
		assertThat(metaNode.isValidValue(new DmtData("ABC"))).isFalse();
		assertThat(metaNode.isValidValue(new DmtData(new Object()))).isFalse();

        assertThat(metaNode.getFormat()).isEqualTo(DmtData.FORMAT_NODE);

        ArrayAssert.assertEquivalenceArrays(new String[] { "B" }, session.getChildNodeNames("./A"));
        Acl acl = session.getNodeAcl(".");
		assertThat(session.getNodeAcl("./A")).isNull();
        assertThat(session.getEffectiveNodeAcl("./A")).isEqualTo(acl);
		assertThat(session.getNodeTitle("./A")).isNull();
        assertThat(session.getNodeType("./A")).isEqualTo(DmtConstants.DDF_SCAFFOLD);
        try {
            assertThat(session.getNodeSize("./A")).isEqualTo(-1);
        } catch (DmtException dmtException) {
            assertThat(dmtException.getCode()).isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
            assertThat(dmtException.getURI()).isEqualTo("./A");
			assertThat(dmtException.getCause()).isNull();
			assertThat(dmtException.getCauses()).isEmpty();
        }
        try {
			assertThat(session.getNodeValue("./A")).isNull();
            fail("");
        } catch (DmtException dmtException) {
            assertThat(dmtException.getCode()).isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
            assertThat(dmtException.getURI()).isEqualTo("./A");
            assertThat(dmtException.getCause()).isEqualTo(null);
            assertThat(dmtException.getCauses().length).isEqualTo(0);
        }

        assertThat(session.getNodeVersion("./A")).isEqualTo(0);

        Date timeStamp = session.getNodeTimestamp("./A");
        assertThat(pluginRegistrationTime <= timeStamp.getTime()).isTrue();
        assertThat(timeStamp.getTime() <= System.currentTimeMillis()).isTrue();

        session.close();
    }

    @Test
    public void testScaffoldNodePattern01() throws DmtException {
        TestDataPlugin dataPlugin = new TestDataPlugin();
        setInteriorNode(dataPlugin, "./A/B");
		ServiceRegistration<DataPlugin> registration = registerDataPlugin(
				dataPlugin, "./A/B");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertInteriorNode("./A/B");
        session.close();

        unregister(registration);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertNodeNotFound("./A");
        assertNodeNotFound("./A/B");
        session.close();
    }

    @Test
    public void testScaffoldNodePattern02() throws DmtException {
        TestDataPlugin dataPluginB = new TestDataPlugin();
        setInteriorNode(dataPluginB, "./A/B");
		ServiceRegistration<DataPlugin> registrationB = registerDataPlugin(
				dataPluginB, "./A/B");

        TestDataPlugin dataPluginC = new TestDataPlugin();
        setInteriorNode(dataPluginC, "./A/C");
		ServiceRegistration<DataPlugin> registrationC = registerDataPlugin(
				dataPluginC, "./A/C");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertInteriorNode("./A/B");
        assertInteriorNode("./A/C");
        session.close();

        unregister(registrationB);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertNodeNotFound("./A/B");
        assertInteriorNode("./A/C");
        session.close();

        unregister(registrationC);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertNodeNotFound("./A");
        assertNodeNotFound("./A/B");
        assertNodeNotFound("./A/C");
        session.close();
    }

    @Test
    public void testScaffoldNodePattern03() throws DmtException {
        TestDataPlugin dataPluginB = new TestDataPlugin();
        setInteriorNode(dataPluginB, "./A/B");
		ServiceRegistration<DataPlugin> registrationB = registerDataPlugin(
				dataPluginB, "./A/B");

        TestDataPlugin dataPluginC = new TestDataPlugin();
        setInteriorNode(dataPluginC, "./A/C");
		ServiceRegistration<DataPlugin> registrationC = registerDataPlugin(
				dataPluginC, "./A/C");

        TestDataPlugin dataPluginY = new TestDataPlugin();
        setInteriorNode(dataPluginY, "./A/X/Y");
		ServiceRegistration<DataPlugin> registrationY = registerDataPlugin(
				dataPluginY, "./A/X/Y");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertInteriorNode("./A/B");
        assertInteriorNode("./A/C");
        assertScaffoldNode("./A/X");
        assertInteriorNode("./A/X/Y");
        session.close();

        unregister(registrationB);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertNodeNotFound("./A/B");
        assertInteriorNode("./A/C");
        assertScaffoldNode("./A/X");
        assertInteriorNode("./A/X/Y");
        session.close();

        unregister(registrationC);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A");
        assertNodeNotFound("./A/B");
        assertNodeNotFound("./A/C");
        assertScaffoldNode("./A/X");
        assertInteriorNode("./A/X/Y");
        session.close();

        unregister(registrationY);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertNodeNotFound("./A");
        assertNodeNotFound("./A/B");
        assertNodeNotFound("./A/C");
        assertNodeNotFound("./A/XX");
        assertNodeNotFound("./A/X/Y");
        session.close();
    }

    @Test
    public void testWriteCommandToScaffoldNodeIsNotAllowedOnExclusiveLockType() throws DmtException {
        TestDataPlugin dataPluginB = new TestDataPlugin();
        setInteriorNode(dataPluginB, "./A/B");
		ServiceRegistration<DataPlugin> registrationB = registerDataPlugin(
				dataPluginB, "./A/B");

        TestDataPlugin dataPluginD = new TestDataPlugin();
        setInteriorNode(dataPluginD, "./C/D");
		ServiceRegistration<DataPlugin> registrationD = registerDataPlugin(
				dataPluginD, "./C/D");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_EXCLUSIVE);
        try {
            session.setNodeTitle("./A", "title");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setNodeType("./A", "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setDefaultNodeValue("./A");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setNodeValue("./A", new DmtData("default"));
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.createInteriorNode("./A/X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createInteriorNode("./A/X", "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X", new DmtData("default"));
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X", new DmtData("default"), "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.deleteNode("./A");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.renameNode("./A", "X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.copy("./A", "./B", false);
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        session.close();

        unregister(registrationB);
        unregister(registrationD);
    }

    @Test
    public void testWriteCommandToScaffoldNodeIsNotAllowedOnAtomicLockType() throws DmtException {
        TestDataPlugin dataPluginB = new TestDataPlugin();
        setInteriorNode(dataPluginB, "./A/B");
		ServiceRegistration<DataPlugin> registrationB = registerDataPlugin(
				dataPluginB, "./A/B");

        TestDataPlugin dataPluginD = new TestDataPlugin();
        setInteriorNode(dataPluginD, "./C/D");
		ServiceRegistration<DataPlugin> registrationD = registerDataPlugin(
				dataPluginD, "./C/D");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_ATOMIC);
        try {
            session.setNodeTitle("./A", "title");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setNodeType("./A", "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setDefaultNodeValue("./A");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.setNodeValue("./A", new DmtData("default"));
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.createInteriorNode("./A/X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createInteriorNode("./A/X", "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X", new DmtData("default"));
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.createLeafNode("./A/X", new DmtData("default"), "type");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A/X");
        }
        try {
            session.deleteNode("./A");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.renameNode("./A", "X");
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        try {
            session.copy("./A", "./B", false);
            fail("Expected to throw DmtException.");
        } catch (DmtException dmtException) {
            assertThrownDmtException(dmtException, DmtException.COMMAND_NOT_ALLOWED, "./A");
        }
        session.close();

        unregister(registrationB);
        unregister(registrationD);
    }

    @Test
    public void testScaffoldNodeFromParentPluginToChildPlugin() throws Exception {
        TestDataMountPlugin plugin1 = new TestDataMountPlugin();
        setInteriorNode(plugin1, "./A");
		ServiceRegistration<DataPlugin> registration1 = registerMountDataPlugin(
				plugin1, "./A", new String[] {
						"B/C"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertInteriorNode("./A");
            assertNodeNotFound("./A/B");
            assertNodeNotFound("./A/B/C");
            assertNodeNotFound("./A/B/C/D");
            assertNodeNotFound("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }

        TestDataMountPlugin plugin2 = new TestDataMountPlugin();
        setInteriorNode(plugin2, "./A/B/C");
		ServiceRegistration<DataPlugin> registration2 = registerMountDataPlugin(
				plugin2, "./A/B/C", new String[] {
						"D/E"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertInteriorNode("./A");
            assertScaffoldNode("./A/B");
            assertInteriorNode("./A/B/C");
            assertNodeNotFound("./A/B/C/D");
            assertNodeNotFound("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }

        TestDataMountPlugin plugin3 = new TestDataMountPlugin();
        setInteriorNode(plugin3, "./A/B/C/D/E");
		ServiceRegistration<DataPlugin> registration3 = registerMountDataPlugin(
				plugin3, "./A/B/C/D/E");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertInteriorNode("./A");
            assertScaffoldNode("./A/B");
            assertInteriorNode("./A/B/C");
            assertScaffoldNode("./A/B/C/D");
            assertInteriorNode("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }

        unregister(registration1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertScaffoldNode("./A");
            assertScaffoldNode("./A/B");
            assertInteriorNode("./A/B/C");
            assertScaffoldNode("./A/B/C/D");
            assertInteriorNode("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }

        unregister(registration2);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertScaffoldNode("./A");
            assertScaffoldNode("./A/B");
            assertScaffoldNode("./A/B/C");
            assertScaffoldNode("./A/B/C/D");
            assertInteriorNode("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }

        unregister(registration3);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertNodeNotFound("./A");
            assertNodeNotFound("./A/B");
            assertNodeNotFound("./A/B/C");
            assertNodeNotFound("./A/B/C/D");
            assertNodeNotFound("./A/B/C/D/E");
            assertNodeNotFound("./A/B/C/D/E/F");
        } finally {
            session.close();
        }
    }

    private void assertThrownDmtException(DmtException dmtException, int code, String uri) {
        assertThat(dmtException.getCode()).isEqualTo(code);
        assertThat(dmtException.getURI()).isEqualTo(uri);
		assertThat(dmtException.getCause()).isNull();
		assertThat(dmtException.getCauses()).isEmpty();
    }
}
