package org.archivemanager.portal.web;

import java.util.ArrayList;
import java.util.List;

public class Subject{

  private String id;
  private List<Object> list = new ArrayList<Object>();


  public Subject(String id){
    this.id = id;
  }

  public String getId(){
    return this.id;
  }

  public List<Object> getList(){
    return list;
  }

  public void addItem(Object item){
    if(!list.contains(item)){
      list.add(item);
    }
  }

  public void removeItem(Object item){
    list.remove(item);
  }

}
