package Compiler.Parser.ParseTokens.Expression;

import Compiler.Scanner.Token;

public class AssignmentExpression extends Expression {
    Token id;
    Expression valueToAssign;
}
