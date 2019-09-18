package com.jpznm.dht.sniffercore.core.dht.coding;

import java.io.DataInput;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class BencodingInputStream extends FilterInputStream implements DataInput {
	private final String encoding;
	private final boolean decodeAsString;

	public BencodingInputStream(InputStream inputStream) {
		this(inputStream, "UTF-8", false);
	}

	public BencodingInputStream(InputStream inputStream, String string) {
		this(inputStream, string, false);
	}

	public BencodingInputStream(InputStream inputStream, boolean bl) {
		this(inputStream, "UTF-8", bl);
	}

	public BencodingInputStream(InputStream inputStream, String encoding, boolean bl) {
		super(inputStream);
		if (encoding == null) {
			throw new NullPointerException("encoding");
		}
		this.encoding = encoding;
		this.decodeAsString = bl;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public boolean isDecodeAsString() {
		return this.decodeAsString;
	}

	public Object readObject() throws IOException {
		int n = this.read();
		if (n == -1) {
			throw new EOFException();
		}
		return this.readObject(n);
	}

	protected Object readObject(int n) throws IOException {
		if (n == 100) {
			return this.readMap0();
		}
		if (n == 108) {
			return this.readList0();
		}
		if (n == 105) {
			return this.readNumber0();
		}
		if (BencodingInputStream.isDigit(n)) {
			byte[] arrby = this.readBytes(n);
			return this.decodeAsString ? new String(arrby, this.encoding) : arrby;
		}
		return this.readCustom(n);
	}

	protected Object readCustom(int n) throws IOException {
		throw new IOException("Not implemented: " + n);
	}

	public byte[] readBytes() throws IOException {
		int n = this.read();
		if (n == -1) {
			throw new EOFException();
		}
		return this.readBytes(n);
	}

	private byte[] readBytes(int n) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append((char) n);
		while ((n = this.read()) != 58) {
			if (n == -1) {
				throw new EOFException();
			}
			stringBuilder.append((char) n);
		}
		int n2 = Integer.parseInt(stringBuilder.toString());
		byte[] arrby = new byte[n2];
		this.readFully(arrby);
		return arrby;
	}

	public String readString() throws IOException {
		return this.readString(this.encoding);
	}

	private String readString(String string) throws IOException {
		return new String(this.readBytes(), string);
	}

	public <T extends Enum<T>> T readEnum(Class<T> class_) throws IOException {
		return Enum.valueOf(class_, this.readString());
	}

	@Override
	public char readChar() throws IOException {
		return this.readString().charAt(0);
	}

	@Override
	public boolean readBoolean() throws IOException {
		return this.readInt() != 0;
	}

	@Override
	public byte readByte() throws IOException {
		return this.readNumber().byteValue();
	}

	@Override
	public short readShort() throws IOException {
		return this.readNumber().shortValue();
	}

	@Override
	public int readInt() throws IOException {
		return this.readNumber().intValue();
	}

	@Override
	public float readFloat() throws IOException {
		return this.readNumber().floatValue();
	}

	@Override
	public long readLong() throws IOException {
		return this.readNumber().longValue();
	}

	@Override
	public double readDouble() throws IOException {
		return this.readNumber().doubleValue();
	}

	public Number readNumber() throws IOException {
		int n = this.read();
		if (n == -1) {
			throw new EOFException();
		}
		if (n != 105) {
			throw new IOException();
		}
		return this.readNumber0();
	}

	private Number readNumber0() throws IOException {
		StringBuilder stringBuilder = new StringBuilder();
		boolean bl = false;
		int n = -1;
		while ((n = this.read()) != 101) {
			if (n == -1) {
				throw new EOFException();
			}
			if (n == 46) {
				bl = true;
			}
			stringBuilder.append((char) n);
		}
		try {
			if (bl) {
				return new BigDecimal(stringBuilder.toString());
			}
			return new BigInteger(stringBuilder.toString());
		} catch (NumberFormatException numberFormatException) {
			throw new IOException("NumberFormatException", numberFormatException);
		}
	}

	public List<?> readList() throws IOException {
		int n = this.read();
		if (n == -1) {
			throw new EOFException();
		}
		if (n != 108) {
			throw new IOException();
		}
		return this.readList0();
	}

	private List<?> readList0() throws IOException {
		ArrayList<Object> arrayList = new ArrayList<Object>();
		int n = -1;
		while ((n = this.read()) != 101) {
			if (n == -1) {
				throw new EOFException();
			}
			arrayList.add(this.readObject(n));
		}
		return arrayList;
	}

	public Map<String, ?> readMap() throws IOException {
		int n = this.read();
		if (n == -1) {
			throw new EOFException();
		}
		if (n != 100) {
			throw new IOException();
		}
		return this.readMap0();
	}

	private Map<String, ?> readMap0() throws IOException {
		TreeMap<String, Object> treeMap = new TreeMap<String, Object>();
		int n = -1;
		while ((n = this.read()) != 101) {
			if (n == -1) {
				throw new EOFException();
			}
			String string = new String(this.readBytes(n), this.encoding);
			Object object = this.readObject();
			treeMap.put(string, object);
		}
		return treeMap;
	}

	@Override
	public void readFully(byte[] arrby) throws IOException {
		this.readFully(arrby, 0, arrby.length);
	}

	@Override
	public void readFully(byte[] arrby, int n, int n2) throws IOException {
		int n3;
		for (int i = 0; i < n2; i += n3) {
			n3 = this.read(arrby, i, n2 - i);
			if (n3 != -1)
				continue;
			throw new EOFException();
		}
	}

	@Override
	public String readLine() throws IOException {
		return this.readString();
	}

	@Override
	public int readUnsignedByte() throws IOException {
		return this.readByte() & 255;
	}

	@Override
	public int readUnsignedShort() throws IOException {
		return this.readShort() & 65535;
	}

	@Override
	public String readUTF() throws IOException {
		return this.readString("UTF-8");
	}

	@Override
	public int skipBytes(int n) throws IOException {
		return (int) this.skip(n);
	}

	private static boolean isDigit(int n) {
		return 48 <= n && n <= 57;
	}
}