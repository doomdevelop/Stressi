package org.wirbleibenalle.stressi.util;

import org.wirbleibenalle.stressi.ui.model.EventItem;

public class EventItemContentAnalyzer {

    public static String createShortDescription(EventItem eventItem){
        String description = eventItem.getDescription();
        String[] descriptionArr = description.split(" ");
        int length = 0;
        String descriptionShort = null;
        for(String word : descriptionArr){
            if(word == null || word.length()==0){
                continue;
            }
            if(descriptionShort == null){
                descriptionShort = word+" ";
                length = descriptionShort.length();
            }else if(length+word.length()<35){
                descriptionShort+=word+" ";
                length = descriptionShort.length();
            }else{
                descriptionShort+= Constants.DOTS;
                break;
            }
        }
        return  descriptionShort;
    }
}
