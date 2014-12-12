

package minicon;
public class Mapping{
    public IPValue variable;
    IPValue mapping;
    
    public Mapping(){
        variable = new IPValue();
        mapping = new IPValue();
}
    
    public Mapping(Mapping amap){
        variable = new IPValue (amap.variable);
		if (amap.mapping == null){
			mapping = null;
		}
		else{
			mapping = new IPValue (amap.mapping);
		}
    }
    
    public Mapping(IPValue newvar,IPValue amap){
        variable = new IPValue(newvar);
		if (amap == null){
			mapping = null;
		}
		else{
			mapping = new IPValue(amap);
		}
    }

    public Mapping(String newvar,String amap){
        variable = new IPValue(newvar);
		if (amap == null){
			mapping = null;
		}
		else{
			mapping = new IPValue(amap);
		}
    }
    
    public void print(){
        System.out.print(printString());
    }
    
    public StringBuffer printString(){
        StringBuffer retval = new StringBuffer();
        retval.append(variable.printString());
        retval.append("->");
        retval.append(mapping.printString());
        return retval;
    }
	
	public Object clone(){
		return new Mapping(variable,mapping);
	}
}
