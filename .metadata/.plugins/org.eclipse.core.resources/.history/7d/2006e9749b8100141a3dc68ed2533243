package minicon;
import java.util.Vector;
public class Set
{
	private Vector [] _set;
	private int _size;
	public Set(int a_size){
		_size = a_size;
	}
	
	
	public Vector createPartitions(){
		int i;
		return  recursiveCreatePartitions(0, new Vector[_size+1]);
		
	}
	
	private Vector recursiveCreatePartitions(int num_to_add, Vector [] vector_so_far){
		int i,j;
		boolean keep_going;
		Vector retval = new Vector();
		Vector temp_vector;
		Vector [] next_vector;
		for( i = 0, keep_going = true; i < _size && keep_going; i++){
			if (vector_so_far[i]== null){
				//if there was nothing in it so far, then we don't want to keep going
				//after this
				vector_so_far[i] = new Vector();
				keep_going = false;
			}
			//now add to the current vector so far
			next_vector = copyVectorArray(vector_so_far);
			//note, need to make sure that this is going to give a deep copy rather
			//than a shallow copy...
			next_vector[i].addElement(new Integer(num_to_add));
			if (num_to_add != _size -1){
				//if we need to add any more
				temp_vector = recursiveCreatePartitions(num_to_add+1,next_vector);
				for (j = 0; j < temp_vector.size(); j++){
					retval.addElement(temp_vector.elementAt(j));
				}
			}//end if we need to check the next value
			else {
				//otherwise, we need to add on the current vector
				retval.addElement(next_vector);
			}
		}//end of looping over all of the the partions
		return retval;
	}//end recursiveCreatePartitions
																	
																		
	private Vector[] copyVectorArray(Vector [] an_array){
		//this function makes a deep copy of a vector array
		int i,j;
		Vector [] retval = new Vector[_size];
		Vector a_vector;
		boolean keep_going;
		for (i = 0, keep_going = true; i < _size && keep_going; i++){
			a_vector = an_array[i];
			if (an_array[i] == null){
				keep_going = false;
				//we know that there will be nothing after this
			}
			else {
				//we need to add the contents of this vector and check the rest
				retval[i] = new Vector();
				for (j = 0; j < a_vector.size();j++){
					retval[i].addElement(a_vector.elementAt(j));
				}
			}
		}

		//now we need to get the size variable
		//retval[_size] = an_array[_size]
		return retval;
	}
	
	public int size(){
		return _size;
	}
	
	public static void main(String args[]){
		int a_size = 4;
		Permutation p = new Permutation(a_size);
		int [] returned = null;
		int i,j,k;
		Vector answers;
		Set s = new Set(a_size);
		Vector [] a_partition_set;
		Vector a_partition;
		answers = s.createPartitions();
		/*while(p.getNext()!=-1){
			returned = p.getCurrent();
			for (i = 0; i < a_size; i++){
				System.out.print(returned[i] + " ");
			}
			System.out.println(" ");
		}
		*/
		for (i = 0; i < answers.size();i++){
			a_partition_set = (Vector[])answers.elementAt(i);
			for(j = 0; j < a_size; j++){
				a_partition = a_partition_set[j];
				if(a_partition != null){
					for (k = 0; k < a_partition.size();k++){
						System.out.print(((Integer)a_partition.elementAt(k)).toString() + " ");
					}
					System.out.print("|");
				}
			}//end looping over one partition
			System.out.println(" ");
		}
		System.out.println("done");
	}
}
