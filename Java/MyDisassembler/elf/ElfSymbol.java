package elf;


public class ElfSymbol {
    public long offset;
    public int st_name;
    public long st_value;
    public long st_size;
    public short st_info;
    public short st_other;
    public short st_shndx;
    public int section_type;
    public ElfFile elfHeader;

    ElfSymbol(ElfParser parser, long offset, int section_type) {
        this.elfHeader = parser.elfFile;
        parser.go(offset);
        this.offset = offset;
        st_name = parser.readInt();
        st_value = parser.readInt();
        st_size = parser.readInt();
        st_info = parser.readUnsignedByte();
        st_other = parser.readUnsignedByte();
        st_shndx = parser.readShort();
        this.section_type = section_type;
    }

    public String getName() throws ElfException {
        if (st_name == 0) return null;
        String symbol_name = null;
        symbol_name = elfHeader.getStringTable().get(st_name);
        return symbol_name;
    }

}
