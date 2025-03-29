public class CompilerFrontendImpl extends CompilerFrontend {
    public CompilerFrontendImpl() {
        super();
    }

    public CompilerFrontendImpl(boolean debug_) {
        super(debug_);
    }

    /*
     * Initializes the local field "lex" to be equal to the desired lexer.
     * The desired lexer has the following specification:
     * 
     * NUM: [0-9]*\.[0-9]+
     * PLUS: \+
     * MINUS: -
     * TIMES: \*
     * DIV: /
     * WHITE_SPACE (' '|\n|\r|\t)*
     */
    @Override
    protected void init_lexer() {
        lex = new LexerImpl();
        lex.add_automaton(TokenType.NUM, createNum());
        lex.add_automaton(TokenType.DIV, createDivide());
        lex.add_automaton(TokenType.LPAREN, createLParen());
        lex.add_automaton(TokenType.MINUS, createMinus());
        lex.add_automaton(TokenType.PLUS, createPlus());
        lex.add_automaton(TokenType.RPAREN, createRParen());
        lex.add_automaton(TokenType.TIMES, createTimes());
        lex.add_automaton(TokenType.WHITE_SPACE, createWhiteSpace());
    }

    private Automaton createNum() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(2, false, true);
        for (char i = '0'; i <= '9'; i++) {
            auto.addTransition(0, i, 0);
            auto.addTransition(1, i, 2);
            auto.addTransition(2, i, 2);
        }
        auto.addTransition(0, '.', 1);
        return auto;
    }

    private Automaton createPlus() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, '+', 1);
        return auto;
    }

    private Automaton createMinus() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, '-', 1);
        return auto;
    }

    private Automaton createTimes() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, '*', 1);
        return auto;
    }

    private Automaton createDivide() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, '/', 1);
        return auto;
    }

    private Automaton createLParen() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, '(', 1);
        return auto;
    }

    private Automaton createRParen() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, false);
        auto.addState(1, false, true);
        auto.addTransition(0, ')', 1);
        return auto;
    }

    private Automaton createWhiteSpace() {
        Automaton auto = new AutomatonImpl();
        auto.addState(0, true, true);
        auto.addTransition(0, ' ', 0);
        auto.addTransition(0, '\n', 0);
        auto.addTransition(0, '\t', 0);
        auto.addTransition(0, '\r', 0);
        return auto;
    }
}
