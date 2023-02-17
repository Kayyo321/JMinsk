import Evaluator.*;
import Diagnostics.Diagnostic;
import Diagnostics.DiagnosticBag;
import Parser.*;

import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        // Define all possible vars outside inf-loop!
        STree syntaxTree;
        final Scanner s = new Scanner(System.in);
        String line;
        DiagnosticBag diagnostics;
        Compilation compilation;
        EvaluationResult evaluationResult;

        while (true) {
            System.out.print("jin> ");
            line = s.nextLine();
            if (line.isEmpty()) {
                break;
            }

            syntaxTree = STree.getTree(line);
            compilation = new Compilation(syntaxTree);
            diagnostics = syntaxTree.getDiagnostics();
            diagnostics.addAll(compilation.getDiagnostics());

            if (!diagnostics.isEmpty()) {
                for (final Diagnostic d: diagnostics.getDiagnostics()) {
                    System.err.println("<!> (Parser) [" + d.getSpan() + "], " + d.getMsg());

                    final String prefix = line.substring(0, d.getSpan().getStart());
                    final String error = line.substring(d.getSpan().getStart(), d.getSpan().getEnd());
                    final String suffix = line.substring(d.getSpan().getEnd());

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

                break;
            } else {
                try {
                    evaluationResult = compilation.Evaluate();

                    if (evaluationResult.getValue() != null) {
                        System.out.println("<<< " + evaluationResult.getValue());
                    } else {
                        System.out.println("<<< (null result)");
                    }
                } catch (final Exception e) {
                    System.err.println("<!> (Compilation) " + e);
                    break;
                }
            }
        }
    }
}
