package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.ParseTokens.Parameter;
import Compiler.Parser.ParseTokens.Statement.CompoundStatement;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;

import static Compiler.Scanner.Token.TokenType.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class FunctionDeclaration extends Declaration implements Printable {
    Token typeSpecifier;
    Token id;
    List<Parameter> params;
    CompoundStatement compoundStatement;

    public FunctionDeclaration(Token typeSpecifier, Token id, List<Parameter> params, CompoundStatement compoundStatement) {
        this.typeSpecifier = typeSpecifier;
        this.id = id;
        this.params = params;
        this.compoundStatement = compoundStatement;
    }

    public static FunctionDeclaration parseFunctionDeclarationPrime(TokenList tokens, Token typeSpecifier, Token id) throws ParseException {
        List<Parameter> params;
        CompoundStatement compoundStatement;
        Token nextToken = tokens.getNextToken();
        if (nextToken.match(OPEN_PARENS_TOKEN)) {
            params = Parameter.parseParameterList(tokens);
            nextToken = tokens.getNextToken();
            if(nextToken.match(CLOSE_PARENS_TOKEN)){
                compoundStatement = CompoundStatement.parseCompoundStatement(tokens);
                return new FunctionDeclaration(typeSpecifier, id, params, compoundStatement);
            }
            throw new ParseException("Expected )", tokens.getIndex());
        }
        throw new ParseException("Expected (", tokens.getIndex());
    }

    @Override
    public String print(String padding) {
        String printValue = padding + "FunctionDeclaration:\n";
        printValue += padding + "TypeSpecifier{" + typeSpecifier.getTokenType() + "}\n";
        printValue += padding + "ID{" + id.getTokenData() + "}\n";
        printValue += padding + "Params{\n";
        if (!params.isEmpty()) {
            for (Parameter param : params) {
                printValue += param.print(padding + "  ");
                if(params.indexOf(param) != params.size()-1)
                    printValue += padding + ",\n";
            }
        }
        printValue += padding + "}\n";
        printValue += padding + "CompoundStatement{\n" + compoundStatement.print(padding + "  ") + padding + "}\n";
        return printValue;
    }
}
