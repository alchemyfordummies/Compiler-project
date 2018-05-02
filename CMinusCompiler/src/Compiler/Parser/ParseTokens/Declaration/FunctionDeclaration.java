package Compiler.Parser.ParseTokens.Declaration;

import Compiler.Parser.ParseTokens.Parameter;
import Compiler.Parser.ParseTokens.Statement.CompoundStatement;
import Compiler.Parser.Printable;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import ProjThreeCode.lowlevel.BasicBlock;
import ProjThreeCode.lowlevel.Data;
import ProjThreeCode.lowlevel.FuncParam;
import ProjThreeCode.lowlevel.Function;

import static Compiler.Scanner.Token.TokenType.*;

import java.io.IOException;
import java.text.ParseException;
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
        String printValue = padding + typeSpecifier.printToken() + " " + id.printToken() + "(";
        if (!params.isEmpty()) {
            for (Parameter param : params) {
                printValue += param.print(padding + "  ");
                if(params.indexOf(param) != params.size()-1)
                    printValue += ",";
            }
        }
        printValue += ")\n";
        printValue += padding + "{\n" + compoundStatement.print(padding + "  ") + padding + "}\n";
        return printValue;
    }

    public Function genFunctionLLCode() throws IOException {
        Function func;
        int type = typeSpecifier.match(Token.TokenType.INT_TOKEN) ? Data.TYPE_INT : Data.TYPE_VOID;
        if(!params.isEmpty()){
            FuncParam firstParam = params.get(0).genLLFuncParam();
            FuncParam currentParam = firstParam;
            for(int i = 1; i < params.size(); i++){
                FuncParam nextParam = params.get(i).genLLFuncParam();
                currentParam.setNextParam(nextParam);
                currentParam = nextParam;
            }
            func = new Function(type, (String) id.getTokenData(), firstParam);
        }
        else{
            func = new Function(type, (String) id.getTokenData());
        }
        func.createBlock0();
       // func.setCurrBlock(func.getFirstBlock());
        BasicBlock functionBlock = new BasicBlock(func);
        func.appendBlock(functionBlock);
        func.setCurrBlock(functionBlock);
        compoundStatement.genLLCode(func);
        func.appendBlock(func.getReturnBlock());
        if(func.getFirstUnconnectedBlock() != null){
            func.appendBlock(func.getFirstUnconnectedBlock());
        }
        return func;
    }
}
