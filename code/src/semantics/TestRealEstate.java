/*
 * Created on Feb 25, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;

import java.util.Vector;

import minicon.Mapping;
import minicon.Statement;
import minicon.Query;
import minicon.RapTimer;
import minicon.Predicate;
import minicon.TimingResults;


/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestRealEstate {

	protected Schema m_hw;
	protected String m_hw_prefix;
	protected Schema m_hwt;
	protected String m_hwt_prefix;
	protected Schema m_hwty;
	protected String m_hwty_prefix;
	protected Schema m_all;
	protected String m_all_prefix;
	protected Vector m_hw_views;
	protected Vector m_hwt_views;
	protected Vector m_hwty_views;
	protected Vector m_all_views;
	protected Predicate [] m_hw_rels;
	protected Predicate [] m_hwt_rels;
	protected Predicate [] m_hwty_rels;
	protected Predicate [] m_all_rels;
	protected TimingResults [] m_hw_results;
	protected TimingResults [] m_hwt_results;
	protected TimingResults [] m_hwty_results;
	protected TimingResults [] m_all_results;
	protected TimingResults [] m_hw_lav_results;
	protected TimingResults [] m_hwt_lav_results;
	protected TimingResults [] m_hwty_lav_results;
	protected TimingResults [] m_all_lav_results;

//	protected Enum m_rel_names;
	public TestRealEstate()
	{
		m_hw = null;
		m_hwt = null;
		m_hwty = null;
		m_all = null;
		m_hw_prefix = "hw";
		m_hwt_prefix = "hwt";
		m_hwty_prefix = "hwty";
		m_all_prefix = "all";		
		m_hw_rels = new Predicate[10];
		m_hwt_rels = new Predicate[10];
		m_hwty_rels = new Predicate[10];
		m_all_rels = new Predicate[10];
		m_hw_views = null;
		m_hwt_views = null;
		m_hwty_views = null;
		m_all_views = null;
	
		m_hw_results = new TimingResults[10];
		m_hwt_results = new TimingResults[10];
		m_hwty_results = new TimingResults[10];
		m_all_results = new TimingResults[10];
		m_hw_lav_results = new TimingResults[10];
		m_hwt_lav_results = new TimingResults[10];
		m_hwty_lav_results = new TimingResults[10];
		m_all_lav_results = new TimingResults[10];
		int i;
		for (i = 0; i < 10; i++)
		{
			m_hw_results[i] = new TimingResults();
			m_hwt_results[i] = new TimingResults();
			m_hwty_results[i] = new TimingResults();
			m_all_results[i] = new TimingResults();
			m_hw_lav_results[i] = new TimingResults();
			m_hwt_lav_results[i] = new TimingResults();
			m_hwty_lav_results[i] = new TimingResults();
			m_all_lav_results[i] = new TimingResults();
		}
		

	}
	
	public void CreateLAVViews()
	{
		m_hw_views = TestLAVRealEstate.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\fake-lav-hw.txt");
		m_hwt_views = TestLAVRealEstate.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\fake-lav-hwt.txt");
		m_hwty_views = TestLAVRealEstate.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\fake-lav-hwty.txt");
		m_all_views = TestLAVRealEstate.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\fake-lav.txt");
		//m_all_views = TestLAVRealEstate.readViewsFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\testy.txt");
		
	}
	
	
	public void CreateRels()
	{
		Predicate a_pred;
		a_pred = new Predicate();
		a_pred.read("hw-house(house_id1,house_desc1,agent1,agent_phone1,mobile1,email1,firm1,firm_phone1,firm_fax1,rlistingbasic_id1,rlistingfeature_id1,rlistingadditional1,price1,address1,neighborhood1,mls,status1,baths1,beds1,levels1,style1,garage1,lot_size1,type1,sqft1,year1,cooling1,heat1,fireplace1,patio1,pool1,rooms1,spa1,view1,city1,state1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,interior1,lot_description1,roof1,site1,taxes1,waterfront1)");
		m_hw_rels[0] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_size_information(rlistingbasic_id2,price2,address2,neighborhood2,mls,status2,baths2,beds2,levels2,style2,garage2,lot_size2,type2,sqft2,year2,house_id2,city2,state2,heat2,cooling2,amentities_id2,features_id2,schools_info_id2,comments2,more_info_contact_id2,exterior2,fireplace2,interior2,lot_description2,roof2,site2,taxes2,view2,waterfront2)");
		m_hw_rels[1] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_superstructure(rlistingbasic_id3,price3,address3,neighborhood3,mls,status3,baths3,beds3,levels3,style3,garage3,lot_size3,type3,sqft3,year3)");
		m_hw_rels[2] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_lot_features(house_id4,exterior4,fireplace4,levels4,interior4,lot_description4,lot_size4,roof4,site4,taxes4,view4,waterfront4,garage4,cooling4,heat4,address4,patio4,pool4,rooms4,spa4)");
		m_hw_rels[3] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_neighborhood(rlistingbasic_id5,price5,address5,neighborhood_name5,mls,status5,baths5,beds5,levels5,style5,garage5,lot_size5,type5,sqft5,year5)");
		m_hw_rels[4] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_financial(house_id7,exterior7,fireplace7,levels7,interior7,lot_description7,lot_size7,roof7,site7,taxes7,view7,waterfront7,garage7)");
		m_hw_rels[5] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hw_contact(house_id6,price6,mls6,address6,city6,state6,baths6,beds6,sqft6,year6,heat6,cooling6,amentities_id6,features_id6,schools_info_id6,comments6,more_info_contact_id6,contact_id6,agent,firm_name6,firm_location6,firm_phone6,mobile6,fax6,pager6,email6,house_desc6,agent_phone6,firm_fax6,rlistingbasic_id6,rlistingfeature_id6,rlistingadditional6,neighborhood6,status6,levels6,style6,garage6,lot_size6,type6)");
		m_hw_rels[6] = a_pred;
		//done with hw  
		//now for hwt
		a_pred = new Predicate();
		a_pred.read("hwt-house(house_id,house_desc0,agent0,agent_phone0,mobile0,email0,firm0,firm_phone0,firm_fax0,rlistingbasic_id0,rlistingfeature_id0,rlistingadditional0,price0,address0,neighborhood0,mls,status0,baths0,beds0,levels0,style0,garage0,lot_size0,type0,sqft0,year0,cooling0,heat0,fireplace0,patio0,pool0,rooms0,spa0,view0,city0,state0,amentities_id0,features_id0,schools_info_id0,comments0,more_info_contact_id0,exterior0,interior0,lot_description0,roof0,site0,taxes0,waterfront0,firm_id0,country0,half_baths0,dining_rooms0,living_rooms0,financing_options_id0,acres0,carport_spaces0,city_sewer0,city_water0,electricity0,golf_course0,handicapped_equipped0,master_bedroom0,available0,new_home0,resort_property0,restrictions0,school_district0,septic_tank0,stories0,sub_division0,to_be_built0,water_coop0,well0,highlights_id0)");
		m_hwt_rels[0] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt-size(rlistingbasic_id1,price1,address1,neighborhood1,mls1,status1,baths1,beds1,levels1,style1,garage1,lot_size1,type1,sqft1,year1,house_id,city1,state1,heat1,cooling1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,fireplace1,interior1,lot_description1,roof1,site1,taxes1,view1,waterfront1,firm_id1,agent1,country1,half_baths1,dining_rooms1,living_rooms1,house_desc1,financing_options_id1,acres1,carport_spaces1,city_sewer1,city_water1,electricity1,golf_course1,handicapped_equipped1,spa1,master_bedroom1,available1,new_home1,pool1,resort_property1,restrictions1,school_district1,septic_tank1,stories1,sub_division1,to_be_built1,water_coop1,well1,highlights_id1)");
		m_hwt_rels[1] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt_superstructure(rlistingbasic_id2,price2,address2,neighborhood2,mls,status2,baths2,beds2,levels2,style2,garage2,lot_size2,type2,sqft2,year2)");
		m_hwt_rels[2] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt_lot(house_id,exterior3,fireplace3,levels3,interior3,lot_description3,lot_size3,roof3,site3,taxes3,view3,waterfront3,garage3,cooling3,heat3,address3,patio3,pool3,rooms3,spa3,firm_id3,agent3,type3,mls3,country3,price3,beds3,full_baths3,half_baths3,dining_rooms3,living_rooms3,house_desc3,financing_options_id3,acres3,sqft3,carport_spaces3,city_sewer3,city_water3,electricity3,golf_course3,handicapped_equipped3,master_bedroom3,available3,new_home3,resort_property3,restrictions3,school_district3,septic_tank3,stories3,sub_division3,to_be_built3,water_coop3,well3,year3,highlights_id3)");
		m_hwt_rels[3] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt_neighborhood(house_id4,firm_id4,agent4,type4,mls,address4,country4,price4,beds4,full_baths4,half_baths4,dining_rooms4,living_rooms4,garage4,house_desc4,financing_options_id4,acres4,lot_size4,sqft4,carport_spaces4,city_sewer4,city_water4,electricity4,fireplace4,golf_course4,handicapped_equipped4,spa4,master_bedroom4,available4,new_home4,pool4,resort_property4,restrictions4,school_district4,septic_tank4,stories4,sub_division4,to_be_built4,water_coop4,waterfront4,well4,year4,highlights_id4,rlistingbasic_id4,neighborhood_name4,status4,baths4,levels4,style4)");
		m_hwt_rels[4] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt_contact(firm_id5,firm_name5,firm_location5,firm_phone5,firm_voice_mail5,toll_free5,firm_fax5,firm_email5,agent_id5,agent_name5,agent_phone5,agent_fax5,mobile5,agent_email5,house_id,price5,mls5,address5,city5,state5,baths5,beds5,sqft5,year5,heat5,cooling5,amentities_id5,features_id5,schools_info_id5,comments5,more_info_contact_id5,contact_id5,agent5,pager5,house_desc5,rlistingbasic_id5,rlistingfeature_id5,rlistingadditional5,neighborhood5,status5,levels5,style5,garage5,lot_size5,type5)");
		m_hwt_rels[5] = a_pred;
		a_pred = new Predicate();
		a_pred.read("hwt_financial(house_id,exterior6,fireplace6,levels6,interior6,lot_description6,lot_size6,roof6,site6,taxes6,view6,waterfront6,garage6)");
		m_hwt_rels[6] = a_pred;
		//done with hwt
		//now for hwty
		a_pred = new Predicate();
		a_pred.read("hwty-house(house_id,address0,house_desc0,home_features0,community_features0,date_posted0,price0,beds0,baths0,category0,classification0,agency_brokerage0,sqft0,lot_size0,year0,garage0,neighborhood0,school0,status0,mls,style0,levels0,available0,agent0,email0,firm_location0,firm_fax0,firm_phone0,phone_day0,phone_evening0,phone_leave_message0,other_ads0,rlistingbasic_id0,agent_phone0,mobile0,firm0,rlistingfeature_id0,rlistingadditional0,type0,cooling0,heat0,fireplace0,patio0,pool0,rooms0,spa0,view0,city0,state0,amentities_id0,features_id0,schools_info_id0,comments0,more_info_contact_id0,exterior0,interior0,lot_description0,roof0,site0,taxes0,waterfront0,firm_id0,country0,half_baths0,dining_rooms0,living_rooms0,financing_options_id0,acres0,carport_spaces0,city_sewer0,city_water0,electricity0,golf_course0,handicapped_equipped0,master_bedroom0,new_home0,resort_property0,restrictions0,school_district0,septic_tank0,stories0,sub_division0,to_be_built0,water_coop0,well0,highlights_id0)");
		m_hwty_rels[0]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty-size(house_id,address1,house_desc1,home_features1,community_features1,date_posted1,price1,beds1,baths1,category1,classification1,agency_brokerage1,sqft1,lot_size1,year1,garage1,neighborhood1,school1,status1,mls1,style1,levels1,available1,agent1,email1,firm_location1,firm_fax1,firm_phone1,phone_day1,phone_evening1,phone_leave_message1,other_ads1,rlistingbasic_id1,type1,city1,state1,heat1,cooling1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,fireplace1,interior1,lot_description1,roof1,site1,taxes1,view1,waterfront1,firm_id1,country1,half_baths1,dining_rooms1,living_rooms1,financing_options_id1,acres1,carport_spaces1,city_sewer1,city_water1,electricity1,golf_course1,handicapped_equipped1,spa1,master_bedroom1,new_home1,pool1,resort_property1,restrictions1,school_district1,septic_tank1,stories1,sub_division1,to_be_built1,water_coop1,well1,highlights_id1)");
		m_hwty_rels[1]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty-superstructure(rlistingbasic_id2,price2,address2,neighborhood2,mls,status2,baths2,beds2,levels2,style2,garage2,lot_size2,type2,sqft2,year2,house_id2,house_desc2,home_features2,community_features2,date_posted2,category2,classification2,agency_brokerage2,school2,available2,agent2,email2,firm_location2,firm_fax2,firm_phone2,phone_day2,phone_evening2,phone_leave_message2,other_ads2)");
		m_hwty_rels[2]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty_lot(house_id,exterior3,fireplace3,levels3,interior3,lot_description3,lot_size3,roof3,site3,taxes3,view3,waterfront3,garage3,cooling3,heat3,address3,patio3,pool3,rooms3,spa3,firm_id3,agent3,type3,mls3,country3,price3,beds3,full_baths3,half_baths3,dining_rooms3,living_rooms3,house_desc3,financing_options_id3,acres3,sqft3,carport_spaces3,city_sewer3,city_water3,electricity3,golf_course3,handicapped_equipped3,master_bedroom3,available3,new_home3,resort_property3,restrictions3,school_district3,septic_tank3,stories3,sub_division3,to_be_built3,water_coop3,well3,year3,highlights_id3)");
		m_hwty_rels[3]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty-neighborhood(house_id4,address4,house_desc4,home_features4,community_features4,date_posted4,price4,beds4,baths4,category4,classification4,agency_brokerage4,sqft4,lot_size4,year4,garage4,neighborhood4,school4,status4,mls,style4,levels4,available4,agent4,email4,firm_location4,firm_fax4,firm_phone4,phone_day4,phone_evening4,phone_leave_message4,other_ads4,rlistingbasic_id4,firm_id4,type4,country4,full_baths4,half_baths4,dining_rooms4,living_rooms4,financing_options_id4,acres4,carport_spaces4,city_sewer4,city_water4,electricity4,fireplace4,golf_course4,handicapped_equipped4,spa4,master_bedroom4,new_home4,pool4,resort_property4,restrictions4,school_district4,septic_tank4,stories4,sub_division4,to_be_built4,water_coop4,waterfront4,well4,highlights_id4)");
		m_hwty_rels[4]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty-contact(house_id5,address5,house_desc5,home_features5,community_features5,date_posted5,price5,beds5,baths5,category5,classification5,agency_brokerage5,sqft5,lot_size5,year5,garage5,neighborhood5,school5,status5,mls,style5,levels5,available5,agent5,agent_email5,firm_location5,firm_fax5,firm_phone5,phone_day5,phone_evening5,phone_leave_message5,other_ads5,rlistingbasic_id5,firm_id5,firm_name5,firm_voice_mail5,toll_free5,firm_email5,agent_id5,agent_name5,agent_phone5,agent_fax5,mobile5,city5,state5,heat5,cooling5,amentities_id5,features_id5,schools_info_id5,comments5,more_info_contact_id5,contact_id5,pager5,rlistingfeature_id5,rlistingadditional5,type5)");
		m_hwty_rels[5]= a_pred;
		a_pred = new Predicate();
		a_pred.read("hwty_financial(house_id6,exterior6,fireplace6,levels6,interior6,lot_description6,lot_size6,roof6,site6,taxes6,view6,waterfront6,garage6)");
		m_hwty_rels[6]= a_pred;		
		//end hwty
		//begin all
		a_pred = new Predicate();
		a_pred.read("all-house(house_id,address0,price0,rooms0,beds0,baths0,basement0,levels0,garage0,sub_division0,school_district0,mls,type0,firm0,firm_phone0,agent0,agent_phone0,house_desc0,construction0,heat0,cooling0,fireplace0,gas0,sewer0,water0,lot_size0,hoa_fee0,taxes0,condo_level0,entry_desc0,living_desc0,dining_desc0,kitchen_desc0,breakfast_desc0,family_desc0,study_desc0,recreation_desc0,laundry_desc0,master_desc0,bedroom2_desc0,bedroom3_desc0,bedroom4_desc0,bedroom5_desc0,bath1_desc0,bath2_desc0,bath3_desc0,bath4_desc0,directions0,home_features0,community_features0,date_posted0,category0,classification0,agency_brokerage0,sqft0,year0,neighborhood0,school0,status0,style0,available0,email0,firm_location0,firm_fax0,phone_day0,phone_evening0,phone_leave_message0,other_ads0,rlistingbasic_id0,mobile0,rlistingfeature_id0,rlistingadditional0,patio0,pool0,spa0,view0,city0,state0,amentities_id0,features_id0,schools_info_id0,comments0,more_info_contact_id0,exterior0,interior0,lot_description0,roof0,site0,waterfront0,firm_id0,country0,half_baths0,dining_rooms0,living_rooms0,financing_options_id0,acres0,carport_spaces0,city_water0,electricity0,golf_course0,handicapped_equipped0,master_bedroom0,new_home0,resort_property0,restrictions0,septic_tank0,stories0,to_be_built0,water_coop0,well0,highlights_id0)");
		m_all_rels[0]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-size(house_id,address1,price1,rooms1,beds1,baths1,basement1,levels1,garage1,sub_division1,school_district1,mls1,type1,firm1,firm_phone1,agent1,agent_phone1,house_desc1,construction1,heat1,cooling1,fireplace1,gas1,sewer1,water1,lot_size1,hoa_fee1,taxes1,condo_level1,entry_desc1,living_desc1,dining_desc1,kitchen_desc1,breakfast_desc1,family_desc1,study_desc1,recreation_desc1,laundry_desc1,master_desc1,bedroom2_desc1,bedroom3_desc1,bedroom4_desc1,bedroom5_desc1,bath1_desc1,bath2_desc1,bath3_desc1,bath4_desc1,directions1,home_features1,community_features1,date_posted1,category1,classification1,agency_brokerage1,sqft1,year1,neighborhood1,school1,status1,style1,available1,email1,firm_location1,firm_fax1,phone_day1,phone_evening1,phone_leave_message1,other_ads1,rlistingbasic_id1,city1,state1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,interior1,lot_description1,roof1,site1,view1,waterfront1,firm_id1,country1,half_baths1,dining_rooms1,living_rooms1,financing_options_id1,acres1,carport_spaces1,city_water1,electricity1,golf_course1,handicapped_equipped1,spa1,master_bedroom1,new_home1,pool1,resort_property1,restrictions1,septic_tank1,stories1,to_be_built1,water_coop1,well1,highlights_id1)");
		m_all_rels[1]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-super(house_id2,address2,price2,rooms2,beds2,baths2,basement2,levels2,garage2,sub_division2,school_district2,mls,type2,firm2,firm_phone2,agent2,agent_phone2,house_desc2,construction2,heat2,cooling2,fireplace2,gas2,sewer2,water2,lot_size2,hoa_fee2,taxes2,condo_level2,entry_desc2,living_desc2,dining_desc2,kitchen_desc2,breakfast_desc2,family_desc2,study_desc2,recreation_desc2,laundry_desc2,master_desc2,bedroom2_desc2,bedroom3_desc2,bedroom4_desc2,bedroom5_desc2,bath1_desc2,bath2_desc2,bath3_desc2,bath4_desc2,directions2,desc_id2,dimension2,level2,rlistingbasic_id2,neighborhood2,status2,style2,sqft2,year2,home_features2,community_features2,date_posted2,category2,classification2,agency_brokerage2,school2,available2,email2,firm_location2,firm_fax2,phone_day2,phone_evening2,phone_leave_message2,other_ads2)");
		m_all_rels[2]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-lot(house_id,exterior3,fireplace3,levels3,interior3,lot_description3,lot_size3,roof3,site3,taxes3,view3,waterfront3,garage3,cooling3,heat3,address3,patio3,pool3,rooms3,spa3,firm_id3,agent3,type3,mls3,country3,price3,beds3,full_baths3,half_baths3,dining_rooms3,living_rooms3,house_desc3,financing_options_id3,acres3,sqft3,carport_spaces3,city_sewer3,city_water3,electricity3,golf_course3,handicapped_equipped3,master_bedroom3,available3,new_home3,resort_property3,restrictions3,school_district3,septic_tank3,stories3,sub_division3,to_be_built3,water_coop3,well3,year3,highlights_id3)");
		m_all_rels[3]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-neighborhood(house_id4,address4,house_desc4,home_features4,community_features4,date_posted4,price4,beds4,baths4,category4,classification4,agency_brokerage4,sqft4,lot_size4,year4,garage4,neighborhood4,school4,status4,mls,style4,levels4,available4,agent4,email4,firm_location4,firm_fax4,firm_phone4,phone_day4,phone_evening4,phone_leave_message4,other_ads4,rlistingbasic_id4,firm_id4,type4,country4,full_baths4,half_baths4,dining_rooms4,living_rooms4,financing_options_id4,acres4,carport_spaces4,city_sewer4,city_water4,electricity4,fireplace4,golf_course4,handicapped_equipped4,spa4,master_bedroom4,new_home4,pool4,resort_property4,restrictions4,school_district4,septic_tank4,stories4,sub_division4,to_be_built4,water_coop4,waterfront4,well4,highlights_id4)");
		m_all_rels[4]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-contact(house_id,address5,price5,rooms5,beds5,baths5,basement5,levels5,garage5,sub_division5,school_district5,mls5,type5,firm_name5,firm_phone5,agent_name5,agent_phone5,house_desc5,construction5,heat5,cooling5,fireplace5,gas5,sewer5,water5,lot_size5,hoa_fee5,taxes5,condo_level5,entry_desc5,living_desc5,dining_desc5,kitchen_desc5,breakfast_desc5,family_desc5,study_desc5,recreation_desc5,laundry_desc5,master_desc5,bedroom2_desc5,bedroom3_desc5,bedroom4_desc5,bedroom5_desc5,bath1_desc5,bath2_desc5,bath3_desc5,bath4_desc5,directions5,home_features5,community_features5,date_posted5,category5,classification5,agency_brokerage5,sqft5,year5,neighborhood5,school5,status5,style5,available5,agent5,agent_email5,firm_location5,firm_fax5,phone_day5,phone_evening5,phone_leave_message5,other_ads5,rlistingbasic_id5,firm_id5,firm_voice_mail5,toll_free5,firm_email5,agent_id5,agent_fax5,mobile5,city5,state5,amentities_id5,features_id5,schools_info_id5,comments5,more_info_contact_id5,contact_id5,pager5,rlistingfeature_id5,rlistingadditional5)");
		m_all_rels[5]= a_pred;		
		a_pred = new Predicate();
		a_pred.read("all-financial(house_id,address6,price6,rooms6,beds6,baths6,basement6,levels6,garage6,sub_division6,school_district6,mls,type6,firm6,firm_phone6,agent6,agent_phone6,house_desc6,construction6,heat6,cooling6,fireplace6,gas6,sewer6,water6,lot_size6,hoa_fee6,taxes6,condo_level6,entry_desc6,living_desc6,dining_desc6,kitchen_desc6,breakfast_desc6,family_desc6,study_desc6,recreation_desc6,laundry_desc6,master_desc6,bedroom2_desc6,bedroom3_desc6,bedroom4_desc6,bedroom5_desc6,bath1_desc6,bath2_desc6,bath3_desc6,bath4_desc6,directions6,amentities_id6,exterior6,interior6,lot_description6,roof6,site6,view6,waterfront6)");
		m_all_rels[6]= a_pred;		
		
	}
	
	public Schema CreateMediatedSchema()
	{
		Schema homeweekers = new Schema();
		Schema nky = new Schema();
		Schema texas = new Schema();
		Schema windermere = new Schema();
		Schema yahoo = new Schema();
		Mapping hw_mapping = new Mapping();
		SemanticMerge hw;
		int i;
		homeweekers.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\homeweekers.txt");
		nky.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\nky.txt");
		texas.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\texas.txt");
		windermere.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\windermere.txt");
		yahoo.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\yahoo.txt");
		hw_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\homeweekers-windermere.txt");
		hw = new SemanticMerge();
		hw.setSchema1(homeweekers);
		hw.setSchema2(windermere);
		hw.setMapping(hw_mapping);
		hw.merge();
		m_hw = hw.getMergedSchema();
		Mapping hwt_mapping = new Mapping();
		hwt_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\hwt.txt");
		SemanticMerge hwt = new SemanticMerge();
		hwt.setSchema1(hw.getMergedSchema());
		hwt.setSchema2(texas);
		hwt.setMapping(hwt_mapping);
		hwt.merge();
		m_hwt = hwt.getMergedSchema();
		Mapping hwty_mapping = new Mapping();
		hwty_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\hwty.txt");
		SemanticMerge hwty = new SemanticMerge();
		hwty.setSchema1(hwt.getMergedSchema());
		hwty.setSchema2(yahoo);
		hwty.setMapping(hwty_mapping);
		hwty.merge();
		m_hwty = hwty.getMergedSchema();
		Mapping all_mapping = new Mapping();
		all_mapping.readFromFile("C:\\Documents and Settings\\rap\\Desktop\\semantics\\realestate1\\all.txt");
		SemanticMerge all_merge = new SemanticMerge();
		all_merge.setSchema1(hwty.getMergedSchema());
		all_merge.setSchema2(nky);
		all_merge.setMapping(all_mapping);
		all_merge.merge();		
		m_all = all_merge.getMergedSchema();
		return all_merge.getMergedSchema();
		//return hwty.getMergedSchema();
	}
	
	public static long askQuery(Query p_query, Schema p_schema)
	{
		RapTimer timer = new RapTimer();
		Vector retval;
		GLAVMapping a_map = p_schema.getMapping();
		timer.start();
		retval = RewriteGLAVQuery.findRewriting(p_query,p_schema);
		timer.stop();
		long time = timer.getAccumulatedTime();
/*		if (retval != null){
			int i, num_rewritings = retval.size();
			for (i = 0; i < num_rewritings; i++)
			{
				System.out.println(((Statement)retval.elementAt(i)).printString().toString());
			}
		}//end if the answer wasn't null
		else
		{
			System.out.println("no rewritings for query " + p_query.printString().toString());
		}
*/		return time;
	}
	
	
	
	public static void main(String[] args) {
		TestRealEstate testy = new TestRealEstate();
		Schema schema = testy.CreateMediatedSchema();
		testy.CreateLAVViews();
		testy.CreateRels();
		Vector views;
		Query hw_query = new Query();
		Query hwt_query = new Query();
		Query hwty_query = new Query();
		Query all_query = new Query();

		hw_query.setHead(new Predicate("q(mls)"));
		hwt_query.setHead(new Predicate("q(mls)"));
		hwty_query.setHead(new Predicate("q(mls)"));
		all_query.setHead(new Predicate("q(mls)"));
		int i,j;
		long result_time;
		double avg_result_time;
		double deviation;
		for (i = 0; i < 7; i++)
		{
			hw_query.addSubgoal(testy.m_hw_rels[i]);
			hwt_query.addSubgoal(testy.m_hwt_rels[i]);
			hwty_query.addSubgoal(testy.m_hwty_rels[i]);
			all_query.addSubgoal(testy.m_all_rels[i]);
			for (j = 0; j < 5; j++)
			{
				result_time = askQuery(hw_query,testy.m_hw);
				testy.m_hw_results[i].addStatistic(result_time);
				System.out.println("hw  \t\t: subgoals = " + i + " " +result_time);
				result_time = askQuery(hwt_query,testy.m_hwt);
				testy.m_hwt_results[i].addStatistic(result_time);
				System.out.println("hwt  \t\t: subgoals = " + i + " " +result_time);
				result_time = askQuery(hwty_query,testy.m_hwty);
				testy.m_hwty_results[i].addStatistic(result_time);
				System.out.println("hwty \t\t: subgoals = " + i + " " + result_time);
				result_time = askQuery(all_query,testy.m_all);
				testy.m_all_results[i].addStatistic(result_time);
				System.out.println("all \t\t: subgoals = " + i + " " + result_time);

				result_time = TestLAVCourses.rewriteQuery(hw_query,testy.m_hw_views);
				System.out.println("hw lav  \t: subgoals = " + i + " " +result_time);
				testy.m_hw_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(hwt_query,testy.m_hwt_views);
				System.out.println("hwt lav \t: subgoals = " + i + " " + result_time);
				testy.m_hwt_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(hwty_query,testy.m_hwty_views);
				System.out.println("hwty lav \t: subgoals = " + i + " " +result_time);
				testy.m_hwty_lav_results[i].addStatistic(result_time);
				result_time = TestLAVCourses.rewriteQuery(all_query,testy.m_all_views);
				System.out.println("all lav \t: subgoals = " + i + " " +result_time);
				testy.m_all_lav_results[i].addStatistic(result_time);
				
				
			}
			
			
		}//end getting all the statistics
		//now output them so that we know what it is:
		System.out.println("hw\thwt\thwty\tall\thw lav\thwt lav\thwty lav \tall lav");
		for (i = 0; i < 6; i++)
		{

			avg_result_time = testy.m_hw_results[i].getAverage();
			System.out.print(avg_result_time);
			avg_result_time = testy.m_hwt_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_hwty_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_all_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_hw_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_hwt_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_hwty_lav_results[i].getAverage();

			System.out.print("\t" + avg_result_time);
			avg_result_time = testy.m_all_lav_results[i].getAverage();

			System.out.print("\t\t" + avg_result_time);
			
			System.out.print("\n");
		}


//		Query q = new Query();
//		Predicate p = new Predicate();
//		p.read("q(mls)");
//		q.setHead(p);
//		q.addSubgoal(testy.m_all_rels[0]);		
//		q.addSubgoal(testy.m_all_rels[1]);
//		q.addSubgoal(testy.m_all_rels[2]);
//		q.addSubgoal(testy.m_all_rels[3]);
//		q.addSubgoal(testy.m_all_rels[4]);
		//q.addSubgoal(testy.m_hwt_rels[5]);
//		q.addSubgoal(testy.m_all_rels[6]);
//		System.out.println(q.printString().toString() + "\n");
//		TestLAVRealEstate.rewriteQuery(q,testy.m_all_views);
//		System.out.println(askQuery(q, estate.m_all));
		//System.out.println(testy.m_hw.printString());
		//System.out.println(estate.m_hwt.printString());
		//System.out.println(estate.m_hwty.printString());
		//System.out.println(estate.m_all.printString());
		
		//q = new Query("q(house_id):-hwt-house(house_id,house_desc,agent,agent_phone,mobile,email,firm,firm_phone,firm_fax,rlistingbasic_id,rlistingfeature_id,rlistingadditional,price,address,neighborhood,mls,status,baths,beds,levels,style,garage,lot_size,type,sqft,year,cooling,heat,fireplace,patio,pool,rooms,spa,view,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,taxes,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_sewer,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,available,new_home,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,well,highlights_id)");
		//works
		//q = new Query("q(house_id):-hwty-house(house_id,address,house_desc,home_features,community_features,date_posted,price,beds,baths,category,classification,agency_brokerage,sqft,lot_size,year,garage,neighborhood,school,status,mls,style,levels,available,agent,email,firm_location,firm_fax,firm_phone,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,agent_phone,mobile,firm,rlistingfeature_id,rlistingadditional,type,cooling,heat,fireplace,patio,pool,rooms,spa,view,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,taxes,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_sewer,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,new_home,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,well,highlights_id)");
		//System.out.println(askQuery(q,schema));
	/*	q = new Query("q(house_id):-all-house(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,mobile,rlistingfeature_id,rlistingadditional,patio,pool,spa,view,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,new_home,resort_property,restrictions,septic_tank,stories,to_be_built,water_coop,well,highlights_id)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(price):-all-size(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,exterior,interior,lot_description,roof,site,view,waterfront,firm_id,country,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_water,electricity,golf_course,handicapped_equipped,spa,master_bedroom,new_home,pool,resort_property,restrictions,septic_tank,stories,to_be_built,water_coop,well,highlights_id)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(price):-all-super(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,desc_id,dimension,level,rlistingbasic_id,neighborhood,status,style,sqft,year,home_features,community_features,date_posted,category,classification,agency_brokerage,school,available,email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(house_id):-all-lot(house_id,exterior,fireplace,levels,interior,lot_description,lot_size,roof,site,taxes,view,waterfront,garage,cooling,heat,address,patio,pool,rooms,spa,firm_id,agent,type,mls,country,price,beds,full_baths,half_baths,dining_rooms,living_rooms,house_desc,financing_options_id,acres,sqft,carport_spaces,city_sewer,city_water,electricity,golf_course,handicapped_equipped,master_bedroom,available,new_home,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,well,year,highlights_id)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(house_id):-all-neighborhood(house_id,address,house_desc,home_features,community_features,date_posted,price,beds,baths,category,classification,agency_brokerage,sqft,lot_size,year,garage,neighborhood,school,status,mls,style,levels,available,agent,email,firm_location,firm_fax,firm_phone,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,firm_id,type,country,full_baths,half_baths,dining_rooms,living_rooms,financing_options_id,acres,carport_spaces,city_sewer,city_water,electricity,fireplace,golf_course,handicapped_equipped,spa,master_bedroom,new_home,pool,resort_property,restrictions,school_district,septic_tank,stories,sub_division,to_be_built,water_coop,waterfront,well,highlights_id)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(elementary):-all-schools(school_info_id,elementary,middle_school,high_school)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(taxes):-all-financial(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm,firm_phone,agent,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,amentities_id,exterior,interior,lot_description,roof,site,view,waterfront)");
		System.out.println(askQuery(q, schema));
		q = new Query("q(house_id):-all-contact(house_id,address,price,rooms,beds,baths,basement,levels,garage,sub_division,school_district,mls,type,firm_name,firm_phone,agent_name,agent_phone,house_desc,construction,heat,cooling,fireplace,gas,sewer,water,lot_size,hoa_fee,taxes,condo_level,entry_desc,living_desc,dining_desc,kitchen_desc,breakfast_desc,family_desc,study_desc,recreation_desc,laundry_desc,master_desc,bedroom2_desc,bedroom3_desc,bedroom4_desc,bedroom5_desc,bath1_desc,bath2_desc,bath3_desc,bath4_desc,directions,home_features,community_features,date_posted,category,classification,agency_brokerage,sqft,year,neighborhood,school,status,style,available,agent,agent_email,firm_location,firm_fax,phone_day,phone_evening,phone_leave_message,other_ads,rlistingbasic_id,firm_id,firm_voice_mail,toll_free,firm_email,agent_id,agent_fax,mobile,city,state,amentities_id,features_id,schools_info_id,comments,more_info_contact_id,contact_id,pager,rlistingfeature_id,rlistingadditional)");
		System.out.println(askQuery(q, schema));
	*/	//firm_phone
		//Query query = new Query("q(course_code):-course-listing-rice-hw(course_code,title,credits,note,section_id,schedule_line,section_code,restrictions,level),Washington.time(time_id,start_time,end_time)");
		//Query query = new Query("q(course_code):-course_listing-hw(course_code,title,section_id,schedule_line,section_code,course_credits,restrictions,note,level)");
		//Query query = new Query("q(course_code):-course-listing-rrhw(course_code,subj,crse,section_id,title,credits,instructor,days,time_id,place_id,note,schedule_line,section_code,restrictions,level)");
		//Query query = new Query("q(course_code):-course-listing(note,course_code,subj,crse,lab,section_id,title,credits,days,time_id,place_id,instructor,limit,enrolled,schedule_line,section_code,restrictions,level)");
		//System.out.println(schema.printString());
		/*Query query = new Query("q(house_id):-all-house(house_id,address1,price1,rooms1,beds1,baths1,basement1,levels1,garage1,sub_division1,school_district1,mls1,type1,firm1,firm_phone1,agent1,agent_phone1,house_desc1,construction1,heat1,cooling1,fireplace1,gas1,sewer1,water1,lot_size1,hoa_fee1,taxes1,condo_level1,entry_desc1,living_desc1,dining_desc1,kitchen_desc1,breakfast_desc1,family_desc1,study_desc1,recreation_desc1,laundry_desc1,master_desc1,bedroom2_desc1,bedroom3_desc1,bedroom4_desc1,bedroom5_desc1,bath1_desc1,bath2_desc1,bath3_desc1,bath4_desc1,directions1,home_features1,community_features1,date_posted1,category1,classification1,agency_brokerage1,sqft1,year1,neighborhood1,school1,status1,style1,available1,email1,firm_location1,firm_fax1,phone_day1,phone_evening1,phone_leave_message1,other_ads1,rlistingbasic_id1,mobile1,rlistingfeature_id1,rlistingadditional1,patio1,pool1,spa1,view1,city1,state1,amentities_id1,features_id1,schools_info_id1,comments1,more_info_contact_id1,exterior1,interior1,lot_description1,roof1,site1,waterfront1,firm_id1,country1,half_baths1,dining_rooms1,living_rooms1,financing_options_id1,acres1,carport_spaces1,city_water1,electricity1,golf_course1,handicapped_equipped1,master_bedroom1,new_home1,resort_property1,restrictions1,septic_tank1,stories1,to_be_built1,water_coop1,well1,highlights_id1)," +
				"all-size(house_id,address2,price2,rooms2,beds2,baths2,basement2,levels2,garage2,sub_division2,school_district2,mls2,type2,firm2,firm_phone2,agent2,agent_phone2,house_desc2,construction2,heat2,cooling2,fireplace2,gas2,sewer2,water2,lot_size2,hoa_fee2,taxes2,condo_level2,entry_desc2,living_desc2,dining_desc2,kitchen_desc2,breakfast_desc2,family_desc2,study_desc2,recreation_desc2,laundry_desc2,master_desc2,bedroom2_desc2,bedroom3_desc2,bedroom4_desc2,bedroom5_desc2,bath1_desc2,bath2_desc2,bath3_desc2,bath4_desc2,directions2,home_features2,community_features2,date_posted2,category2,classification2,agency_brokerage2,sqft2,year2,neighborhood2,school2,status2,style2,available2,email2,firm_location2,firm_fax2,phone_day2,phone_evening2,phone_leave_message2,other_ads2,rlistingbasic_id2,city2,state2,amentities_id2,features_id2,schools_info_id2,comments2,more_info_contact_id2,exterior2,interior2,lot_description2,roof2,site2,view2,waterfront2,firm_id2,country2,half_baths2,dining_rooms2,living_rooms2,financing_options_id2,acres2,carport_spaces2,city_water2,electricity2,golf_course2,handicapped_equipped2,spa2,master_bedroom2,new_home2,pool2,resort_property2,restrictions2,septic_tank2,stories2,to_be_built2,water_coop2,well2,highlights_id2)," +
				"all-super(house_id,address3,price3,rooms3,beds3,baths3,basement3,levels3,garage3,sub_division3,school_district3,mls3,type3,firm3,firm_phone3,agent3,agent_phone3,house_desc3,construction3,heat3,cooling3,fireplace3,gas3,sewer3,water3,lot_size3,hoa_fee3,taxes3,condo_level3,entry_desc3,living_desc3,dining_desc3,kitchen_desc3,breakfast_desc3,family_desc3,study_desc3,recreation_desc3,laundry_desc3,master_desc3,bedroom2_desc3,bedroom3_desc3,bedroom4_desc3,bedroom5_desc3,bath1_desc3,bath2_desc3,bath3_desc3,bath4_desc3,directions3,desc_id3,dimension3,level3,rlistingbasic_id3,neighborhood3,status3,style3,sqft3,year3,home_features3,community_features3,date_posted3,category3,classification3,agency_brokerage3,school3,available3,email3,firm_location3,firm_fax3,phone_day3,phone_evening3,phone_leave_message3,other_ads3)," +
				"all-lot(house_id,exterior4,fireplace4,levels4,interior4,lot_description4,lot_size4,roof4,site4,taxes4,view4,waterfront4,garage4,cooling4,heat4,address4,patio4,pool4,rooms4,spa4,firm_id4,agent4,type4,mls4,country4,price4,beds4,full_baths4,half_baths4,dining_rooms4,living_rooms4,house_desc4,financing_options_id4,acres4,sqft4,carport_spaces4,city_sewer4,city_water4,electricity4,golf_course4,handicapped_equipped4,master_bedroom4,available4,new_home4,resort_property4,restrictions4,school_district4,septic_tank4,stories4,sub_division4,to_be_built4,water_coop4,well4,year4,highlights_id4)," +
				"all-neighborhood(house_id,address5,house_desc5,home_features5,community_features5,date_posted5,price5,beds5,baths5,category5,classification5,agency_brokerage5,sqft5,lot_size5,year5,garage5,neighborhood5,school5,status5,mls5,style5,levels5,available5,agent5,email5,firm_location5,firm_fax5,firm_phone5,phone_day5,phone_evening5,phone_leave_message5,other_ads5,rlistingbasic_id5,firm_id5,type5,country5,full_baths5,half_baths5,dining_rooms5,living_rooms5,financing_options_id5,acres5,carport_spaces5,city_sewer5,city_water5,electricity5,fireplace5,golf_course5,handicapped_equipped5,spa5,master_bedroom5,new_home5,pool5,resort_property5,restrictions5,school_district5,septic_tank5,stories5,sub_division5,to_be_built5,water_coop5,waterfront5,well5,highlights_id5)," +
				"all-financial(house_id,address7,price7,rooms7,beds7,baths7,basement7,levels7,garage7,sub_division7,school_district7,mls7,type7,firm7,firm_phone7,agent7,agent_phone7,house_desc7,construction7,heat7,cooling7,fireplace7,gas7,sewer7,water7,lot_size7,hoa_fee7,taxes7,condo_level7,entry_desc7,living_desc7,dining_desc7,kitchen_desc7,breakfast_desc7,family_desc7,study_desc7,recreation_desc7,laundry_desc7,master_desc7,bedroom2_desc7,bedroom3_desc7,bedroom4_desc7,bedroom5_desc7,bath1_desc7,bath2_desc7,bath3_desc7,bath4_desc7,directions7,amentities_id7,exterior7,interior7,lot_description7,roof7,site7,view7,waterfront7)," +
				"all-contact(house_id,address8,price8,rooms8,beds8,baths8,basement8,levels8,garage8,sub_division8,school_district8,mls8,type8,firm_name8,firm_phone8,agent_name8,agent_phone8,house_desc8,construction8,heat8,cooling8,fireplace8,gas8,sewer8,water8,lot_size8,hoa_fee8,taxes8,condo_level8,entry_desc8,living_desc8,dining_desc8,kitchen_desc8,breakfast_desc8,family_desc8,study_desc8,recreation_desc8,laundry_desc8,master_desc8,bedroom2_desc8,bedroom3_desc8,bedroom4_desc8,bedroom5_desc8,bath1_desc8,bath2_desc8,bath3_desc8,bath4_desc8,directions8,home_features8,community_features8,date_posted8,category8,classification8,agency_brokerage8,sqft8,year8,neighborhood8,school8,status8,style8,available8,agent8,agent_email8,firm_location8,firm_fax8,phone_day8,phone_evening8,phone_leave_message8,other_ads8,rlistingbasic_id8,firm_id8,firm_voice_mail8,toll_free8,firm_email8,agent_id8,agent_fax8,mobile8,city8,state8,amentities_id8,features_id8,schools_info_id8,comments8,more_info_contact_id8,contact_id8,pager8,rlistingfeature_id8,rlistingadditional8)");

		System.out.println(askQuery(query, schema));
		*/
		}
}
