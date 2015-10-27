/*
GNU Lesser General Public License

ListAutomationAction
Copyright (C) 2000 Howard Kistler

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

package com.hexidec.ekit.action;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.StringTokenizer;
import javax.swing.JEditorPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.hexidec.ekit.EkitCore;
import com.hexidec.ekit.component.*;

import com.hexidec.util.Translatrix;

/** Class for automatically creating bulleted lists from selected text
  */
public class ListAutomationAction extends HTMLEditorKit.InsertHTMLTextAction
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 972500364067149448L;

	/** @deprecated inner variable should not be accessed directly */
	// TODO reduce visibility to private in next release
	protected EkitCore parentEkit;
	private HTML.Tag baseTag;
	private HTMLUtilities htmlUtilities;

	public ListAutomationAction(EkitCore ekit, String sLabel, HTML.Tag listType)
	{
		super(sLabel, "", listType, HTML.Tag.LI);
		parentEkit = ekit;
		baseTag    = listType;
		htmlUtilities = new HTMLUtilities(ekit);
	}

	public void actionPerformed(ActionEvent ae)
	{
		try
		{
			JEditorPane jepEditor = parentEkit.getTextPane();
			String selTextBase = jepEditor.getSelectedText();
			if(selTextBase == null || selTextBase.length() < 1)
			{
				int pos = parentEkit.getCaretPosition();
				// FIXME why do set position to position - programming with side effects is very ugly!
				parentEkit.setCaretPosition(pos);
				if(ae.getActionCommand() != "newListPoint")
				{
					if(htmlUtilities.checkParentsTag(new HTML.Tag[] {HTML.Tag.OL, HTML.Tag.UL}) != null)
					{
						SimpleInfoDialog.show(parentEkit.getParentWindow(), Translatrix.getTranslationString("Error"),
							true, Translatrix.getTranslationString("ErrorNestedListsNotSupported"));
						return;
					}
				}
				String sListType = (baseTag == HTML.Tag.OL ? "ol" : "ul");
				StringBuffer sbNew = new StringBuffer();
				if(htmlUtilities.checkParentsTag(baseTag))
				{
					sbNew.append("<li></li>");
					getHTMLEditorKit(jepEditor).insertHTML(parentEkit.getExtendedHtmlDoc(), pos, sbNew.toString(), 0, 0, HTML.Tag.LI);
				}
				else
				{
					sbNew.append("<" + sListType + "><li></li></" + sListType + "><p>&nbsp;</p>");
					getHTMLEditorKit(jepEditor).insertHTML(parentEkit.getExtendedHtmlDoc(), pos, sbNew.toString(), 0, 0, baseTag);
					//insertHTML(jepEditor, parentEkit.getExtendedHtmlDoc(), pos, sbNew.toString(), 0, 0, baseTag);
				}
				parentEkit.refreshOnUpdate();
			}
			else
			{
				String sListType = (baseTag == HTML.Tag.OL ? "ol" : "ul");
				HTMLDocument htmlDoc = (HTMLDocument)(jepEditor.getDocument());
				int iStart = jepEditor.getSelectionStart();
				int iEnd   = jepEditor.getSelectionEnd();
				String selText = htmlDoc.getText(iStart, iEnd - iStart);
				StringBuffer sbNew = new StringBuffer();
				String sToken = ((selText.indexOf("\r") > -1) ? "\r" : "\n");
				StringTokenizer stTokenizer = new StringTokenizer(selText, sToken);
				sbNew.append("<" + sListType + ">");
				while(stTokenizer.hasMoreTokens())
				{
					sbNew.append("<li>");
					sbNew.append(stTokenizer.nextToken());
					sbNew.append("</li>");
				}
				sbNew.append("</" + sListType + "><p>&nbsp;</p>");
				htmlDoc.remove(iStart, iEnd - iStart);
				getHTMLEditorKit(jepEditor).insertHTML(htmlDoc, iStart, sbNew.toString(), 1, 1, null);
			}
		}
		catch (BadLocationException ble)
		{
			System.err.println("htmldoc length: " + parentEkit.getExtendedHtmlDoc().getLength());
			System.err.println("panedoc length: " + parentEkit.getTextPane().getDocument().getLength());
			System.err.println("text length: " + parentEkit.getTextPane().getText().length());
			System.err.println("wanted offest: " + ble.offsetRequested());
			parentEkit.logException("BadLocationException in ListAutomationAction.actionPerformed method", ble);
		}
		catch (IOException ioe)
		{
			parentEkit.logException("IOException in ListAutomationAction.actionPerformed method", ioe);
		}
	}
}