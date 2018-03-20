// Tim Froberg & Tyler Bowdish
// 2/5/18
// Project 1

package Compiler.Scanner;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class CMinusScanner implements Scanner {
    private enum State {
        START,
        IN_NUM,
        IN_ID,
        CHECK_ASSIGNMENT,
        CHECK_ENTERING_COMMENT,
        IN_COMMENT,
        CHECK_LEAVING_COMMENT,
        CHECK_GREATER_EQUAL,
        CHECK_LESS_EQUAL,
        CHECK_NOT_EQUAL,
        DONE,
        ERROR
    }

    private String fileContents;
    private ArrayList<Token> tokensFound;

    private int contentIndex;

    public CMinusScanner(File file) {
        fileContents = readFile(file);
        tokensFound = new ArrayList<>();

        contentIndex = 0;
    }

    public ArrayList<Token> getTokensFound() {
        return tokensFound;
    }

    @Override
    public Token getNextToken() {
        State currentState = State.START;
        int tokenStartIndex = -1;
        Token currentToken = new Token(Token.TokenType.ERROR);

        while (currentState != State.DONE) {
            if (contentIndex == fileContents.length()) {
                currentToken = new Token(Token.TokenType.END_OF_FILE);
                currentState = State.DONE;
            } else {
                char currentCharacter = fileContents.charAt(contentIndex);
                switch (currentState) {
                    case START:
                        if (Character.isDigit(currentCharacter)) {
                            tokenStartIndex = contentIndex;
                            currentState = State.IN_NUM;
                        } else if (Character.isLetter(currentCharacter)) {
                            tokenStartIndex = contentIndex;
                            currentState = State.IN_ID;
                        } else if (currentCharacter == '=') {
                            currentState = State.CHECK_ASSIGNMENT;
                        } else if (currentCharacter == '>') {
                            currentState = State.CHECK_GREATER_EQUAL;
                        } else if (currentCharacter == '<') {
                            currentState = State.CHECK_LESS_EQUAL;
                        } else if (Character.isWhitespace(currentCharacter)) {
                            contentIndex++;
                            continue;
                        } else if (currentCharacter == '/') {
                            currentState = State.CHECK_ENTERING_COMMENT;
                        } else if (currentCharacter == '!') {
                            currentState = State.CHECK_NOT_EQUAL;
                        } else {
                            currentState = State.DONE;
                            switch (currentCharacter) {
                                case ',':
                                    currentToken = new Token(Token.TokenType.COMMA_TOKEN);
                                    break;
                                case '+':
                                    currentToken = new Token(Token.TokenType.PLUS_TOKEN);
                                    break;
                                case '-':
                                    currentToken = new Token(Token.TokenType.MINUS_TOKEN);
                                    break;
                                case '*':
                                    currentToken = new Token(Token.TokenType.MULTIPLY_TOKEN);
                                    break;
                                case '(':
                                    currentToken = new Token(Token.TokenType.OPEN_PARENS_TOKEN);
                                    break;
                                case ')':
                                    currentToken = new Token(Token.TokenType.CLOSE_PARENS_TOKEN);
                                    break;
                                case '[':
                                    currentToken = new Token(Token.TokenType.OPEN_BRACKET_TOKEN);
                                    break;
                                case ']':
                                    currentToken = new Token(Token.TokenType.CLOSE_BRACKET_TOKEN);
                                    break;
                                case '{':
                                    currentToken = new Token(Token.TokenType.OPEN_CURLY_BRACE_TOKEN);
                                    break;
                                case '}':
                                    currentToken = new Token(Token.TokenType.CLOSE_CURLY_BRACE_TOKEN);
                                    break;
                                case ';':
                                    currentToken = new Token(Token.TokenType.SEMICOLON_TOKEN);
                                    break;
                                default:
                                    currentToken = new Token(Token.TokenType.ERROR);
                            }
                        }

                        break;
                    case IN_ID:
                        if (!Character.isLetter(currentCharacter)) {
                            if (Character.isDigit(currentCharacter)) {
                                contentIndex--;
                                currentToken = new Token(Token.TokenType.ERROR);
                                currentState = State.DONE;
                            } else {
                                contentIndex--;
                                currentState = State.DONE;
                                currentToken = new Token(Token.TokenType.ID_TOKEN);
                            }
                        }

                        break;
                    case IN_NUM:
                        if (!Character.isDigit(currentCharacter)) {
                            if (Character.isLetter(currentCharacter)) {
                                contentIndex--;
                                currentState = State.DONE;
                                currentToken = new Token(Token.TokenType.ERROR);
                            } else {
                                contentIndex--;
                                currentState = State.DONE;
                                currentToken = new Token(Token.TokenType.NUM_TOKEN);
                            }
                        }

                        break;
                    case CHECK_ASSIGNMENT:
                        currentState = State.DONE;
                        if (currentCharacter == '=') {
                            currentToken = new Token(Token.TokenType.EQUALS_TOKEN);
                        } else {
                            contentIndex--;
                            currentToken = new Token(Token.TokenType.ASSIGNMENT_TOKEN);
                        }

                        break;
                    case CHECK_GREATER_EQUAL:
                        currentState = State.DONE;
                        if (currentCharacter == '=') {
                            currentToken = new Token(Token.TokenType.GREATER_THAN_EQUALS_TOKEN);
                        } else {
                            contentIndex--;
                            currentToken = new Token(Token.TokenType.GREATER_THAN_TOKEN);
                        }

                        break;
                    case CHECK_LESS_EQUAL:
                        currentState = State.DONE;
                        if (currentCharacter == '=') {
                            currentToken = new Token(Token.TokenType.LESS_THAN_EQUALS_TOKEN);
                        } else {
                            contentIndex--;
                            currentToken = new Token(Token.TokenType.LESS_THAN_TOKEN);
                        }

                        break;
                    case CHECK_NOT_EQUAL:
                        currentState = State.DONE;
                        if (currentCharacter == '=') {
                            currentToken = new Token(Token.TokenType.NOT_EQUALS_TOKEN);
                        } else {
                            contentIndex--;
                            currentToken = new Token(Token.TokenType.ERROR);
                        }

                        break;
                    case CHECK_ENTERING_COMMENT:
                        if (currentCharacter == '*') {
                            currentState = State.IN_COMMENT;
                        } else {
                            contentIndex--;
                            currentToken = new Token(Token.TokenType.DIVIDE_TOKEN);
                            currentState = State.DONE;
                        }

                        break;
                    case IN_COMMENT:
                        if (currentCharacter == '*') {
                            currentState = State.CHECK_LEAVING_COMMENT;
                        }
                        break;
                    case CHECK_LEAVING_COMMENT:
                        if (currentCharacter == '/') {
                            currentState = State.START;
                        } else {
                            currentState = State.IN_COMMENT;
                        }

                        break;
                    default:
                        currentState = State.DONE;
                        currentToken = new Token(Token.TokenType.ERROR);
                        break;
                }
            }
            if (currentState == State.DONE) {
                if (currentToken.getTokenType() == Token.TokenType.ID_TOKEN) {
                    String tokenValue = fileContents.substring(tokenStartIndex, contentIndex + 1);
                    switch (tokenValue) {
                        case "else":
                            currentToken = new Token(Token.TokenType.ELSE_TOKEN);
                            break;
                        case "if":
                            currentToken = new Token(Token.TokenType.IF_TOKEN);
                            break;
                        case "int":
                            currentToken = new Token(Token.TokenType.INT_TOKEN);
                            break;
                        case "return":
                            currentToken = new Token(Token.TokenType.RETURN_TOKEN);
                            break;
                        case "void":
                            currentToken = new Token(Token.TokenType.VOID_TOKEN);
                            break;
                        case "while":
                            currentToken = new Token(Token.TokenType.WHILE_TOKEN);
                            break;
                        default:
                            currentToken.setTokenData(tokenValue);
                            break;
                    }
                } else if (currentToken.getTokenType() == Token.TokenType.NUM_TOKEN) {
                    Integer tokenValue = Integer.parseInt(fileContents.substring(tokenStartIndex, contentIndex + 1));
                    currentToken.setTokenData(tokenValue);
                }
            }
            contentIndex++;
        }
        return currentToken;
    }

    @Override
    public Token viewNextToken() {
        return null;
    }

    public void scan() {
        if (fileContents != null) {
            Token currentToken = getNextToken();
            tokensFound.add(currentToken);
            while (currentToken.getTokenType() != Token.TokenType.END_OF_FILE) {
                currentToken = getNextToken();
                tokensFound.add(currentToken);
            }
        }
    }

    private String readFile(File file) {
        try {
            byte[] fileBytes = Files.readAllBytes(file.toPath());
            return new String(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printAllTokens() {
        for (Token token : tokensFound) {
            if (token.getTokenType() == Token.TokenType.ID_TOKEN ||
                    token.getTokenType() == Token.TokenType.NUM_TOKEN) {
                System.out.println(token.getTokenType().toString() + ": " + token.getTokenData());
            } else {
                System.out.println(token.getTokenType().toString());
            }
        }
    }
}
