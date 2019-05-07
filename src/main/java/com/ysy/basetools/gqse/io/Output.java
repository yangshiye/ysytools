package com.ysy.basetools.gqse.io;


import com.ysy.basetools.util.ListUtil;
import com.ysy.basetools.util.NumUtil;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by guoqiang on 2017/10/19.
 */
public class Output {
    private static final byte[] EMPTY_BYTES = new byte[0];
    private final List<Buffer> bufferList = new LinkedList<Buffer>();
    private List<Data> dataList;
    private byte[] buffer;
    private int position;
    private int capacity;
    private int bufferListTotal;
    private int dataListTotal;

    public static void main(String args[]) {
        Output output = new Output(4);
        output.writeByte(1);
        output.writeByte(2);
        output.writeByte(3);
        Data data1 = output.appendData();
        Data data2 = output.appendData();
        Data data3 = output.appendData();
        data3.setBytes(new byte[]{9});
        data2.setBytes(new byte[]{7, 8});
        data1.setBytes(new byte[]{4, 5, 6});
        output.writeByte(10);
        data1 = output.appendData();
        output.writeByte(19);
        data1.setBytes(new byte[]{11, 12, 13, 14, 15, 16, 17, 18});
        System.out.println(Arrays.toString(output.toBytes()));

        byte b = 1;
        output = new Output(4);
        for (int i = 0; i < 90; i++) {
            boolean arr = true;
            if (b > 19) {
                arr = false;
            }
            if (arr) {
                int len = NumUtil.randomInt(6) + 3;
                byte[] temp = new byte[len];
                for (int k = 0; k < len; k++) {
                    temp[k] = b++;
                }
                output.appendData().setBytes(temp);
            } else {
                output.writeByte(b++);
            }

            if (b > 30) {
                b = 0;
            }

        }
        System.out.println(Arrays.toString(output.toBytes()));

    }

    public Output() {
        this(4096);
    }

    public Output(int size) {
        buffer = new byte[size];
        capacity = size;
    }

    public int getTotal() {
        return bufferListTotal + dataListTotal + position;
    }

    private boolean require(int required) {
        if (buffer.length - position >= required) {
            return true;//当要输入的数据信息小于或等于要输入的数据时返回成功信息
        }
        if (position > 0) {
            Buffer bufferData = new Buffer(buffer, position, dataList);
            bufferList.add(bufferData);
            bufferListTotal += bufferData.position;
            dataList = null;
        }
        int newLen = Math.max(required, ((required + getTotal()) >> 1));
        buffer = new byte[newLen];
        capacity = newLen;
        position = 0;
        return true;
    }

    public Data appendData() {
        Data data = new Data(position);
        if (dataList == null) {
            dataList = new LinkedList<Data>();
        }
        dataList.add(data);
        dataListTotal += data.bytes.length;
        return data;
    }

    public byte[] toBytes() {
        int len = getTotal();
        byte[] bytes = new byte[len];
        int idx = 0;
        if (ListUtil.nEmpty(bufferList)) {
            for (Buffer temp : bufferList) {
                idx = toBytes(bytes, idx, temp.bytes, temp.position, temp.dataList);
            }
        }
        idx = toBytes(bytes, idx, buffer, position, dataList);
        return bytes;
    }

    private static int toBytes(byte[] result, int idx, byte[] bytes, final int position, List<Data> dataList) {
        int tempPosition = 0;
        if (ListUtil.nEmpty(dataList)) {
            for (Data data : dataList) {
                if (data.idx > tempPosition) {
                    int len = data.idx - tempPosition;
                    System.arraycopy(bytes, tempPosition, result, idx, len);
                    tempPosition += len;
                    idx += len;
                }
                try {
                    System.arraycopy(data.bytes, 0, result, idx, data.bytes.length);
                    idx += data.bytes.length;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (tempPosition < position) {
            int len = position - tempPosition;
            System.arraycopy(bytes, tempPosition, result, idx, len);
            tempPosition += len;
            idx += len;
        }
        return idx;
    }

    /**
     * Writes a byte.
     */
    public void write(int value) {
        if (position == capacity) require(1);
        buffer[position++] = (byte) value;
    }

    public void writeByteArr(byte[] bytes) {
        this.writeInt(bytes.length, true);
        this.write(bytes);
    }

    /**
     * Writes the bytes. Note the byte[] length is not written.
     */
    public void write(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("bytes cannot be null.");
        writeBytes(bytes, 0, bytes.length);
    }

    /**
     * Writes the bytes. Note the byte[] length is not written.
     */
    public void write(byte[] bytes, int offset, int length) {
        writeBytes(bytes, offset, length);
    }

    // byte

    public void writeByte(byte value) {
        if (position == capacity) require(1);
        buffer[position++] = value;
    }

    public void writeByte(int value) {
        if (position == capacity) require(1);
        buffer[position++] = (byte) value;
    }

    /**
     * Writes the bytes. Note the byte[] length is not written.
     */
    public void writeBytes(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("bytes cannot be null.");
        writeBytes(bytes, 0, bytes.length);
    }

    /**
     * Writes the bytes. Note the byte[] length is not written.
     */
    public void writeBytes(byte[] bytes, int offset, int count) {
        if (bytes == null) throw new IllegalArgumentException("bytes cannot be null.");
        int copyCount = Math.min(capacity - position, count);
        while (true) {
            System.arraycopy(bytes, offset, buffer, position, copyCount);
            position += copyCount;
            count -= copyCount;
            if (count == 0) return;
            offset += copyCount;
            copyCount = Math.min(capacity, count);
            require(copyCount);
        }
    }

    // int

    /**
     * Writes a 4 byte int. Uses BIG_ENDIAN byte order.
     */
    public void writeInt(int value) {
        require(4);
        byte[] buffer = this.buffer;
        buffer[position++] = (byte) (value >> 24);
        buffer[position++] = (byte) (value >> 16);
        buffer[position++] = (byte) (value >> 8);
        buffer[position++] = (byte) value;
    }

    /**
     * Writes a 1-5 byte int. This stream may consider such a variable length encoding request as a hint. It is not guaranteed that
     * a variable length encoding will be really used. The stream may decide to use native-sized integer representation for
     * efficiency reasons.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (5 bytes).
     */
    public int writeInt(int value, boolean optimizePositive) {
        return writeVarInt(value, optimizePositive);
    }

    /**
     * Writes a 1-5 byte int. It is guaranteed that a varible length encoding will be used.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (5 bytes).
     */
    public int writeVarInt(int value, boolean optimizePositive) {
        if (!optimizePositive) value = (value << 1) ^ (value >> 31);
        if (value >>> 7 == 0) {
            require(1);
            buffer[position++] = (byte) value;
            return 1;
        }
        if (value >>> 14 == 0) {
            require(2);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7);
            return 2;
        }
        if (value >>> 21 == 0) {
            require(3);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14);
            return 3;
        }
        if (value >>> 28 == 0) {
            require(4);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21);
            return 4;
        }
        require(5);
        buffer[position++] = (byte) ((value & 0x7F) | 0x80);
        buffer[position++] = (byte) (value >>> 7 | 0x80);
        buffer[position++] = (byte) (value >>> 14 | 0x80);
        buffer[position++] = (byte) (value >>> 21 | 0x80);
        buffer[position++] = (byte) (value >>> 28);
        return 5;
    }

    public static byte[] intToBytes(int value, boolean optimizePositive) {
        byte[] buffer;
        if (!optimizePositive) value = (value << 1) ^ (value >> 31);
        if (value >>> 7 == 0) {
            buffer = new byte[1];
            buffer[0] = (byte) value;
            return buffer;
        } else if (value >>> 14 == 0) {
            buffer = new byte[2];
            buffer[0] = (byte) ((value & 0x7F) | 0x80);
            buffer[1] = (byte) (value >>> 7);
            return buffer;
        } else if (value >>> 21 == 0) {
            buffer = new byte[3];
            buffer[0] = (byte) ((value & 0x7F) | 0x80);
            buffer[1] = (byte) (value >>> 7 | 0x80);
            buffer[2] = (byte) (value >>> 14);
            return buffer;
        } else if (value >>> 28 == 0) {
            buffer = new byte[4];
            buffer[0] = (byte) ((value & 0x7F) | 0x80);
            buffer[1] = (byte) (value >>> 7 | 0x80);
            buffer[2] = (byte) (value >>> 14 | 0x80);
            buffer[3] = (byte) (value >>> 21);
            return buffer;
        } else {
            buffer = new byte[5];
            buffer[0] = (byte) ((value & 0x7F) | 0x80);
            buffer[1] = (byte) (value >>> 7 | 0x80);
            buffer[2] = (byte) (value >>> 14 | 0x80);
            buffer[3] = (byte) (value >>> 21 | 0x80);
            buffer[4] = (byte) (value >>> 28);
            return buffer;
        }
    }

    // string

    /**
     * Writes the length and string, or null. Short strings are checked and if ASCII they are written more efficiently, else they
     * are written as UTF8. If a string is known to be ASCII, {@link #writeAscii(String)} may be used. The string can be read using
     *
     * @param value May be null.
     */
    public void writeString(String value) {
        if (value == null) {
            writeByte(0x80); // 0 means null, bit 8 means UTF8.
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            writeByte(1 | 0x80); // 1 means empty string, bit 8 means UTF8.
            return;
        }
        // Detect ASCII.
        boolean ascii = false;
        if (charCount > 1 && charCount < 64) {
            ascii = true;
            for (int i = 0; i < charCount; i++) {
                int c = value.charAt(i);
                if (c > 127) {
                    ascii = false;
                    break;
                }
            }
        }
        if (ascii) {
            if (capacity - position < charCount)
                writeAscii_slow(value, charCount);
            else {
                value.getBytes(0, charCount, buffer, position);
                position += charCount;
            }
            buffer[position - 1] |= 0x80;
        } else {
            writeUtf8Length(charCount + 1);
            int charIndex = 0;
            if (capacity - position >= charCount) {
                // Try to write 8 bit chars.
                byte[] buffer = this.buffer;
                int position = this.position;
                for (; charIndex < charCount; charIndex++) {
                    int c = value.charAt(charIndex);
                    if (c > 127) break;
                    buffer[position++] = (byte) c;
                }
                this.position = position;
            }
            if (charIndex < charCount) writeString_slow(value, charCount, charIndex);
        }
    }

    /**
     * @param value May be null.
     */
    public void writeString(CharSequence value) {
        if (value == null) {
            writeByte(0x80); // 0 means null, bit 8 means UTF8.
            return;
        }
        int charCount = value.length();
        if (charCount == 0) {
            writeByte(1 | 0x80); // 1 means empty string, bit 8 means UTF8.
            return;
        }
        writeUtf8Length(charCount + 1);
        int charIndex = 0;
        if (capacity - position >= charCount) {
            // Try to write 8 bit chars.
            byte[] buffer = this.buffer;
            int position = this.position;
            for (; charIndex < charCount; charIndex++) {
                int c = value.charAt(charIndex);
                if (c > 127) break;
                buffer[position++] = (byte) c;
            }
            this.position = position;
        }
        if (charIndex < charCount) writeString_slow(value, charCount, charIndex);
    }

    /**
     * Writes a string that is known to contain only ASCII characters. Non-ASCII strings passed to this method will be corrupted.
     * Each byte is a 7 bit character with the remaining byte denoting if another character is available. This is slightly more
     *
     * @param value May be null.
     */
    public void writeAscii(String value) {
        if (value == null) {
            writeByte(0x80); // 0 means null, bit 8 means UTF8.
            return;
        }
        int charCount = value.length();
        switch (charCount) {
            case 0:
                writeByte(1 | 0x80); // 1 is string length + 1, bit 8 means UTF8.
                return;
            case 1:
                writeByte(2 | 0x80); // 2 is string length + 1, bit 8 means UTF8.
                writeByte(value.charAt(0));
                return;
        }
        if (capacity - position < charCount)
            writeAscii_slow(value, charCount);
        else {
            value.getBytes(0, charCount, buffer, position);
            position += charCount;
        }
        buffer[position - 1] |= 0x80; // Bit 8 means end of ASCII.
    }

    /**
     * Writes the length of a string, which is a variable length encoded int except the first byte uses bit 8 to denote UTF8 and
     * bit 7 to denote if another byte is present.
     */
    private void writeUtf8Length(int value) {
        if (value >>> 6 == 0) {
            require(1);
            buffer[position++] = (byte) (value | 0x80); // Set bit 8.
        } else if (value >>> 13 == 0) {
            require(2);
            byte[] buffer = this.buffer;
            buffer[position++] = (byte) (value | 0x40 | 0x80); // Set bit 7 and 8.
            buffer[position++] = (byte) (value >>> 6);
        } else if (value >>> 20 == 0) {
            require(3);
            byte[] buffer = this.buffer;
            buffer[position++] = (byte) (value | 0x40 | 0x80); // Set bit 7 and 8.
            buffer[position++] = (byte) ((value >>> 6) | 0x80); // Set bit 8.
            buffer[position++] = (byte) (value >>> 13);
        } else if (value >>> 27 == 0) {
            require(4);
            byte[] buffer = this.buffer;
            buffer[position++] = (byte) (value | 0x40 | 0x80); // Set bit 7 and 8.
            buffer[position++] = (byte) ((value >>> 6) | 0x80); // Set bit 8.
            buffer[position++] = (byte) ((value >>> 13) | 0x80); // Set bit 8.
            buffer[position++] = (byte) (value >>> 20);
        } else {
            require(5);
            byte[] buffer = this.buffer;
            buffer[position++] = (byte) (value | 0x40 | 0x80); // Set bit 7 and 8.
            buffer[position++] = (byte) ((value >>> 6) | 0x80); // Set bit 8.
            buffer[position++] = (byte) ((value >>> 13) | 0x80); // Set bit 8.
            buffer[position++] = (byte) ((value >>> 20) | 0x80); // Set bit 8.
            buffer[position++] = (byte) (value >>> 27);
        }
    }

    private void writeString_slow(CharSequence value, int charCount, int charIndex) {
        for (; charIndex < charCount; charIndex++) {
            if (position == capacity) require(Math.min(capacity, charCount - charIndex));
            int c = value.charAt(charIndex);
            if (c <= 0x007F) {
                buffer[position++] = (byte) c;
            } else if (c > 0x07FF) {
                buffer[position++] = (byte) (0xE0 | c >> 12 & 0x0F);
                require(2);
                buffer[position++] = (byte) (0x80 | c >> 6 & 0x3F);
                buffer[position++] = (byte) (0x80 | c & 0x3F);
            } else {
                buffer[position++] = (byte) (0xC0 | c >> 6 & 0x1F);
                require(1);
                buffer[position++] = (byte) (0x80 | c & 0x3F);
            }
        }
    }

    private void writeAscii_slow(String value, int charCount) {
        byte[] buffer = this.buffer;
        int charIndex = 0;
        int charsToWrite = Math.min(charCount, capacity - position);
        while (charIndex < charCount) {
            value.getBytes(charIndex, charIndex + charsToWrite, buffer, position);
            charIndex += charsToWrite;
            position += charsToWrite;
            charsToWrite = Math.min(charCount - charIndex, capacity);
            if (require(charsToWrite)) buffer = this.buffer;
        }
    }

    // float

    /**
     * Writes a 4 byte float.
     */
    public void writeFloat(float value) {
        writeInt(Float.floatToIntBits(value));
    }

    /**
     * Writes a 1-5 byte float with reduced precision.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (5 bytes).
     */
    public int writeFloat(float value, float precision, boolean optimizePositive) {
        return writeInt((int) (value * precision), optimizePositive);
    }

    // short

    /**
     * Writes a 2 byte short. Uses BIG_ENDIAN byte order.
     */
    public void writeShort(int value) {
        require(2);
        buffer[position++] = (byte) (value >>> 8);
        buffer[position++] = (byte) value;
    }

    // long

    /**
     * Writes an 8 byte long. Uses BIG_ENDIAN byte order.
     */
    public void writeLong(long value) {
        require(8);
        byte[] buffer = this.buffer;
        buffer[position++] = (byte) (value >>> 56);
        buffer[position++] = (byte) (value >>> 48);
        buffer[position++] = (byte) (value >>> 40);
        buffer[position++] = (byte) (value >>> 32);
        buffer[position++] = (byte) (value >>> 24);
        buffer[position++] = (byte) (value >>> 16);
        buffer[position++] = (byte) (value >>> 8);
        buffer[position++] = (byte) value;
    }

    /**
     * Writes a 1-9 byte long. This stream may consider such a variable length encoding request as a hint. It is not guaranteed
     * that a variable length encoding will be really used. The stream may decide to use native-sized integer representation for
     * efficiency reasons.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (9 bytes).
     */
    public int writeLong(long value, boolean optimizePositive) {
        return writeVarLong(value, optimizePositive);
    }

    /**
     * Writes a 1-9 byte long. It is guaranteed that a varible length encoding will be used.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (9 bytes).
     */
    public int writeVarLong(long value, boolean optimizePositive) {
        if (!optimizePositive) value = (value << 1) ^ (value >> 63);
        if (value >>> 7 == 0) {
            require(1);
            buffer[position++] = (byte) value;
            return 1;
        }
        if (value >>> 14 == 0) {
            require(2);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7);
            return 2;
        }
        if (value >>> 21 == 0) {
            require(3);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14);
            return 3;
        }
        if (value >>> 28 == 0) {
            require(4);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21);
            return 4;
        }
        if (value >>> 35 == 0) {
            require(5);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21 | 0x80);
            buffer[position++] = (byte) (value >>> 28);
            return 5;
        }
        if (value >>> 42 == 0) {
            require(6);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21 | 0x80);
            buffer[position++] = (byte) (value >>> 28 | 0x80);
            buffer[position++] = (byte) (value >>> 35);
            return 6;
        }
        if (value >>> 49 == 0) {
            require(7);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21 | 0x80);
            buffer[position++] = (byte) (value >>> 28 | 0x80);
            buffer[position++] = (byte) (value >>> 35 | 0x80);
            buffer[position++] = (byte) (value >>> 42);
            return 7;
        }
        if (value >>> 56 == 0) {
            require(8);
            buffer[position++] = (byte) ((value & 0x7F) | 0x80);
            buffer[position++] = (byte) (value >>> 7 | 0x80);
            buffer[position++] = (byte) (value >>> 14 | 0x80);
            buffer[position++] = (byte) (value >>> 21 | 0x80);
            buffer[position++] = (byte) (value >>> 28 | 0x80);
            buffer[position++] = (byte) (value >>> 35 | 0x80);
            buffer[position++] = (byte) (value >>> 42 | 0x80);
            buffer[position++] = (byte) (value >>> 49);
            return 8;
        }
        require(9);
        buffer[position++] = (byte) ((value & 0x7F) | 0x80);
        buffer[position++] = (byte) (value >>> 7 | 0x80);
        buffer[position++] = (byte) (value >>> 14 | 0x80);
        buffer[position++] = (byte) (value >>> 21 | 0x80);
        buffer[position++] = (byte) (value >>> 28 | 0x80);
        buffer[position++] = (byte) (value >>> 35 | 0x80);
        buffer[position++] = (byte) (value >>> 42 | 0x80);
        buffer[position++] = (byte) (value >>> 49 | 0x80);
        buffer[position++] = (byte) (value >>> 56);
        return 9;
    }

    // boolean

    /**
     * Writes a 1 byte boolean.
     */
    public void writeBoolean(boolean value) {
        if (position == capacity) require(1);
        buffer[position++] = (byte) (value ? 1 : 0);
    }

    // char

    /**
     * Writes a 2 byte char. Uses BIG_ENDIAN byte order.
     */
    public void writeChar(char value) {
        require(2);
        buffer[position++] = (byte) (value >>> 8);
        buffer[position++] = (byte) value;
    }

    // double

    /**
     * Writes an 8 byte double.
     */
    public void writeDouble(double value) {
        writeLong(Double.doubleToLongBits(value));
    }

    /**
     * Writes a 1-9 byte double with reduced precision.
     *
     * @param optimizePositive If true, small positive numbers will be more efficient (1 byte) and small negative numbers will be
     *                         inefficient (9 bytes).
     */
    public int writeDouble(double value, double precision, boolean optimizePositive) {
        return writeLong((long) (value * precision), optimizePositive);
    }

    /**
     * Returns the number of bytes that would be written with {@link #writeInt(int, boolean)}.
     */
    static public int intLength(int value, boolean optimizePositive) {
        if (!optimizePositive) value = (value << 1) ^ (value >> 31);
        if (value >>> 7 == 0) return 1;
        if (value >>> 14 == 0) return 2;
        if (value >>> 21 == 0) return 3;
        if (value >>> 28 == 0) return 4;
        return 5;
    }

    /**
     * Returns the number of bytes that would be written with {@link #writeLong(long, boolean)}.
     */
    static public int longLength(long value, boolean optimizePositive) {
        if (!optimizePositive) value = (value << 1) ^ (value >> 63);
        if (value >>> 7 == 0) return 1;
        if (value >>> 14 == 0) return 2;
        if (value >>> 21 == 0) return 3;
        if (value >>> 28 == 0) return 4;
        if (value >>> 35 == 0) return 5;
        if (value >>> 42 == 0) return 6;
        if (value >>> 49 == 0) return 7;
        if (value >>> 56 == 0) return 8;
        return 9;
    }

    // Methods implementing bulk operations on arrays of primitive types

    /**
     * Bulk output of an int array.
     */
    public void writeInts(int[] object, boolean optimizePositive) {
        for (int i = 0, n = object.length; i < n; i++)
            writeInt(object[i], optimizePositive);
    }

    /**
     * Bulk output of an long array.
     */
    public void writeLongs(long[] object, boolean optimizePositive) {
        for (int i = 0, n = object.length; i < n; i++)
            writeLong(object[i], optimizePositive);
    }

    /**
     * Bulk output of an int array.
     */
    public void writeInts(int[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeInt(object[i]);
    }

    /**
     * Bulk output of an long array.
     */
    public void writeLongs(long[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeLong(object[i]);
    }

    /**
     * Bulk output of a float array.
     */
    public void writeFloats(float[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeFloat(object[i]);
    }

    /**
     * Bulk output of a short array.
     */
    public void writeShorts(short[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeShort(object[i]);
    }

    /**
     * Bulk output of a char array.
     */
    public void writeChars(char[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeChar(object[i]);
    }

    /**
     * Bulk output of a double array.
     */
    public void writeDoubles(double[] object) {
        for (int i = 0, n = object.length; i < n; i++)
            writeDouble(object[i]);
    }

    public void writeBooleans(boolean[] obj) {
        for (int i = 0, n = obj.length; i < n; i++)
            writeBoolean(obj[i]);
    }

    public void writeStrings(String[] obj) {
        for (int i = 0, n = obj.length; i < n; i++)
            writeString(obj[i]);
    }

    private static class Buffer {
        private final byte[] bytes;
        private final List<Data> dataList;
        private final int position;

        public Buffer(byte[] bytes, int position, List<Data> dataList) {
            this.bytes = bytes;
            this.dataList = dataList;
            this.position = position;
        }
    }

    public class Data {
        private byte[] bytes;
        private final int idx;

        private Data(int idx) {
            this.bytes = EMPTY_BYTES;
            this.idx = idx;
        }

        public void setBytes(byte[] bytes) {
            if (bytes == null) {
                throw new NullPointerException("bytes is null");
            } else {
                int oldLen = this.bytes.length;
                int newLen = bytes.length;
                this.bytes = bytes;
                dataListTotal += (newLen - oldLen);
            }
        }

        public void setInt(int value) {
            byte[] bytes = intToBytes(value, true);
            this.setBytes(bytes);
        }
    }
}
