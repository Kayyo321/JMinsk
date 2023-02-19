import Diagnostics.VarSymbol;
import Evaluator.*;
import Diagnostics.Diagnostic;
import Diagnostics.DiagnosticBag;
import Parser.*;

import java.io.IOException;
import java.util.*;

public class Jinsk {
    // If an error occurs twice, only print it out once; record it here.
    private static final List<Diagnostic> previousErrors = new ArrayList<>();

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
            } else if (line.charAt(0) == '$') {
                line = line.replace('$', ' ');
                try {
                    Runtime.getRuntime().exec("cmd /c " + line);
                } catch (final IOException e) {
                    System.err.println("<!> (Command) '" + line + "', was returned this error message: " + e);
                }

                continue;
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
                    evaluationResult = compilation.evaluate(variables);
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
        for (final Diagnostic d: diagnosticBag.getDiagnostics()) {
            if (previousErrors.contains(d)) {
                continue;
            }

            previousErrors.add(d);

            System.err.println("<!> (Diagnostic) [" + d.span() + "], " + d.msg());

            final String prefix = src.substring(0, d.span().start());
            // Exception will occur when printing out raw EOF, print fake instead.
            final String error = d.msg().contains("<Eof>") ? "\\0" : src.substring(d.span().start(), Math.min(d.span().getEnd(), src.length()));
            final String suffix = d.msg().contains("<Eof>") ? "..." : src.substring(d.span().getEnd());

            System.err.print("\t");
            System.err.print(prefix);

            System.err.print(error);

            System.err.println(suffix);
            System.err.print("\t");

            // Print underline:
            int i;

            for (i = 0; i < prefix.length(); i++) {
                System.err.print(" ");
            }

            for (i = 0; i < error.length(); i++) {
                System.err.print("^");
            }

            System.err.println();
        }
    }
}
