package com.ecams.common.base;

import java.util.*;
import org.jdom.*;
import org.jdom.input.*;
import org.jdom.output.*;
import org.jdom.input.SAXBuilder;

public class JDOMReadFile {

  public Document make(HashMap hm) throws Exception {
    
    Element data = new Element("data");

    Element element = new Element("item");
    Set set = hm.keySet();
    Object []items = set.toArray();
    for(int i = 0; i < items.length; i++) {
      String name = (String) items[i]; 
      String value = (String) hm.get(name);

      addElement(element,name,value);
    }

    data.addContent(element);

    Document document = new Document(data);

    return document;
  }

  public Element addElement(Element parent, String name, String value) {
    Element element = new Element(name);
    element.setText(value);
    parent.addContent(element);
    return parent;
  }

  public void addAttribute(Element element, String name, String value){
    Attribute attribute = new Attribute(name,value);
    element.setAttribute(attribute);
  }


}