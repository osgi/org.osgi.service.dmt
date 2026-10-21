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
package org.osgi.test.cases.residentialmanagement;

import static org.assertj.core.api.Assertions.assertThat;
import static org.osgi.test.assertj.bundle.BundleAssert.assertThat;

import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.startlevel.FrameworkStartLevel;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtSession;

/**
 * This test case checks the possible operations on the Framework subtree
 * This includes 
 * - get and set the Framework-Startlevel
 * - install and uninstall a bundle
 * - change the state of an installed bundle
 * 
 * @author Steffen Druesedow (Deutsche Telekom Laboratories)
 */
public class FrameworkOperationsTestCase extends RMTTestBase {

	
	/**
	 * asserts that StartLevels can be set via the RMT
	 * @throws Exception 
	 */
	@Test
	public void testSetFrameworkStartLevels() throws Exception {

		FrameworkStartLevel fwStartLevel = context.getBundle(0).adapt(FrameworkStartLevel.class);
		int initialStartLevel = fwStartLevel.getStartLevel();
		int initialBundleStartLevel = fwStartLevel.getInitialBundleStartLevel();
		
		// read value and perform changes in an atomic session
		session = dmtAdmin.getSession(FRAMEWORK_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FRAMEWORK_ROOT).isNotNull();
		int rmtStartLevel = session.getNodeValue(FRAMEWORK_ROOT + "/" + STARTLEVEL ).getInt();
		int rmtInitialBundleStartLevel = session.getNodeValue(FRAMEWORK_ROOT + "/" + INITIAL_BUNDLE_STARTLEVEL ).getInt();
		
		assertThat(rmtStartLevel).as("The value of the StartLevel is incorrect.").isEqualTo(initialStartLevel);
		assertThat(rmtInitialBundleStartLevel).as("The value of the InitialBundleStartLevel is incorrect.").isEqualTo(initialBundleStartLevel);

		try {
			// set a new value for the startlevel
			session.setNodeValue(FRAMEWORK_ROOT + "/" + STARTLEVEL , new DmtData(initialStartLevel + 1));
			session.setNodeValue(FRAMEWORK_ROOT + "/" + INITIAL_BUNDLE_STARTLEVEL, new DmtData(initialBundleStartLevel + 1));
			
			session.commit();
			
			// check the new values in a new shared session
			rmtStartLevel = session.getNodeValue(FRAMEWORK_ROOT + "/" + STARTLEVEL ).getInt();
			rmtInitialBundleStartLevel = session.getNodeValue(FRAMEWORK_ROOT + "/" + INITIAL_BUNDLE_STARTLEVEL ).getInt();
			
			assertThat(fwStartLevel.getStartLevel()).as("The value of the Framework StartLevel was not set correctly.").isEqualTo(initialStartLevel + 1);
			assertThat(fwStartLevel.getInitialBundleStartLevel()).as("The value of the Framework initialBundleStartLevel was not set correctly.").isEqualTo(initialBundleStartLevel + 1);
		}
		finally {
			fwStartLevel.setStartLevel(initialStartLevel, (FrameworkListener[]) null);
			fwStartLevel.setInitialBundleStartLevel(initialBundleStartLevel);
		}
	}
	
	
	/**
	 * tests installation and uninstallation of a bundle via RMT
	 * @throws Exception 
	 */
	@Test
	public void testBundleInstallUninstall() throws Exception {
		final String BUNDLE_KEY = "TestBundle";
		final String BUNDLE_URL = context.getBundle().getEntry(TESTBUNDLE_EXPORTPACKAGE).toString();

		
		session = dmtAdmin.getSession(FRAMEWORK_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FRAMEWORK_ROOT).isNotNull();

		String bundleUri = BUNDLE + "/" + BUNDLE_KEY;
		// --------- Install a bundle -----------
		// ensure that no such bundle exists already in the RMT
		assertThat(session.isNodeUri(bundleUri)).as("TestBundle must not be in the bundle list initially!").isFalse();
		session.createInteriorNode(bundleUri);
		session.setNodeValue(bundleUri + "/" + URL_STRING, new DmtData(BUNDLE_URL));
		session.setNodeValue(bundleUri + "/" + AUTOSTART, new DmtData(true));
		session.setNodeValue(bundleUri + "/" + REQUESTED_STATE, new DmtData("ACTIVE"));

		session.commit();
		testBundle1 = context.getBundle("TestBundle");
		assertThat(testBundle1).as("No bundle with this location is installed.").isNotNull();
		assertThat(testBundle1).as("The testBundle should be in 'ACTIVE' state now.").isInState(Bundle.ACTIVE);
		
		// check that the bundle has been installed, there are no faults and the state is active
		// TODO: check FaultType/FaultMessage after discussion about cardinality is finished
//		assertFalse( "There must be no FaultType after successfull operation.", session.isNodeUri(bundleUri + "/" + FAULT_TYPE));
//		assertFalse( "There must be no FaultMessage after successfull operation.", session.isNodeUri(bundleUri + "/" + FAULT_MESSAGE));
		assertThat(session.isNodeUri(bundleUri + "/" + STATE)).as("There must be a State node after successfull installation.").isTrue();
		assertThat(session.getNodeValue(bundleUri + "/" + STATE).getString()).as(bundleUri + "/" + STATE + " node value is not correct!").isEqualTo("ACTIVE");
		assertThat(session.isNodeUri(bundleUri + "/" + BUNDLEID)).as("TestBundle must have a bundle id after successfull installation!").isTrue();
		assertThat(session.isNodeUri(bundleUri + "/" + VERSION)).as("TestBundle must have a bundle-version after successfull installation!").isTrue();
		assertThat(session.isNodeUri(bundleUri + "/" + SYMBOLIC_NAME)).as("TestBundle must have a SymbolicName after successfull installation!").isTrue();
		
		long id = session.getNodeValue(bundleUri + "/" + BUNDLEID).getLong();
		
		
		// --------- Uninstall a bundle -----------
		// uninstall bundle via RMT
		session.setNodeValue(bundleUri + "/" + REQUESTED_STATE, new DmtData("UNINSTALLED"));
		session.commit();
		
		// check that the bundle has been uninstalled and there are no faults 
		// TODO: check FaultType/FaultMessage after discussion about cardinality is finished
//		assertFalse( "There must be no FaultType after successfull operation.", session.isNodeUri(bundleUri + "/" + FAULT_TYPE));
//		assertFalse( "There must be no FaultMessage after successfull operation.", session.isNodeUri(bundleUri + "/" + FAULT_MESSAGE));
		assertThat(session.isNodeUri(bundleUri + "/" + STATE)).as("There must be no State node after un-installation.").isFalse();
		
		assertThat(context.getBundle(id)).as("The testBundle was not un-installed successfully!").isNull();
		
	}
	
	
	/**
	 * tests state-changes of a bundle via the RMT (starting/stopping)
	 * @throws Exception 
	 */
	@Test
	public void testBundleStateChanges() throws Exception {
		testBundle1 = installer.installBundle(context.getBundle().getEntry(TESTBUNDLE_EXPORTPACKAGE), false);
		
		// ensure that the bundle is in INSTALLED state
		assertThat(Bundle.ACTIVE == testBundle1.getState()).as("testbundle should not be in 'ACTIVE' state initially").isFalse();
		
		session = dmtAdmin.getSession(FRAMEWORK_ROOT, DmtSession.LOCK_TYPE_ATOMIC);
		assertThat(session).as("Null DMT session for: " + FRAMEWORK_ROOT).isNotNull();
		String uri = FRAMEWORK_ROOT + "/" + BUNDLE;
		
		// get the corresponding bundle entry in the RMT 
		String bundleBaseUri = null;
		String[] bundles = session.getChildNodeNames(uri);
		for (String bundle : bundles ) {
			long id = session.getNodeValue(uri + "/" + bundle + "/" + BUNDLEID).getLong();
			if ( id == testBundle1.getBundleId() )
				bundleBaseUri = uri + "/" + bundle; 
		}
		assertThat(bundleBaseUri).as("Can't find the testBundle in the RMT bundle map!").isNotNull();

		// check initial state of testBundle in the RMT
		assertThat(session
						.getNodeValue(bundleBaseUri + "/" + STATE).getString()).as("testBundle initial state is not correct!").isEqualTo(getBundleStateString(testBundle1.getState()));
		// attempt bundle resolving
		session.setNodeValue(bundleBaseUri + "/" + REQUESTED_STATE, new DmtData("RESOLVED"));
		session.commit();

		// check real bundle state
		assertThat(testBundle1).as("testBundle should be in 'RESOLVED' state now.").isInState(Bundle.RESOLVED);

		// check for Faults in previous commit
		assertThat(session.getNodeValue(bundleBaseUri + "/" + FAULT_TYPE).getInt()).as("There is an unexpected fault type in: " + bundleBaseUri
				+ "/" + FAULT_TYPE).isEqualTo(-1);
		assertThat(session
				.getNodeValue(bundleBaseUri + "/" + FAULT_MESSAGE).getString()).as("There is an unexpected fault message in: "
				+ bundleBaseUri + "/" + FAULT_MESSAGE).isEmpty();
		// check state of testBundle in the RMT
		assertThat(session
				.getNodeValue(bundleBaseUri + "/" + STATE).getString()).as("testBundle state is not correct!").isEqualTo("RESOLVED");
		// attempt bundle start
		session.setNodeValue(bundleBaseUri + "/" + REQUESTED_STATE, new DmtData("ACTIVE"));
		session.commit();
		
		// check real bundle state
		assertThat(testBundle1).as("testBundle should be in 'ACTIVE' state now.").isInState(Bundle.ACTIVE);
		
		// check bundle state in RMT now and stop it again via RMT
		// check for Faults in previous commit
		assertThat(session.getNodeValue(bundleBaseUri + "/" + FAULT_TYPE).getInt()).as("There is an unexpected fault type in: " + bundleBaseUri
				+ "/" + FAULT_TYPE).isEqualTo(-1);
		assertThat(session
				.getNodeValue(bundleBaseUri + "/" + FAULT_MESSAGE).getString()).as("There is an unexpected fault message in: "
				+ bundleBaseUri + "/" + FAULT_MESSAGE).isEmpty();
		assertThat(session
				.getNodeValue(bundleBaseUri + "/" + STATE).getString()).as("testBundle state is not correct!").isEqualTo("ACTIVE");
		session.setNodeValue(bundleBaseUri + "/" + REQUESTED_STATE, new DmtData("RESOLVED"));
		session.commit();
		
		// check real bundle state
		assertThat(testBundle1).as("testBundle should be in 'RESOLVED' state now.").isInState(Bundle.RESOLVED);

		// check bundle state in RMT again
		assertThat(session
				.getNodeValue(bundleBaseUri + "/" + STATE).getString()).as("testBundle state is not correct!").isEqualTo("RESOLVED");
	}


}
