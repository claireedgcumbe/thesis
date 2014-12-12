/*
 * Created on Feb 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package semantics;
import minicon.View;
/**
 * @author rap
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ViewSchemaCombo {

	protected Schema m_schema;
	protected View m_view;
	
	public ViewSchemaCombo()
	{
		m_schema = null;
		m_view = null;
	}
	
	public ViewSchemaCombo(Schema p_schema, View p_view)
	{
		m_schema = p_schema;
		m_view = p_view;
		
	}
	
	public ViewSchemaCombo(View p_view, Schema p_schema)
	{
		m_schema = p_schema;
		m_view = p_view;
		
	}

	public Schema getSchema()
	{
		return m_schema;
	}
	public View getView()
	{
		return m_view;
	}
	
	public void setSchema(Schema p_schema)
	{
		m_schema = p_schema;
	}
	
	public void setView(View p_view)
	{
		m_view = p_view;
	}
	
	
	
	public static void main(String[] args) {
	}
}
