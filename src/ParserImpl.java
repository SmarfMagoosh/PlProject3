import java.util.Stack;

enum Term {
    T, F, L, M, A
}

public class ParserImpl extends Parser {
    private Expr currExpr = null;

    private TokenList filterWS(TokenList ts) {
        if (ts == null) {
            return null;
        } else if (ts.elem.ty == TokenType.WHITE_SPACE) {
            return filterWS(ts.rest);
        } else {
            return new TokenList(ts.elem, filterWS(ts.rest));
        }
    }

    /*
     * Implements a recursive-descent parser for the following CFG:
     * 
     * T -> F AddOp T              { if ($2.type == TokenType.PLUS) { $$ = new PlusExpr($1,$3); } else { $$ = new MinusExpr($1, $3); } }
     * T -> F                      { $$ = $1; }
     * F -> Lit MulOp F            { if ($2.type == TokenType.Times) { $$ = new TimesExpr($1,$3); } else { $$ = new DivExpr($1, $3); } }
     * F -> Lit                    { $$ = $1; }
     * Lit -> NUM                  { $$ = new FloatExpr(Float.parseFloat($1.lexeme)); }
     * Lit -> LPAREN T RPAREN      { $$ = $2; }
     * AddOp -> PLUS               { $$ = $1; }
     * AddOp -> MINUS              { $$ = $1; }
     * MulOp -> TIMES              { $$ = $1; }
     * MulOp -> DIV                { $$ = $1; }
     */
    @Override
    public Expr do_parse() throws Exception {
        tokens = filterWS(tokens);
        return parseT();
    }

    private Expr parseT() throws Exception {
        Expr left = parseF();
        if (peek(TokenType.PLUS, 0)) {
            consume(TokenType.PLUS);
            return new PlusExpr(left, parseT());
        } else if (peek(TokenType.MINUS, 0)) {
            consume(TokenType.MINUS);
            return new MinusExpr(left, parseT());
        }
        return left;
    }

    private Expr parseF() throws Exception {
        Expr left = parseLit();
        if (peek(TokenType.TIMES, 0)) {
            consume(TokenType.TIMES);
            return new TimesExpr(left, parseF());
        } else if (peek(TokenType.DIV, 0)) {
            consume(TokenType.DIV);
            return new DivExpr(left, parseF());
        }
        return left;
    }

    private Expr parseLit() throws Exception {
        if (peek(TokenType.NUM, 0)) {
            String s = consume(TokenType.NUM).lexeme;
            float lit = Float.parseFloat(s);
            return new FloatExpr(lit);
        } else if (peek(TokenType.LPAREN, 0)) {
            consume(TokenType.LPAREN);
            Expr t = parseT();
            consume(TokenType.RPAREN);
            return t;
        }
        throw new Exception("Unexpected token: " + tokens.elem);
    }
}
