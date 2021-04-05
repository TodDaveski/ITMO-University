package elf;

public class ElfStringTable extends ElfSection {
    private final byte[] data;

    ElfStringTable(ElfParser parser, long offset, int length, ElfSectionHeader header) throws ElfException {
        super(parser, header);

        parser.go(offset);
        data = new byte[length];
        int bytesRead = parser.read(data);
        if (bytesRead != length)
            throw new ElfException("Error reading string table (read " + bytesRead + "bytes - expected to " + "read " + data.length + "bytes)");

    }

    public String get(int index) {
        int cur = index;
        while (data[cur] != '\0')
            cur++;
        return new String(data, index, cur - index);
    }

}
