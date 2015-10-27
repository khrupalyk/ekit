/*
GNU Lesser General Public License

ExtendedWhiteSpaceHandlingHTMLWriter
Copyright (C) 2007 Christoph Weissenborn

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package com.hexidec.ekit.component;

import java.io.IOException;
import java.io.Writer;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;


/**
 * A HTMLWriter which is able to suppress unnecessary whitespaces used for proper html formating.
 * 
 * @author <a href="mailto:christoph_w@users.sourceforge.net">Christoph Wei&szlig;enborn</a>
 */
public class ExtendedWhiteSpaceHandlingHTMLWriter extends HTMLWriter
{
	private boolean fPrintSpace = true;
	
	/**
     * Creates a new NoSpaceHTMLWriter.
     *
     * @param w  a Writer
     * @param doc an HTMLDocument
     * @param pos the document location from which to fetch the content
     * @param len the amount to write out
	 */
	public ExtendedWhiteSpaceHandlingHTMLWriter(Writer w, HTMLDocument doc, int pos, int len)
	{
		super(w, doc, pos, len);
	}

	/**
	 * Creates a new NoSpaceHTMLWriter.
	 *
	 * @param w  a Writer
	 * @param doc an HTMLDocument
	 * @param pos the document location from which to fetch the content
	 * @param len the amount to write out
	 */
	public ExtendedWhiteSpaceHandlingHTMLWriter(Writer w, HTMLDocument doc, int pos, int len, boolean printSpace)
	{
		super(w, doc, pos, len);
		fPrintSpace = printSpace;
	}

	public void setPrintSpace(boolean printSpace)
	{
		fPrintSpace = printSpace;
	}
	
	/** {@inheritDoc} */
    protected boolean getCanWrapLines()
    {
		return fPrintSpace;
	}

	/** {@inheritDoc} */
	protected void indent()
	throws IOException
	{
		if (fPrintSpace)
		{
			super.indent();
		}
	}

}
