package elf;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ElfParser {
    final ElfFile elfFile;
    public ByteArrayInputStream byteArray;

    ElfParser(ElfFile elfFile, ByteArrayInputStream byteArray) {
        this.elfFile = elfFile;
        this.byteArray = byteArray;
    }


    public int read(byte[] data) {
        try {
            return byteArray.read(data);
        } catch (IOException e) {
            throw new RuntimeException("Error reading " + data.length + " bytes", e);
        }
    }
    short readUnsignedByte() {
        int val = -1;
        val = byteArray.read();
        if (val < 0) throw new ElfException("Trying to read outside file");
        return (short) val;
    }

    short byteSwap(short arg) {
        return (short) ((arg << 8) | ((arg >>> 8) & 0xFF));
    }

    int byteSwap(int arg) {
        return ((byteSwap((short) arg)) << 16) | (((byteSwap((short) (arg >>> 16)))) & 0xFFFF);
    }

    public short readShort() throws ElfException {
        int ch1 = readUnsignedByte();
        int ch2 = readUnsignedByte();
        short val = (short) ((ch1 << 8) + (ch2 << 0));
        val = byteSwap(val);
        return val;
    }

    public int readInt() throws ElfException {
        int ch1 = readUnsignedByte();
        int ch2 = readUnsignedByte();
        int ch3 = readUnsignedByte();
        int ch4 = readUnsignedByte();
        int val = ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));

        val = byteSwap(val);
        return val;
    }


    public void go(long offset) {
        byteArray.reset();
        if (byteArray.skip(offset) != offset) throw new ElfException("seeking outside file");
    }


}
