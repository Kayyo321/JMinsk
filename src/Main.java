import Evaluator.*;
import Parser.*;
import Binding.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(final String[] args) {
        Evaluator evaluator;
        STree syntaxTree;
        final Scanner s = new Scanner(System.in);
        String line;
        Object result;
        List<String> diagnostics;
        Binder binder = new Binder();
        BoundExpression boundExpression;

        while (true) {
            System.out.print("jin> ");
            line = s.nextLine();
            if (line.isEmpty()) {
                break;
            }

            syntaxTree = STree.getTree(line);

            diagnostics = syntaxTree.getDiagnostics();
            diagnostics.addAll(binder.getDiagnostics());

            boundExpression = binder.bindExpr(syntaxTree.getRoot());

            if (!diagnostics.isEmpty()) {
                for (final String diagnostic: diagnostics) {
                    System.err.println("<!> " + diagnostic);
                }

                break;
            } else {
                evaluator = new Evaluator(boundExpression);
                try {
                    result = evaluator.eval();

                    System.out.println("<DEBUG> result: " + result);
                } catch (final Exception e) {
                    System.err.println("<!> (Thrown from evaluator) " + e);
                    break;
                }
            }
        }
    }
}
