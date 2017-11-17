package org.archivemanager.portal.web;

import java.util.ArrayList;
import java.util.List;

import org.heed.openapps.entity.Entity;

public class Subject{

  private String id;
  private List<Entity> list = new ArrayList<Entity>();


  public Subject(String id){
    this.id = id;
  }

  public String getId(){
    return this.id;
  }

  public List<Entity> getList(){
    return list;
  }

  public void addItem(Entity item){
    if(!list.contains(item)){
      list.add(item);
    }
  }

  public void removeItem(Entity item){
    list.remove(item);
  }

}
