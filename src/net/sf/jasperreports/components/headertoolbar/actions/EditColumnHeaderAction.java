package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


public class EditColumnHeaderAction extends AbstractTableAction
{

	private  EditColumnHeaderData  editColumnHeaderData;
	
	public EditColumnHeaderAction(){
	}
	
	public void setEditColumnHeaderData(EditColumnHeaderData editColumnHeaderData) {
		this.editColumnHeaderData = editColumnHeaderData;
	}

	public EditColumnHeaderData getEditColumnHeaderData() {
		return editColumnHeaderData;
	}

	public String getName() {
		return "edit_column_header_action";
	}

	public void performAction() 
	{
		if (editColumnHeaderData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(editColumnHeaderData.getTableUuid()));
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
						new EditColumnHeaderCommand(table, editColumnHeaderData), 
						getJasperReportsContext(), 
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
