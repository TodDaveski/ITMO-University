package elf;

public class ElfSymbolTable extends ElfSection {
    public ElfSymbol[] symbols;

    public ElfSymbolTable(ElfParser parser, ElfSectionHeader header) {
        super(parser, header);

        int num_entries =  (header.size / header.entry_size);
        symbols = new ElfSymbol[num_entries];
        for (int i = 0; i < num_entries; i++) {
            final long symbolOffset = header.section_offset + (i * header.entry_size);
            symbols[i] = new ElfSymbol(parser, symbolOffset, header.type);
        }
    }
}
