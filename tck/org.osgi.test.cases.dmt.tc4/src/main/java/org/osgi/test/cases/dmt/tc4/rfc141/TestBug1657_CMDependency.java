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
package org.osgi.test.cases.dmt.tc4.rfc141;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.dmt.DmtAdmin;
import org.osgi.test.common.annotation.InjectBundleContext;

/**
 * This testcase tests that there is no blocking dependency from
 * ConfigurationAdmin at startup of the ConfigurationAdmin.
 *
 * It corresponds to the bug report No. 1657 at Bugzilla
 * see: https://www.osgi.org/members/bugzilla/show_bug.cgi?id=1657
 * @author steffen
 *
 */
public class TestBug1657_CMDependency{

	@InjectBundleContext
	BundleContext		context;

	Bundle configAdminBundle;
	Bundle dmtAdminBundle;

	@BeforeEach
	public void setUp() throws Exception {
		System.out.println("setting up");

		// stopping the configuration admin service
		ServiceReference< ? > ref = context
				.getServiceReference(ConfigurationAdmin.class);
		if (ref != null ) {
			configAdminBundle = ref.getBundle();
			configAdminBundle.stop();
			System.out.println( "stopped configuration-admin bundle" );
			Thread.sleep(200);
		}
		ref = context.getServiceReference(DmtAdmin.class);
		if (ref != null ) {
			dmtAdminBundle = ref.getBundle();
			dmtAdminBundle.stop();
			System.out.println( "stopped dmt-admin bundle" );
			Thread.sleep(200);
		}
	}

	@AfterEach
	public void tearDown() throws Exception {
		System.out.println( "tearing down");
	}


	/**
	 * tests existence of specified constants for data formats in org.osgi.service.dmt.DmtData
	 */
	@Test
	public void testDmtAdminStartWithoutCM() throws Exception {
		dmtAdminBundle.start();
		configAdminBundle.start();
	}

}
