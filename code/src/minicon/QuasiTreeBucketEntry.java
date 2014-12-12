package minicon;
import java.util.Vector;
public class QuasiTreeBucketEntry
{
	Vector _entries;
	boolean [] _subgoals_covered;
	int _num_subgoals;
	public QuasiTreeBucketEntry(MDWithoutExistentialCheck a_md){
		int i;
		_num_subgoals = a_md.size();
		_subgoals_covered = new boolean[_num_subgoals];
		for (i = 0; i < _num_subgoals; i++){
			_subgoals_covered[i] = a_md.subgoalsCovered[i];
		}
		_entries= new Vector(10);
		_entries.addElement(a_md);
	}
	
	public int size(){
		return _entries.size();
	}
	public boolean addEntry(MDWithoutExistentialCheck a_md){
		int i;
		boolean [] a_md_subgoals = a_md.subgoalsCovered;
		for (i = 0; i < _num_subgoals; i++){
			if (_subgoals_covered[i] != a_md_subgoals[i]){
				return false;
			}
		}
		//if, at this point, we're still here, then we can add the value
		_entries.addElement(a_md);
		return true;
	}
	
	public boolean[] getSubgoals(){
		return _subgoals_covered;
	}
	
	public boolean checkCovered(int i){
		return _subgoals_covered[i];
	}
	
	public QuasiTreeBucketEntry copy(){
		QuasiTreeBucketEntry retval = new QuasiTreeBucketEntry(MDI(0));
		//now we just need to add the rest of the elments;
		int i;
		int num_elts = _entries.size();
		for (i = 1; i < num_elts; i++){
			retval._entries.addElement(_entries.elementAt(i));
		}
		return retval;	
	}
										 
	public MDWithoutExistentialCheck MDI(int i){
		return (MDWithoutExistentialCheck)_entries.elementAt(i);
	}
}
