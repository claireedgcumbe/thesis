//import java.lang.*;
package minicon;
import java.io.*;
import java.util.Vector;


public class RunCanonicalExamples{
    //private Query currentQuery;
    //private Vector Views;
    Algorithm _algorithm;
    public RunCanonicalExamples(){
        //currentQuery = new Query();
        //Views = new Vector(10);
        _algorithm = null;
}
    
    public void testExclusions(){
        String output;
        Vector results;
        _algorithm = new BucketEndingAlgorithm();
        Query a_query = new Query();
        View a_view_1 = new View();
        View a_view_2 = new View();
        a_query.read("q(a):-e(a,b),e1(b,c),e2(c,d)");
        a_view_1.read("v1(x,y):-e(x,z),e1(z,y)");
        a_view_2.read("v2(r,s):-e1(r,t),e2(t,s)");
        _algorithm.setQuery(a_query);
        _algorithm.addView(a_view_1);
        _algorithm.addView(a_view_2);
        output = _algorithm.run();
        results = _algorithm.expandAllAnswers(output);
        int i;
        for (i = 0; i < results.size();i++){
            System.out.println(_algorithm.answerIsContained((Statement)results.elementAt(i)));
            ((Statement)results.elementAt(i)).print();
        }

		
		//}

        //output = _algorithm.printRewritings(results);
        //System.out.print(output);
  		System.out.println(results.size());
      
    }
    
    public void testEqualities(){
        String output;
        Vector results;
        _algorithm = new BucketEndingAlgorithm();
        Query a_query = new Query();
        View a_view_1 = new View();
        View a_view_2 = new View();
        a_query.read("q(x):-e(x,y,z),e2(y,m)");
        a_view_1.read("v1(a):-e(a,a,z)");
        a_view_2.read("v2(b):-e2(b,m)");
        _algorithm.setQuery(a_query);
        _algorithm.addView(a_view_1);
        _algorithm.addView(a_view_2);
        output = _algorithm.run();
        results = _algorithm.expandAllAnswers(output);
         int i;
        for (i = 0; i < results.size();i++){
            System.out.println(_algorithm.answerIsContained((Statement)results.elementAt(i)));
            
            ((Statement)results.elementAt(i)).print();
        }
		System.out.println(results.size());
        //output = _algorithm.printRewritings(results);
        //System.out.print(output);
    }       
    
    public void testEasy(){
        String output;
        Vector results;
        //Algorithm a = new Algorithm();
        _algorithm = new BucketEndingAlgorithm();
        Query a_query = new Query();
        View a_view = new View();
        a_query.read("q(a):-e(a,b),e(b,a)");
        a_view.read("v(c,d):-e(c,f),e(f,d)");
        _algorithm.setQuery(a_query);
        _algorithm.addView(a_view);
        a_view = new View();
        a_view.read("v2(e,f):-e(e,g),e(g,f)");
        _algorithm.addView(a_view);
        output = _algorithm.run();
         results = _algorithm.expandAllAnswers(output);
         int i;
        for (i = 0; i < results.size();i++){
            System.out.println(_algorithm.answerIsContained((Statement)results.elementAt(i)));
            ((Statement)results.elementAt(i)).print();
        }
        //output = _algorithm.printRewritings(results);
        //System.out.print(output);
		System.out.println(results.size());

	}
	
	public void testQuestions(){
	        String output;
        Vector results;
        //Algorithm a = new Algorithm();
        _algorithm = new BucketEndingAlgorithm();
        Query a_query = new Query();
        View a_view = new View();
        a_query.read("q(x) :- e1(x), e2(x,y), e3(y,z), y > 25");
        a_view.read("V1(A):- e1(A)");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V2(B):- e2(B,C), C > 25");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V3(E):- e2(E,D), e3(D,F), D > 24");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V4(G):- e2(G,H), e3(H,I), H > 26");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V5(K):- e1(J), e2(J,K), e3(K,L), K > 25");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V6(M,N):- e2(M,N)");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V7(P):- e1(O),e2(O,P)");
        _algorithm.addView(a_view);
        a_view = new View();
		a_view.read("V8(R):- e3(R,R)");
        _algorithm.setQuery(a_query);
        _algorithm.addView(a_view);
        output = _algorithm.run();
        results = _algorithm.expandAllAnswers(output);
		System.out.println(output); 
        int i;
       // for (i = 0; i < output.size();i++){
        //    //System.out.println(_algorithm.answerIsContained((Statement)results.elementAt(i)));
            //((Statement)output.elementAt(i)).print();
       // }
 
	}//end testQuestions
	
    public static void main(String args[]){
        RunCanonicalExamples e = new RunCanonicalExamples();
		e.testEasy();
		e.testEqualities();
		e.testExclusions();
		//e.testQuestions();
        System.out.println("done");
    }
    
}
