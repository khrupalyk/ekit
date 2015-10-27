/*
GNU Lesser General Public License

EkitApplet - Java Swing HTML Editor & Viewer Applet
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

package com.hexidec.ekit;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import javax.swing.JApplet;

/** EkitApplet
  * Applet for editing and saving HTML in a Java browser component
  *
  * REQUIREMENTS
  * Java 2 (JDK 1.3 or 1.4)
  * Swing Library
  *
  * @authors Howard Kistler, Yaodong Liu, Gyoergy Magoss, Oliver Moser, Michael Goldberg, Cecile Rostaing, Thomas
  *          Gauweiler, Frits Jalvingh, Jerry Pommer, Ruud Noordermeer, Mindaugas Idzelis, Raymond Penners, Steve
  *          Birmingham, Rafael Cieplinski, Nico Mack, Michael Pearce, Murray Altheim, Mattias Malmgren, Maciej
  *          Kubicki, Elisabeth Novotny, Christoph Wei&szlig;enborn
  * @version 1.4
  */

public class EkitApplet extends JApplet
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= -5329967345355364652L;

	/* Components */
	EkitCore ekitCore;

	/** Constructor
	  */
	public EkitApplet()
	{
		getRootPane().putClientProperty("defeatSystemEventQueueCheck", Boolean.TRUE);
	}

	private static boolean isTrue(String value, boolean defaultValue) {
		if (value != null) {
			return Boolean.TRUE.toString().equalsIgnoreCase(value);
		}
		return defaultValue;
	}

	/** Applet Init
	  */
	public void init()
	{
		String sRawDocument = this.getParameter("DOCUMENT");
		String sStyleSheetRef = this.getParameter("STYLESHEET");
		boolean base64 = isTrue(getParameter("BASE64"), false);
		URL urlCSS = null;
		try
		{
			if (sStyleSheetRef != null && sStyleSheetRef.length() > 0)
			{
				urlCSS = new URL(this.getCodeBase(), sStyleSheetRef);
			}
		}
		catch (MalformedURLException murle)
		{
			murle.printStackTrace(System.err);
		}
		boolean showToolBarMulti = isTrue(getParameter("TOOLBARMULTI"), true);
		boolean showToolBar = showToolBarMulti || isTrue(getParameter("TOOLBAR"), true);
		boolean showViewSource = isTrue(getParameter("SOURCEVIEW"), false);
		String sLanguage = this.getParameter("LANGCODE");
		String sCountry = this.getParameter("LANGCOUNTRY");
		boolean editModeExclusive = isTrue(getParameter("EXCLUSIVE"), true);
		boolean showMenuIcons = isTrue(getParameter("MENUICONS"), true);
		boolean spellChecker = isTrue(getParameter("SPELLCHECK"), false);
		String toolbarSeq = getParameter("TOOLBARSEQ");
		if (toolbarSeq != null) {
			toolbarSeq = toolbarSeq.toUpperCase();
		} else {
			toolbarSeq = (showToolBarMulti ? EkitCore.TOOLBAR_DEFAULT_MULTI : EkitCore.TOOLBAR_DEFAULT_SINGLE);
		}

		if (spellChecker)
		{
			ekitCore = new EkitCoreSpell(sRawDocument, urlCSS, showToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, showToolBarMulti, toolbarSeq);
		}
		else
		{
			ekitCore = new EkitCore(sRawDocument, urlCSS, showToolBar, showViewSource, showMenuIcons, editModeExclusive, sLanguage, sCountry, base64, false, showToolBarMulti, toolbarSeq);
		}

		/* Add menus, based on whether or not they are requested (all are shown by default) */
		Vector vcMenus = new Vector();
		if(isTrue(getParameter("MENU_EDIT"), true)) { vcMenus.add(EkitCore.KEY_MENU_EDIT); }
		if(isTrue(getParameter("MENU_VIEW"), true)) { vcMenus.add(EkitCore.KEY_MENU_VIEW); }
		if(isTrue(getParameter("MENU_FONT"), true)) { vcMenus.add(EkitCore.KEY_MENU_FONT); }
		if(isTrue(getParameter("MENU_FORMAT"), true)) { vcMenus.add(EkitCore.KEY_MENU_FORMAT); }
		if(isTrue(getParameter("MENU_INSERT"), true)) { vcMenus.add(EkitCore.KEY_MENU_INSERT); }
		if(isTrue(getParameter("MENU_TABLE"), true)) { vcMenus.add(EkitCore.KEY_MENU_TABLE); }
		if(isTrue(getParameter("MENU_FORMS"), true)) { vcMenus.add(EkitCore.KEY_MENU_FORMS); }
		if(isTrue(getParameter("MENU_SEARCH"), true)) { vcMenus.add(EkitCore.KEY_MENU_SEARCH); }
		if(isTrue(getParameter("MENU_TOOLS"), true)) { vcMenus.add(EkitCore.KEY_MENU_TOOLS); }
		if(isTrue(getParameter("MENU_HELP"), true)) { vcMenus.add(EkitCore.KEY_MENU_HELP); }
		this.setJMenuBar(ekitCore.getCustomMenuBar(vcMenus));

		/* Add the components to the app */
		if(showToolBar)
		{
			final Container contentPane = getContentPane();
			if(showToolBarMulti)
			{
				contentPane.setLayout(new GridBagLayout());
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.fill       = GridBagConstraints.HORIZONTAL;
				gbc.anchor     = GridBagConstraints.NORTH;
				gbc.gridheight = 1;
				gbc.gridwidth  = 1;
				gbc.weightx    = 1.0;
				gbc.weighty    = 0.0;
				gbc.gridx      = 1;

				ekitCore.initializeMultiToolbars(toolbarSeq);

				gbc.gridy = 1;
				contentPane.add(ekitCore.getToolBarMain(showToolBar && ekitCore.getToolBarMain(showToolBar).getComponentCount() > 0), gbc);

				gbc.gridy = 2;
				contentPane.add(ekitCore.getToolBarFormat(showToolBar && ekitCore.getToolBarFormat(showToolBar).getComponentCount() > 0), gbc);

				gbc.gridy = 3;
				contentPane.add(ekitCore.getToolBarStyles(showToolBar && ekitCore.getToolBarStyles(showToolBar).getComponentCount() > 0), gbc);

				gbc.anchor     = GridBagConstraints.CENTER;
				gbc.fill       = GridBagConstraints.BOTH;
				gbc.weighty    = 1.0;
				gbc.gridy      = 4;
				contentPane.add(ekitCore, gbc);
			}
			else
			{
				ekitCore.initializeSingleToolbar(toolbarSeq);

				contentPane.setLayout(new BorderLayout());
				contentPane.add(ekitCore, BorderLayout.CENTER);
				contentPane.add(ekitCore.getToolBar(showToolBar), BorderLayout.NORTH);
			}
		}
	}

	/* Applet methods */
	public void start()   { }
	public void stop()    { }
	public void destroy() { }

	/** Method for passing back the document text to the applet's container.
	  * This is the entire document, including the top-level HTML tags.
	  */
	public String getDocumentText()
	{
		return ekitCore.getDocumentText();
	}

	/** Method for passing back the document body to the applet's container.
	  * This is only the text contained within the BODY tags.
	  */
	public String getDocumentBody()
	{
		return ekitCore.getDocumentSubText("body");
	}

	/** Method for setting the document manually.
	  * Will need code in the web page to call this.
	  */
	public void setDocumentText(String text)
	{
		ekitCore.setDocumentText(text);
	}
}
