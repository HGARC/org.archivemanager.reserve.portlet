package org.archivemanager.portal.web;

import java.util.ArrayList;
import java.util.List;

import org.heed.openapps.entity.Entity;

public class Subjects{

    int current;
    List<Subject> listSub;

    public Subjects(){
      int current = 0;
      listSub = new ArrayList<Subject>();
    }

    public List<Subject> getListSub(){
      return listSub;
    }

    //gets the position where the id is or returns -1
    public int getPosition(String id){
      int length = listSub.size();
      if(length == 0){
        return -1;
      }else{
        for(int i = 0; i < length; i++){
          if (listSub.get(i).getId().equals(id)) {
            return i;
          }
        }
        return -1;
      }
    }

    public Subject addSubject(String subject){
      int pos = getPosition(subject);
      if( pos == -1){
        listSub.add(new Subject(subject));
        current++;
        return listSub.get(current-1);
      }else{
        return listSub.get(pos);
      }
    }

    public Subject addItem(String subject, Entity item){
      int pos = getPosition(subject);
      if(pos == -1){
        listSub.add(new Subject(subject));
        listSub.get(current).addItem(item);
        current++;
        return listSub.get(current-1);
      }else{
        listSub.get(pos).addItem(item);
        return listSub.get(pos);
      }
    }

    public Subject deleteItem(String subject, Entity item){
      int pos = getPosition(subject);
      if(pos == -1){
        listSub.add(new Subject(subject));
        current++;
        return listSub.get(current-1);
      }else{
        listSub.get(pos).removeItem(item);
        return listSub.get(pos);
      }
    }

    public void deleteSubject(String subject){
      int pos = getPosition(subject);
      if(pos != -1){
        listSub.remove(listSub.get(pos));
        current--;
      }
    }



}
