package org.archivemanager.portal.web;

import org.heed.openapps.User;
import java.util.ArrayList;
import java.util.List;
import org.heed.openapps.entity.Entity;


public class Useri extends User{

  private Subjects subjects;

  public Useri(String username, String email){
    this.setUsername(username);
    this.setEmail(email);
    this.subjects = new Subjects();
  }

  public Subjects getSubjects(){
    return subjects;
  }

  public Subject addSubject(String subject){
    return this.subjects.addSubject(subject);
  }

  public Subject addItem(String subject, Entity item){
    return this.subjects.addItem(subject,item);
  }

  public Subject deleteItem(String subject, Entity item){
    return this.subjects.deleteItem(subject,item);
  }

  public void deleteSubject(String subject){
    this.subjects.deleteSubject(subject);
  }

}
