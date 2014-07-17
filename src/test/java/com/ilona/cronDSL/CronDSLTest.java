package com.ilona.cronDSL;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CronDSLTest {

	private CronDSL cronDSL = new CronDSL();

	@Test
	public void testSecond() {
		assertEquals("* * * * * ?", cronDSL.convert("every second"));
	}
	
	@Test
	public void testMinute() {
		assertEquals("0 * * * * ?", cronDSL.convert("every minute"));
	}
	
	@Test
	public void testHour() {
		assertEquals("0 0 * * * ?", cronDSL.convert("every hour"));
	}
	
	@Test
	public void testDayOfMonth() {
		assertEquals("0 0 0 * * ?", cronDSL.convert("every day"));
		assertEquals(
				"0 0 0 5/2 * ?",
				cronDSL.convert("every 2nd day starting from 5th day of every month"));
		assertEquals("0 0 0 L * ?",
				cronDSL.convert("on the last day of every month"));
		assertEquals("0 0 0 L-2 * ?",
				cronDSL.convert("on the 2nd-to-last last day of every month"));
		assertEquals("0 0 0 15-17 9 ?",
				cronDSL.convert("from 15th to 17th day of September"));
		assertEquals("0 0 0 1,5,9 * ?",
				cronDSL.convert("on the 1st, 5th and 9th day of every month"));
		assertEquals("0 0 0 15W * ?",
				cronDSL.convert("the nearest weekday to the 15th of every month"));
		assertEquals("0 0 0 LW 1 ?",
				cronDSL.convert("last weekday of the month"));
	}
	
	@Test
	public void testMonth() {
		
		assertEquals("0 0 0 1 * ?", cronDSL.convert("every month"));
		assertEquals("0 0 0 1 5/2 ?",
				cronDSL.convert("every 2nd month starting from May"));
		assertEquals("0 0 0 1 1-5 ?", cronDSL.convert("from January to May"));
		assertEquals("0 0 0 1 1,5,10 ?",
				cronDSL.convert("January, May and October"));
	}
	
	@Test
	public void testDayOfWeek() {
		assertEquals("0 0 0 1 2 ?", cronDSL.convert("every Monday"));
	}

	@Test
	public void testYear() {
		assertEquals("0 0 0 1 1 ? *", cronDSL.convert("every year"));
	}

	@Test
	public void testAll() {
		assertEquals("0 0 0 1 1 ?", cronDSL.convert(""));
		assertEquals("0 0 12 * * ? ",
				cronDSL.convert("At 12pm (noon) every day"));
		assertEquals("0 15 10 * * ? ", cronDSL.convert("At 10:15am every day"));
		assertEquals("0 15 10 * * ? 2005",
				cronDSL.convert("At 10:15am every day during the year 2005"));
	}

}
