package org.leibnizcenter.cfg.rule;

import org.leibnizcenter.cfg.algebra.semiring.dbl.DblSemiring;
import org.leibnizcenter.cfg.algebra.semiring.dbl.LogSemiring;
import org.leibnizcenter.cfg.category.Category;
import org.leibnizcenter.cfg.category.nonterminal.NonTerminal;

/**
 * Factory for {@link Rule rules}
 * <p>
 * Created by Maarten on 31-7-2016.
 */
public class RuleFactory {
    private final DblSemiring semiring;

    public RuleFactory(DblSemiring semiring) {
        this.semiring = semiring;
    }

    @SuppressWarnings("unused")
    public RuleFactory() {
        this(LogSemiring.get());
    }

    /**
     * Instantiates a new rule with given probability <strong>as a probability between 0 and 1</strong>. The
     * semiring will take care in converting the number.
     */
    private static Rule newRuleWithRawProbability(double probability, double srElement, NonTerminal LHS, Category... RHS) {
        return new Rule(probability, srElement, LHS, RHS);
    }

    /**
     * Instantiates a new rule with a probability score of one (whatever that means for the given semiring)
     */
    public Rule newRule(NonTerminal LHS, Category... RHS) {
        return newRuleWithRawProbability(1.0, semiring.one(), LHS, RHS);
    }

    /**
     * Instantiates a new rule with given probability <strong>as a probability between 0 and 1</strong>. The
     * semiring will take care in converting the number.
     */
    public Rule newRule(double probability, NonTerminal LHS, Category... RHS) {
        return newRuleWithRawProbability(probability, semiring.fromProbability(probability), LHS, RHS);
    }

    public LexicalErrorRule newLexicalErrorRule(double probability, NonTerminal LHS, Category... RHS) {
        return new LexicalErrorRule(probability, semiring.fromProbability(probability), LHS, RHS);
    }
}
