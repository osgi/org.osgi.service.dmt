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
 * Jan 26, 2005  Leonardo Barros
 * 1             Implement TCK
 * ============  ==============================================================
 * Feb 14, 2005  Luiz Felipe Guimaraes
 * 1             Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-001)
 * ============  ==============================================================
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtException;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Vector;

import org.junit.jupiter.api.Test;
import org.osgi.service.dmt.DmtIllegalStateException;
import org.osgi.test.cases.dmt.tc1.tbc.DmtConstants;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * This class tests DmtException constructors according to MEG specification 
 */

public class DmtException extends DmtTestControl {
	private static final String EXCEPTION_MSG = "exception_msg";

	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String,int,String)
	 * 
	 * @spec DmtException.DmtException(String,int,String)
	 * 
	 */
	@Test
	public void testDmtException001() {
		System.out.println("#testDmtException001");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				DmtConstants.OSGi_LOG,
				org.osgi.service.dmt.DmtException.INVALID_URI,
				DmtConstants.MESSAGE);

		assertThat(de
				.getURI()).as("Asserts getURI() method").isEqualTo(DmtConstants.OSGi_LOG);
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("INVALID_URI: '"
				+ DmtConstants.OSGi_LOG + "': " + DmtConstants.MESSAGE);
		
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.INVALID_URI);
		
		assertThat(de.getCauses().length).as("Asserts getCauses() method").isZero();
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();
	}
	
	
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String,int,String) and 'null' values whenever it is possible
	 * 
	 * @spec DmtException.DmtException(String,int,String)
	 * 
	 */
	@Test
	public void testDmtException002() {
		System.out.println("#testDmtException002");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException((String)null, org.osgi.service.dmt.DmtException.CONCURRENT_ACCESS, null);
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
        
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.CONCURRENT_ACCESS);
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("CONCURRENT_ACCESS");
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length==0).as("Asserts that getCauses() returns an empty array if there is no cause").isTrue();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();
		
	}
	
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String[],int,String)
	 * 
	 * @spec DmtException.DmtException(String[],int,String)
	 * 
	 */
	@Test
	public void testDmtException003() {
		System.out.println("#testDmtException003");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				new String[] {".","a","b","c" },
				org.osgi.service.dmt.DmtException.COMMAND_NOT_ALLOWED,
				DmtConstants.MESSAGE);

		String uri = "./a/b/c";

		assertThat(de.getURI()).as("Asserts getURI() method").isEqualTo(uri);

        assertThat(de.getMessage()).as("Asserts getMessage() method").isEqualTo("COMMAND_NOT_ALLOWED: '"
				+ uri + "': " + DmtConstants.MESSAGE);

		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.COMMAND_NOT_ALLOWED);
		
		assertThat(de.getCauses().length).as("Asserts getCauses() method").isZero();
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();

	}
	
	
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String[],int,String) and 'null' values whenever it is possible
	 * 
	 * @spec DmtException.DmtException(String[],int,String)
	 * 
	 */
	@Test
	public void testDmtException004() {
		System.out.println("#testDmtException004");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException((String[])null, org.osgi.service.dmt.DmtException.ALERT_NOT_ROUTED, null);
		
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
		
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.ALERT_NOT_ROUTED);
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("ALERT_NOT_ROUTED");
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length==0).as("Asserts that getCauses() returns an empty array if there is no cause").isTrue();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();
		
	}

	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
	 * DmtException(String,int,String,Throwable)
     * 
	 * @spec DmtException.DmtException(String,int,String,Throwable)
	 * 
	 */
	@Test
	public void testDmtException005() {
		System.out.println("#testDmtException005");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				DmtConstants.OSGi_LOG,
				org.osgi.service.dmt.DmtException.CONCURRENT_ACCESS,
				DmtConstants.MESSAGE, new Exception());

		assertThat(de
				.getURI()).as("Asserts getURI() method").isEqualTo(DmtConstants.OSGi_LOG);
		

        assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("CONCURRENT_ACCESS: '"
				+ DmtConstants.OSGi_LOG + "': " + DmtConstants.MESSAGE);
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.CONCURRENT_ACCESS);
		
		assertException("Asserts getCause() method", Exception.class, de
				.getCause());
		
		assertThat(de.getCauses().length).as("Asserts getCauses() method").isOne();
		
		assertException("Asserts getCauses() method", Exception.class,
				de
				.getCauses()[0]);
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();
	}
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String,int,String,Throwable)  and 'null' values whenever it is possible
	 * 
	 * @spec DmtException.DmtException(String,int,String,Throwable)
	 * 
	 */
	@Test
	public void testDmtException006() {
		System.out.println("#testDmtException006");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException((String)null, org.osgi.service.dmt.DmtException.COMMAND_FAILED, null,
				(Throwable)null);
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("COMMAND_FAILED");
		
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.COMMAND_FAILED);
		
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length==0).as("Asserts that getCauses() returns an empty array if there is no cause").isTrue();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();

	}	
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String[],int,String,Throwable)
	 * 
	 * @spec DmtException.DmtException(String[],int,String,Throwable)
	 * 
	 */
	@Test
	public void testDmtException007() {
		System.out.println("#testDmtException007");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				new String[] {".","d", "e", "f"},
				org.osgi.service.dmt.DmtException.REMOTE_ERROR,
				DmtConstants.MESSAGE, new IllegalArgumentException());
		String uri = "./d/e/f";

		assertThat(de.getURI()).as("Asserts getURI() method").isEqualTo(uri);

        assertThat(de.getMessage()).as("Asserts getMessage() method").isEqualTo("REMOTE_ERROR: '" + uri
				+ "': " + DmtConstants.MESSAGE);
        
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.REMOTE_ERROR);
		
		assertException("Asserts getCause() method",
				IllegalArgumentException.class, de.getCause());
		
		assertThat(de.getCauses().length).as("Asserts getCauses() method").isOne();
		
		assertException("Asserts getCauses() method",
				IllegalArgumentException.class,
				de.getCauses()[0]);
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();
	}
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
	 * DmtException(String[],int,String,Throwable) and 'null' values whenever it is possible
     * 
	 * @spec DmtException.DmtException(String[],int,String,Throwable)
	 * 
	 */
	@Test
	public void testDmtException008() {
		System.out.println("#testDmtException008");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException((String[])null, org.osgi.service.dmt.DmtException.URI_TOO_LONG, null,
				(Throwable)null);
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("URI_TOO_LONG");
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.URI_TOO_LONG);
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length==0).as("Asserts that getCauses() returns an empty array if there is no cause").isTrue();
		assertThat(!de.isFatal()).as("Asserts isFatal() method").isTrue();

	}
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
     * DmtException(String,int,String,Vector,boolean)
	 * 
	 * @spec DmtException.DmtException(String,int,String,Vector,boolean)
	 * 
	 */
	@Test
	public void testDmtException009() {
		System.out.println("#testDmtException009");
		Vector<Exception> causes = new Vector<>();
		causes.add(0, new DmtIllegalStateException(EXCEPTION_MSG));
		causes.add(1, new Exception(EXCEPTION_MSG));
		causes.add(2, new IllegalArgumentException(EXCEPTION_MSG));
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				DmtConstants.OSGi_CONFIGURATION,
				org.osgi.service.dmt.DmtException.COMMAND_NOT_ALLOWED,
				DmtConstants.MESSAGE, causes,true);

		assertThat(de.getURI()).as("Asserts getURI() method").isEqualTo(DmtConstants.OSGi_CONFIGURATION);
		
        assertThat(de.getMessage()).as("Asserts getMessage() method").isEqualTo("COMMAND_NOT_ALLOWED: '"
				+ DmtConstants.OSGi_CONFIGURATION + "': "
				+ DmtConstants.MESSAGE);
        
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.COMMAND_NOT_ALLOWED);
		
		assertException(
				"Asserts that getCause returns the first exception in case of more than one exception", 
				DmtIllegalStateException.class, de.getCause());
		
		Throwable[] causesReturned = de.getCauses();
		assertThat(causesReturned.length).as("Asserts the size of getCauses() method").isEqualTo(causes.size());
		
		int found=0;
		int expected = causes.size();
        for (int i = 0; i < causesReturned.length; i++) {
            Object obj = causesReturned[i];
            if (obj instanceof DmtIllegalStateException) {
                if (((DmtIllegalStateException)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            } else if (obj instanceof Exception) {
                if (((Exception)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            } else if (obj instanceof IllegalArgumentException) {
                if (((IllegalArgumentException)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            }           
            
		}
		assertThat(found).as("Asserts the all of the exceptions were returned correctly").isEqualTo(expected);
		assertThat(de.isFatal()).as("Asserts isFatal() method").isTrue();
	}

	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
	 * DmtException(String,int,String,Vector,boolean) and 'null' values whenever it is possible
     * 
	 * @spec DmtException.DmtException(String,int,String,Vector,boolean)
	 * 
	 */
	@Test
	public void testDmtException010() {
		System.out.println("#testDmtException010");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				(String) null,
				org.osgi.service.dmt.DmtException.TRANSACTION_ERROR, null, null,
				true);
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("TRANSACTION_ERROR");
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.TRANSACTION_ERROR);
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length == 0).as("Asserts that getCauses() returns an empty array if there is no cause").isTrue();
		assertThat(de.isFatal()).as("Asserts isFatal() method").isTrue();
	}
	
	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
	 * DmtException(String[],int,String,Vector,boolean)
     * 
	 * @spec DmtException.DmtException(String[],int,String,Vector,boolean)
	 * 
	 */
	@Test
	public void testDmtException011() {
		System.out.println("#testDmtException011");
		Vector<Exception> causes = new Vector<>();
		causes.add(0, new IllegalArgumentException(EXCEPTION_MSG));
		causes.add(1, new Exception(EXCEPTION_MSG));
		causes.add(2, new DmtIllegalStateException(EXCEPTION_MSG));
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				new String [] {".","g", "h", "i"},
				org.osgi.service.dmt.DmtException.FEATURE_NOT_SUPPORTED,
				DmtConstants.MESSAGE, causes, true);

		String uri = "./g/h/i";

		assertThat(de.getURI()).as("Asserts getURI() method").isEqualTo(uri);

        assertThat(de.getMessage()).as("Asserts getMessage() method").isEqualTo("FEATURE_NOT_SUPPORTED: '"
				+ uri + "': " + DmtConstants.MESSAGE);
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.FEATURE_NOT_SUPPORTED);

		assertException(
				"Asserts that getCause returns the first exception in case of more than one exception", 
				IllegalArgumentException.class, de.getCause());
		
		Throwable[] causesReturned = de.getCauses();
		assertThat(causesReturned.length).as("Asserts the size of getCauses() method").isEqualTo(causes.size());
        
        int found=0;
        int expected = causes.size();
        for (int i = 0; i < causesReturned.length; i++) {
            Object obj = causesReturned[i];
            if (obj instanceof DmtIllegalStateException) {
                if (((DmtIllegalStateException)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            } else if (obj instanceof Exception) {
                if (((Exception)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            } else if (obj instanceof IllegalArgumentException) {
                if (((IllegalArgumentException)obj).toString().indexOf(EXCEPTION_MSG)>-1) {
                    found++;
                }
            }
		}

		assertThat(found).as("Asserts the all of the exceptions were returned correctly").isEqualTo(expected);
		assertThat(de.isFatal()).as("Asserts isFatal() method").isTrue();
	}

	/**
	 * Tests if the values that are passed as parameters for the constructor are equal 
	 * to the values returned through its corresponding get methods using the constructor
	 * DmtException(String[],int,String,Vector,boolean) and 'null' values whenever it is possible
     * 
	 * @spec DmtException.DmtException(String[],int,String,Vector,boolean)
	 * 
	 */
	@Test
	public void testDmtException012() {
		System.out.println("#testDmtException012");
		org.osgi.service.dmt.DmtException de = new org.osgi.service.dmt.DmtException(
				(String[]) null,
				org.osgi.service.dmt.DmtException.NODE_ALREADY_EXISTS, null,
				null, true);
		
		assertThat(de.getURI()).as("Asserts getURI() method").isNull();
		assertThat(de
				.getMessage()).as("Asserts getMessage() method").isEqualTo("NODE_ALREADY_EXISTS");
		assertThat(de.getCode()).as("Asserts getCode() method").isEqualTo(org.osgi.service.dmt.DmtException.NODE_ALREADY_EXISTS);
		assertThat(de.getCause()).as("Asserts getCause() method").isNull();
		assertThat(de.getCauses().length==0).as("Asserts that getCauses() returns an empty Vector if there is no cause").isTrue();
		
		assertThat(de.isFatal()).as("Asserts isFatal() method").isTrue();
	}
	

}
