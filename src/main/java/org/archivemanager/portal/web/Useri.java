package org.archivemanager.portal.web;

import org.heed.openapps.User;
import java.util.ArrayList;
import java.util.List;
//import org.heed.openapps.entity.Entity;


public class Useri extends User{

  private int current;
  private List<Subject> subjects;

  public Useri(String username, String email){
    this.setUsername(username);
    this.setEmail(email);
    this.subjects = new ArrayList<Subject>();
  }

  public List<Subject> getSubjects(){
    return subjects;
  }

  //gets the position where the id is or returns -1
  public int getPosition(String id){
    int length = subjects.size();
    if(length == 0){
      return -1;
    }else{
      for(int i = 0; i < length; i++){
        if (subjects.get(i).getId().equals(id)) {
          return i;
        }
      }
      return -1;
    }
  }

  public Subject addSubject(String subject){
    int pos = getPosition(subject);
    if( pos == -1){
      subjects.add(new Subject(subject));
      current++;
      return subjects.get(current-1);
    }else{
      return subjects.get(pos);
    }
  }

  public Subject addItem(String subject, Object item){
    int pos = getPosition(subject);
    if(pos == -1){
      subjects.add(new Subject(subject));
      subjects.get(current).addItem(item);
      current++;
      return subjects.get(current-1);
    }else{
      subjects.get(pos).addItem(item);
      return subjects.get(pos);
    }
  }

  public Subject deleteItem(String subject, Object item){
    int pos = getPosition(subject);
    if(pos == -1){
      subjects.add(new Subject(subject));
      current++;
      return subjects.get(current-1);
    }else{
      subjects.get(pos).removeItem(item);
      return subjects.get(pos);
    }
  }

  public void deleteSubject(String subject){
    int pos = getPosition(subject);
    if(pos != -1){
      subjects.remove(subjects.get(pos));
      current--;
    }
  }

}
