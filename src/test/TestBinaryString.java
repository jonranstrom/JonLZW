package test;

import junit.framework.Assert;

import main.BinaryString;
@SuppressWarnings("deprecation")

public class TestBinaryString extends LZWTestCase  {
	
	public void testFromString(){
		Assert.assertEquals(1, BinaryString.toInt("00001"));
		Assert.assertEquals(21, BinaryString.toInt("10101"));
		Assert.assertEquals(33, BinaryString.toInt("100001"));
		Assert.assertEquals(257, BinaryString.toInt("100000001"));
	}
	
	public void testToString(){
		Assert.assertEquals("00001", BinaryString.toString(1,5));
		Assert.assertEquals("10101", BinaryString.toString(21,5));
		Assert.assertEquals("100000", BinaryString.toString(32,5));// 5 minimum, but 6 can come
		Assert.assertEquals("100001", BinaryString.toString(33,6));
		Assert.assertEquals("100000001", BinaryString.toString(257,6));
	}
}
