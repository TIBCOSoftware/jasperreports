/*
 * ============================================================================
 *                   The JasperReports License, Version 1.0
 * ============================================================================
 * 
 * Copyright (C) 2001-2004 Teodor Danciu (teodord@users.sourceforge.net). All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * 3. The end-user documentation included with the redistribution, if any, must
 *    include the following acknowledgment: "This product includes software
 *    developed by Teodor Danciu (http://jasperreports.sourceforge.net)."
 *    Alternately, this acknowledgment may appear in the software itself, if
 *    and wherever such third-party acknowledgments normally appear.
 * 
 * 4. The name "JasperReports" must not be used to endorse or promote products 
 *    derived from this software without prior written permission. For written 
 *    permission, please contact teodord@users.sourceforge.net.
 * 
 * 5. Products derived from this software may not be called "JasperReports", nor 
 *    may "JasperReports" appear in their name, without prior written permission
 *    of Teodor Danciu.
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 * APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 * DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 * OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 * ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 * (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/*
 * ============================================================================
 *                   GNU Lesser General Public License
 * ============================================================================
 *
 * JasperReports - Free Java report-generating library.
 * Copyright (C) 2001-2004 Teodor Danciu teodord@users.sourceforge.net
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
 * Teodor Danciu
 * 173, Calea Calarasilor, Bl. 42, Sc. 1, Ap. 18
 * Postal code 030615, Sector 3
 * Bucharest, ROMANIA
 * Email: teodord@users.sourceforge.net
 */

import java.util.*;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id$
 */
public class CustomBeanFactory
{


	/**
	 *
	 */
	private static CustomBean[] data =
		{
			new CustomBean("Berne", new Integer(9), "James Schneider", "277 Seventh Av."),
			new CustomBean("Berne", new Integer(22), "Bill Ott", "250 - 20th Ave."),
			new CustomBean("Boston", new Integer(23), "Julia Heiniger", "358 College Av."),
			new CustomBean("Boston", new Integer(32), "Michael Ott", "339 College Av."),
			new CustomBean("Chicago", new Integer(39), "Mary Karsen", "202 College Av."),
			new CustomBean("Chicago", new Integer(35), "George Karsen", "412 College Av."),
			new CustomBean("Chicago", new Integer(11), "Julia White", "412 Upland Pl."),
			new CustomBean("Dallas", new Integer(47), "Janet Fuller", "445 Upland Pl."),
			new CustomBean("Dallas", new Integer(43), "Susanne Smith", "2 Upland Pl."),
			new CustomBean("Dallas", new Integer(40), "Susanne Miller", "440 - 20th Ave."),
			new CustomBean("Dallas", new Integer(36), "John Steel", "276 Upland Pl."),
			new CustomBean("Dallas", new Integer(37), "Michael Clancy", "19 Seventh Av."),
			new CustomBean("Dallas", new Integer(19), "Susanne Heiniger", "86 - 20th Ave."),
			new CustomBean("Dallas", new Integer(10), "Anne Fuller", "135 Upland Pl."),
			new CustomBean("Dallas", new Integer(4), "Sylvia Ringer", "365 College Av."),
			new CustomBean("Dallas", new Integer(0), "Laura Steel", "429 Seventh Av."),
			new CustomBean("Lyon", new Integer(38), "Andrew Heiniger", "347 College Av."),
			new CustomBean("Lyon", new Integer(28), "Susanne White", "74 - 20th Ave."),
			new CustomBean("Lyon", new Integer(17), "Laura Ott", "443 Seventh Av."),
			new CustomBean("Lyon", new Integer(2), "Anne Miller", "20 Upland Pl."),
			new CustomBean("New York", new Integer(46), "Andrew May", "172 Seventh Av."),
			new CustomBean("New York", new Integer(44), "Sylvia Ott", "361 College Av."),
			new CustomBean("New York", new Integer(41), "Bill King", "546 College Av."),
			new CustomBean("Oslo", new Integer(45), "Janet May", "396 Seventh Av."),
			new CustomBean("Oslo", new Integer(42), "Robert Ott", "503 Seventh Av."),
			new CustomBean("Paris", new Integer(25), "Sylvia Steel", "269 College Av."),
			new CustomBean("Paris", new Integer(18), "Sylvia Fuller", "158 - 20th Ave."),
			new CustomBean("Paris", new Integer(5), "Laura Miller", "294 Seventh Av."),
			new CustomBean("San Francisco", new Integer(48), "Robert White", "549 Seventh Av."),
			new CustomBean("San Francisco", new Integer(7), "James Peterson", "231 Upland Pl.")
		};  
            

	/**
	 *
	 */
	public static Object[] getBeanArray()
	{
		return data;
	}


	/**
	 *
	 */
	public static Collection getBeanCollection()
	{
		return Arrays.asList(data);
	}


}
