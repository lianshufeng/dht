package com.jpznm.dht.sniffercore.core.dht.coding;

import java.io.DataOutput;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class BencodingOutputStream extends FilterOutputStream implements DataOutput {
	private final String encoding;

	public BencodingOutputStream(OutputStream outputStream) {
		this(outputStream, "UTF-8");
	}

	public BencodingOutputStream(OutputStream outputStream, String string) {
		super(outputStream);
		if (string == null) {
			throw new NullPointerException("encoding");
		}
		this.encoding = string;
	}

	public String getEncoding() {
		return this.encoding;
	}

	public void writeObject(Object object) throws IOException {
		if (object == null) {
			this.writeNull();
		} else if (object instanceof byte[]) {
			this.writeBytes((byte[]) object);
		} else if (object instanceof Boolean) {
			this.writeBoolean((Boolean) object);
		} else if (object instanceof Character) {
			this.writeChar(((Character) object).charValue());
		} else if (object instanceof Number) {
			this.writeNumber((Number) object);
		} else if (object instanceof String) {
			this.writeString((String) object);
		} else if (object instanceof Collection) {
			this.writeCollection((Collection<?>) object);
		} else if (object instanceof Map) {
			this.writeMap((Map<?, ?>) object);
		} else if (object instanceof Enum) {
			this.writeEnum((Enum<?>) object);
		} else if (object.getClass().isArray()) {
			this.writeArray(object);
		} else {
			this.writeCustom(object);
		}
	}

	public void writeNull() throws IOException {
		throw new IOException("Null is not supported");
	}

	protected void writeCustom(Object object) throws IOException {
		throw new IOException("Cannot bencode " + object);
	}

	public void writeBytes(byte[] arrby) throws IOException {
		this.writeBytes(arrby, 0, arrby.length);
	}

	public void writeBytes(byte[] arrby, int n, int n2) throws IOException {
		this.write(Integer.toString(n2).getBytes(this.encoding));
		this.write(58);
		this.write(arrby, n, n2);
	}

	@Override
	public void writeBoolean(boolean bl) throws IOException {
		this.writeNumber(bl ? BencodingUtils.TRUE : BencodingUtils.FALSE);
	}

	@Override
	public void writeChar(int n) throws IOException {
		this.writeString(Character.toString((char) n));
	}

	@Override
	public void writeByte(int n) throws IOException {
		this.writeNumber(Byte.valueOf((byte) n));
	}

	@Override
	public void writeShort(int n) throws IOException {
		this.writeNumber((short) n);
	}

	@Override
	public void writeInt(int n) throws IOException {
		this.writeNumber(n);
	}

	@Override
	public void writeLong(long l) throws IOException {
		this.writeNumber(l);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		this.writeNumber(Float.valueOf(f));
	}

	@Override
	public void writeDouble(double d) throws IOException {
		this.writeNumber(d);
	}

	public void writeNumber(Number number) throws IOException {
		String string = number.toString();
		this.write(105);
		this.write(string.getBytes(this.encoding));
		this.write(101);
	}

	public void writeString(String string) throws IOException {
		this.writeBytes(string.getBytes(this.encoding));
	}

	public void writeCollection(Collection<?> collection) throws IOException {
		this.write(108);
		for (Object obj : collection) {
			this.writeObject(obj);
		}
		this.write(101);
	}

	public void writeMap(Map<?, ?> map) throws IOException {
		if (!(map instanceof SortedMap)) {
			map = new TreeMap<>(map);
		}
		this.write(100);
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			Object obj = entry.getKey();
			Object obj2 = entry.getValue();
			if (obj instanceof String) {
				this.writeString((String) obj);
			} else {
				this.writeBytes((byte[]) obj);
			}
			this.writeObject(obj2);
		}
		this.write(101);
	}

	public void writeEnum(Enum<?> enum_) throws IOException {
		this.writeString(enum_.name());
	}

	public void writeArray(Object object) throws IOException {
		this.write(108);
		int n = Array.getLength(object);
		for (int i = 0; i < n; ++i) {
			this.writeObject(Array.get(object, i));
		}
		this.write(101);
	}

	@Override
	public void writeBytes(String string) throws IOException {
		this.writeString(string);
	}

	@Override
	public void writeChars(String string) throws IOException {
		this.writeString(string);
	}

	@Override
	public void writeUTF(String string) throws IOException {
		this.writeBytes(string.getBytes("UTF-8"));
	}
}