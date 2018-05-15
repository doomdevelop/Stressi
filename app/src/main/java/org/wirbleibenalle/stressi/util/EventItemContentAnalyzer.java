package org.wirbleibenalle.stressi.util;

import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.wirbleibenalle.stressi.ui.model.EventItem;

public class EventItemContentAnalyzer {

    public String createShortDescription(EventItem eventItem) {
        String description = eventItem.getDescription();
        String[] descriptionArr = description.split(" ");
        int length = 0;
        StringBuilder descriptionShort = new StringBuilder();
        for (String word : descriptionArr) {
            if (word == null || word.isEmpty()) {
                continue;
            }
            if (length + word.length() < 35) {
                descriptionShort.append(word).append(" ");
                length = descriptionShort.length();
            } else {
                descriptionShort.append(Constants.DOTS);
                break;
            }
        }
        return descriptionShort.toString();
    }

    public DateTime createEventDateTime(EventItem eventItem) {
        LocalTime localTime = new LocalTime();
        String[] hourMin = eventItem.getTime().split(":");
        localTime.hourOfDay().setCopy(Integer.valueOf(hourMin[0]));
        localTime.minuteOfHour().setCopy(Integer.valueOf(hourMin[1]));
        localTime.secondOfMinute().setCopy(0);

        return eventItem.getLocalDate().toDateTime(localTime);
    }

    public String createShareEventSubject(EventItem eventItem, String shortDescription) {
        return eventItem.getPlace() + ": " + shortDescription;
    }

    public String createShareEventTextContent(EventItem eventItem) {
        StringBuilder textBuilder = new StringBuilder(eventItem.getPlace());

        if (!eventItem.getPlace().equals(eventItem.getAddress())) {
            textBuilder.append("\n").append(eventItem.getAddress());
        }
        textBuilder.append("\n\n").append(DateUtil.formatDateForTitle(eventItem.getLocalDate()))
            .append(" ").append(eventItem.getTime());
        textBuilder.append("\n\n").append(eventItem.getDescription())
            .append("\n\n").append(Constants.FULL_URL).append(eventItem.getLocalDate().toString());
        return textBuilder.toString();
    }
}
