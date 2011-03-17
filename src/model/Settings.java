package model;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Item;

// This singleton class contains the settings and the corresponding form items

public class Settings {
	
	// Standard Singleton pattern used here.
	
	private static Settings _instance = null;
	
	private Settings() {
		
	}
	
	public static Settings getInstance() {
		if (null == _instance){
			_instance = new Settings();
		}
		return _instance;
	}

	
	// Settings specific to this exercise starts here
	private Item[] m_formItems = null;
	
	public Item[] getItems() {
		if (null == m_formItems) {
			m_formItems = new Item[] {
				createColorMixTypeItem()
			};
		}
		return m_formItems;
	}
	
	// Select if we use additive or subtractive colors
	private ChoiceGroup m_colorMixType = null;
	
	private Item createColorMixTypeItem(){
		m_colorMixType = new ChoiceGroup("Color Mixing Type", ChoiceGroup.EXCLUSIVE);
		
		m_colorMixType.append("Additive Colors", null);
		m_colorMixType.append("Subtractive Colors", null);
		
		return (Item)m_colorMixType;
	}
	
	public int getColorMixType() {
		return m_colorMixType.getSelectedIndex();
	}
}
