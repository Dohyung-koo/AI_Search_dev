package com.diquest.openapi.videopotal;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.diquest.openapi.util.OpenAPIErrorResponse;

@XmlAccessorType(XmlAccessType.FIELD)
public class Section_List {
	
	@XmlElement(name = "section")
	private List<Xml_Section> xml_section;
	
	public List<Xml_Section> getXml_section() {
		return xml_section;
	}

	public void setXml_section(List<Xml_Section> xml_section) {
		this.xml_section = xml_section;
	}

}
	
	