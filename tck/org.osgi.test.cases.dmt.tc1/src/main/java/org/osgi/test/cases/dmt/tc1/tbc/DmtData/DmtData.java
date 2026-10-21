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

/* REVISION HISTORY:
 *
 * Date         Author(s)
 * CR           Headline
 * ===========  ==============================================================
 * 26/01/2005   Andre Assad
 * 1            Implement TCK
 * ===========  ==============================================================
 * 14/02/2005   Leonardo Barros
 * 1            Updates after formal inspection (BTC_MEG_TCK_CODE-INSPR-002)
 */

package org.osgi.test.cases.dmt.tc1.tbc.DmtData;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.osgi.test.cases.dmt.tc1.tbc.DmtTestControl;

/**
 * 
 * This class contains all the test methods for all the DmtData constructors.
 */
public class DmtData extends DmtTestControl {
	/**
	 * This method asserts that when a DmtData(byte[],true) is created, the format is
	 * DmtData.FORMAT_BASE64, DmtData.getSize() and DmtData.getBase64() return the expected value
	 * 
	 * @spec DmtData.DmtData(byte[],boolean)
	 */
	@Test
	public void testDmtData001() {
		try {		
			System.out.println("#testDmtData001");
			byte[] value = new byte[] { 1, 10, 127 };
			String expectedValue= "01 0A 7F";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,true);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_BASE64").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_BASE64);
			
			assertThat(Arrays.equals(value, data
					.getBase64())).as("Asserting the value").isTrue();
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length);
			
			assertThat(data.toString()).as("Asserts that two-digit hexadecimal numbers for each byte separated by spaces is "
							+
					"returned in case of FORMAT_BASE64").isEqualTo(expectedValue);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	
	/**
	 * This method asserts that when a DmtData(byte[]) is created, the format is
	 * DmtData.FORMAT_BINARY, DmtData.getSize() and DmtData.getBinary() return the expected value
	 * 
	 * @spec DmtData.DmtData(byte[])
	 */
	@Test
	public void testDmtData002() {
		try {		
			System.out.println("#testDmtData002");
			byte[] value = new byte[] { 3, 55, 100 };
			String expectedValue= "03 37 64";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_BINARY").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_BINARY);
			
			assertThat(Arrays.equals(value, data
					.getBinary())).as("Asserting the value").isTrue();
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length);
			
			assertThat(data.toString()).as("Asserts that two-digit hexadecimal numbers for each byte separated by "
							+
					"spaces is returned in case of FORMAT_BINARY").isEqualTo(expectedValue);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	
	/**
	 * This method asserts that when a DmtData(boolean) is created, the format is
	 * DmtData.FORMAT_BOOLEAN, DmtData.getSize() and DmtData.getBoolean() return the expected value
	 * 
	 * @spec DmtData.DmtData(boolean)
	 */
	@Test
	public void testDmtData003() {
		try {		
			System.out.println("#testDmtData003");
			boolean value = false;
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_BOOLEAN").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_BOOLEAN);
			
			assertThat(data.getBoolean()).as("Asserting the value").isEqualTo(value);
			assertThat(data.getSize()).as("Asserting the size").isOne();
			assertThat(data.toString()).as("Asserting the string returned").isEqualTo(String.valueOf(value));
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	
	/**
	 * This method asserts that when a DmtData(String,DmtData.FORMAT_DATE) is created, the format is
	 * DmtData.FORMAT_DATE, DmtData.getSize() and DmtData.getDate() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData004() {
		try {		
			System.out.println("#testDmtData004");
			String value = "20051001";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,org.osgi.service.dmt.DmtData.FORMAT_DATE);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_DATE").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_DATE);
			
			assertThat(data.getDate()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
			assertThat(data
					.getSize()).as("Asserting the size").isEqualTo(data.toString().length());
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	
	/**
	 * This method asserts that when a DmtData(float) is created, the format is
	 * DmtData.FORMAT_FLOAT, DmtData.getSize() and DmtData.getFloat() return the expected value
	 * 
	 * @spec DmtData.DmtData(float)
	 */
	@Test
	public void testDmtData005() {
		try {		
			System.out.println("#testDmtData005");
			float value = 2.4F;
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_FLOAT").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_FLOAT);
			
			assertThat(value == data.getFloat()).as("Asserting the value").isTrue();
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(4);
			
			assertThat(data.toString()).as("Asserting the string returned").isEqualTo(String.valueOf(value));
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	/**
	 * This method asserts that when a DmtData(int) is created, the format is
	 * DmtData.FORMAT_INTEGER, DmtData.getSize() and DmtData.getInt() return the expected value
	 * 
	 * @spec DmtData.DmtData(int)
	 */
	@Test
	public void testDmtData006() {
		try {		
			System.out.println("#testDmtData006");
			int value = 2;
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_INTEGER").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_INTEGER);
			
			assertThat(data.getInt()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(4);
			
			assertThat(data.toString()).as("Asserting the string returned").isEqualTo(String.valueOf(value));
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	/**
	 * This method asserts that the format of DmtData.NULL_VALUE is 
	 * DmtData.FORMAT_NULL and DmtData.getSize() return the expected value
	 * 
	 * @spec DmtData.NULL_VALUE
	 */
	@Test
	public void testDmtData007() {
		try {		
			System.out.println("#testDmtData007");
			org.osgi.service.dmt.DmtData data  = org.osgi.service.dmt.DmtData.NULL_VALUE;
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_NULL").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_NULL);
			
			assertThat(data.getSize()).as("Asserting the size").isZero();
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo("null");
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	/**
	 * This method asserts that when a DmtData(String) is created, the format is
	 * DmtData.FORMAT_STRING, DmtData.getSize() and DmtData.getString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String)
	 */
	@Test
	public void testDmtData008() {
		try {		
			System.out.println("#testDmtData008");
			String value = "TestString";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_STRING").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_STRING);
			
			assertThat(data.getString()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length());
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	/**
	 * This method asserts that when a DmtData(String,DmtData.FORMAT_TIME) is created, the format is
	 * DmtData.FORMAT_TIME, DmtData.getSize() and DmtData.getTime() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData009() {
		try {		
			System.out.println("#testDmtData009");
			String value = "120000";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,org.osgi.service.dmt.DmtData.FORMAT_TIME);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_TIME").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_TIME);
			
			assertThat(data.getTime()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
			assertThat(data
					.getSize()).as("Asserting the size").isEqualTo(data.toString().length());
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	/**
	 * This method asserts that when a DmtData(String,DmtData.FORMAT_TIME) is created, the format is
	 * DmtData.FORMAT_TIME, DmtData.getSize() and DmtData.getTime() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData010() {
		try {		
			System.out.println("#testDmtData010");
			String value = "120000Z";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,org.osgi.service.dmt.DmtData.FORMAT_TIME);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_TIME").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_TIME);
			
			assertThat(data.getTime()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	/**
	 * This method asserts that when a DmtData(String,DmtData.FORMAT_XML) is created, the format is
	 * DmtData.FORMAT_XML, DmtData.getSize() and DmtData.getXml() return the expected value.
	 * Is also ensures that the validity of the XML is not checked by this constructor
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData011() {
		try {		
			System.out.println("#testDmtData011");
			String value = "testXml";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,org.osgi.service.dmt.DmtData.FORMAT_XML);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_XML").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_XML);
			
			assertThat(data.getXml()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length());
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
		
	
	/**
	 * This method asserts that when a DmtData(String,DmtData.FORMAT_STRING) is created, the format is
	 * DmtData.FORMAT_STRING, DmtData.getSize() and DmtData.getString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData012() {
		try {		
			System.out.println("#testDmtData012");
			String value = "TestString2";
			
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,org.osgi.service.dmt.DmtData.FORMAT_STRING);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_STRING").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_STRING);
			
			assertThat(data.getString()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length());
			
			assertThat(data
					.toString()).as("Asserting the string returned").isEqualTo(value);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
    
    /**
     * This method asserts that when a DmtData(byte[],false) is created, the format is
     * DmtData.FORMAT_BINARY, DmtData.getSize() and DmtData.getBinary() return the expected value
     * 
     * @spec DmtData.DmtData(byte[],boolean)
     */
    @Test
    public void testDmtData013() {
        try {       
            System.out.println("#testDmtData013");
            byte[] value = new byte[] { 1, 10, 127 };
            String expectedValue= "01 0A 7F";
            org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(value,false);
            assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_BINARY").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_BINARY);
            
            assertThat(Arrays.equals(value, data
					.getBinary())).as("Asserting the value").isTrue();
            
            assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length);
            
            assertThat(data.toString()).as("Asserts that two-digit hexadecimal numbers for each byte separated by spaces is "
							+
                    "returned in case of FORMAT_BINARY").isEqualTo(expectedValue);
            
        } catch(Exception e) {
        	failUnexpectedException(e);

        }
    }
	
	/**
	 * This method asserts that when a DmtData(String) is created with null, the format is
	 * DmtData.FORMAT_STRING, DmtData.getSize() and DmtData.getString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String)
	 */
	@Test
	public void testDmtData014() {
		try {		
			System.out.println("#testDmtData014");
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData((String)null);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_STRING").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_STRING);
			
			assertThat(data.getString()).as("Asserting the value").isNull();

			assertThat(data.getSize()).as("Asserting the size").isZero();
			
			assertThat(data.toString()).as("Asserting the string returned").isEmpty();
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	
	/**
	 * This method asserts that when a DmtData(String,FORMAT_STRING) is created with null, the format is
	 * DmtData.FORMAT_STRING, DmtData.getSize() and DmtData.getString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData015() {
		try {		
			System.out.println("#testDmtData015");
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData((String) null,org.osgi.service.dmt.DmtData.FORMAT_STRING);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_STRING").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_STRING);
			
			assertThat(data.getString()).as("Asserting the value").isNull();

			assertThat(data.getSize()).as("Asserting the size").isZero();
			
			assertThat(data.toString()).as("Asserting the string returned").isEmpty();
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	
	/**
	 * This method asserts that when a DmtData(String,FORMAT_XML) is created with null, the format is
	 * DmtData.FORMAT_XML, DmtData.getSize() and DmtData.getString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,int)
	 */
	@Test
	public void testDmtData016() {
		try {		
			System.out.println("#testDmtData016");
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData((String)null,org.osgi.service.dmt.DmtData.FORMAT_XML);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_XML").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_XML);
			
			assertThat(data.getXml()).as("Asserting the value").isNull();

			assertThat(data.getSize()).as("Asserting the size").isZero();
			
			assertThat(data.toString()).as("Asserting the string returned").isEmpty();
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	
	/**
	 * This method asserts that when a DmtData(String,String) is created the format is
	 * DmtData.FORMAT_RAW_STRING, DmtData.getSize() and DmtData.getRawString() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,String)
	 */
	@Test
	public void testDmtData017() {
		try {		
			System.out.println("#testDmtData017");
			String format = "format";
			String value = "data";
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(format,value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_RAW_STRING").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_RAW_STRING);
			
			assertThat(data.getRawString()).as("Asserting the value").isEqualTo(value);
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length());
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	
	/**
	 * This method asserts that when a DmtData(String,String) is created the format is
	 * DmtData.FORMAT_RAW_BINARY, DmtData.getSize() and DmtData.getRawBinary() return the expected value
	 * 
	 * @spec DmtData.DmtData(String,byte[])
	 */
	@Test
	public void testDmtData018() {
		try {		
			System.out.println("#testDmtData018");
			String format = "format";
			byte[] value = new byte[] {1};
			org.osgi.service.dmt.DmtData data  = new org.osgi.service.dmt.DmtData(format,value);
			assertThat(data.getFormat()).as("Asserting DmtData.FORMAT_RAW_BINARY").isEqualTo(org.osgi.service.dmt.DmtData.FORMAT_RAW_BINARY);
			
			assertThat(Arrays.equals(value, data
					.getRawBinary())).as("Asserting the value").isTrue();
			
			assertThat(data.getSize()).as("Asserting the size").isEqualTo(value.length);
			
		} catch(Exception e) {
			failUnexpectedException(e);

		}
	}
	
}
