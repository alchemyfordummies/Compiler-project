package Compiler.Parser;

public interface Parser {
    Program parse();
    void printAST(Program program);
}
