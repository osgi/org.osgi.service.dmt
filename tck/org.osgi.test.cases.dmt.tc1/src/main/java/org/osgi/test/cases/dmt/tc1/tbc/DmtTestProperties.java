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

package org.osgi.test.cases.dmt.tc1.tbc;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

/**
 * The property helpers of OSGiTestCaseProperties, unchanged.
 */
final class DmtTestProperties {
	private DmtTestProperties() {
		// empty
	}

	/**
	 * Return the property value from the bundle context properties.
	 * 
	 * @param key The property key name.
	 * @return The property value or null if the property is not set.
	 */
	public static String getProperty(String key) {
		return AccessController.doPrivileged((PrivilegedAction<String>) () -> {
			Bundle bundle = FrameworkUtil
					.getBundle(DmtTestProperties.class);
			if (bundle != null) {
				BundleContext context;
				try {
					context = bundle.getBundleContext();
				} catch (SecurityException e) {
					context = null;
				}
				if (context != null) {
					return context.getProperty(key);
				}
			}
			return System.getProperty(key);
		});
	}
}
