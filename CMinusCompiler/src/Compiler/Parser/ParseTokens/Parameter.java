package Compiler.Parser.ParseTokens;

import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import java.text.ParseException;
import java.util.ArrayList;

import static Compiler.Scanner.Token.TokenType.*;

public class Parameter {
    private Token id;
    private boolean isArray;

    public void setId(Token id) {
        this.id = id;
    }

    public void setIsArray(boolean isArray) {
        this.isArray = isArray;
    }

    public ArrayList<Parameter> parseParameterList(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        ArrayList<Parameter> parameters = new ArrayList<>();

        if (nextToken.match(VOID_TOKEN)) {
            return parameters;
        } else {
            nextToken = tokens.getNextToken();
            while (nextToken.match(INT_TOKEN)) {
                parameters.add(parseParameter(tokens));
                nextToken = tokens.getNextToken();

                if (nextToken.match(CLOSE_PARENS_TOKEN)) {
                    return parameters;
                } else if (nextToken.match(COMMA_TOKEN)) {
                    nextToken = tokens.getNextToken();
                } else {
                    throw new ParseException("Invalid token for parameter", 6);
                }
            }

            throw new ParseException("Parameters may only be of type int", 6);
        }
    }

    public Parameter parseParameter(TokenList tokens) throws ParseException {
        Token nextToken = tokens.getNextToken();
        Parameter parameter = new Parameter();

        if (nextToken.match(ID_TOKEN)) {
            parameter.setId(nextToken);
            nextToken = tokens.getNextToken();
        } else {
            throw new ParseException("No parameter name given", 6);
        }

        if (nextToken.match(OPEN_BRACKET_TOKEN)) {
            tokens.getNextToken();
            if (nextToken.match(CLOSE_BRACKET_TOKEN)) {
                parameter.setIsArray(true);
                return parameter;
            } else {
                throw new ParseException("Expected close bracket for parameter of type array", 6);
            }
        } else if (nextToken.match(COMMA_TOKEN) || nextToken.match(CLOSE_PARENS_TOKEN)) {
            tokens.ungetToken();
            return parameter;
        } else {
            throw new ParseException("Invalid token for parameter", 6);
        }
    }
}