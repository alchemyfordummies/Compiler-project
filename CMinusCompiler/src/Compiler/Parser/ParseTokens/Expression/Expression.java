package Compiler.Parser.ParseTokens.Expression;

import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static Compiler.Scanner.Token.TokenType.*;

public class Expression implements Printable {

    public static Expression parseExpression(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(ID_TOKEN)) {
            return parseExpressionPrime(tokens, nextToken);
        } else if (nextToken.match(OPEN_PARENS_TOKEN)) {
            Expression expression1 = parseExpression(tokens);
            nextToken = tokens.getNextToken();
            if (nextToken.match(CLOSE_PARENS_TOKEN)) {
                return parseSimpleExpressionPrime(tokens, expression1);
            } else {
                throw new ParseException("Expected )", 0);
            }
        } else if (nextToken.match(NUM_TOKEN)) {
            Expression expression1 = new NumExpression(nextToken);
            return parseSimpleExpressionPrime(tokens, expression1);
        } else {
            throw new ParseException("Expected ID, (, NUM", 0);
        }
    }

    public static Expression parseExpressionPrime(TokenList tokens, Token idToken) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(ASSIGNMENT_TOKEN)) {
            Expression lhs = new VarExpression(idToken, null);
            Expression rhs = parseExpression(tokens);
            return new AssignmentExpression(lhs, rhs);
        } else if (nextToken.match(OPEN_BRACKET_TOKEN)) {
            Expression index = parseExpression(tokens);
            Expression varExpr = new VarExpression(idToken, index);
            nextToken = tokens.getNextToken();
            if (nextToken.match(CLOSE_BRACKET_TOKEN)) {
                return parseExpressionDoublePrime(tokens, varExpr);
            } else {
                throw new ParseException("Expected ]", 0);
            }
        } else if (nextToken.match(OPEN_PARENS_TOKEN)) {
            List<Expression> args = parseArgs(tokens);
            if (tokens.getNextToken().match(CLOSE_PARENS_TOKEN)) {
                Expression callExpr = new CallExpression(idToken, args);
                return parseSimpleExpressionPrime(tokens, callExpr);
            }
            throw new ParseException("Expected ) token", 0);
        } else {
            tokens.ungetToken();
            Expression varExpression = new VarExpression(idToken, null);
            return parseSimpleExpressionPrime(tokens, varExpression);
        }
    }

    public static Expression parseExpressionDoublePrime(TokenList tokens, Expression expression1) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(ASSIGNMENT_TOKEN)) {
            Expression rhs = parseExpression(tokens);
            return new AssignmentExpression(expression1, rhs);
        } else if (nextToken.match(MULTIPLY_TOKEN) || nextToken.match(DIVIDE_TOKEN) || nextToken.match(SEMICOLON_TOKEN)
                || nextToken.match(CLOSE_BRACKET_TOKEN) || nextToken.match(CLOSE_PARENS_TOKEN) || nextToken.match(COMMA_TOKEN)
                || nextToken.match(MINUS_TOKEN) || nextToken.match(PLUS_TOKEN)) {
            tokens.ungetToken();
            return parseSimpleExpressionPrime(tokens, expression1);
        } else {
            throw new ParseException("Expected first or follow of expression'", 0);
        }
    }

    public static Expression parseSimpleExpressionPrime(TokenList tokens, Expression expression1) throws ParseException {
        Expression lhs = parseAddExprPrime(tokens, expression1);
        Token nextToken = tokens.viewNextToken();
        if (nextToken.match(LESS_THAN_EQUALS_TOKEN) || nextToken.match(LESS_THAN_TOKEN) || nextToken.match(GREATER_THAN_EQUALS_TOKEN)
                || nextToken.match(GREATER_THAN_TOKEN) || nextToken.match(EQUALS_TOKEN) || nextToken.match(NOT_EQUALS_TOKEN)) {
            tokens.getNextToken();
            Expression rhs = parseAddExpr(tokens);
            return new BinaryExpression(lhs, rhs, nextToken);
        } else {
            return lhs;
        }
    }

    public static Expression parseAddExpr(TokenList tokens) throws ParseException {
        Expression lhs = parseTerm(tokens);
        Token next = tokens.viewNextToken();
        while (next.match(PLUS_TOKEN) || next.match(MINUS_TOKEN)) {
            tokens.getNextToken();
            Expression rhs = parseTerm(tokens);
            lhs = new BinaryExpression(lhs, rhs, next);
            next = tokens.viewNextToken();
        }
        return lhs;
    }

    public static Expression parseAddExprPrime(TokenList tokens, Expression expression1) throws ParseException {
        Expression lhs = parseTermPrime(tokens, expression1);
        Token next = tokens.viewNextToken();
        while (next.match(PLUS_TOKEN) || next.match(MINUS_TOKEN)) {
            tokens.getNextToken();
            Expression rhs = parseTerm(tokens);
            lhs = new BinaryExpression(lhs, rhs, next);
            next = tokens.viewNextToken();
        }
        return lhs;
    }

    public static Expression parseTerm(TokenList tokens) throws ParseException {
        Expression lhs = parseFactor(tokens);
        Token next = tokens.viewNextToken();
        while (next.match(MULTIPLY_TOKEN) || next.match(DIVIDE_TOKEN)) {
            tokens.getNextToken();
            Expression rhs = parseFactor(tokens);
            lhs = new BinaryExpression(lhs, rhs, next);
            next = tokens.viewNextToken();
        }
        return lhs;
    }

    public static Expression parseTermPrime(TokenList tokens, Expression expression1) throws ParseException {
        Token next = tokens.viewNextToken();
        while (next.match(MULTIPLY_TOKEN) || next.match(DIVIDE_TOKEN)) {
            tokens.getNextToken();
            Expression rhs = parseFactor(tokens);
            expression1 = new BinaryExpression(expression1, rhs, next);
            next = tokens.viewNextToken();
        }
        return expression1;
    }

    public static Expression parseFactor(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(OPEN_PARENS_TOKEN)) {
            Expression toReturn = parseExpression(tokens);
            nextToken = tokens.getNextToken();
            if (nextToken.match(CLOSE_PARENS_TOKEN)) {
                return toReturn;
            }
            throw new ParseException("Expected )", 0);
        } else if (nextToken.match(ID_TOKEN)) {
            return parseVarCall(tokens, nextToken);
        } else if (nextToken.match(NUM_TOKEN)) {
            return new NumExpression(nextToken);
        } else {
            throw new ParseException("Expected first of factor", 0);
        }
    }

    public static Expression parseVarCall(TokenList tokens, Token id) throws ParseException {
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(OPEN_BRACKET_TOKEN)) {
            Expression index = parseExpression(tokens);
            nextToken = tokens.getNextToken();
            if (nextToken.match(CLOSE_BRACKET_TOKEN)) {
                return new VarExpression(id, index);
            } else {
                throw new ParseException("Expected close bracket", 0);
            }
        } else if (nextToken.match(OPEN_PARENS_TOKEN)) {
            List<Expression> args = parseArgs(tokens);
            if (nextToken.match(CLOSE_PARENS_TOKEN)) {
                return new CallExpression(id, args);
            } else {
                throw new ParseException("Expected close bracket", 0);
            }
        } else if (nextToken.match(MULTIPLY_TOKEN) || nextToken.match(DIVIDE_TOKEN) || nextToken.match(PLUS_TOKEN)
                || nextToken.match(DIVIDE_TOKEN) || nextToken.match(LESS_THAN_TOKEN) || nextToken.match(LESS_THAN_EQUALS_TOKEN)
                || nextToken.match(EQUALS_TOKEN) || nextToken.match(NOT_EQUALS_TOKEN) || nextToken.match(GREATER_THAN_TOKEN)
                || nextToken.match(GREATER_THAN_EQUALS_TOKEN) || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(CLOSE_PARENS_TOKEN)
                || nextToken.match(CLOSE_BRACKET_TOKEN) || nextToken.match(COMMA_TOKEN)) {
            tokens.ungetToken();
            return new VarExpression(id, null);
        } else {
            throw new ParseException("Expected first or follow of varcall", 0);
        }
    }

    public static List<Expression> parseArgs(TokenList tokens) throws ParseException {
        Token nextToken = tokens.viewNextToken();
        if (nextToken.match(CLOSE_PARENS_TOKEN)) {
            return new ArrayList<>();
        } else if (nextToken.match(ID_TOKEN) || nextToken.match(NUM_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN)) {
            return parseArgList(tokens);
        } else {
            throw new ParseException("Expected first or follow of args", 0);
        }
    }

    public static List<Expression> parseArgList(TokenList tokens) throws ParseException {
        List<Expression> args = new ArrayList<>();
        args.add(parseExpression(tokens));
        Token next = tokens.viewNextToken();
        while (next.match(COMMA_TOKEN)) {
            tokens.getNextToken();
            args.add(parseExpression(tokens));
            next = tokens.viewNextToken();
        }
        return args;
    }

    @Override
    public String print(String padding) {
        return "";
    }

    public void genLLCode(Function function) throws IOException{
    }

    public Operand genLLOperand(){
        return null;
    }

    public int genLLCodeAndRegister(Function function) throws IOException{
        return 0;
    }
}
