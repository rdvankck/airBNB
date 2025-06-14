package com.emlakuygulamasi.model;

import java.util.List;
import java.util.Date;

public class Chat {
    private List<String> participants;
    private String listingId;
    private String lastMessage;
    private Date lastMessageTimestamp;

    public Chat() {}

    public Chat(List<String> participants) {
        this.participants = participants;
    }

    public List<String> getParticipants() { return participants; }
    public void setParticipants(List<String> participants) { this.participants = participants; }
    public String getListingId() { return listingId; }
    public void setListingId(String listingId) { this.listingId = listingId; }
    public String getLastMessage() { return lastMessage; }
    public void setLastMessage(String lastMessage) { this.lastMessage = lastMessage; }
    public Date getLastMessageTimestamp() { return lastMessageTimestamp; }
    public void setLastMessageTimestamp(Date lastMessageTimestamp) { this.lastMessageTimestamp = lastMessageTimestamp; }
} 