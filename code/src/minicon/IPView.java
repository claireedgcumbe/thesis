//import java.lang.*;
package minicon;
import java.util.Vector;
public class IPView extends StatementWIP {
    public IPView (StatementWIP astate){
		super(astate);       
	}
	
	
    public IPView(){
        super();
    }
    public IPView(String a_string)
    {
	   super(a_string);
	   
    }
    
			

	
    public IPView(IPView a){
		super((StatementWIP)a);
	}//end copy constructor
}
