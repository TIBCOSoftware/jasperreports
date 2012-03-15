package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


public class HideUnhideColumnsAction extends AbstractTableAction
{

	private HideUnhideColumnData columnData;
	
	public HideUnhideColumnsAction(){
	}
	
	public void setColumnData(HideUnhideColumnData columnData) {
		this.columnData = columnData;
	}

	public HideUnhideColumnData getColumnData() {
		return columnData;
	}

	public String getName() {
		return "hide_unhide_column_action";
	}

	public void performAction() 
	{
		if (columnData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(columnData.getTableUuid()));
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
						new HideUnhideColumnsCommand(table, columnData), 
						getJasperReportsContext(), 
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
