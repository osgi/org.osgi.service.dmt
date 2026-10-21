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
 * Jul 12, 2005  Luiz Felipe Guimaraes
 * 118           Implement sendAlert
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tb1.NotificationService;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServicePermission;
import org.osgi.framework.ServiceReference;
import org.osgi.service.dmt.DmtData;
import org.osgi.service.dmt.DmtException;
import org.osgi.service.dmt.notification.AlertItem;
import org.osgi.service.dmt.notification.NotificationService;
import org.osgi.service.dmt.security.AlertPermission;
import org.osgi.service.permissionadmin.PermissionInfo;
import org.osgi.test.cases.dmt.tc2.tbc.Activators.DefaultRemoteAlertSenderActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Activators.DefaultRemoteAlertSenderImpl;
import org.osgi.test.cases.dmt.tc2.tbc.Activators.HighestRankingRemoteAlertSenderActivator;
import org.osgi.test.cases.dmt.tc2.tbc.Activators.HighestRankingRemoteAlertSenderImpl;
import org.osgi.test.cases.dmt.tc2.tbc.Activators.RemoteAlertSenderImpl;
import org.osgi.test.cases.dmt.tc2.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;
import org.osgi.test.cases.dmt.tc2.tbc.Plugin.ExecPlugin.TestExecPluginActivator;
import org.osgi.test.cases.dmt.tc2.tbc.TestInterface;


/**
 * @author Luiz Felipe Guimaraes
 * 
 *         This test case validates the implementation of
 *         <code>sendNotification</code> method of NotificationService,
 *         according to MEG specification
 */
public class SendNotification implements TestInterface {
	private DmtTestControl tbc;

	private NotificationService notificationService;
	
	public SendNotification(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	@Override
	public void run() {
		prepare();
		if (DmtConstants.SUPPORTS_ASYNCHRONOUS_NOTIFICATION) {
			testSendNotification001();
			testSendNotification002();
			testSendNotification003();
			testSendNotification004();
			testSendNotification005();
			testSendNotification006();
			testSendNotification007();
			testSendNotification008();
			testSendNotification009();
			testSendNotification010();
			testSendNotification011();
		} else {
			testSendNotificationFeatureNotSupported001();
		}
	}

	private void prepare() {
		tbc.setPermissions(new PermissionInfo(AlertPermission.class.getName(),
				"*", "*"));
	}

	private void sendNotification(String principal, int code, String correlator,
			AlertItem[] items) throws DmtException {
		if (notificationService == null) {
			BundleContext bc = tbc.getContext();
			ServiceReference<NotificationService> sr = bc
					.getServiceReference(NotificationService.class);
			notificationService = bc.getService(sr);
		}
		notificationService.sendNotification(principal, code, correlator, items);
	}
	
	/**
	 * It tests if the DmtAdmin.SendNotification really fowards the parameters
	 * to the RemoteAlertSender. It also tests that only the specified principal
	 * needs AlertPermission.
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification001() {
		try {
			System.out.println("#testSendNotification001");

			String mark = "mark";
			DmtData data = new DmtData("test");
			AlertItem item = new AlertItem(DmtConstants.OSGi_LOG,
					DmtConstants.MIMETYPE, mark, data);

			String mark2 = "mark2";
			DmtData data2 = new DmtData(10);
			String mimetype2 = "text/xml";
			AlertItem item2 = new AlertItem(TestExecPluginActivator.ROOT,
					mimetype2, mark2, data2);

			String correlator = "correlator";
			int code = 2;
			AlertItem[] items = new AlertItem[] { item, item2 };

			RemoteAlertSenderImpl.resetAlert();

			tbc.setPermissions(new PermissionInfo(AlertPermission.class
					.getName(), DmtConstants.PRINCIPAL, "*"));

			sendNotification(DmtConstants.PRINCIPAL,code,correlator,items);

			assertThat(RemoteAlertSenderImpl.codeFound == code).as("Asserts that the code sent by sendNotification was the expected").isTrue();
			assertThat(RemoteAlertSenderImpl.correlatorFound).as("Asserts that the correlator sent by sendNotification was the expected").isEqualTo(correlator);
			assertThat(RemoteAlertSenderImpl.serverIdFound).as("Asserts that the principal sent by sendNotification was the expected").isEqualTo(DmtConstants.PRINCIPAL);
			assertThat(RemoteAlertSenderImpl.itemsFound.length).as("Asserts that the number of AlertItems sent by sendNotification was the expected").isEqualTo(items.length);
			assertThat(RemoteAlertSenderImpl.itemsFound[0].toString()).as("Asserts that the AlertItems sent by sendNotification were the expected").isEqualTo(items[0].toString());
			assertThat(RemoteAlertSenderImpl.itemsFound[1].toString()).as("Asserts that the AlertItems sent by sendNotification were the expected").isEqualTo(items[1].toString());

		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		} finally {
			prepare();
		}
	}

	/**
	 * It tests if the DmtAdmin.sendNotification really fowards the parameters
	 * to the RemoteAlertSender, even if no principal is specified (when there
	 * is only one protocol adapter). It also validates that an empty array for
	 * alert items can be passed and that null can be passed as principal.
	 * Finally, it tests that if the principal parameter is null, the target of
	 * the AlertPermission must be "*".
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification002() {
		try {
			System.out.println("#testSendNotification002");

			int code = 10;
			String correlator = "newCorrelator";
			AlertItem[] items = new AlertItem[] {};

			RemoteAlertSenderImpl.resetAlert();
			sendNotification(null,code,correlator,items);

			assertThat(RemoteAlertSenderImpl.serverIdFound).as("Asserts that if the DmtAdmin is connected to only one protocol adapter, the principal name can be omitted").isNull();
			assertThat(RemoteAlertSenderImpl.codeFound == code).as("Asserts that the code sent by sendNotification was the expected").isTrue();
			assertThat(RemoteAlertSenderImpl.correlatorFound).as("Asserts that the correlator sent by sendNotification was the expected").isEqualTo(correlator);
			assertThat(RemoteAlertSenderImpl.itemsFound.length).as("Asserts that the number of AlertItems sent by sendNotification was the expected").isEqualTo(items.length);

		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		}
	}

	/**
	 * It tests if the correlator parameter can be null as the specified
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification003() {
		try {
			System.out.println("#testSendNotification003");

			String mark = "mark";
			DmtData data = new DmtData("test");
			AlertItem item = new AlertItem(DmtConstants.OSGi_LOG,
					DmtConstants.MIMETYPE, mark, data);

			AlertItem[] items = new AlertItem[] { item };
			int code = 0;
			RemoteAlertSenderImpl.resetAlert();
			sendNotification(DmtConstants.PRINCIPAL,code,null,items);

			assertThat(RemoteAlertSenderImpl.codeFound == code).as("Asserts that the code sent by sendNotification was the expected").isTrue();
			assertThat(RemoteAlertSenderImpl.correlatorFound).as("Asserts that the correlator sent by sendNotification was the expected").isNull();
			assertThat(RemoteAlertSenderImpl.serverIdFound).as("Asserts that the principal sent by sendNotification was the expected").isEqualTo(DmtConstants.PRINCIPAL);
			assertThat(RemoteAlertSenderImpl.itemsFound.length).as("Asserts that the number of AlertItems sent by sendNotification was the expected").isEqualTo(items.length);
			assertThat(RemoteAlertSenderImpl.itemsFound[0].toString()).as("Asserts that the AlertItems sent by sendNotification were the expected").isEqualTo(items[0].toString());

		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		}
	}

	/**
	 * It tests if the items parameter can be null as the specified
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification004() {
		try {
			System.out.println("#testSendNotification004");

			int code = 0;
			String correlator = "newCorrelator";
			RemoteAlertSenderImpl.resetAlert();

			sendNotification(DmtConstants.PRINCIPAL,code,correlator,null);

			assertThat(RemoteAlertSenderImpl.codeFound == code).as("Asserts that the code sent by sendNotification was the expected").isTrue();
			assertThat(RemoteAlertSenderImpl.correlatorFound).as("Asserts that the correlator sent by sendNotification was the expected").isEqualTo(correlator);
			assertThat(RemoteAlertSenderImpl.serverIdFound).as("Asserts that the principal sent by sendNotification was the expected").isEqualTo(DmtConstants.PRINCIPAL);
			assertThat(RemoteAlertSenderImpl.itemsFound).as("Asserts that the number of AlertItems sent by sendNotification was the expected").isNull();

		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		}
	}

	/**
	 * It tests if DmtException.ALERT_NOT_ROUTED is thrown when the alert can
	 * not be routed to the server
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification005() {
		try {
			System.out.println("#testSendNotification005");

			RemoteAlertSenderImpl.resetAlert();
			sendNotification(DmtConstants.PRINCIPAL_3,0,null,null);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that DmtException.ALERT_NOT_ROUTED is thrown when the alert can not be routed to the server").isEqualTo(DmtException.ALERT_NOT_ROUTED);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);

		}
	}

	/**
	 * It tests if any exception thrown on the RemoteAlertSender is propagated
	 * wrapped in a DmtException with the code REMOTE_ERROR
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification006() {
		try {
			System.out.println("#testSendNotification006");

			RemoteAlertSenderImpl.resetAlert();
			sendNotification(DmtConstants.PRINCIPAL,
					RemoteAlertSenderImpl.CODE_EXCEPTION, null, null);

			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			assertThat(e.getCode()).as("Asserts that any exception thrown on the RemoteAlertSender is propagated wrapped in a DmtException with the code REMOTE_ERROR").isEqualTo(DmtException.REMOTE_ERROR);
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);

		}
	}

	/**
	 * If multiple Remote Alert Sender services register for the same service,
	 * then the service with the highest value for the service.ranking property
	 * must be used.
	 * 
	 * @spec 117.8.1 Routing Alerts
	 */
	private void testSendNotification007() {
		HighestRankingRemoteAlertSenderActivator highestRankingRemoteAlertSenderActivator = null;
		try {
			System.out.println("#testSendNotification007");
			tbc.setPermissions(new PermissionInfo[] {
					new PermissionInfo(ServicePermission.class.getName(), "*",
							ServicePermission.REGISTER),
					new PermissionInfo(AlertPermission.class.getName(), "*",
							"*") });

			// Installs the highest ranking remote alert sender
			highestRankingRemoteAlertSenderActivator = new HighestRankingRemoteAlertSenderActivator();
			highestRankingRemoteAlertSenderActivator.start(tbc.getContext());

			HighestRankingRemoteAlertSenderImpl.resetAlert();

			int codeExpected = 21;

			sendNotification(DmtConstants.PRINCIPAL, codeExpected, null, null);

			assertThat(HighestRankingRemoteAlertSenderImpl.codeFound).as("Asserts that if multiple Remote Alert Sender services register for the same service,"
							+ " then the service with the highest value for the service.ranking property must be used.").isEqualTo(codeExpected);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		} finally {
			try {
				highestRankingRemoteAlertSenderActivator.stop(tbc.getContext());
			} catch (Exception e1) {
				System.out.println("#Failed uninstalling the HighestRankingRemoteAlertSender");
			}
			prepare();
		}
	}

	/**
	 * Asserts that if 'principals' property is not registered, the Remote Alert
	 * Sender service is treated as the default sender.
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification008() {
		DefaultRemoteAlertSenderActivator defaultRemoteAlertSenderActivator = null;
		try {
			System.out.println("#testSendNotification008");
			tbc.setPermissions(new PermissionInfo[] {
					new PermissionInfo(ServicePermission.class.getName(), "*",
							ServicePermission.REGISTER),
					new PermissionInfo(AlertPermission.class.getName(), "*",
							"*") });
			// Installs a default alert sender (it doesnt specify the principal
			// in its registration)
			defaultRemoteAlertSenderActivator = new DefaultRemoteAlertSenderActivator();
			defaultRemoteAlertSenderActivator.start(tbc.getContext());

			DefaultRemoteAlertSenderImpl.resetAlert();

			int codeExpected = 11;
			sendNotification(null, codeExpected, null, null);

			assertThat(DefaultRemoteAlertSenderImpl.codeFound).as("Asserts that if 'servers' property is not registered, the Remote Alert Sender service is treated as the default sender.").isEqualTo(codeExpected);
		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		} finally {
			try {
				defaultRemoteAlertSenderActivator.stop(tbc.getContext());
			} catch (Exception e) {
				System.out.println("#Failed stopping the default remote alert sender");
			}
			prepare();
		}
	}

	/**
	 * It tests if the SecurityException is thrown when there is no
	 * AlertPermission for that principal
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification009() {
		try {
			System.out.println("#testSendNotification009");
			tbc.setPermissions(new PermissionInfo[0]);

			sendNotification(DmtConstants.PRINCIPAL, 1, "", new AlertItem[0]);
			DmtTestControl.failException("", SecurityException.class);
		} catch (SecurityException e) {
			System.out.println("SecurityException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
		} finally {
			prepare();
		}
	}

	/**
	 * It tests if the DmtAdmin.SendNotification can foward the alert for many
	 * principals (when more than one principal is registered in the respective
	 * RemoteAlertSender)
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification010() {
		try {
			System.out.println("#testSendNotification010");

			int code = 11;
			String correlator = "correlator2";
			AlertItem[] items = new AlertItem[] { new AlertItem((String) null,
					null, null, null) };

			RemoteAlertSenderImpl.resetAlert();
			sendNotification(DmtConstants.PRINCIPAL_2,code,correlator,items);
			
			assertThat(RemoteAlertSenderImpl.serverIdFound).as("Asserts that the RemoteAlertSender can have more than one principal").isEqualTo(DmtConstants.PRINCIPAL_2);
			assertThat(RemoteAlertSenderImpl.codeFound == code).as("Asserts that the code sent by sendNotification was the expected").isTrue();
			assertThat(RemoteAlertSenderImpl.correlatorFound).as("Asserts that the correlator sent by sendNotification was the expected").isEqualTo(correlator);
			assertThat(RemoteAlertSenderImpl.itemsFound.length).as("Asserts that the number of AlertItems sent by sendNotification was the expected").isEqualTo(items.length);

		} catch (Exception e) {
			tbc.failUnexpectedException(e);

		}
	}

	/**
	 * Asserts that if the principal parameter is null and the target of the
	 * AlertPermission is not "*" SecurityException is thrown.
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotification011() {
		try {
			System.out.println("#testSendNotification011");
			tbc.setPermissions(new PermissionInfo[0]);

			sendNotification(null, 1, "", new AlertItem[0]);
			DmtTestControl.failException("", SecurityException.class);
		} catch (SecurityException e) {
			System.out.println("SecurityException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(SecurityException.class, e);
		} finally {
			prepare();
		}
	}

	/**
	 * Asserts that if the underlying management protocol doesn't support
	 * asynchronous notifications, DmtException.FEATURE_NOT_SUPPORTED is thrown
	 * 
	 * @spec NotificationService.sendNotification(String,int,String,AlertItem[])
	 */
	private void testSendNotificationFeatureNotSupported001() {
		try {
			System.out.println("#testSendNotificationFeatureNotSupported001");
			sendNotification(null, 0, null, null);
			DmtTestControl.failException("", DmtException.class);
		} catch (DmtException e) {
			System.out.println("DmtException.FEATURE_NOT_SUPPORTED correctly thrown because the management "
					+ "protocol doesn't support asynchronous notifications");
		} catch (Exception e) {
			tbc.failExpectedOtherException(DmtException.class, e);

		}
	}
}
