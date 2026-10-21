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
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.MetaNode;
import org.osgi.service.dmt.Uri;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.ArrayAssert;
import org.osgi.test.cases.dmt.tc4.ext.util.MountPointEvent;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataMountPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.TestMetaNode;

public class SharedMountPointTest extends DmtAdminTestCase {

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
    public void testMountPointNotIndicatePluginTableOrder01() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"B1"
				});

        MountPointEvent addedEvent = pluginTable.getMountPointEvent(0);
        assertThat(addedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEvent.getMountPoint().getMountPath())).isEqualTo("./A1");

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
        setLeafNode(sharedPluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/#");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
		assertThat(session.getChildNodeNames("./A1")).isEmpty();
        assertNodeNotFound("./A1/B1");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEvent = pluginTable.getMountPointEvent(1);
        assertThat(removedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEvent.getMountPoint().getMountPath())).isEqualTo("./A1");

        unregister(registrationB1);
    }

    @Test
    public void testMountPointNotIndicatePluginTableOrder02() throws Exception {
        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
        setLeafNode(sharedPluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/#");

        MountPointEvent addedEvent = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String mountUri = Uri.toUri(addedEvent.getMountPoint().getMountPath());
		assertThat(mountUri).startsWith("./A1");
        setInteriorNode(sharedPluginB1, mountUri);

        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"B1"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
		assertThat(session.getChildNodeNames("./A1")).hasSize(1);
        assertInteriorNode(mountUri);
        assertNodeNotFound("./A1/B1");
        session.close();

        unregister(registrationA1);

        unregister(registrationB1);
        MountPointEvent removedEvent = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEvent.getMountPoint().getMountPath())).isEqualTo(mountUri);
    }

    @Test
    public void testRootURINotIndicatePluginTableOrder01() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});

        MountPointEvent addedEvent = pluginTable.getMountPointEvent(0);
        assertThat(addedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEvent.getMountPoint().getMountPath())).isEqualTo("./A1");

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
        setLeafNode(sharedPluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/B1");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
		assertThat(session.getChildNodeNames("./A1")).isEmpty();
        assertNodeNotFound("./A1/B1");
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEvent = pluginTable.getMountPointEvent(1);
        assertThat(removedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEvent.getMountPoint().getMountPath())).isEqualTo("./A1");

        unregister(registrationB1);
    }

    @Test
    public void testRootURINotIndicatePluginTablOrder02() throws Exception {
        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
        setLeafNode(sharedPluginB1, "./A1/B1");
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/B1");

        MountPointEvent addedEvent = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEvent.getMountPoint().getMountPath())).isEqualTo("./A1/B1");

        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertLeafNode("./A1/B1");
        session.close();

        unregister(registrationA1);

        unregister(registrationB1);
        MountPointEvent removedEvent = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEvent.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEvent.getMountPoint().getMountPath())).isEqualTo("./A1/B1");
    }

    @Test
    public void testRemountEnumeratedPlugins01() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});
        MountPointEvent addedEventA1 = pluginTable.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEventA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/#");
        MountPointEvent addedEventB1 = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB1 = addedEventB1.getMountPoint().getMountPath();
        String rootNodeNameB1 = getLastSegment(mountPathB1);
        assertEnumratedPluginMountPoint(mountPathB1, "./A1");
        setInteriorNode(sharedPluginB1, mountPathB1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertChildNodeNames("./A1", new String[] { rootNodeNameB1 });
        assertInteriorNode(mountPathB1);
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginTable.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertChildNodeNames("./A1", new String[] { rootNodeNameB1 });
        assertInteriorNode(mountPathB1);
        session.close();

        TestDataMountPlugin newPluginTable = new TestDataMountPlugin();
        setInteriorNode(newPluginTable, "./A1");
		ServiceRegistration<DataPlugin> newRegistrationA1 = registerMountDataPlugin(
				newPluginTable, "./A1", new String[] {
						"#"
				});
        MountPointEvent addedEventNewA1 = newPluginTable.getMountPointEvent(0);
        assertThat(addedEventNewA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEventNewA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertChildNodeNames("./A1", new String[] { rootNodeNameB1 });
        assertInteriorNode(mountPathB1);
        session.close();

        unregister(newRegistrationA1);
        MountPointEvent removedEventNewA1 = newPluginTable.getMountPointEvent(1);
        assertThat(removedEventNewA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventNewA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        unregister(registrationB1);
        MountPointEvent removedEventB1 = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventB1.getMountPoint().getMountPath())).isEqualTo(Uri.toUri(mountPathB1));
    }

    @Test
    public void testRemountEnumeratedPlugins02() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});
        MountPointEvent addedEventA1 = pluginTable.getMountPointEvent(0);
        assertThat(addedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEventA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/#");
        MountPointEvent addedEventB1 = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB1 = addedEventB1.getMountPoint().getMountPath();
        String rootNodeNameB1 = getLastSegment(mountPathB1);
        assertEnumratedPluginMountPoint(mountPathB1, "./A1");
        setInteriorNode(sharedPluginB1, mountPathB1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertInteriorNode("./A1");
        assertChildNodeNames("./A1", new String[] { rootNodeNameB1 });
        assertInteriorNode(mountPathB1);
        session.close();

        unregister(registrationA1);
        MountPointEvent removedEventA1 = pluginTable.getMountPointEvent(1);
        assertThat(removedEventA1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventA1.getMountPoint().getMountPath())).isEqualTo("./A1");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertChildNodeNames("./A1", new String[] { rootNodeNameB1 });
        assertInteriorNode(mountPathB1);
        session.close();

        TestDataMountPlugin sharedPluginB2 = new TestDataMountPlugin();
        setInteriorNode(sharedPluginB2, "./A1/B2");
		ServiceRegistration<DataPlugin> registrationB2 = registerMountDataPlugin(
				sharedPluginB2, "./A1/B2");

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        assertScaffoldNode("./A1");
        assertInteriorNode(mountPathB1);
        assertNodeNotFound("./A1/B2");
        session.close();

        unregister(registrationB1);
        MountPointEvent removedEventB1 = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventB1.getMountPoint().getMountPath())).isEqualTo(Uri.toUri(mountPathB1));

        MountPointEvent addedEventB2 = sharedPluginB2.getMountPointEvent(0);
        assertThat(addedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        assertThat(Uri.toUri(addedEventB2.getMountPoint().getMountPath())).isEqualTo("./A1/B2");

        unregister(registrationB2);
        MountPointEvent removedEventB2 = sharedPluginB2.getMountPointEvent(1);
        assertThat(removedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        assertThat(Uri.toUri(removedEventB2.getMountPoint().getMountPath())).isEqualTo("./A1/B2");
    }

    @Test
    public void testMountUnknownEnumeratedPlugins() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPlugin(
				sharedPluginB1, "./A1/#");

        MountPointEvent addedEventB1 = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB1 = addedEventB1.getMountPoint().getMountPath();
        int mountPonintNumberB1 = assertEnumratedPluginMountPoint(mountPathB1, "./A1");

        TestDataMountPlugin sharedPluginB2 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB2 = registerMountDataPlugin(
				sharedPluginB2, "./A1/#");

        MountPointEvent addedEventB2 = sharedPluginB2.getMountPointEvent(0);
        assertThat(addedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB2 = addedEventB2.getMountPoint().getMountPath();
        int mountPonintNumberB2 = assertEnumratedPluginMountPoint(mountPathB2, "./A1");

        assertThat(mountPonintNumberB2).isEqualTo(mountPonintNumberB1 + 1);

        unregister(registrationB1);
        MountPointEvent removedEventB1 = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        ArrayAssert.assertEquivalenceArrays(mountPathB1, removedEventB1.getMountPoint().getMountPath());

        unregister(registrationB2);
        MountPointEvent removedEventB2 = sharedPluginB2.getMountPointEvent(1);
        assertThat(removedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        ArrayAssert.assertEquivalenceArrays(mountPathB2, removedEventB2.getMountPoint().getMountPath());

        unregister(registrationA1);
    }

    @Test
    public void testMountKnownEnumeratedPlugins() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPluginWithServicePIDs(
				sharedPluginB1, "./A1/#", new String[] {
						"B1"
				});
        MountPointEvent addedEventB1 = sharedPluginB1.getMountPointEvent(0);
        assertThat(addedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB1 = addedEventB1.getMountPoint().getMountPath();
        int mountPonintNumberB1 = assertEnumratedPluginMountPoint(mountPathB1, "./A1");

        TestDataMountPlugin sharedPluginB2 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB2 = registerMountDataPluginWithServicePIDs(
				sharedPluginB2, "./A1/#", new String[] {
						"B2"
				});
        MountPointEvent addedEventB2 = sharedPluginB2.getMountPointEvent(0);
        assertThat(addedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] mountPathB2 = addedEventB2.getMountPoint().getMountPath();
        int mountPonintNumberB2 = assertEnumratedPluginMountPoint(mountPathB2, "./A1");

		assertThat(mountPonintNumberB1).isNotEqualTo(mountPonintNumberB2);

        unregister(registrationB1);
        MountPointEvent removedEventB1 = sharedPluginB1.getMountPointEvent(1);
        assertThat(removedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        ArrayAssert.assertEquivalenceArrays(mountPathB1, removedEventB1.getMountPoint().getMountPath());

        TestDataMountPlugin newSharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> newRegistrationB1 = registerMountDataPluginWithServicePIDs(
				newSharedPluginB1, "./A1/#", new String[] {
						"B1"
				});
        MountPointEvent newAddedEventB1 = newSharedPluginB1.getMountPointEvent(0);
        assertThat(newAddedEventB1.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] newMountPathB1 = newAddedEventB1.getMountPoint().getMountPath();
        int newMountPonintNumberB1 = assertEnumratedPluginMountPoint(newMountPathB1, "./A1");

        assertThat(newMountPonintNumberB1).isEqualTo(mountPonintNumberB1);

        unregister(registrationB2);
        MountPointEvent removedEventB2 = sharedPluginB2.getMountPointEvent(1);
        assertThat(removedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_REMOVED);
        ArrayAssert.assertEquivalenceArrays(mountPathB2, removedEventB2.getMountPoint().getMountPath());

        TestDataMountPlugin newSharedPluginB2 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> newRegistrationB2 = registerMountDataPluginWithServicePIDs(
				newSharedPluginB2, "./A1/#", new String[] {
						"B2"
				});
        MountPointEvent newAddedEventB2 = newSharedPluginB2.getMountPointEvent(0);
        assertThat(newAddedEventB2.getType()).isEqualTo(MountPointEvent.MOUNT_POINTS_ADDED);
        String[] newMountPathB2 = newAddedEventB2.getMountPoint().getMountPath();
        int newMountPonintNumberB2 = assertEnumratedPluginMountPoint(newMountPathB2, "./A1");

        assertThat(newMountPonintNumberB2).isEqualTo(mountPonintNumberB2);

        unregister(registrationA1);
        unregister(newRegistrationB1);
        unregister(newRegistrationB2);
    }

    @Test
    public void testGetMetaNodeNotMountSharedPlugins() throws Exception {
        TestDataMountPlugin pluginTable = new TestDataMountPlugin();
        setInteriorNode(pluginTable, "./A1");
		ServiceRegistration<DataPlugin> registrationA1 = registerMountDataPlugin(
				pluginTable, "./A1", new String[] {
						"#"
				});

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertSharedMountPointMetaNode(session.getMetaNode("./A1/1"), MetaNode.DYNAMIC);
        } finally {
            session.close();
        }

        TestDataMountPlugin sharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB1 = registerMountDataPluginWithServicePIDs(
				sharedPluginB1, "./A1/#", new String[] {
						"B1"
				});
        MountPointEvent addedEventB1 = sharedPluginB1.getMountPointEvent(0);
        String[] mountPathB1 = addedEventB1.getMountPoint().getMountPath();
        setInteriorNode(sharedPluginB1, mountPathB1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        TestDataMountPlugin sharedPluginB2 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> registrationB2 = registerMountDataPluginWithServicePIDs(
				sharedPluginB2, "./A1/#", new String[] {
						"B2"
				});
        MountPointEvent addedEventB2 = sharedPluginB2.getMountPointEvent(0);
        String[] mountPathB2 = addedEventB2.getMountPoint().getMountPath();
        setInteriorNode(sharedPluginB2, mountPathB2);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        unregister(registrationB1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertSharedMountPointMetaNode(session.getMetaNode(Uri.toUri(mountPathB1)), MetaNode.DYNAMIC);
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        TestDataMountPlugin newSharedPluginB1 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> newRegistrationB1 = registerMountDataPluginWithServicePIDs(
				newSharedPluginB1, "./A1/#", new String[] {
						"B1"
				});
        setInteriorNode(newSharedPluginB1, mountPathB1);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        unregister(registrationB2);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
            assertSharedMountPointMetaNode(session.getMetaNode(Uri.toUri(mountPathB2)), MetaNode.DYNAMIC);
        } finally {
            session.close();
        }

        TestDataMountPlugin newSharedPluginB2 = new TestDataMountPlugin();
		ServiceRegistration<DataPlugin> newRegistrationB2 = registerMountDataPluginWithServicePIDs(
				newSharedPluginB2, "./A1/#", new String[] {
						"B2"
				});
        setInteriorNode(newSharedPluginB2, mountPathB2);

        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        unregister(registrationA1);
        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB1)).getClass()).isEqualTo(TestMetaNode.class);
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        unregister(newRegistrationB1);
        session = dmtAdmin.getSession(".", DmtSession.LOCK_TYPE_SHARED);
        try {
            assertThat(session.getMetaNode(Uri.toUri(mountPathB2)).getClass()).isEqualTo(TestMetaNode.class);
        } finally {
            session.close();
        }

        unregister(newRegistrationB2);
    }

    private void assertSharedMountPointMetaNode(MetaNode sharedMountPointMetaNode, int scope) {
        assertThat(sharedMountPointMetaNode.can(MetaNode.CMD_GET)).isTrue();
        assertThat(sharedMountPointMetaNode.getDefault()).isNull();
        assertThat(sharedMountPointMetaNode.getDescription()).isNull();
        assertThat(sharedMountPointMetaNode.getFormat()).isEqualTo(DmtData.FORMAT_NODE);
        assertThat(sharedMountPointMetaNode.getMax()).isCloseTo(Double.MAX_VALUE, within(0.0d));
        assertThat(sharedMountPointMetaNode.getMaxOccurrence()).isEqualTo(Integer.MAX_VALUE);
        assertThat(sharedMountPointMetaNode.getMimeTypes()).isNull();
        assertThat(sharedMountPointMetaNode.getMin()).isCloseTo(Double.MIN_VALUE, within(0.0d));
        assertThat(sharedMountPointMetaNode.getRawFormatNames()).isNull();
        assertThat(sharedMountPointMetaNode.getScope()).isEqualTo(scope);
        assertThat(sharedMountPointMetaNode.getValidNames()).isNull();
        assertThat(sharedMountPointMetaNode.getValidValues()).isNull();
        assertThat(sharedMountPointMetaNode.isLeaf()).isFalse();

        assertThat(sharedMountPointMetaNode.isValidName("foo")).isFalse();
        assertThat(sharedMountPointMetaNode.isValidName(Integer.toString(Integer.MIN_VALUE))).isFalse();
        assertThat(sharedMountPointMetaNode.isValidName("-1")).isFalse();
        assertThat(sharedMountPointMetaNode.isValidName("0")).isFalse();
        assertThat(sharedMountPointMetaNode.isValidName("1")).isTrue();
        assertThat(sharedMountPointMetaNode.isValidName("2")).isTrue();
        assertThat(sharedMountPointMetaNode.isValidName(Integer.toString(Integer.MAX_VALUE - 1))).isTrue();
        assertThat(sharedMountPointMetaNode.isValidName(Integer.toString(Integer.MAX_VALUE))).isFalse();
        assertThat(sharedMountPointMetaNode.isValidName(Integer.toString(Integer.MAX_VALUE) + "0")).isFalse();

        assertThat(sharedMountPointMetaNode.isValidValue(new DmtData(1))).isFalse();
        assertThat(sharedMountPointMetaNode.isValidValue(new DmtData("foo"))).isFalse();
        assertThat(sharedMountPointMetaNode.isZeroOccurrenceAllowed()).isTrue();
    }
}
