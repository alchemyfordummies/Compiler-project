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
        if (tokens.getNextToken().match(OPEN_PARENS_TOKEN)) {
            Token nextToken = tokens.viewNextToken();
            if (nextToken.match(VOID_TOKEN)) {
                return null;
            } else if (nextToken.match(INT_TOKEN)) {
                params = Parameter.parseParameterList(tokens);
                compoundStatement = CompoundStatement.parseCompoundStatement(tokens);
                return new FunctionDeclaration(typeSpecifier, id, params, compoundStatement);
            } else {
                throw new ParseException("PARSE ERROR", 4);
            }
        } else {
            throw new ParseException("PARSE ERROR", 4);
        }
    }

    @Override
    public String print(){
        String printValue = "FunctionDeclaration:";
        printValue += "TypeSpecifier{" + typeSpecifier.getTokenType() + "}";
        printValue += "ID{" + id.getTokenData() + "}";
        printValue += "Params{";
        if(!params.isEmpty()){
            for(Parameter param : params){
                printValue += param.print() + ",";
            }
            printValue = printValue.substring(0, printValue.length() - 1);
        }
        printValue += "}";
        printValue += "CompoundStatement{" + compoundStatement.print() + "}";
        return printValue;
    }
}
