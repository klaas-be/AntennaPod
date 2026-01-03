package de.danoeh.antennapod.core.bookmark;

import java.util.List;

public class BookmarkManager {
    private static BookmarkManager instance;
    private final BookmarkRepository repository;

    private BookmarkManager() {
        // LATER: Change this to new DatabaseBookmarkRepository()
        this.repository = new de.danoeh.antennapod.core.bookmark.DatabaseBookmarkRepository();
    }

    public static synchronized BookmarkManager getInstance() {
        if (instance == null) {
            instance = new BookmarkManager();
        }
        return instance;
    }

    public void addBookmark(long feedId, long feedItemId, long position) {
        // We can add validation logic here (e.g. don't add duplicate timestamps)
        Bookmark newBookmark = new Bookmark(feedId, feedItemId, position, "Bookmark");
        repository.addBookmark(newBookmark);
    }

    public List<Bookmark> getBookmarks(long feedId, long feedItemId) {
        return repository.getBookmarksForEpisode(feedId, feedItemId);
    }
}
