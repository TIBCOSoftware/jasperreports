package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


public class ResizeColumnAction extends AbstractTableAction
{

	private ResizeColumnData resizeColumnData;
	
	public ResizeColumnAction(){
	}
	
	public void setResizeColumnData(ResizeColumnData resizeColumnData) {
		this.resizeColumnData = resizeColumnData;
	}

	public ResizeColumnData getResizeColumnData() {
		return resizeColumnData;
	}

	public String getName() {
		return "resize_column_action";
	}

	public void performAction() 
	{
		if (resizeColumnData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(resizeColumnData.getUuid()));
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
						new ResizeColumnAndParentsCommand(componentElement, resizeColumnData), 
						getJasperReportsContext(), 
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
