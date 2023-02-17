package Evaluator;

import Binding.Binder;
import Binding.BoundExpression;
import Diagnostics.DiagnosticBag;
import Parser.STree;

public class Compilation {
    private final STree syntax;
    private DiagnosticBag diagnostics = new DiagnosticBag();

    public Compilation(final STree syntax) {
        this.syntax = syntax;
    }

    public STree getSyntax() { return this.syntax; }
    public DiagnosticBag getDiagnostics() { return this.diagnostics; }

    public EvaluationResult Evaluate() throws Exception {
        final Binder binder = new Binder();
        final BoundExpression boundExpression = binder.bindExpr(syntax.getRoot());

        this.diagnostics = syntax.getDiagnostics();
        this.diagnostics.addAll(binder.getDiagnostics());

        if (!diagnostics.isEmpty()) {
            return new EvaluationResult(diagnostics, null);
        }

        final Evaluator evaluator = new Evaluator(boundExpression);
        final Object value = evaluator.eval();

        return new EvaluationResult(new DiagnosticBag(), value);
    }
}
