package Compiler.Parser.ParseTokens.Statement;

import Compiler.Parser.ParseTokens.Declaration.Declaration;
import Compiler.Parser.ParseTokens.Declaration.VarDeclaration;
import Compiler.Parser.TokenList;
import Compiler.Scanner.Token;
import com.sun.org.apache.bcel.internal.generic.RET;
import com.sun.xml.internal.bind.v2.model.core.ID;
import org.omg.CORBA.IDLTypeOperations;

import javax.swing.plaf.nimbus.State;

import static Compiler.Scanner.Token.TokenType.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CompoundStatements extends Statement{
    List<VarDeclaration> localDeclarations;
    List<Statement> statementList;

    public CompoundStatements(List<VarDeclaration> localDeclarations, List<Statement> statementList){
        this.localDeclarations = localDeclarations;
        this.statementList = statementList;
    }

    public static CompoundStatements parseCompoundStatements(TokenList tokens) throws ParseException{
        Token nextToken = tokens.getNextToken();
        if(nextToken.match(OPEN_CURLY_BRACE_TOKEN)){
            List<VarDeclaration> varDecls = parseLocalDeclarations(tokens);
            List<Statement> statements = parseStatementList(tokens);
            nextToken = tokens.getNextToken();
            if(nextToken.match(CLOSE_CURLY_BRACE_TOKEN)){
                throw new ParseException("Expected close curly brace", 0);
            }
            else{
                return new CompoundStatements(varDecls, statements);
            }
        }
        else{
            throw new ParseException("Expected open curly brace", 0);
        }
    }

    public static List<VarDeclaration> parseLocalDeclarations(TokenList tokenList) throws ParseException{
        List<VarDeclaration> varDeclarations = new ArrayList<>();
        Token nextToken = tokenList.viewNextToken();
        if(nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                || nextToken.match(ELSE_TOKEN) || nextToken.match(CLOSE_CURLY_BRACE_TOKEN)){
            return varDeclarations;
        }
        else if(nextToken.match(INT_TOKEN)){
            while(nextToken.match(INT_TOKEN)){
                varDeclarations.add(parseVarDeclaration(tokenList));
            }
            return varDeclarations;
        }
        else{
            throw new ParseException("Expected first or follow of varDecl", 1);
        }
    }

    public static List<Statement> parseStatementList(TokenList tokens) throws ParseException{
        List<Statement> statements = new ArrayList<>();
        Token nextToken = tokens.viewNextToken();
        if(nextToken.match(CLOSE_CURLY_BRACE_TOKEN)){
            return statements;
        }
        else{
            while(nextToken.match(ID_TOKEN) || nextToken.match(OPEN_PARENS_TOKEN) || nextToken.match(NUM_TOKEN)
                    || nextToken.match(SEMICOLON_TOKEN) || nextToken.match(IF_TOKEN) || nextToken.match(WHILE_TOKEN)
                    || nextToken.match(RETURN_TOKEN) || nextToken.match(OPEN_CURLY_BRACE_TOKEN)){
                statements.add(Statement.parseStatement(tokens));
            }
            return statements;
        }
    }

    public static VarDeclaration parseVarDeclaration(TokenList tokens) throws ParseException{
        Token nextToken = tokens.getNextToken();
        if(nextToken.match(INT_TOKEN)){
            Token idToken = tokens.getNextToken();
            if(idToken.match(ID_TOKEN)){
                Token openBracketToken = tokens.getNextToken();
                if(openBracketToken.match(OPEN_BRACKET_TOKEN)){
                    Token numToken = tokens.getNextToken();
                    if(numToken.match(NUM_TOKEN)){
                        Token closeBracketToken = tokens.getNextToken();
                        if(closeBracketToken.match(CLOSE_BRACKET_TOKEN)){
                            return new VarDeclaration(nextToken, idToken, numToken);
                        }
                        else{
                            throw new ParseException("Expected close bracket in varDecl", 1);
                        }
                    }
                    else{
                        throw new ParseException("Expected num in varDecl index", 1);
                    }
                }
                else{
                    tokens.ungetToken();
                    return new VarDeclaration(nextToken, idToken, null);
                }
            }
            else {
                throw new ParseException("Expected id token in varDecl", 1);
            }
        }
        else{
            throw new ParseException("Expected int token in varDecl", 1);
        }
    }
}
