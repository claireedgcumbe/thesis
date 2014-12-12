//import java.lang.*;
package minicon;
//import java.io.*;
//import java.util.Vector;


public class BucketMD{
        //the md for the new bucket algorithm.
        //since we are dealing with the original bucket algorithm, 
        //the only thing that we need to know is what variables the head 
        //uses, and what the original view is so that we can check 
        //equality later, and use in expansion.
    public Predicate _head;
    public View _original;
    public QueryEquality _query_equality;
    public BucketMD(){
        _head = new Predicate();
        _original = new View();
        _query_equality = new QueryEquality();
}
    
    public BucketMD(Predicate head, View original){
        Predicate original_head = original.getHead();
        _head = new Predicate(head);
        //note, we make a copy of the head rather than just maintaining the 
        //pointer because later when we set the variables to be the same, we'll 
        //need them seperate, and this will be faster than doing a character by 
        //character search on the string
        _original = original;
        //now we need to add the query equality.  fortunately, since we should
        //never wind up with more than 10 or so variables, n_2 shouldn't be too bad
        _query_equality = new QueryEquality();
        int i, j;
        for (i = 0; i < head.size(); i++){
            for (j = i+1; j < head.size(); j++){
                //loop over them, see if they're the same
                if (head.variableI(i).equals(head.variableI(j))){
                    //then we need to add an equality; we assume that
                    //all views had no two variables the same... which is a 
                    //reasonable thing to assume.  since we only need to know
                    //about variables in the head, this is all  that we need to know.
                    _query_equality.addEquality(original_head.variableI(i), original_head.variableI(j));
                }
            }
        }
    }
}
