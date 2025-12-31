package de.danoeh.antennapod.model.feed;

public class Bookmark {
    private final long feedItemId;
    private final long position; // in milliseconds
    private String title;

    public Bookmark(long feedItem, long position, String title) {

        this.feedItemId = feedItem;
        this.position = position;
        this.title = title;
    }

    public long getFeedItem() { return feedItemId; }
    public long getPosition() { return position; }
    public String getTitle() { return title; }
}
