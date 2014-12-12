/*
 * Created on Feb 26, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.StringReader;
import java.util.Vector;

import minicon.BucketEndingAlgorithm;
import minicon.Predicate;
import minicon.Query;
import minicon.View;

/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestLAVRealEstate {

	protected static Vector readViewsFromFile(String p_filename)
	{
		Vector retval = new Vector();
		StringBuffer a_buffer = new StringBuffer();
		String line;
		try
		{
			FileReader file = new FileReader(p_filename);
			BufferedReader input = new BufferedReader(file);
			line = input.readLine();
			while (line != null)
			{
				a_buffer.append(line + "\n");
				line = input.readLine();
			}
			
			file.close();
			input.close();
			retval = readViewsFromString(a_buffer.toString());
		}
		catch(Exception e)
		{
			System.out.println("can't open filename " + p_filename);
		}
		
		return retval;
	}
	
	public static Vector readViewsFromString(String p_input)
	{
		BufferedReader input;
		String line = "";
		StringBuffer statement = new StringBuffer();
		Predicate pred;
		Vector retval = new Vector();
		//Schema input_schema = new Schema();
		//Mapping mapping = new Mapping();
		View view;
		
		try {
			StringReader string_reader = new StringReader(p_input);
			input = new BufferedReader(string_reader);
			boolean success;
			line = input.readLine().trim();//get rid of the ### line
			//now get the mapping
			while (line != null)
			{
				//System.out.println("started parsing line " + line + "\n");
				if (!line.startsWith("//") && !line.equals("")){
					statement.append(line);
					if (line.endsWith(".") || (!line.endsWith(",") && !line.endsWith(":-")))
					{
						//then we're done parsing it
						view = new View();
						if (line.endsWith(".")){
							statement.setLength(statement.length() - 1);	
						}
						success = view.read(statement.toString());
						//System.out.println("statement added = " + statement.substring(0,statement.length() - 1));
						//System.out.println("view added" + view.printString().toString());
						if (success){
							retval.addElement(view);
						}
						else
						{
							System.out.println("failed to parse in mapping.readFromString" + statement.toString() + "ignoring it");
						}
						statement = new StringBuffer();
	
					}
					
				}//end of if it wasn't a comment.
				line = input.readLine();
				if (line != null){
					line.trim();
				}
			}//end getting the mapping
			input.close();
		}//end try block
		catch (Exception e)
		{
			System.out.println("Problem parsing " + line);
		}
		return retval;
	}

	public static String rewriteQuery(Query p_query, Vector p_views)
	{
		String retval = null;
		BucketEndingAlgorithm minicon = new BucketEndingAlgorithm();
		int i, numviews;
		View a_view;
		numviews = p_views.size();
		for (i = 0; i < numviews; i++)
		{
			a_view = (View)p_views.elementAt(i);
			minicon.addView(a_view);
		}
		minicon.setQuery(p_query);
		retval = minicon.run();
		System.out.println(retval);
		System.out.println(minicon.getTotalTime());
		return retval;

	}
	
	public static void main(String[] args) {
		int i, numviews;
		Query q;
		Vector views = readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\fake-lav.txt");
		numviews = views.size();
		View a_view;
		/*for (i =0; i < numviews; i++)
		{
			a_view = (View)views.elementAt(i);
			System.out.println(a_view.printString().toString());
		}
		*/
/*		q = new Query("q(house_id):-all-house(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,mobile,rlistingfeature_id,rlistingadditional,patio,pool,spa,view,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,new_home,resort_property,restrictions,septic_tank,stories,to_be_built,water_coop,well,highlights_id)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(price):-all-size(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,view,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_water,electricity,golf_course,handicapped_equipped,spa,master_bedroom,new_home,pool,resort_property,restrictions,septic_tank,stories,to_be_built,water_coop,well,highlights_id)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(price):-all-super(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,desc_id,dimension,level,rlistingbasic_id,neighborhood,status,style,sqft,year,home_features,community_features,date_posted,category,classification,agency_brokerage,school,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(house_id):-all-lot(house_id,exterior,fireplace,levels,interior,lot_description,lot_size,roof,site,taxes,view,waterfront,garage,cooling,heat,address,patio,pool,rooms,spa,firm_id,agent,type,mls,country,price,beds,full_baths,half_baths,dining_rooms,living_rooms,house_desc,financing_options_id,acres,sqft,carport_spaces,city_sewer,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,available,new_home,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,well,year,highlights_id)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(house_id):-all-neighborhood(house_id,address,house_desc,home_features,community_features,date_posted,price,beds,baths,category,classification,agency_brokerage,sqft,lot_size,year,garage,neighborhood,school,status,mls,style,levels,available,agent,email,firm_location,firm_fax,firm_phone,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,firm_id,type,country,full_baths,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_sewer,city_water,electricity,fireplace,golf_course,handicapped_equipped,spa,master_bedroom,new_home,pool,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,waterfront,well,highlights_id)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(elementary):-all-schools(school_info_id,elementary,middle_school,high_school)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(taxes):-all-financial(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,amentities_id,exterior,interior,lot_description,roof,site,view,waterfront)");
		System.out.println(rewriteQuery(q,views));
		q = new Query("q(house_id):-all-contact(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm_name,firm_phone,agent_name,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,agent,agent_email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,firm_id,firm_voice_mail,toll_free,firm_email,agent_id,agent_fax,mobile,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,contact_id,pager,rlistingfeature_id,rlistingadditional)");
		System.out.println(rewriteQuery(q,views));
*/
		q = new Query("q(house_id):-all-house(house_id,address1,price1,rooms1,beds1,baths1,basement1,levels1,garage1,sub_division1,school_district1,mls1,type1,firm1,firm_phone1,agent1,agent_phone1,house_desc1,construction1,heat1,cooling1,fireplace1,gas1,sewer1,water1,lot_size1,hoa_fee1,taxes1,condo_level1,entry_desc1,living_desc1,dining_desc1,kitchen_desc1,breakfast_desc1,family_desc1,study_desc1,recreation_desc1,laundry_desc1,master_desc1,bedroom2_desc1,bedroom3_desc1,bedroom4_desc1,bedroom5_desc1,bath1_desc1,bath2_desc1,bath3_desc1,bath4_desc1,directions1,home_features1,community_features1,date_posted1,category1,classification1,agency_brokerage1,sqft1,year1,neighborhood1,school1,status1,style1,available1,email1,firm_location1,firm_fax1,phone_day1,phone_evening1,phone_leave_message1,other_ads1,rlistingbasic_id1,mobile1,rlistingfeature_id1,rlistingadditional1,patio1,pool1,spa1,view1,city1,state1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,interior1,lot_description1,roof1,site1,waterfront1,firm_id1,country1,half_baths1,dining_rooms1,living_rooms1,financing_options_id1,acres1,carport_spaces1,city_water1,electricity1,golf_course1,handicapped_equipped1,master_bedroom1,new_home1,resort_property1,restrictions1,septic_tank1,stories1,to_be_built1,water_coop1,well1,highlights_id1)," +
				"all-size(house_id,address2,price2,rooms2,beds2,baths2,basement2,levels2,garage2,sub_division2,school_district2,mls2,type2,firm2,firm_phone2,agent2,agent_phone2,house_desc2,construction2,heat2,cooling2,fireplace2,gas2,sewer2,water2,lot_size2,hoa_fee2,taxes2,condo_level2,entry_desc2,living_desc2,dining_desc2,kitchen_desc2,breakfast_desc2,family_desc2,study_desc2,recreation_desc2,laundry_desc2,master_desc2,bedroom2_desc2,bedroom3_desc2,bedroom4_desc2,bedroom5_desc2,bath1_desc2,bath2_desc2,bath3_desc2,bath4_desc2,directions2,home_features2,community_features2,date_posted2,category2,classification2,agency_brokerage2,sqft2,year2,neighborhood2,school2,status2,style2,available2,email2,firm_location2,firm_fax2,phone_day2,phone_evening2,phone_leave_message2,other_ads2,rlistingbasic_id2,city2,state2,amentities_id2,features_id2,schools_info_id2,comments2,more_info_contact_id2,exterior2,interior2,lot_description2,roof2,site2,view2,waterfront2,firm_id2,country2,half_baths2,dining_rooms2,living_rooms2,financing_options_id2,acres2,carport_spaces2,city_water2,electricity2,golf_course2,handicapped_equipped2,spa2,master_bedroom2,new_home2,pool2,resort_property2,restrictions2,septic_tank2,stories2,to_be_built2,water_coop2,well2,highlights_id2)," +
				"all-super(house_id,address3,price3,rooms3,beds3,baths3,basement3,levels3,garage3,sub_division3,school_district3,mls3,type3,firm3,firm_phone3,agent3,agent_phone3,house_desc3,construction3,heat3,cooling3,fireplace3,gas3,sewer3,water3,lot_size3,hoa_fee3,taxes3,condo_level3,entry_desc3,living_desc3,dining_desc3,kitchen_desc3,breakfast_desc3,family_desc3,study_desc3,recreation_desc3,laundry_desc3,master_desc3,bedroom2_desc3,bedroom3_desc3,bedroom4_desc3,bedroom5_desc3,bath1_desc3,bath2_desc3,bath3_desc3,bath4_desc3,directions3,desc_id3,dimension3,level3,rlistingbasic_id3,neighborhood3,status3,style3,sqft3,year3,home_features3,community_features3,date_posted3,category3,classification3,agency_brokerage3,school3,available3,email3,firm_location3,firm_fax3,phone_day3,phone_evening3,phone_leave_message3,other_ads3)," +
				"all-lot(house_id,exterior4,fireplace4,levels4,interior4,lot_description4,lot_size4,roof4,site4,taxes4,view4,waterfront4,garage4,cooling4,heat4,address4,patio4,pool4,rooms4,spa4,firm_id4,agent4,type4,mls4,country4,price4,beds4,full_baths4,half_baths4,dining_rooms4,living_rooms4,house_desc4,financing_options_id4,acres4,sqft4,carport_spaces4,city_sewer4,city_water4,electricity4,golf_course4,handicapped_equipped4,master_bedroom4,available4,new_home4,resort_property4,restrictions4,school_district4,septic_tank4,stories4,sub_division4,to_be_built4,water_coop4,well4,year4,highlights_id4)," +
				"all-neighborhood(house_id,address5,house_desc5,home_features5,community_features5,date_posted5,price5,beds5,baths5,category5,classification5,agency_brokerage5,sqft5,lot_size5,year5,garage5,neighborhood5,school5,status5,mls5,style5,levels5,available5,agent5,email5,firm_location5,firm_fax5,firm_phone5,phone_day5,phone_evening5,phone_leave_message5,other_ads5,rlistingbasic_id5,firm_id5,type5,country5,full_baths5,half_baths5,dining_rooms5,living_rooms5,financing_options_id5,acres5,carport_spaces5,city_sewer5,city_water5,electricity5,fireplace5,golf_course5,handicapped_equipped5,spa5,master_bedroom5,new_home5,pool5,resort_property5,restrictions5,school_district5,septic_tank5,stories5,sub_division5,to_be_built5,water_coop5,waterfront5,well5,highlights_id5)," +
				"all-financial(house_id,address7,price7,rooms7,beds7,baths7,basement7,levels7,garage7,sub_division7,school_district7,mls7,type7,firm7,firm_phone7,agent7,agent_phone7,house_desc7,construction7,heat7,cooling7,fireplace7,gas7,sewer7,water7,lot_size7,hoa_fee7,taxes7,condo_level7,entry_desc7,living_desc7,dining_desc7,kitchen_desc7,breakfast_desc7,family_desc7,study_desc7,recreation_desc7,laundry_desc7,master_desc7,bedroom2_desc7,bedroom3_desc7,bedroom4_desc7,bedroom5_desc7,bath1_desc7,bath2_desc7,bath3_desc7,bath4_desc7,directions7,amentities_id7,exterior7,interior7,lot_description7,roof7,site7,view7,waterfront7)," +
				"all-contact(house_id,address8,price8,rooms8,beds8,baths8,basement8,levels8,garage8,sub_division8,school_district8,mls8,type8,firm_name8,firm_phone8,agent_name8,agent_phone8,house_desc8,construction8,heat8,cooling8,fireplace8,gas8,sewer8,water8,lot_size8,hoa_fee8,taxes8,condo_level8,entry_desc8,living_desc8,dining_desc8,kitchen_desc8,breakfast_desc8,family_desc8,study_desc8,recreation_desc8,laundry_desc8,master_desc8,bedroom2_desc8,bedroom3_desc8,bedroom4_desc8,bedroom5_desc8,bath1_desc8,bath2_desc8,bath3_desc8,bath4_desc8,directions8,home_features8,community_features8,date_posted8,category8,classification8,agency_brokerage8,sqft8,year8,neighborhood8,school8,status8,style8,available8,agent8,agent_email8,firm_location8,firm_fax8,phone_day8,phone_evening8,phone_leave_message8,other_ads8,rlistingbasic_id8,firm_id8,firm_voice_mail8,toll_free8,firm_email8,agent_id8,agent_fax8,mobile8,city8,state8,amentities_id8,features_id8,schools_info_id8,comments8,more_info_contact_id8,contact_id8,pager8,rlistingfeature_id8,rlistingadditional8)");

		rewriteQuery(q,views);

/*		Query big_query = new Query("(course_code):-"+
		"course-listing(dk1,course_code,dk2,dk3,dk4,dk5,dk83,dk6,dk7,dk8,dk9,dk10,dk11,dk12,dk13,dk14,dk15,dk16),"+
		"course_code(dk17,course_code,dk18,dk19,dk20,dk21,dk22,dk23,dk24,dk25,dk26,dk27,dk28,dk29,dk30,dk31,dk32),"+
		"section(dk33,course_code,dk34,dk35,dk36,dk37,dk38,dk39,dk40,dk41,dk42,dk43,dk44,dk45,dk46,dk47,dk48,dk49,dk50,dk51,dk52),"+
		"lecture(course_code,dk53,dk54,dk55,dk56,section_id,dk58,dk59,dk60,dk61,dk62,dk63,dk64,dk65,dk66,dk67,dk68,dk69,dk70,dk71),"+
		"times(time_id,start_time,end_time),"+
		"places(place_id,building,room)");
		rewriteQuery(big_query,views);
*/
	}
}
