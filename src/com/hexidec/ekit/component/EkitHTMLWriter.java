/*
GNU Lesser General Public License

EkitHTMLWriter
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
import java.io.StringWriter;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLWriter;

/**
 * @author <a href="mailto:christoph_w@users.sourceforge.net">Christoph Wei&szlig;enborn</a>
 */
public class EkitHTMLWriter {

	public static void write(Document doc) throws IOException, BadLocationException {
		System.out.println("HTML document:");
		System.out.println(toString((HTMLDocument) doc));
		System.out.println("#// end of html document");
	}

	public static String toString(HTMLDocument doc) throws IOException, BadLocationException {
		final StringWriter sw = new StringWriter();
		HTMLWriter w = new HTMLWriter(sw, doc);
		w.write();
		return sw.toString();
	}

	public static String toString(HTMLDocument doc, int start, int length) throws IOException, BadLocationException {
		final StringWriter sw = new StringWriter();
		HTMLWriter w = new HTMLWriter(sw, doc, start, length);
		w.write();
		return sw.toString();
	}

	public static String toShortString(HTMLDocument doc) throws IOException, BadLocationException {
		final StringWriter sw = new StringWriter();
		HTMLWriter w = new ExtendedWhiteSpaceHandlingHTMLWriter(sw, doc, 0, doc.getLength(), false);
		w.write();
		return sw.toString();
	}
	
}
