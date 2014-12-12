package minicon;
//import java.lang.*;

public class IPQuery extends StatementWIP{
    //the point here is to just make sure that you don't 
    //call a view with a statement and 
    //vice versa
    public IPQuery (StatementWIP a_state){
	   super(a_state);
    }

    public IPQuery(String a_string)
    {
	   super(a_string);
    }
    

    public IPQuery(){
	   super();
    }

    public int size(){
	   return super.size();
    }
}
