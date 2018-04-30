package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Declaration.VarDeclaration;
import Compiler.Parser.ParseTokens.Expression.Expression;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.CodeItem;
import ProjThreeCode.lowlevel.Function;
import ProjThreeCode.lowlevel.Operation;

import java.text.ParseException;

import static Compiler.Scanner.Token.TokenType.*;

public class ExpressionStatement extends Statement implements Printable {
    Expression expression;

    public ExpressionStatement(Expression expression) {
        this.expression = expression;
    }

    public static ExpressionStatement parseExpressionStatement(TokenList tokens) throws ParseException {
        Token currentToken = tokens.viewNextToken();
        if (currentToken.match(ID_TOKEN) || currentToken.match(NUM_TOKEN) || currentToken.match(OPEN_PARENS_TOKEN)) {
            Expression expression = Expression.parseExpression(tokens);
            currentToken = tokens.getNextToken();
            if (currentToken.match(SEMICOLON_TOKEN)) {
                return new ExpressionStatement(expression);
            } else {
                throw new ParseException("Expected semicolon", 0);
            }
        } else if (currentToken.match(SEMICOLON_TOKEN)) {
            tokens.getNextToken();
            return new ExpressionStatement(null);
        } else {
            throw new ParseException("Expected semicolon or expression", 2);
        }
    }

    @Override
    public String print(String padding) {
        String toPrint = "";
        if (expression != null)
            toPrint += expression.print(padding + "  ");
        return toPrint;
    }

    public void genLLCode(Function func){
        expression.genLLCode(func);

    }
}
