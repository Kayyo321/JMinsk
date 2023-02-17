package Evaluator;

import Diagnostics.DiagnosticBag;

public class EvaluationResult {
    private final DiagnosticBag diagnostics;
    private final Object value;

    public EvaluationResult(final DiagnosticBag diagnostics, final Object value) {

        this.diagnostics = diagnostics;
        this.value = value;
    }

    public DiagnosticBag getDiagnostics() { return diagnostics; }
    public Object getValue() { return value; }
}
