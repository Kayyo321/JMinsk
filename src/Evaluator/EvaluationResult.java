package Evaluator;

import Diagnostics.DiagnosticBag;

public record EvaluationResult(DiagnosticBag diagnostics, Object value) {}
