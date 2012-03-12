package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


public class EditColumnValuesAction extends AbstractTableAction
{

	private  EditColumnValueData  editColumnValueData;
	
	public EditColumnValuesAction(){
	}
	
	public void setEditColumnValueData(EditColumnValueData editColumnValueData) {
		this.editColumnValueData = editColumnValueData;
	}

	public EditColumnValueData getEditColumnValueData() {
		return editColumnValueData;
	}

	public String getName() {
		return "edit_column_value_action";
	}

	public void performAction() 
	{
		if (editColumnValueData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(editColumnValueData.getTableUuid()));
			if (target != null)
			{
				JRIdentifiable identifiable = target.getIdentifiable();
				JRDesignComponentElement componentElement = identifiable instanceof JRDesignComponentElement ? (JRDesignComponentElement)identifiable : null;
				StandardTable table = componentElement == null ? null : (StandardTable)componentElement.getComponent();
				
				// obtain command stack
				CommandStack commandStack = getCommandStack();
				
				// execute command
				commandStack.execute(
					new ResetInCacheCommand(
						new EditColumnValuesCommand(table, editColumnValueData), 
						getJasperReportsContext(), 
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
