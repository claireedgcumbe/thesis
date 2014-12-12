package minicon;
import java.util.Vector;

public class VectorSort
{
    public static void sort(Vector a_vector)
    {
	   Object [] array = a_vector.toArray();	   
	   int size = a_vector.size();
	   
	   Quick(array,0,size - 1);
	   a_vector.clear();
	   int i;
	   for (i = 0; i < size;i++){
		  a_vector.addElement(array[i]);
	   }
	   

	   
    }
    

    public static int Pivot(Object [] an_array ,int first,int last)
    // postcondition: returns piv such that
    //                first <= k <= piv, an_array[k] <= an_array[piv]
    //                piv < k <= last, an_array[piv] < an_array[k]                
    // (from Bently, programming Pearls)
    {
	   int k,p=first;
	   double piv;
	   Object temp;
	   
	   piv = ((Double)an_array[first]).doubleValue();                    // first element is pivot
	   
	   for(k=first+1; k <= last; k++)
		  {
			 if (((Double)an_array[k]).doubleValue() <= piv)               // belongs in first "half"
				{
				    p++;                       // p now indexes "greater" element
				    temp = an_array[p];
				    an_array[p] = an_array[k];
				    an_array[k] = temp;
				    
				    //Swap(an_array[k],an_array[p]);           // swap smaller and greater!
				}
		  }
	   temp = an_array[p];
	   an_array[p] = an_array[first];
	   an_array[first] = temp;
	   
	   //Swap(an_array[p],an_array[first]);               // put pivot in proper location
	   return p;
    }
    
    public static void Quick( Object an_array[],int first,int last)
    // postcondition: an_array[first] <= ... <= an_array[list]     
    {
	   int piv;
	   
	   if (first < last)
		  {
			 piv = Pivot(an_array,first,last);    // find pivot and divide vector
			 Quick(an_array,first,piv-1);         // sort first "half"
			 Quick(an_array,piv+1,last);          // sort other  half
		  }
    }
}

