import Diagnostics.VarSymbol;
import Evaluator.*;
import Diagnostics.Diagnostic;
import Diagnostics.DiagnosticBag;
import Parser.*;

import java.util.*;

public class Jinsk {
    public static void main(final String[] args) {
        STree syntaxTree;
        final Scanner s = new Scanner(System.in);
        String line;
        DiagnosticBag diagnostics;
        Compilation compilation;
        EvaluationResult evaluationResult;
        final Map<VarSymbol, Object> variables = new HashMap<>();

        while (true) {
            System.out.print("jin > ");
            line = s.nextLine();
            if (line.isEmpty()) {
                break;
            }

            syntaxTree = STree.getTree(line);
            compilation = new Compilation(syntaxTree);
            diagnostics = syntaxTree.diagnostics();
            diagnostics.addAll(compilation.getDiagnostics());

            if (!diagnostics.isEmpty()) {
                printDiagnostics(line, diagnostics);
                break;
            } else {
                try {
                    evaluationResult = compilation.Evaluate(variables);
                    diagnostics.addAll(evaluationResult.diagnostics());

                    if (!diagnostics.isEmpty()) {
                        printDiagnostics(line, diagnostics);
                        break;
                    }

                    if (evaluationResult.value() != null) {
                        System.out.println("jin < " + evaluationResult.value());
                    } else {
                        System.out.println();
                    }
                } catch (final Exception e) {
                    System.err.println("<!> (Compilation) " + e);
                    break;
                }
            }
        }
    }

    private static void printDiagnostics(final String src, final DiagnosticBag diagnosticBag) {
        // If an error occurs twice, only print it out once; record it here.
        final List<Diagnostic> previousErrors = new ArrayList<>();

        for (final Diagnostic d: diagnosticBag.getDiagnostics()) {
            if (previousErrors.contains(d)) {
                continue;
            }

            previousErrors.add(d);

            System.err.println("<!> (Parser) [" + d.span() + "], " + d.msg());

            final String prefix = src.substring(0, d.span().start());
            final String error = src.substring(d.span().start(), d.span().getEnd());
            final String suffix = src.substring(d.span().getEnd());

            System.err.print("\t");
            System.err.print(prefix);

            System.err.print(error);

            System.err.println(suffix);
            System.err.print("\t");

            // Print underline:
            for (int i = 0; i < prefix.length(); i++) {
                System.err.print(" ");
            }

            for (int i = 0; i < error.length(); i++) {
                System.err.print("^");
            }

            System.err.println();
        }
    }
}
