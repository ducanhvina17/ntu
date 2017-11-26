import java.util.ArrayList;
import java.util.Map;

import org.jpl7.*;

class Query {
    ArrayList<Term> query(String X) {
        if (!org.jpl7.Query.hasSolution("consult('src/assignment.pl')"))
            System.out.println("Consult failed");

        org.jpl7.Query q1 = new org.jpl7.Query(X);
        if (!q1.hasSolution()) {
            System.out.println(X + " has no solution");
            return null;
        }

        Map<String, Term>[] a = q1.allSolutions();
        ArrayList<Term> categories = new ArrayList<>();

        for (int i = 0; i < a.length; i++) {
            Term term = a[i].get("X");
            System.out.println("X = " + term);

            int u = 0;
            for (Term oneTerm : term.toTermArray()) {
                categories.add(oneTerm);
                System.out.println("term[" + i + "][" + u + "] = " + oneTerm);
                u++;
            }
        }

        return categories;
    }
}