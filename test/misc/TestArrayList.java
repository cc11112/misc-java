package misc;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

public class TestArrayList {
	
	private ArrayList<String> list;

	@Before
	public void setUp() {
		list = new ArrayList<String>();
	}

	@After
	public void tearDown() throws Exception {
		list = null;
	}

	@Test
	public void testInitiallyEmpty() {
		assertEquals(0, list.size());
		assertTrue(list.isEmpty());
	}
	
	@Test
	public void testAdd() {
		list.add("hello");
		list.add("world");
		list.add("wassup");
		assertEquals(3, list.size());
		assertEquals("hello", list.get(0));
		assertEquals("world", list.get(1));
		assertEquals("wassup", list.get(2));
		assertFalse(list.isEmpty());
	}
}
