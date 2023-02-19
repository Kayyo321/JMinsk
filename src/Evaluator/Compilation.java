package Evaluator;

import Binding.Binder;
import Binding.BoundExpression;
import Diagnostics.DiagnosticBag;
import Diagnostics.VarSymbol;
import Parser.STree;

import java.util.Map;

public class Compilation {
    private final STree syntax;
    private DiagnosticBag diagnostics = new DiagnosticBag();

    public Compilation(final STree syntax) {
        this.syntax = syntax;
    }

    public DiagnosticBag getDiagnostics() { return this.diagnostics; }

    public EvaluationResult evaluate(final Map<VarSymbol, Object> variables) throws Exception {
        final Binder binder = new Binder(variables);
        final BoundExpression boundExpression = binder.bindExpr(syntax.root());

        this.diagnostics = syntax.diagnostics();
        this.diagnostics.addAll(binder.getDiagnostics());

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(diagnostics, null);
        }

        final Evaluator evaluator = new Evaluator(boundExpression, variables);
        final Object value = evaluator.eval();

        return new EvaluationResult(new DiagnosticBag(), value);
    }
}
