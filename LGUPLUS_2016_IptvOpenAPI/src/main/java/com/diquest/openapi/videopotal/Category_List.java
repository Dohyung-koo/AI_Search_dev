package com.diquest.openapi.videopotal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Category_List {
	
	@XmlElement(name = "category")
	private List<Xml_Category> xml_category;

	public List<Xml_Category> getXml_category() {
		return xml_category;
	}

	public void setXml_category(List<Xml_Category> xml_category) {
		this.xml_category = xml_category;
	}
	
	
}
	
	