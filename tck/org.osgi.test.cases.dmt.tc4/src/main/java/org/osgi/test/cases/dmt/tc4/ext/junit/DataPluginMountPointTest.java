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

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.service.dmt.spi.MountPoint;
import org.osgi.test.cases.dmt.tc4.ext.util.ArrayAssert;
import org.osgi.test.cases.dmt.tc4.ext.util.MountPointEvent;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataMountPlugin;

public class DataPluginMountPointTest extends DmtAdminTestCase {

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
    public void test1MountPointCallback() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1");

        MountPointEvent addedEvent = pluginA1.getMountPointEvent(0);
        assertThat(addedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEvent.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEvent = pluginA1.getMountPointEvent(1);
        assertThat(removedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEvent.getMountPoint()).isEqualTo(addedEvent.getMountPoint());
    }

    @Test
    public void test2MountPointsCallbackPattern01() throws Exception {
        TestDataMountPlugin pluginA = new TestDataMountPlugin();
        setLeafNode(pluginA, "./A1");
        setLeafNode(pluginA, "./A2");
		ServiceRegistration<DataPlugin> registrationA = registerMountDataPlugin(
				pluginA, new String[] {
						"./A1", "./A2"
				});

        MountPointEvent addedEvent0 = pluginA.getMountPointEvent(0);
        assertThat(addedEvent0.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEvent0.getMountPoint());

        MountPointEvent addedEvent1 = pluginA.getMountPointEvent(1);
        assertThat(addedEvent1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEvent0.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertLeafNode("./A1");
        assertLeafNode("./A2");
        session.close();

        unregister(registrationA);
        MountPointEvent removedEvent0 = pluginA.getMountPointEvent(2);
        MountPointEvent removedEvent1 = pluginA.getMountPointEvent(3);
        assertThat(removedEvent0.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEvent0.getMountPoint()).isEqualTo(addedEvent0.getMountPoint());
        assertThat(removedEvent1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEvent1.getMountPoint()).isEqualTo(addedEvent1.getMountPoint());
    }

    @Test
    public void test2MountPluginsCallbackPattern02() throws Exception {
        TestDataMountPlugin pluginA = new TestDataMountPlugin();
        setLeafNode(pluginA, "./A1");
        setLeafNode(pluginA, "./A2/B1");
		ServiceRegistration<DataPlugin> registrationA = registerMountDataPlugin(
				pluginA, new String[] {
						"./A1", "./A2/B1"
				});

        MountPointEvent addedEvent1 = pluginA.getMountPointEvent(0);
        assertThat(addedEvent1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEvent1.getMountPoint());
        MountPointEvent addedEvent2 = pluginA.getMountPointEvent(1);
        assertThat(addedEvent2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A2/B1", addedEvent2.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertLeafNode("./A1");
        assertLeafNode("./A2/B1");
        session.close();

        unregister(registrationA);
        MountPointEvent removedEvent1 = pluginA.getMountPointEvent(2);
        assertThat(removedEvent1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEvent1.getMountPoint()).isEqualTo(addedEvent1.getMountPoint());
        MountPointEvent removedEvent2 = pluginA.getMountPointEvent(3);
        assertThat(removedEvent2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEvent2.getMountPoint()).isEqualTo(addedEvent2.getMountPoint());
    }

    @Test
    public void test2MountPluginsCallbackPattern01() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});

        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginA2B2 = new TestDataMountPlugin();
        setLeafNode(pluginA2B2, "./A1/B2");
        setLeafNode(pluginA2B2, "./A2");
		ServiceRegistration<DataPlugin> registrationA2B2 = registerMountDataPlugin(
				pluginA2B2, new String[] {
						"./A1/B2", "./A2"
				});

        MountPointEvent addedEventA2B21 = pluginA2B2.getMountPointEvent(0);
        assertThat(addedEventA2B21.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A2", addedEventA2B21.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertNodeNotFound("./A1/B1");
        assertNodeNotFound("./A1/B2");
        assertLeafNode("./A2");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());

        MountPointEvent addedEventA2B22 = pluginA2B2.getMountPointEvent(1);
        assertThat(addedEventA2B22.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B2", addedEventA2B22.getMountPoint());

        unregister(registrationA2B2);
        MountPoint[] removedMountPoints = new MountPoint[2];
        MountPointEvent removedEventA2B21 = pluginA2B2.getMountPointEvent(2);
        assertThat(removedEventA2B21.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        removedMountPoints[0] = removedEventA2B21.getMountPoint();
        MountPointEvent removedEventA2B22 = pluginA2B2.getMountPointEvent(3);
        assertThat(removedEventA2B21.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        removedMountPoints[1] = removedEventA2B22.getMountPoint();
        ArrayAssert.assertEquivalenceArrays(new MountPoint[]{
        		addedEventA2B22.getMountPoint(), addedEventA2B21.getMountPoint()}, removedMountPoints);
    }

    @Test
    public void test2MountPluginsCallbackPattern03() throws Exception {
        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setLeafNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1");
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginB2C2 = new TestDataMountPlugin();
        setLeafNode(pluginB2C2, "./A1/B2/C2");
		ServiceRegistration<DataPlugin> registrationB2C2 = registerMountDataPlugin(
				pluginB2C2, "./A1/B2/C2");
        MountPointEvent addedEventB2C2 = pluginB2C2.getMountPointEvent(0);
        assertThat(addedEventB2C2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B2/C2", addedEventB2C2.getMountPoint());

        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertLeafNode("./A1/B1");
        assertScaffoldNode("./A1/B2");
        assertLeafNode("./A1/B2/C2");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());

        unregister(registrationB2C2);
        MountPointEvent removedEventA2B2 = pluginB2C2.getMountPointEvent(1);
        assertThat(removedEventA2B2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA2B2.getMountPoint()).isEqualTo(addedEventB2C2.getMountPoint());

        unregister(registrationA1);
    }

    @Test
    public void test3MountPluginsCallbackPattern01() throws Exception {
        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationA1);
        unregister(registrationB1);
        unregister(registrationC1);
    }

    @Test
    public void test3MountPluginsCallbackPattern02() throws Exception {
        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationA1);
        unregister(registrationB1);
        unregister(registrationC1);
    }

    @Test
    public void test3MountPluginsCallbackPattern03() throws Exception {
        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationA1);
        unregister(registrationB1);
        unregister(registrationC1);
    }

    @Test
    public void test3MountPluginsCallbackPattern04() throws Exception {
        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationA1);
        unregister(registrationB1);
        unregister(registrationC1);
    }

    @Test
    public void test3MountPluginsCallbackPattern05() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());

        unregister(registrationC1);
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());
    }

    @Test
    public void test3MountPluginsCallbackPattern06() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
        MountPointEvent reAddedEventC1 = pluginC1.getMountPointEvent(2);
        assertThat(reAddedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", reAddedEventC1.getMountPoint());

        unregister(registrationC1);
        MountPointEvent reRemovedEventC1 = pluginC1.getMountPointEvent(3);
        assertThat(reRemovedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertMountPath("./A1/B1/C1", reRemovedEventC1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPatten08() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setLeafNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1");
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertLeafNode("./A1/B1/C1");
        session.close();

        unregister(registrationC1);
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPattern10() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setInteriorNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1", new String[] {
						"D1"
				});
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginD1 = new TestDataMountPlugin();
        setLeafNode(pluginD1, "./A1/B1/C1/D1");
		ServiceRegistration<DataPlugin> registrationD1 = registerMountDataPlugin(
				pluginD1, "./A1/B1/C1/D1");
        MountPointEvent addedEventD1 = pluginD1.getMountPointEvent(0);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertLeafNode("./A1/B1/C1/D1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());
        MountPointEvent removedEventD1 = pluginD1.getMountPointEvent(1);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());

        unregister(registrationC1);
        unregister(registrationD1);

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPattern11() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setInteriorNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1", new String[] {
						"D1"
				});
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginD1 = new TestDataMountPlugin();
        setLeafNode(pluginD1, "./A1/B1/C1/D1");
		ServiceRegistration<DataPlugin> registrationD1 = registerMountDataPlugin(
				pluginD1, "./A1/B1/C1/D1");
        MountPointEvent addedEventD1 = pluginD1.getMountPointEvent(0);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertLeafNode("./A1/B1/C1/D1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());
        MountPointEvent removedEventD1 = pluginD1.getMountPointEvent(1);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
        addedEventC1 = pluginC1.getMountPointEvent(2);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());
        addedEventD1 = pluginD1.getMountPointEvent(2);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertScaffoldNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertLeafNode("./A1/B1/C1/D1");
        session.close();

        unregister(registrationC1);
        removedEventC1 = pluginC1.getMountPointEvent(3);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());

        unregister(registrationD1);
        removedEventD1 = pluginD1.getMountPointEvent(3);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPattern12() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setInteriorNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1", new String[] {
						"D1"
				});
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        TestDataMountPlugin pluginD1 = new TestDataMountPlugin();
        setLeafNode(pluginD1, "./A1/B1/C1/D1");
		ServiceRegistration<DataPlugin> registrationD1 = registerMountDataPlugin(
				pluginD1, "./A1/B1/C1/D1");
        MountPointEvent addedEventD1 = pluginD1.getMountPointEvent(0);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertLeafNode("./A1/B1/C1/D1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());
        MountPointEvent removedEventD1 = pluginD1.getMountPointEvent(1);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
        addedEventC1 = pluginC1.getMountPointEvent(2);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());
        addedEventD1 = pluginD1.getMountPointEvent(2);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertScaffoldNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertLeafNode("./A1/B1/C1/D1");
        session.close();

        unregister(registrationD1);
        removedEventD1 = pluginD1.getMountPointEvent(3);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());

        unregister(registrationC1);
        removedEventC1 = pluginC1.getMountPointEvent(3);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPattern13() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
        setInteriorNode(pluginB1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1/D1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginD1 = new TestDataMountPlugin();
        setInteriorNode(pluginD1, "./A1/B1/C1/D1");
		ServiceRegistration<DataPlugin> registrationD1 = registerMountDataPlugin(
				pluginD1, "./A1/B1/C1/D1", new String[] {
						"E1"
				});
        MountPointEvent addedEventD1 = pluginD1.getMountPointEvent(0);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());

        TestDataMountPlugin pluginE1 = new TestDataMountPlugin();
        setLeafNode(pluginE1, "./A1/B1/C1/D1/E1");
		ServiceRegistration<DataPlugin> registrationE1 = registerMountDataPlugin(
				pluginE1, "./A1/B1/C1/D1/E1");
        MountPointEvent addedEventE1 = pluginE1.getMountPointEvent(0);
        assertThat(addedEventE1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1/E1", addedEventE1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        assertInteriorNode("./A1/B1/C1/D1");
        assertLeafNode("./A1/B1/C1/D1/E1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventD1 = pluginD1.getMountPointEvent(1);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());
        MountPointEvent removedEventE1 = pluginE1.getMountPointEvent(1);
        assertThat(removedEventE1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventE1.getMountPoint()).isEqualTo(addedEventE1.getMountPoint());

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());
        addedEventD1 = pluginD1.getMountPointEvent(2);
        assertThat(addedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1", addedEventD1.getMountPoint());
        addedEventE1 = pluginE1.getMountPointEvent(2);
        assertThat(addedEventE1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1/D1/E1", addedEventE1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertScaffoldNode("./A1/B1");
        assertScaffoldNode("./A1/B1/C1");
        assertInteriorNode("./A1/B1/C1/D1");
        assertLeafNode("./A1/B1/C1/D1/E1");
        session.close();

        unregister(registrationD1);
        removedEventD1 = pluginD1.getMountPointEvent(3);
        assertThat(removedEventD1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventD1.getMountPoint()).isEqualTo(addedEventD1.getMountPoint());

        unregister(registrationE1);
        removedEventE1 = pluginE1.getMountPointEvent(3);
        assertThat(removedEventE1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventE1.getMountPoint()).isEqualTo(addedEventE1.getMountPoint());
    }

    @Test
    public void testMountPluginCallbackPattern14() throws Exception {
        TestDataMountPlugin pluginA1 = new TestDataMountPlugin();
        setInteriorNode(pluginA1, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginA1, "./A1", new String[] {
						"B1"
				});
        MountPointEvent addedEventA1 = pluginA1.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1", addedEventA1.getMountPoint());

        TestDataMountPlugin pluginB1 = new TestDataMountPlugin();
        setInteriorNode(pluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				pluginB1, "./A1/B1", new String[] {
						"C1"
				});
        MountPointEvent addedEventB1 = pluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1", addedEventB1.getMountPoint());

        TestDataMountPlugin pluginC1 = new TestDataMountPlugin();
        setInteriorNode(pluginC1, "./A1/B1/C1");
		ServiceRegistration<DataPlugin> registrationC1 = registerMountDataPlugin(
				pluginC1, "./A1/B1/C1", new String[] {
						"D1"
				});
        MountPointEvent addedEventC1 = pluginC1.getMountPointEvent(0);
        assertThat(addedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertMountPath("./A1/B1/C1", addedEventC1.getMountPoint());

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertInteriorNode("./A1/B1");
        assertInteriorNode("./A1/B1/C1");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = pluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventB1.getMountPoint()).isEqualTo(addedEventB1.getMountPoint());
        MountPointEvent removedEventC1 = pluginC1.getMountPointEvent(1);
        assertThat(removedEventC1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventC1.getMountPoint()).isEqualTo(addedEventC1.getMountPoint());

        TestDataMountPlugin pluginD1 = new TestDataMountPlugin();
        setLeafNode(pluginD1, "./A1/B1/C1/D1");
		ServiceRegistration<DataPlugin> registrationD1 = registerMountDataPlugin(
				pluginD1, "./A1/B1/C1/D1");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertNodeNotFound("./A1/B1");
        assertNodeNotFound("./A1/B1/C1");
        assertNodeNotFound("./A1/B1/C1/D1");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginA1.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(removedEventA1.getMountPoint()).isEqualTo(addedEventA1.getMountPoint());

        unregister(registrationC1);
        unregister(registrationD1);
    }
}
