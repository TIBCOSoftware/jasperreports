/*
 * ============================================================================
 * GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2006 JasperSoft Corporation http://www.jaspersoft.com
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307, USA.
 * 
 * JasperSoft Corporation
 * 303 Second Street, Suite 450 North
 * San Francisco, CA 94107
 * http://www.jaspersoft.com
 */
package net.sf.jasperreports.olap;

import java.io.StringReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.olap.mapping.AxisPosition;
import net.sf.jasperreports.olap.mapping.DataMapping;
import net.sf.jasperreports.olap.mapping.Mapping;
import net.sf.jasperreports.olap.mapping.MappingLexer;
import net.sf.jasperreports.olap.mapping.MappingMetadata;
import net.sf.jasperreports.olap.mapping.MappingParser;
import net.sf.jasperreports.olap.mapping.MemberDepth;
import net.sf.jasperreports.olap.mapping.MemberMapping;
import net.sf.jasperreports.olap.mapping.MemberProperty;
import net.sf.jasperreports.olap.mapping.Tuple;
import net.sf.jasperreports.olap.mapping.TuplePosition;
import net.sf.jasperreports.olap.result.JROlapCell;
import net.sf.jasperreports.olap.result.JROlapHierarchy;
import net.sf.jasperreports.olap.result.JROlapHierarchyLevel;
import net.sf.jasperreports.olap.result.JROlapMember;
import net.sf.jasperreports.olap.result.JROlapMemberTuple;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.result.JROlapResultAxis;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import antlr.ANTLRException;


/**
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id$
 */
public class JROlapDataSource implements JRDataSource, MappingMetadata
{
	private static final Log log = LogFactory.getLog(JROlapDataSource.class);
	
	protected final JROlapResult olapResult;
	protected JROlapResultAxis[] axes;
	protected final JROlapHierarchy[][] queryHierarchies;
	protected final int hierarchiesCount;
	
	protected Map fieldMatchers;
	protected int[][] fieldsMaxDepths;
	protected boolean[] iteratePositions;
	protected boolean iterate;

	protected boolean dataField;
	
	protected Map fieldValues;
	protected int[] axisPositions;
	protected boolean first;
	protected int[][] maxDepths;

	public JROlapDataSource(JRDataset dataset, JROlapResult result)
	{
		this.olapResult = result;
		axes = result.getAxes();
		
		queryHierarchies = new JROlapHierarchy[axes.length][];
		fieldsMaxDepths = new int[axes.length][];
		maxDepths = new int[axes.length][];
		int hCount = 0;
		for (int i = 0; i < axes.length; i++)
		{
			queryHierarchies[i] = axes[i].getHierarchiesOnAxis();
			
			hCount += queryHierarchies[i].length;
			fieldsMaxDepths[i] = new int[queryHierarchies[i].length];
			maxDepths[i] = new int[queryHierarchies[i].length];
		}
		hierarchiesCount = hCount;

		axisPositions = new int[axes.length];		

		init(dataset);
	}

	public boolean next() throws JRException
	{
		boolean next;
		boolean matchMaxLevel;
		do
		{
			if (iterate)
			{
				next = nextPositions();
			}
			else
			{
				next = first;
				first = false;
			}

			if (!next)
			{
				break;
			}

			resetMaxDepths();
			for (Iterator it = fieldMatchers.entrySet().iterator(); it.hasNext();)
			{
				Map.Entry entry = (Map.Entry) it.next();
				Object fieldName = entry.getKey();
				FieldMatcher matcher = (FieldMatcher) entry.getValue();
				if (matcher.matches())
				{
					Object value = matcher.value();
					fieldValues.put(fieldName, value);
				}
			}
			
			matchMaxLevel = true;
			axes_loop:
			for (int i = 0; i < axes.length; i++)
			{
				if (iteratePositions[i])
				{
					for (int j = 0; j < fieldsMaxDepths[i].length; j++)
					{
						if (maxDepths[i][j] < fieldsMaxDepths[i][j])
						{
							matchMaxLevel = false;
							break axes_loop;
						}
					}
				}
			}
		}
		while (!matchMaxLevel);
		
		return next;
	}

	
	private void resetMaxDepths()
	{
		for (int i = 0; i < axes.length; ++i)
		{
			if (iteratePositions[i])
			{
				for (int j = 0; j < maxDepths[i].length; j++)
				{
					maxDepths[i][j] = 0;
				}
			}
		}
	}

	protected boolean nextPositions()
	{
		boolean next;
		int i = 0;
		for (; i < axes.length; ++i)
		{
			if (iteratePositions[i])
			{
				++axisPositions[i];
				if (axisPositions[i] >= axes[i].getTupleCount())
				{
					axisPositions[i] = 0;
				}
				else
				{
					break;
				}
			}
		}
		
		next = i < axes.length;
		return next;
	}

	public Object getFieldValue(JRField jrField) throws JRException
	{
		return fieldValues.get(jrField.getName());
	}


	private void init(JRDataset dataset)
	{
		iteratePositions = new boolean[axes.length];
		
		fieldMatchers = new HashMap();
		
		dataField = false;
		JRField[] fields = dataset.getFields();
		if (fields != null)
		{
			for (int i = 0; i < fields.length; i++)
			{
				JRField field = fields[i];
				String fieldMapping = getFieldMapping(field);
				
		        MappingLexer lexer = new MappingLexer(new StringReader(fieldMapping));
		        MappingParser parser = new MappingParser(lexer);
		        parser.setMappingMetadata(this);
		        Mapping mapping;
				try
				{
					mapping = parser.mapping();
				}
				catch (ANTLRException e)
				{
					log.error("Error parsing field mapping", e);
					throw new JRRuntimeException(e);
				}
				
				if (mapping == null)
				{
					throw new JRRuntimeException("Invalid field mapping \"" + fieldMapping + "\".");
				}

				processMappingMembers(mapping);

				FieldMatcher fieldMatcher = createFieldMatcher(mapping);
				fieldMatchers.put(field.getName(), fieldMatcher);
			}
		}
		
		if (!dataField)
		{
			Arrays.fill(iteratePositions, true);
		}
		
		initIterate();
	}

	private void processMappingMembers(Mapping mapping)
	{
		for (Iterator it = mapping.memberMappings(); it.hasNext();)
		{
			net.sf.jasperreports.olap.mapping.Member member = (net.sf.jasperreports.olap.mapping.Member) it.next();
			processMemberInfo(member);
		}
	}

	private FieldMatcher createFieldMatcher(Mapping mapping)
	{
		FieldMatcher fieldMatcher;
		if (mapping instanceof MemberMapping)
		{
			fieldMatcher = new MemberFieldMatcher((MemberMapping) mapping);
		}
		else if (mapping instanceof DataMapping)
		{
			dataField = true;
			fieldMatcher = new DataFieldMatcher((DataMapping) mapping);
		}
		else
		{
			throw new JRRuntimeException("internal error");
		}
		
		return fieldMatcher;
	}

	protected String getFieldMapping(JRField field) 
	{
		return field.getDescription();
	}

	private void initIterate()
	{
		int firstPos = 0;
		while (firstPos < axes.length && !iteratePositions[firstPos])
		{
			++firstPos;
		}
		
		if (firstPos < axes.length)
		{
			iterate = true;
			axisPositions[firstPos] = -1;
		}
		else
		{
			iterate = false;
			first = true;
		}
		
		fieldValues = new HashMap();
	}

	
	protected void processMemberInfo(net.sf.jasperreports.olap.mapping.Member member)
	{
		MemberDepth memberDepth = member.getDepth();
		if (memberDepth != null)
		{
			int depth = memberDepth.getDepth();
			int axis = member.getAxis().getIdx();
			int idx = member.getPosition().getIdx();
			
			if (depth > fieldsMaxDepths[axis][idx])
			{
				fieldsMaxDepths[axis][idx] = depth;
			}
		}
	}

	
	public int getDimensionIndex(net.sf.jasperreports.olap.mapping.Axis axis, String dimension)
	{
		JROlapHierarchy[] hierarchies = axes[axis.getIdx()].getHierarchiesOnAxis();
		int dimensionIndex = -1;
		for (int i = 0; i < hierarchies.length; i++)
		{
			JROlapHierarchy hierarchy = hierarchies[i];
			if (hierarchy.getDimensionName().equals(dimension))
			{
				dimensionIndex = i;
			}
		}
		
		if (dimensionIndex == -1)
		{
			throw new JRRuntimeException("Could not find dimension \"" + dimension + "\" on axis " + axis.getIdx() + ".");
		}
		
		return dimensionIndex;
	}

	public int getLevelDepth(TuplePosition pos, String levelName)
	{
		JROlapHierarchy hierarchy = axes[pos.getAxis().getIdx()].getHierarchiesOnAxis()[pos.getIdx()];
		JROlapHierarchyLevel[] levels = hierarchy.getLevels();
		int levelIndex = -1;
		for (int i = 0; i < levels.length; i++)
		{
			JROlapHierarchyLevel level = levels[i];
			if (level != null && level.getName().equals(levelName))
			{
				levelIndex = level.getDepth();
				break;
			}
		}
		
		if (levelIndex == -1)
		{
			throw new JRRuntimeException("Could not find level \"" + levelName + "\" on hierarchy " + hierarchy.getDimensionName() + ".");
		}
		
		return levelIndex;
	}

	
	protected void setMatchMemberDepth(net.sf.jasperreports.olap.mapping.Member memberInfo, JROlapMember member)
	{
		int memberDepth = member.getDepth();
		int axis = memberInfo.getAxis().getIdx();
		int pos = memberInfo.getPosition().getIdx();
		if (maxDepths[axis][pos] < memberDepth)
		{
			maxDepths[axis][pos] = memberDepth;
		}
	}
	
	
	protected abstract class FieldMatcher
	{
		public abstract boolean matches();
		
		public abstract Object value();
		
		public final JROlapMember member(net.sf.jasperreports.olap.mapping.Member memberInfo, int[] positions)
		{
			int axisIdx = memberInfo.getAxis().getIdx();
			JROlapResultAxis axis = axes[axisIdx];
			JROlapMemberTuple tuple = axis.getTuple(positions[axisIdx]);
			JROlapMember[] members = tuple.getMembers();
			int pos = memberInfo.getPosition().getIdx();
			return members[pos];
		}
	}
	
	protected class MemberFieldMatcher extends FieldMatcher
	{
		final net.sf.jasperreports.olap.mapping.Member memberInfo;
		final MemberProperty property;
		JROlapMember member;
		
		MemberFieldMatcher(MemberMapping mapping)
		{
			this.memberInfo = mapping.getMember();
			this.property = mapping.getProperty();
		}
		
		public boolean matches()
		{
			member = member(memberInfo, axisPositions);
			setMatchMemberDepth(memberInfo, member);
			member = memberInfo.ancestorMatch(member);
			return member != null;
		}

		public Object value()
		{
			Object value;
			if (property != null)
			{
				value = member.getPropertyValue(property.getName());
			}
			else if (memberInfo.getDepth() == null)
			{
				value = member.getMondrianMember();
			}
			else
			{
				value = member.getName();
			}
			return value;
		}
	}
	
	
	protected class DataFieldMatcher extends FieldMatcher
	{
		private final boolean formatted;
		private final int[] dataPositions;
		private final net.sf.jasperreports.olap.mapping.Member[] members;
		private int[] positions;

		public DataFieldMatcher(DataMapping mapping)
		{
			this.formatted = mapping.isFormatted();
			
			List mappingPositions = mapping.getPositions();
			if (mappingPositions == null)
			{
				this.dataPositions = null;
				positions = axisPositions;
			}
			else
			{
				if (mappingPositions.size() != axes.length)
				{
					throw new JRRuntimeException("Incorrect data mapping: the number of positions doesn't match the number of axes.");
				}
				
				this.dataPositions = new int[axes.length];
				int c = 0;
				for (Iterator iter = mappingPositions.iterator(); iter.hasNext(); ++c)
				{
					AxisPosition position = (AxisPosition) iter.next();
					int pos;
					if (position.isSpecified())
					{
						pos = position.getPosition();
					}
					else
					{
						pos = -1;
						iteratePositions[c] = true;
					}
					dataPositions[c] = pos;
				}
			}
			
			List filter = mapping.getFilter();
			if (filter == null || filter.isEmpty())
			{
				this.members = null;
			}
			else
			{
				this.members = new net.sf.jasperreports.olap.mapping.Member[filter.size()];
				filter.toArray(this.members);
			}
		}

		public boolean matches()
		{
			if (dataPositions != null)
			{
				setPositions();
			}

			boolean matches = true;
			if (members != null)
			{
				for (int i = 0; i < members.length; i++)
				{
					net.sf.jasperreports.olap.mapping.Member memberInfo = members[i];
					JROlapMember member = member(memberInfo, positions);
					setMatchMemberDepth(memberInfo, member);
					if (!memberInfo.matches(member))
					{
						matches = false;
						break;
					}
				}
			}

			return matches;
		}

		public Object value()
		{
			JROlapCell cell = olapResult.getCell(positions);
			
			if (cell != null && cell.isError())
			{
				throw new JRRuntimeException("OLAP cell calculation returned error.");
			}
			
			Object value;
			if (cell == null || cell.isNull())
			{
				value = null;
			}
			else if (formatted)
			{
				value = cell.getFormattedValue();
			}
			else
			{
				value = cell.getValue();
			}
			
			return value;
		}
		
		void setPositions()
		{
			positions = new int[axes.length];
			for (int i = 0; i < axes.length; i++)
			{
				int dataPosition = dataPositions[i];
				positions[i] = dataPosition == -1 ? axisPositions[i] : dataPosition;
			}
		}
	}


	public int getTuplePosition(int axisIndex, Tuple tuple)
	{
		if (axisIndex > axes.length)
		{
			throw new JRRuntimeException("OLAP result doesn't contain Axis(" + axisIndex + ").");
		}
		
		String[] memberUniqueNames = tuple.getMemberUniqueNames();
		JROlapResultAxis axis = axes[axisIndex];
		int tupleCount = axis.getTupleCount();
		
		int pos = -1;
		for (int i = 0; i < tupleCount; i++)
		{
			JROlapMemberTuple memberTuple = axis.getTuple(i);
			JROlapMember[] resMembers = memberTuple.getMembers();
			if (resMembers.length == memberUniqueNames.length)
			{
				boolean eq = true;
				for (int j = 0; eq && j < resMembers.length; ++j)
				{
					if (!memberUniqueNames[j].equals(resMembers[j].getUniqueName()))
					{
						eq = false;
					}
				}
				
				if (eq)
				{
					pos = i;
					break;
				}
			}
		}
		
		if (pos == -1)
		{
			StringBuffer sb = new StringBuffer();
			sb.append('(');
			for (int i = 0; i < memberUniqueNames.length; i++)
			{
				if (i > 0)
				{
					sb.append(',');
				}
				sb.append(memberUniqueNames[i]);
			}
			throw new JRRuntimeException("No such tuple " + sb + " on axis " + axisIndex + ".");
		}
		
		return pos;
	}
}
