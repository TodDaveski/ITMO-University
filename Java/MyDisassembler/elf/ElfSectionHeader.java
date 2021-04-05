package elf;

public class ElfSectionHeader {
    public ElfFile elfHeader;
    public int name_ndx;
    public int type;
    public int flags;
    public int address;
    public int section_offset;
    public int size;
    public int link;
    public int info;
    public int address_alignment;
    public int entry_size;

    ElfSectionHeader(final ElfParser parser, long offset) {
        this.elfHeader = parser.elfFile;
        parser.go(offset);

        name_ndx = parser.readInt();
        type = parser.readInt();
        flags = parser.readInt();
        address = parser.readInt();
        section_offset = parser.readInt();
        size = parser.readInt();
        link = parser.readInt();
        info = parser.readInt();
        address_alignment = parser.readInt();
        entry_size = parser.readInt();
    }

    public String getName() {
        if (name_ndx == 0) return null;
        ElfStringTable tbl = (ElfStringTable)  elfHeader.sections[elfHeader.sh_string_ndx];
        return tbl.get(name_ndx);
    }
}
