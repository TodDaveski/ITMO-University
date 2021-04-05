package elf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ElfFile {
    public ElfSection[] sections;
    ByteArrayInputStream byteArray;
    public ElfParser parser;
    public int ph_offset;
    public int sh_offset;
    public int ph_entry_size;
    public int sh_entry_size;
    public int num_ph;
    public int num_sh;
    public int sh_string_ndx;


    private ElfSymbolTable symbolTableSection;

    public ElfFile(InputStream in) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        boolean checkedMagic = false;
        int length;
        int readInBuffer = 0;
        byte[] buffer = new byte[4096];
        while ((length = in.read(buffer, readInBuffer, buffer.length - readInBuffer)) != -1) {
            if (!checkedMagic) {
                if (length < 4 || (!(0x7f == buffer[0] && 'E' == buffer[1] && 'L' == buffer[2] && 'F' == buffer[3])))
                    throw new ElfException("Not an elf file");
                checkedMagic = true;
            }
            byteArrayOutputStream.write(buffer, 0, length);
        }
        byteArray = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        parser = new ElfParser(this, byteArray);
        byte[] ident = new byte[16];
        int bytesRead = parser.read(ident);
        if (bytesRead != ident.length)
            throw new ElfException("Error reading elf header");
        if (!(0x7f == ident[0] && 'E' == ident[1] && 'L' == ident[2] && 'F' == ident[3]))
            throw new ElfException("Bad magic number for file");
        if (ident[4] != 1)
            throw new ElfException("Not RISC V-32 bit");
        if (ident[5] != 1)
            throw new ElfException("Not Little-Endian");
        if (ident[6] != 1)
            throw new ElfException("Invalid elf version: " + ident[6]);
        parser.readShort();
        if (parser.readShort() != 0xF3)
            throw new ElfException("Invalid architecture, not RISC-V");
        parser.readInt(); //version
        parser.readInt(); //entry point
        ph_offset = parser.readInt();
        sh_offset = parser.readInt();
        parser.readInt(); //flags
        parser.readShort(); //eh_size
        ph_entry_size = parser.readShort();
        num_ph = parser.readShort();
        sh_entry_size = parser.readShort();
        num_sh = parser.readShort();
        sh_string_ndx = parser.readShort();
        sections = new ElfSection[num_sh];

        for (int i = 0; i < num_sh; i++) {
            final long sectionHeaderOffset = sh_offset + (i * sh_entry_size);
            ElfSectionHeader header = new ElfSectionHeader(parser, sectionHeaderOffset);

            switch (header.type) {
                case 2:
                case 11:
                    sections[i] = new ElfSymbolTable(parser, header);
                    break;
                case 3:
                    sections[i] = new ElfStringTable(parser, header.section_offset, (int) header.size, header);
                    break;
                default:
                    sections[i] = new ElfSection(parser, header);
            }
        }
    }

    private ElfStringTable findStringTableWithName(String tableName) throws ElfException {
        return (ElfStringTable) firstByName(tableName);
    }


    public ElfStringTable getStringTable() throws ElfException {
        return findStringTableWithName(".strtab");
    }


    public ElfSymbolTable getSymTab() throws ElfException {
        return (symbolTableSection != null) ? symbolTableSection : (symbolTableSection = (ElfSymbolTable) firstByName(".symtab"));
    }

    public ElfSymbol getSymbol(long address) throws ElfException {
        ElfSymbol symbol;
        long value;
        ElfSymbolTable sh = getSymTab();
        int numSymbols = sh.symbols.length;
        for (int i = 1; i < numSymbols; i++) {
            symbol = sh.symbols[i];
            value = symbol.st_value;
            if (address == value &&  ((symbol.st_info & 0xf) == 0 || (symbol.st_info & 0xf) == 2))   {
                return symbol;
        }
        }
        return null;
    }


    public ElfSection firstByName(String sectionName) throws ElfException {
        for (int i = 1; i < num_sh; i++) {
            ElfSection sh = sections[i];
            if (sectionName.equals(sh.header.getName()))  { return sh; }
        }
        return null;
    }



}
