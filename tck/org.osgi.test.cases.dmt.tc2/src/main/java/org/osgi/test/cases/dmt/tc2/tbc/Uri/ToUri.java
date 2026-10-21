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
 * Apr 17, 2006  Luiz Felipe Guimaraes
 * 283           [MEGTCK][DMT] DMT API changes
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc2.tbc.Uri;

import static org.assertj.core.api.Assertions.assertThat;

import org.osgi.service.dmt.Uri;
import org.osgi.test.cases.dmt.tc2.tbc.DmtTestControl;



/**
 * @author Luiz Felipe Guimaraes
 * 
 * This Class Validates the implementation of <code>Uri.toUri(String[])<code> method, 
 * according to MEG specification
 */
public class ToUri {
	private DmtTestControl tbc;
	
	public ToUri(DmtTestControl tbc) {
		this.tbc = tbc;
	}

	public void run() {
		testToUri001();
		testToUri002();
		testToUri003();
		testToUri004();
	}

	/**
	 * Asserts that NullPointerException is thrown if the specified URI is null 
	 * 
	 * @spec Uri.toUri(String[])
	 */
	private void testToUri001() {

		try {
			System.out.println("#testToUri001");
			Uri.toUri(null);
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class,e);
		}
	
	}
	
	/**
	 * Asserts that NullPointerException is thrown if any segment of the URI is null 
	 * 
	 * @spec Uri.toUri(String[])
	 */
	private void testToUri002() {

		try {
			System.out.println("#testToUri002");
			Uri.toUri(new String[] { ".", "Test",null, "Dmt" });
			DmtTestControl.failException("", NullPointerException.class);
		} catch (NullPointerException e) {
			System.out.println("NullPointerException correctly thrown");
		} catch (Exception e) {
			tbc.failExpectedOtherException(NullPointerException.class,e);
		}
	
	}
	/**
	 * Asserts that Uri.toUri(String[]) returns an empty URI ("") 
	 * if the specified path is an empty array
	 *   
	 * @spec Uri.touri(String[])
	 */
	private void testToUri003() {

		try {
			System.out.println("#testToUri003");
			assertThat(Uri.toUri(new String[0])).as("Asserts that Uri.toUri(String[]) returns an empty URI (\"\") if " +
					"the specified path is an empty array").isEqualTo("");
		} catch (Exception e) {
			tbc.failUnexpectedException(e);
		}
	}
	
	/**
	 * Asserts that IllegalArgumentException is thrown if the specified URI is malformed 
	 * 
	 * @spec Uri.toUri(String[])
	 */

	private void testToUri004() {
		try {
			System.out.println("#testToUri004");
			for (int i = 0; i < DmtTestControl.URIS_TOO_LONG.length; i++) {
				try {
					Uri.toUri(DmtTestControl.toPath(DmtTestControl.URIS_TOO_LONG[i]));
					DmtTestControl.failException("", IllegalArgumentException.class);
				} catch (IllegalArgumentException e) {
					System.out.println("IllegalArgumentException correctly thrown when calling Uri.toUri(" + DmtTestControl.URIS_TOO_LONG[i] + ")");
				}
			}
			
		} catch (Exception e) {
			tbc.failExpectedOtherException(IllegalArgumentException.class,e);
		}
	}
	
}

