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

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.dmt.DmtAdmin;
import org.osgi.service.dmt.DmtConstants;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtEventListener;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.DmtIllegalStateException;
import org.osgi.service.dmt.DmtSession;
import org.osgi.service.dmt.Uri;
import org.osgi.service.dmt.spi.DataPlugin;
import org.osgi.service.dmt.spi.ExecPlugin;
import org.osgi.service.dmt.spi.MountPlugin;
import org.osgi.service.dmt.spi.MountPoint;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataMountPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.TestDataPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.TestExecPlugin;
import org.osgi.test.cases.dmt.tc4.ext.util.TestNode;
import org.osgi.test.common.annotation.InjectBundleContext;

public abstract class DmtAdminTestCase {

	protected ServiceReference<DmtAdmin>	dmtAdminRef;

	protected DmtAdmin				dmtAdmin;

	protected DmtSession			session;

	protected LinkedList<ServiceRegistration< ? >>	registrationList		= new LinkedList<>();

	@InjectBundleContext
	protected static BundleContext	context;

	private final Map<DmtEventListener,ServiceRegistration< ? >>	listenerRegistrationMap	= new HashMap<>();

	@BeforeEach
	public void setUp() throws Exception {
	}

	@AfterEach
	public void tearDown() throws Exception {
	}

	public void getDmtAdmin() {
		dmtAdminRef = context.getServiceReference(DmtAdmin.class);
		dmtAdmin = context.getService(dmtAdminRef);
	}

	protected void closeDmtSession() throws DmtException {
		if ((session != null) && (session.getState() == DmtSession.STATE_OPEN)) {
			try {
				session.close();
			} catch (DmtIllegalStateException exception) {
				exception.printStackTrace();
			}
		}
	}

	protected void unregisterPlugins() {
		while (!registrationList.isEmpty()) {
			ServiceRegistration< ? > registration = registrationList
					.removeLast();
			registration.unregister();
		}
	}

	protected void assertNodeNotFound(String nodeUri) {
		try {
			session.getNodeTitle(nodeUri);
			fail("");
		} catch (DmtException exception) {
			assertNodeNotFoundDmtException(nodeUri, exception);
		}
		try {
			session.getNodeType(nodeUri);
			fail("");
		} catch (DmtException exception) {
			assertNodeNotFoundDmtException(nodeUri, exception);
		}
		try {
			session.getNodeValue(nodeUri);
			fail("");
		} catch (DmtException exception) {
			assertNodeNotFoundDmtException(nodeUri, exception);
		}
	}

	protected void setInteriorNode(TestDataPlugin plugin, String uri) {
		plugin.setNode(uri, TestNode.newInteriorNode(uri + ":Title", uri + ":Type"));
	}

	protected void setInteriorNode(TestDataMountPlugin plugin, String uri) {
		plugin.setNode(uri, TestNode.newInteriorNode(uri + ":Title", uri + ":Type"));
	}

	protected void setInteriorNode(TestDataMountPlugin plugin, String[] path) {
		setInteriorNode(plugin, Uri.toUri(path));
	}

	protected void setLeafNode(TestDataPlugin plugin, String uri) {
		plugin.setNode(uri, TestNode.newLeafNode(uri + ":Title", uri + ":Type", new DmtData(uri + ":Value")));
	}

	protected void setLeafNode(TestDataMountPlugin plugin, String uri) {
		plugin.setNode(uri, TestNode.newLeafNode(uri + ":Title", uri + ":Type", new DmtData(uri + ":Value")));
	}

	protected void setLeafNode(TestDataMountPlugin plugin, String[] path) {
		setLeafNode(plugin, Uri.toUri(path));
	}

	protected void assertScaffoldNode(String uri) throws DmtException {
		assertThat(session.getNodeTitle(uri)).isNull();
		assertThat(session.getNodeType(uri)).isEqualTo(DmtConstants.DDF_SCAFFOLD);

		try {
			assertThat(session.getNodeValue(uri)).isNull();
			fail("");
		} catch (DmtException exception) {
			assertThat(exception.getCode()).isEqualTo(DmtException.COMMAND_NOT_ALLOWED);
			assertThat(exception.getURI()).isEqualTo(uri);
		}
	}

	protected void assertInteriorNode(String[] path) throws DmtException {
		assertInteriorNode(Uri.toUri(path));
	}

	protected void assertInteriorNode(String uri) throws DmtException {
		assertThat(session.isLeafNode(uri)).isFalse();
		assertThat(session.getNodeTitle(uri)).isEqualTo(uri + ":Title");
		assertThat(session.getNodeType(uri)).isEqualTo(uri + ":Type");
		assertInteriorNodeNoValue(uri);
	}

	protected void assertInteriorNodeNoValue(String uri) {
		try {
			session.getNodeValue(uri);
			fail("There is an unexpected node value for: " + uri);
		} catch (DmtException dmtE) {
			assertThat(dmtE.getCode()).as("FEATURE_NOT_SUPPORTED is expected.").isEqualTo(DmtException.FEATURE_NOT_SUPPORTED);
		}
	}

	protected void assertInteriorNode(String fromUri, String toUri) throws DmtException {
		assertThat(session.isLeafNode(toUri)).isFalse();
		assertThat(session.getNodeTitle(toUri)).isEqualTo(fromUri + ":Title");
		assertThat(session.getNodeType(toUri)).isEqualTo(fromUri + ":Type");
		assertThat(session.getNodeValue(toUri)).isNull();
	}

	protected void assertLeafNode(String uri) throws DmtException {
		assertThat(session.isLeafNode(uri)).isTrue();
		assertThat(session.getNodeTitle(uri)).isEqualTo(uri + ":Title");
		assertThat(session.getNodeType(uri)).isEqualTo(uri + ":Type");
		assertThat(session.getNodeValue(uri)).isEqualTo(new DmtData(uri + ":Value"));
	}

	protected void assertLeafNode(String fromUri, String toUri) throws DmtException {
		assertThat(session.isLeafNode(toUri)).isTrue();
		assertThat(session.getNodeTitle(toUri)).isEqualTo(fromUri + ":Title");
		assertThat(session.getNodeType(toUri)).isEqualTo(fromUri + ":Type");
		assertThat(session.getNodeValue(toUri)).isEqualTo(new DmtData(fromUri + ":Value"));
	}

	protected void assertChildNodeNames(String uri, String[] expectedChildNodeNames) throws DmtException {
		assertThat(Arrays.equals(expectedChildNodeNames, session.getChildNodeNames(uri))).isTrue();
	}

	protected void assertNodeNotFound(String[] path) {
		assertNodeNotFound(Uri.toUri(path));
	}

	protected void assertNodeNotFoundDmtException(String nodeUri, DmtException exception) {
		assertThat(exception.getCode()).isEqualTo(DmtException.NODE_NOT_FOUND);
		assertThat(exception.getURI()).isEqualTo(nodeUri);
		assertThat(exception.getCause()).isNull();
		assertThat(exception.getCauses()).isEmpty();
	}

	protected static void assertMountPath(String expectedMountURI, MountPoint mountPoint) {
		assertThat(Uri.toUri(mountPoint.getMountPath())).isEqualTo(expectedMountURI);
	}

	protected static void assertMountPaths(String[] expectedMountURIs, MountPoint[] mountPoints) {
		String[] actualMountURIs = new String[mountPoints.length];
		for (int i = 0; i < mountPoints.length; i++) {
			actualMountURIs[i] = Uri.toUri(mountPoints[i].getMountPath());
		}
		assertThat(Arrays.equals(expectedMountURIs, actualMountURIs)).isTrue();
	}

	protected int assertEnumratedPluginMountPoint(String[] mountPath, String parentNodeUri) {
		assertThat(Uri.toUri(mountPath).startsWith(parentNodeUri)).isTrue();
		assertThat(mountPath.length).isEqualTo(Uri.toPath(parentNodeUri).length + 1);
		int lastIndex = mountPath.length - 1;
		return Integer.parseInt(mountPath[lastIndex]);
	}

	protected void assertExecute(TestExecPlugin plugin, String uri, String corrleator, String data) throws DmtException {
		session.execute(uri, corrleator, data);
		assertThat(Arrays.equals(Uri.toPath(uri), plugin.getNodePath())).isTrue();
		assertThat(plugin.getCorrelator()).isEqualTo(corrleator);
		assertThat(plugin.getData()).isEqualTo(data);
		assertThat(plugin.wasExecuteCalled()).isTrue();
	}

	protected void assertExecuteFailed(TestExecPlugin plugin, String uri) throws DmtException {
		assertExecuteFailed(plugin, uri, DmtException.COMMAND_FAILED);
	}

	protected void assertExecuteFailed(TestExecPlugin plugin, String uri, int code) throws DmtException {
		try {
			session.execute(uri, "to be failed.");
			fail("");
		} catch (DmtException dmtException) {
			assertThat(dmtException.getCode()).isEqualTo(code);
			assertThat(dmtException.getURI()).isEqualTo(uri);
			assertThat(dmtException.getCause()).isNull();
			assertThat(dmtException.getCauses()).isEmpty();
			assertThat(plugin.wasExecuteCalled()).isFalse();
		}
	}

	protected ServiceRegistration<DataPlugin> registerDataPlugin(
			DataPlugin dataPlugin, String dataRootUri) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUri);
		return registerDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerDataPlugin(
			DataPlugin dataPlugin, String[] dataRootUris) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUris);
		return registerDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerDataPlugin(
			DataPlugin dataPlugin,
			Dictionary<String,Object> serviceProps) {
		ServiceRegistration<DataPlugin> registration = context
				.registerService(DataPlugin.class, dataPlugin, serviceProps);
		registrationList.add(registration);
		return registration;
	}

	protected ServiceRegistration<DataPlugin> registerMountingDataPlugin(
			DataPlugin dataPlugin, String dataRootUri, String[] mountPoints) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUri);
		serviceProps.put(DataPlugin.MOUNT_POINTS, mountPoints);
		return registerDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerMountDataPlugin(
			DataPlugin dataPlugin, String dataRootUri) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUri);
		return registerMountDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerMountDataPlugin(
			DataPlugin dataPlugin, String[] dataRootUris) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUris);
		return registerMountDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerMountDataPlugin(
			DataPlugin dataPlugin, String dataRootUri, String[] mountPoints) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUri);
		serviceProps.put(DataPlugin.MOUNT_POINTS, mountPoints);
		return registerMountDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerMountDataPluginWithServicePIDs(
			DataPlugin dataPlugin, String dataRootUri, String[] servicePIDs) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(Constants.SERVICE_PID, servicePIDs);
		serviceProps.put(DataPlugin.DATA_ROOT_URIS, dataRootUri);
		return registerMountDataPlugin(dataPlugin, serviceProps);
	}

	protected ServiceRegistration<DataPlugin> registerMountDataPlugin(
			DataPlugin dataPlugin,
			Dictionary<String,Object> serviceProps) {
		ServiceRegistration<DataPlugin> registration = context
				.registerService(DataPlugin.class, dataPlugin, serviceProps);
		registrationList.add(registration);
		return registration;
	}

	protected ServiceRegistration< ? > registerExecPlugin(ExecPlugin execPlugin,
			String dataRootUri) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, dataRootUri);
		return registerExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerExecPlugin(ExecPlugin execPlugin,
			String[] execRootUris) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, execRootUris);
		return registerExecPlugin(execPlugin, execRootUris);
	}

	protected ServiceRegistration<ExecPlugin> registerExecPlugin(
			ExecPlugin execPlugin,
			Dictionary<String,Object> serviceProps) {
		ServiceRegistration<ExecPlugin> registration = context
				.registerService(ExecPlugin.class, execPlugin, serviceProps);
		registrationList.add(registration);
		return registration;
	}

	protected ServiceRegistration<ExecPlugin> registerMountingExecPlugin(
			ExecPlugin execPlugin, String dataRootUri, String[] mountPoints) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, dataRootUri);
		serviceProps.put(ExecPlugin.MOUNT_POINTS, mountPoints);
		return registerExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerMountExecPlugin(
			ExecPlugin execPlugin, String dataRootUri) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, dataRootUri);
		return registerMountExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerMountExecPlugin(
			ExecPlugin execPlugin, String[] execRootUris) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, execRootUris);
		return registerMountExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerMountExecPlugin(
			ExecPlugin execPlugin, String dataRootUri, String[] mountPoints) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, dataRootUri);
		serviceProps.put(ExecPlugin.MOUNT_POINTS, mountPoints);
		return registerMountExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerMountExecPluginWithServicePIDs(
			ExecPlugin execPlugin, String execRootUris, String[] servicePIDs) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(Constants.SERVICE_PID, servicePIDs);
		serviceProps.put(ExecPlugin.EXEC_ROOT_URIS, execRootUris);
		return registerMountExecPlugin(execPlugin, serviceProps);
	}

	protected ServiceRegistration< ? > registerMountExecPlugin(
			ExecPlugin execPlugin,
			Dictionary<String,Object> serviceProps) {
		String[] objectClasses = new String[] {ExecPlugin.class.getName(), MountPlugin.class.getName()};
		ServiceRegistration< ? > registration = context
				.registerService(objectClasses, execPlugin, serviceProps);
		registrationList.add(registration);
		return registration;
	}

	protected void unregister(ServiceRegistration< ? > registration) {
		registrationList.remove(registration);
		registration.unregister();
	}

	protected String getLastSegment(String[] path) {
		return path[path.length - 1];
	}

	protected void addDmtEventListener(String principal, int type, String uri, DmtEventListener listener) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DmtEventListener.FILTER_PRINCIPAL, new String[] {principal});
		serviceProps.put(DmtEventListener.FILTER_EVENT, Integer.valueOf(type));
		serviceProps.put(DmtEventListener.FILTER_SUBTREE, new String[] {uri});
		ServiceRegistration<DmtEventListener> registration = context
				.registerService(DmtEventListener.class, listener,
						serviceProps);
		listenerRegistrationMap.put(listener, registration);
	}

	protected void addDmtEventListener(int type, String uri, DmtEventListener listener) {
		Dictionary<String,Object> serviceProps = new Hashtable<>();
		serviceProps.put(DmtEventListener.FILTER_EVENT, Integer.valueOf(type));
		serviceProps.put(DmtEventListener.FILTER_SUBTREE, new String[] {uri});
		ServiceRegistration<DmtEventListener> registration = context
				.registerService(DmtEventListener.class, listener,
						serviceProps);
		listenerRegistrationMap.put(listener, registration);
	}

	protected void removeDmtEventListener(DmtEventListener listener) {
		ServiceRegistration< ? > registration = listenerRegistrationMap
				.get(listener);
		if (registration != null) {
			registration.unregister();
		}
	}
}
