import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.InputMismatchException;

import elf.*;

public class MyDisassembler {
    final ElfFile file;
    public String[] visibility = new String[]{
            "DEFAULT", "INTERNAL", "HIDDEN", "PROTECTED", "UNKNOWN"
    };

    public MyDisassembler(ElfFile file) {
        this.file = file;
    }


    public void disassembleAll(OutputStreamWriter output) {
        PrintWriter writer = new PrintWriter(output);
        disassembleText(writer);
        disassembleSymTab(writer);
        writer.flush();
    }

    String getRegister(int reg) {
        switch (reg) {
            case (0):
                return "zero";
            case (1):
                return "ra";
            case (2):
                return "sp";
            case (3):
                return "gp";
            case (4):
                return "tp";
            case (5):
            case (6):
            case (7):
                return "t" + (reg - 5);
            case  (8):
                return "s0";
            case (9):
                return "s1";
        }
        if (10 <= reg && reg <= 17)
            return "a" + (reg - 10);
        else if (18 <= reg && reg <= 27)
            return "s" + (reg - 18 + 2);
        else if (28 <= reg && reg <= 31)
            return "t" + (reg - 28 + 3);
        throw new ElfException("RISC-V doesn't have register " + reg);
    }

    private String getSymbol(long loc) {
        ElfSymbol symb = file.getSymbol(loc);
        String locS = String.format("0x%08X", loc);
        if (symb != null && symb.st_value == loc && symb.section_type == 2) {
            locS += " <" + symb.getName() + ">";
        }
        return locS;
    }

    private void disassembleText(PrintWriter out) {
        ElfSection textSection = file.firstByName(".text");
        if (textSection == null)
            throw new ElfException("No .text found");
        file.getSymTab();
        long curOffset = 0;
        int maxSymbolLen = 10;
        file.parser.go(textSection.header.section_offset);
        while (curOffset < textSection.header.size) {
            long virtualAddress = curOffset + textSection.header.address;
            out.printf(String.format("%08X:", virtualAddress));
            int instruction = file.parser.readInt();
            ElfSymbol symb = file.getSymbol(virtualAddress);
            if (symb != null) {
                out.printf("<%" + maxSymbolLen + "s> ", symb.getName());
            } else {
                out.printf(" ".repeat(maxSymbolLen + 3));
            }

            int opcode = instruction & ((1 << 7) - 1);
            int rd = instruction >> 7 & ((1 << 5) - 1);
            int funct3 = instruction >> 12 & ((1 << 3) - 1);
            int rs1 = instruction >> 15 & ((1 << 5) - 1);
            int rs2 = instruction >> 20 & ((1 << 5) - 1);
            int imm110 = instruction >> 20 & ((1 << 12) - 1);
            int funct7 = instruction >> 25;
            if (opcode == 0b0110111) {
                out.printf("%6s %s, %s%n", "lui", getRegister(rd),
                        Integer.toUnsignedString((instruction >>> 12) << 12));
            } else if (instruction == 0b1110011) {
                out.printf("%6s%n", "ecall");
            } else if (opcode == 0b0010111) {
                out.printf("%6s %s, %s%n", "auipc", getRegister(rd),
                        Integer.toUnsignedString((instruction >>> 12) << 12));
            } else if (opcode == 0b1101111) {
                int imm = instruction >> 12;
                int offset = (((imm >>> 9) & ((1 << 10) - 1)) << 1) |
                        (((imm >>> 8) & 1) << 11) |
                        ((imm & ((1 << 8) - 1)) << 12) |
                        (((imm >>> 19) & 1) << 20);
                if ((offset & (1 << 20)) != 0) {
                    offset = -offset & ((1 << 20) - 1);
                }
                out.printf("%6s %s, %d #%s%n", "jal", getRegister(rd),
                        offset, getSymbol(virtualAddress + offset));
            } else if (opcode == 0b1100111 && funct3 == 0b000) {
                if ((imm110 & (1 << 11)) != 0) {
                    imm110 = -(-imm110 & ((1 << 11) - 1));
                }
                out.printf("%6s %s, %s, %d%n", "jalr", getRegister(rd),
                        getRegister(rs1), imm110);
            } else if (opcode == 0b1100011) {
                int offset = (((instruction >>> 8) & ((1 << 4) - 1)) <<
                        1) |
                        (((instruction >>> 25) & ((1 << 6) - 1)) << 5) |
                        (((instruction >>> 7) & 1) << 11) |
                        (((instruction >>> 31) & 1) << 12);
                if ((offset & (1 << 12)) != 0) {
                    offset = -offset & ((1 << 12) - 1);
                }
                String instr = new String[]{"beq", "bne", "??", "??",
                        "blt", "bge", "bltu", "bgeu"}[funct3];
                out.printf("%6s %s, %s, %d #%s %n", instr,
                        getRegister(rs1), getRegister(rs2), offset, getSymbol(virtualAddress +
                                offset));
            } else if (opcode == 0b0000011) {
                if ((imm110 & (1 << 11)) != 0) {
                    imm110 = -(-imm110 & ((1 << 11) - 1));
                }
                String instr = new String[]{"lb", "lh", "lw", "??",
                        "lbu", "lhu", "??", "??"}[funct3];
                out.printf("%6s %s, %s, %d%n", instr,
                        getRegister(rd), getRegister(rs1), imm110);
            } else if (opcode == 0b0100011) {
                String instr = new String[]{"sb", "sh", "sw", "??", "??",
                        "??", "??", "??"}[funct3];
                int imm = rd | ((imm110 >>> 5) << 5);
                if ((imm & (1 << 11)) != 0) {
                    imm = -(-imm & ((1 << 11) - 1));
                }
                out.printf("%6s %s, %d(%s)%n", instr,
                        getRegister(rs2), imm, getRegister(rs1));
            } else if (opcode == 0b0010011) {
                if ((imm110 & (1 << 11)) != 0) {
                    imm110 = -(-imm110 & ((1 << 11) - 1));
                }
                imm110 = -(imm110 & (1 << 11)) + (imm110 & ((1 << 11)-1));
                if (funct3 == 0b001) {
                    out.printf("%6s %s, %s, %d%n", "slli", getRegister(rd),
                            getRegister(rs1), imm110);
                } else if (funct3 == 0b101) {
                    if (funct7 == 0b0100000) {
                        out.printf("%6s %s, %s, %d%n", "srai",
                                getRegister(rd), getRegister(rs1), imm110 & ((1 << 5) - 1));
                    } else {
                        out.printf("%6s %s, %s, %d%n", "srli",
                                getRegister(rd), getRegister(rs1), imm110);
                    }
                } else {
                    String instr = new String[]{"addi", "??", "slti",
                            "sltiu", "xori", "??", "ori", "andi"}[funct3];
                    out.printf("%6s %s, %s, %d%n", instr,
                            getRegister(rd), getRegister(rs1), imm110);
                }
            } else if (opcode == 0b110011) {
                if (funct7 == 0b0100000) {
                    String instr = new String[]{"sub", "??", "??", "??",
                            "??", "sra", "??", "??"}[funct3];
                    out.printf("%6s %s, %s, %s%n", instr,
                            getRegister(rd), getRegister(rs2), getRegister(rs1));
                } else if (funct7 == 0) {
                    String instr = new String[]{"add", "sll", "slt",
                            "sltu", "xor", "srl", "or", "and"}[funct3];
                    out.printf("%6s %s, %s, %s%n", instr,
                            getRegister(rd), getRegister(rs2), getRegister(rs1));
                } else if (funct7 == 1) {
                    String instr = new String[]{"mul", "mulh", "mulhsu",
                            "mulhu", "div", "divu", "rem", "remu"}[funct3];
                    out.printf("%6s %s, %s, %s%n", instr,
                            getRegister(rd), getRegister(rs2), getRegister(rs1));
                }
            } else {
                out.printf("????%n");
            }
            curOffset += 4;
        }
    }


    private void disassembleSymTab(PrintWriter out) {
        out.println("Symtable:");
        ElfSymbolTable symtable = file.getSymTab();
        int symbolCount = symtable.symbols.length;
        int firstColWidth = (int) Math.floor(Math.log10(Math.abs(symbolCount))) + 1 + (symbolCount <= 0 ? 1 : 0);
        out.println(String.format("%" + (firstColWidth + 2) + "s   %8s %5s %7s %7s %8s %4s %s",
                "Symbol".substring(0, firstColWidth + 2), "Value", "Size", "Type", "Bind", "Vis", "Index", "Name"));
        for (int i = 0; i < symbolCount; i++) {
            ElfSymbol symbol = symtable.symbols[i];
            out.println(String.format("[%" + firstColWidth + "s] 0x%08X %5s %7s %7s %8s %4s %s",
                    i,
                    symbol.st_value,
                    symbol.st_size,
                    symbolTypeToString(symbol.st_info & 0x0F),
                    bindingToString(symbol.st_info >> 4),
                    visibility[(symbol.st_other < 0 || symbol.st_other > 3) ? 4 : symbol.st_other],
                    shindexToString(symbol.st_shndx),
                    symbol.st_name == 0 ? "" : symbol.getName()
            ));
        }
    }


    static String symbolTypeToString(int type) {
        switch (type) {
            case (0):
                return "NOTYPE";
            case (1):
                return "OBJECT";
            case (2):
                return "FUNC";
            case (3):
                return "SECTION";
            case (4):
                return "FILE";
            case (13):
                return "LOPROC";
            case (15):
                return "HIPROC";
            default:
                return "UNKNOWN";
        }
    }

    private String shindexToString(short stShndx) {
        if (stShndx == -15) {
            return "ABS";
        } else if (stShndx == -14) {
            return "COMMON";
        } else if (Short.compareUnsigned((short) -256, stShndx) <= 0 && Short.compareUnsigned(stShndx, (short) -225) <= 0) {
            return "PROC_RES";
        } else if (Short.compareUnsigned((short) -224, stShndx) <= 0 && Short.compareUnsigned(stShndx, (short) -193) <= 0) {
            return "OS_RES";
        } else if (stShndx == 0) {
            return "UNDEF";
        } else if (stShndx == -1) {
            return "XINDEX";
        } else if (-256 <= stShndx && stShndx <= -1) {
            return "RESERVED";
        } else {
            return String.valueOf(stShndx);
        }


    }

    static String bindingToString(int binding) {
        switch (binding) {
            case (1):
                return "GLOBAL";
            case (15):
                return "HIPROC";
            case (0):
                return "LOCAL";
            case (13):
                return "LOPROC";
            case (2):
                return "WEAK";
            default:
                return "UNKNOWN";
        }
    }


}
