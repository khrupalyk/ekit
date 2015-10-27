/*
GNU Lesser General Public License

FontSelectorDialog
Copyright (C) 2003 Howard Kistler

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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;

import com.hexidec.util.Translatrix;

/** Class for providing a dialog that lets the user specify values for tag attributes
  */
public class FontSelectorDialog extends JDialog implements ItemListener, PropertyChangeListener, WindowListener
{
	/** <code>serialVersionUID</code> */
	private static final long	serialVersionUID	= 422771137838485456L;

	private Vector vcFontnames = null;
	private JComboBox jcmbFontlist;
	private String fontName = new String();
	private JOptionPane jOptionPane;
	private JTextPane jtpFontPreview;
	private String defaultText;
	private final Object[]	buttonLabels	= {Translatrix.getTranslationString("DialogAccept"),
			Translatrix.getTranslationString("DialogCancel") };

	/**
	 * Creates and displays a FontSelectorDialog with an empty demo text.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param attribName
	 * @return the FontSelectorDialog instance
	 */
	public static FontSelectorDialog show(Window parent, String title, boolean bModal, String attribName)
	{
		return show(parent, title, bModal, attribName, "");
	}

	/**
	 * Creates and displays a FontSelectorDialog.
	 * @param parent the parent Frame or Dialog of the FontSelectorDialog
	 * @param title the dialog title
	 * @param bModal whether the dialog should be modal
	 * @param attribName
	 * @param demoText the displayed text for demonstrating the font
	 * @return the FontSelectorDialog instance
	 */
	public static FontSelectorDialog show(Window parent, String title, boolean bModal, String attribName, String demoText)
	{
		FontSelectorDialog me;
		if (parent instanceof Frame)
		{
			me = new FontSelectorDialog((Frame) parent, title, bModal, attribName, demoText);
		}
		else if (parent instanceof Dialog)
		{
			me = new FontSelectorDialog((Dialog) parent, title, bModal, attribName, demoText);
			me.pack();
			me.show();
		}
		else
		{
			throw new IllegalArgumentException("parent must be a Frame or a Dialog");
		}
		return me;
	}

	private FontSelectorDialog(Dialog parent, String title, boolean bModal, String attribName, String demoText)
	{
		super(parent, title, bModal);
		init(attribName, demoText);
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String, String)} instead
	 */
	// TODO reduce visibility to private in next release
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName, String demoText)
	{
		super(parent, title, bModal);
		init(attribName, demoText);
		pack();
		show();
	}

	private void init(String attribName, String demoText)
	{
		if(demoText != null && demoText.length() > 0)
		{
			if(demoText.length() > 24)
			{
				defaultText = demoText.substring(0, 24);
			}
			else
			{
				defaultText = demoText;
			}
		}
		else
		{
			defaultText = "aAbBcCdDeEfFgGhH,.0123";
		}

		/* Obtain available fonts */
		String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
		vcFontnames = new Vector(fonts.length - 5);
		for(int i = 0; i < fonts.length; i++)
		{
			if(!fonts[i].equals("Dialog") && !fonts[i].equals("DialogInput") && !fonts[i].equals("Monospaced") && !fonts[i].equals("SansSerif") && !fonts[i].equals("Serif"))
			{
				vcFontnames.add(fonts[i]);
			}
		}
		jcmbFontlist = new JComboBox(vcFontnames);
		jcmbFontlist.addItemListener(this);

		jtpFontPreview = new JTextPane();
		final HTMLEditorKit kitFontPreview = new HTMLEditorKit();
		final HTMLDocument docFontPreview = (HTMLDocument)(kitFontPreview.createDefaultDocument());
		jtpFontPreview.setEditorKit(kitFontPreview);
		jtpFontPreview.setDocument(docFontPreview);
		jtpFontPreview.setMargin(new Insets(4, 4, 4, 4));
		jtpFontPreview.setBounds(0, 0, 120, 18);
		jtpFontPreview.setText(getFontSampleString(defaultText));
		Object[] panelContents = { attribName, jcmbFontlist, Translatrix.getTranslationString("FontSample"), jtpFontPreview };

		jOptionPane = new JOptionPane(panelContents, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, buttonLabels, buttonLabels[0]);
		setContentPane(jOptionPane);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		addWindowListener(this);

		jOptionPane.addPropertyChangeListener(this);
	}

	/** {@inheritDoc} */
	public void propertyChange(PropertyChangeEvent e)
	{
		String prop = e.getPropertyName();
		if(isVisible()
			&& (e.getSource() == jOptionPane)
			&& (prop.equals(JOptionPane.VALUE_PROPERTY) || prop.equals(JOptionPane.INPUT_VALUE_PROPERTY)))
		{
			Object value = jOptionPane.getValue();
			if(value == JOptionPane.UNINITIALIZED_VALUE)
			{
				return;
			}
			jOptionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			if(value.equals(buttonLabels[0]))
			{
				fontName = (String)(jcmbFontlist.getSelectedItem());
				setVisible(false);
			}
			else
			{
				fontName = null;
				setVisible(false);
			}
		}
	}

	/* ItemListener method */
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getStateChange() == ItemEvent.SELECTED)
		{
			jtpFontPreview.setText(getFontSampleString(defaultText));
		}
	}

	/**
	 * @deprecated use {@link #show(Window, String, boolean, String)} instead
	 */
	// TODO reduce visibility to private in next release
	public FontSelectorDialog(Frame parent, String title, boolean bModal, String attribName)
	{
		this(parent, title, bModal, attribName, "");
	}

	public String getFontName()
	{
		return fontName;
	}

	private String getFontSampleString(String demoText)
	{
		return "<HTML><BODY><FONT FACE=" + '"' + jcmbFontlist.getSelectedItem() + '"' + ">" + demoText + "</FONT></BODY></HTML>";
	}

	/** {@inheritDoc} */
	public void windowClosing(WindowEvent we)
	{
		jOptionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
	}

	/** {@inheritDoc} */
	public void windowActivated(WindowEvent e) {}

	/** {@inheritDoc} */
	public void windowClosed(WindowEvent e) {}

	/** {@inheritDoc} */
	public void windowDeactivated(WindowEvent e) {}

	/** {@inheritDoc} */
	public void windowDeiconified(WindowEvent e) {}

	/** {@inheritDoc} */
	public void windowIconified(WindowEvent e) {}

	/** {@inheritDoc} */
	public void windowOpened(WindowEvent e) {}

}

