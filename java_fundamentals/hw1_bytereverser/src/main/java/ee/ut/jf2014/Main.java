package ee.ut.jf2014;

import java.io.IOException;
import ee.ut.jf2014.homework1.ByteReverser;

public class Main {
	public static void main(String[] args) throws IOException {
		new ByteReverser(args[0]).reverseBytes();
	}
}