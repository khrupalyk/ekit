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

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;


/**
 * A HTMLEditorKit using a special HTMLWriter for writing the documents content. The used HTMLWriter is able to
 * suppress unnecessary whitespaces used for proper html formating.
 * 
 * @see ExtendedWhiteSpaceHandlingHTMLWriter
 * @author <a href="mailto:christoph_w@users.sourceforge.net">Christoph Wei&szlig;enborn</a>
 */
public class ExtendedWhiteSpaceHandlingHTMLEditorKit extends ExtendedHTMLEditorKit
{
	private boolean fPrintSpace;

	/**
	 * Creates an instance which uses a {@link HTMLWriter} writing formatted html.
	 */
	public ExtendedWhiteSpaceHandlingHTMLEditorKit()
	{
		super();
		fPrintSpace = true;
	}

	/**
	 * Creates an instance which may use a {@link HTMLWriter} writing html without additional whitespace.
	 * @param writeFormattedHTML whether to use the default formatted html creating HTMLWriter
	 */
	public ExtendedWhiteSpaceHandlingHTMLEditorKit(boolean writeFormattedHTML)
	{
		super();
		fPrintSpace = writeFormattedHTML;
	}

	/**
	 * Getter for property printSpace which means to produce formatted html.
	 *
	 * @return whether the default formatted html creating HTMLWriter is used
	 */
	public boolean getPrintSpace()
	{
		return fPrintSpace;
	}

	/**
	 * Setter for property printSpace which means to produce formatted html.
	 *
	 * @param printSpace whether to use the default formatted html creating HTMLWriter
	 */
	public void setPrintSpace(boolean printSpace)
	{
		fPrintSpace = printSpace;
	}

	/** {@inheritDoc} */
	public void write(Writer out, Document doc, int pos, int len)
	throws IOException, BadLocationException
	{
		if (doc instanceof HTMLDocument)
		{
			HTMLWriter w = new ExtendedWhiteSpaceHandlingHTMLWriter(out, (HTMLDocument) doc, pos, len, fPrintSpace);
			w.write();
		}
		else {
			super.write(out, doc, pos, len);
		}
	}

}
