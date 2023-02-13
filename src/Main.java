import Evaluator.Evaluator;
import Parser.*;

import java.util.Scanner;

public class Main {
    private Main() {
        Evaluator evaluator;
        STree syntaxTree;
        final Scanner s = new Scanner(System.in);
        String line;

        while (true) {
            System.out.print("> ");
            line = s.nextLine();
            if (line.isEmpty()) {
                break;
            }

            syntaxTree = STree.getAST(line);

            if (!syntaxTree.getDiagnostics().isEmpty()) {
                for (final String diagnostic: syntaxTree.getDiagnostics()) {
                    System.err.println("<!> " + diagnostic);
                }
            } else {
                evaluator = new Evaluator(syntaxTree.getRoot());
                try {
                    final float result = evaluator.eval();

                    System.out.println("<DEBUG> result: " + result);
                } catch (final Exception e) {
                    System.err.println("<!> " + e.toString());
                }
            }
        }
    }

    public static void main(final String[] args) {
        new Main();
    }
}
