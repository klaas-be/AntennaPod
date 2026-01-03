package de.danoeh.antennapod.core.bookmark;

public class Bookmark {
    private final long feedItemId;
    private final long position; // in milliseconds
    private final long feedId;
    private String title;

    public Bookmark(long feed, long feedItem, long position, String title) {

        this.feedId = feed;
        this.feedItemId = feedItem;
        this.position = position;
        this.title = title;
    }

    public long getFeedItem() { return feedItemId; }
    public long getPosition() { return position; }
    public String getTitle() { return title; }

    public long getFeed() {
        return this.feedId;
    }
}
