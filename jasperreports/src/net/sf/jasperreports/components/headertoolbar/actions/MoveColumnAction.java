package net.sf.jasperreports.components.headertoolbar.actions;

import java.util.UUID;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRIdentifiable;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.web.commands.CommandStack;
import net.sf.jasperreports.web.commands.CommandTarget;
import net.sf.jasperreports.web.commands.ResetInCacheCommand;


public class MoveColumnAction extends AbstractTableAction
{

	private MoveColumnData moveColumnData;
	
	public MoveColumnAction(){
	}
	
	public void setMoveColumnData(MoveColumnData moveColumnData) {
		this.moveColumnData = moveColumnData;
	}

	public MoveColumnData getMoveColumnData() {
		return moveColumnData;
	}

	public String getName() {
		return "move_column_action";
	}

	public void performAction() 
	{
		if (moveColumnData != null) 
		{
			CommandTarget target = getCommandTarget(UUID.fromString(moveColumnData.getUuid()));
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
						new MoveColumnCommand(table, moveColumnData), 
						getJasperReportsContext(),
						getReportContext(), 
						target.getUri()
						)
					);
			}
		}
	}

}
