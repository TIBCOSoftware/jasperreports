package net.sf.jasperreports.engine.design;

import java.util.ArrayList;
import java.util.List;

public class CompilationUnits
{

	private JRCompilationUnit[] reportUnits;
	private JRCompilationUnit[] sourceUnits;
	private int[] sourceUnitIndexes;

	public CompilationUnits(JRCompilationUnit[] reportUnits)
	{
		this.reportUnits = reportUnits;
		initSourceUnits();
	}

	private void initSourceUnits()
	{
		sourceUnitIndexes = new int[reportUnits.length];
		List<JRCompilationUnit> sourceUnitList = new ArrayList<>(reportUnits.length);
		for (int i = 0; i < reportUnits.length; i++)
		{
			JRCompilationUnit unit = reportUnits[i];
			if (unit.hasSource())
			{
				sourceUnitIndexes[i] = sourceUnitList.size();
				sourceUnitList.add(unit);
			}
			else
			{
				sourceUnitIndexes[i] = -1;
			}
		}
		sourceUnits = sourceUnitList.toArray(new JRCompilationUnit[sourceUnitList.size()]);
	}
	
	public JRCompilationUnit[] getSourceUnits()
	{
		return sourceUnits;
	}
	
	public JRCompilationUnit getCompiledUnit(int index)
	{
		//Java compilers update the source unit array when builtin functions are used
		//return from the source unit array when found
		int sourceUnitIndex = sourceUnitIndexes[index];
		return sourceUnitIndex >= 0 ? sourceUnits[sourceUnitIndex] : reportUnits[index];
	}

}
