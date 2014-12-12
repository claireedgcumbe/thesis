//import java.lang.*;
package minicon;
import java.util.Vector;
import java.util.Random;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.io.*;


public class QuasiTreeAlgorithm extends BucketEndingAlgorithm{
	//protected Vector [] _buckets;
	public QuasiTreeAlgorithm(){
		super();
		//_buckets = null;
	}


	public String type(){
		return "QuasiTree";
	}

	
	public String printRewritings(Vector rewritings){
		StringBuffer retval = new StringBuffer("");
		int i;
		_num_rewritings = 0;
		QuasiTreeMetaMD a_meta_md;
		if (rewritings == null || rewritings.size()==0){
			return ("no results\n");
		}
		
		for (i = 0; i < rewritings.size(); i++){
			a_meta_md = (QuasiTreeMetaMD)rewritings.elementAt(i);
			retval.append(a_meta_md.getRewritings());
			_num_rewritings+= a_meta_md.getNumRewritings();
		}


		return retval.toString();
	}//end printRewritings
	
	public Vector combineMDList(){
		//this function combines the md list.  this is completely different from the 
		//old version, in fact, I think this one will be recursive for clarity.
		return combineMDBuckets(0,new QuasiTreeMetaMD(myQuery.getHead(),_size),0);
	}//end combineMDList

		
	public Vector combineMDBuckets(int bucket_to_check, QuasiTreeMetaMD meta_MD, int current_bucket_elt){
		QuasiTreeMetaMD new_meta_md = meta_MD.copy();
		Vector retval = null;
		Vector temp_vector = null;
		int next_to_check;
		if (_buckets[bucket_to_check].size() == 0){
			return null;
		}
		if (new_meta_md.addMD((QuasiTreeBucketEntry)_buckets[bucket_to_check].elementAt(current_bucket_elt))){
			//then we need to check this one and add it with the rest
			next_to_check = new_meta_md.getNextUncovered(bucket_to_check);
			if (next_to_check != -1){
				//then we need to check the next bucket
				//and we need to check all items in that bucket, so we need to start at zero
				//note, this should fix a previous bug
				temp_vector = combineMDBuckets(next_to_check,new_meta_md,0);
			}//end of if we need to check the next bucket	
			if (new_meta_md.getNumUncovered() != 0){
				//then this wasn't a final answer
				new_meta_md = null;
			}
		}//end if we needed to add this one
		else{
			//we did nothing, so I'll mark that by setting new_meta_md to null
			new_meta_md = null;
		}
		//now check the rest of the current bucket
		//next_to_check = new_meta_md.getNextUncovered(bucket_to_check);
		if (current_bucket_elt +1 < _buckets[bucket_to_check].size()){
			//then we need to check the rest.
			retval = combineMDBuckets(bucket_to_check,meta_MD,current_bucket_elt +1);
		}
		//now we have to combine the answers
		if (retval == null && temp_vector == null){
			if(new_meta_md == null){
				//then there were no answers, return null
				return null;
			}
			else{
				//then we need to return a vector with just new_meta
				retval = new Vector();
				retval.addElement(new_meta_md);
				return retval;
			}//end of if we need to just return the new meta md
		}//end of if there were no answers returned by either recursive call
		if (retval !=null && temp_vector != null){
			//then we need to combine the two
			int i;
			for (i = 0; i < temp_vector.size(); i++){
				retval.addElement(temp_vector.elementAt(i));
			}
		}//end of if we need to combine the two
		else{
			if (retval == null){
				//move over to retval so that it will all be symmetrical
				//we know that temp_vector is not null, or we would have exited earlier
				retval = temp_vector;
			}
		}
		//at this point we know that we have all of the stuff in retval unless there 
		//is something in new_meta_md
		if (new_meta_md !=null){
			retval.addElement(new_meta_md);
		}
		return retval;
	}//end recursiveCombineMDList
	
	


	protected boolean putInBucket(MDWithoutExistentialCheck a_md){
		int i;
		int which_bucket = a_md.firstSubgoalCovered();
		//now we need to look through the things in which_bucket until we find
		//one that has the same subgoals covered as a_md
		QuasiTreeBucketEntry quasi_entry;		
		Vector bucket_entry = _buckets[which_bucket];
		int num_bucket_entries = bucket_entry.size();
		for (i = 0; i < num_bucket_entries; i++){
			quasi_entry = (QuasiTreeBucketEntry) bucket_entry.elementAt(i);
			if (quasi_entry.addEntry(a_md)){
				//then we can stop
				return true;
			}
		}
		//if we get here, then we need to add a new bucket entry
		bucket_entry.addElement(new QuasiTreeBucketEntry(a_md));
		return true;
	}
	
	public static void main(String args[]) throws IOException{
		System.out.println("Entering main for BucketEndingAlgorithm");
		BucketEndingAlgorithm a = new BucketEndingAlgorithm();
		View v0 = new View();
		View v1 = new View();
		View v2 = new View();
		View v3 = new View();
		View v4 = new View();
		View v5 = new View();
		View v6 = new View();
		Query q = new Query();
		v0.read("V0(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-8005(0,1,2,3,4),21005(5,6,7,8,9),5005(10,11,12,13,14),12005(15,16,17,18,19),11005(20,21,22,23,24),10005(0,9,10,17,21)");
		v1.read("V1(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-17005(0,1,2,3,4),8005(5,6,7,8,9),14005(10,11,12,13,14),20005(15,16,17,18,19),9005(20,21,22,23,24),19005(0,9,10,17,21)");
		v2.read("V2(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-20005(0,1,2,3,4),22005(5,6,7,8,9),13005(10,11,12,13,14),14005(15,16,17,18,19),16005(20,21,22,23,24),9005(0,9,10,17,21)");
		v3.read("V3(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-22005(0,1,2,3,4),19005(5,6,7,8,9),7005(10,11,12,13,14),9005(15,16,17,18,19),16005(20,21,22,23,24),18005(0,9,10,17,21)");
		v4.read("V4(1,2,3,4,5,6,7,8,11,12,13,14,15,16,18,19,20,22,23,24):-16005(0,1,2,3,4),19005(5,6,7,8,9),17005(10,11,12,13,14),24005(15,16,17,18,19),22005(20,21,22,23,24),10005(0,9,10,17,21)");
		v5.read("V5(1,2,3,4,6,7,8,9,10,11,12,13,15,17,18,19,20,21,23,24):-14005(0,1,2,3,4),23005(5,6,7,8,9),22005(10,11,12,13,14),21005(15,16,17,18,19),19005(20,21,22,23,24),9005(0,5,14,16,22)");
		v6.read("V6(1,2,3,4,6,7,8,9,10,11,12,13,15,17,18,19,20,21,23,24):-22005(0,1,2,3,4),10005(5,6,7,8,9),14005(10,11,12,13,14),17005(15,16,17,18,19),6005(20,21,22,23,24),21005(0,5,14,16,22)");
		a.addView(v0);
		a.addView(v1);
		a.addView(v2);
		a.addView(v3);
		a.addView(v4);
		a.addView(v5);
		a.addView(v6);		
		v0.read("V0(0,1,6,7,8,2,9,10,11,3,12,13,14,4,15,16,17,5,18,19,20,21,22,23):-12005(0,1,6,7,8),16005(1,2,9,10,11),23005(2,3,12,13,14),21005(3,4,15,16,17),11005(4,5,18,19,20),15005(5,6,21,22,23)");
		v1.read("V1(0,1,6,7,8,2,9,10,11,3,12,13,14,4,15,16,17,5,18,19,20,21,22,23):-12005(0,1,6,7,8),20005(1,2,9,10,11),7005(2,3,12,13,14),17005(3,4,15,16,17),8005(4,5,18,19,20),13005(5,6,21,22,23)");
		q.read("q(0,6):-12005(0,1,6,7,8),7005(1,2,9,10,11),17005(2,3,12,13,14),8005(3,4,15,16,17),13005(4,5,18,19,20),15005(5,6,21,22,23)");

		
		q.read("q(0,15):-8005(0,1,2,3,4),18005(5,6,7,8,9),14005(10,11,12,13,14),17005(15,16,17,18,19),23005(20,21,22,23,24),16005(0,9,10,17,21)");
		a.setQuery(q);
		String answers = a.run();
		System.out.println(answers);
		System.out.println("done");
	}
}
