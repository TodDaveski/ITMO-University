package elf;

public class ElfSection {
    public final ElfSectionHeader header;
    private final ElfParser parser;

    public ElfSection(ElfParser parser, ElfSectionHeader header) {
        this.header = header;
        this.parser = parser;
    }
}
