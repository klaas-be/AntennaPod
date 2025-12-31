package de.danoeh.antennapod.core.bookmark;

import de.danoeh.antennapod.model.feed.Bookmark;
import java.util.List;

public class BookmarkManager {
    private static BookmarkManager instance;
    private final BookmarkRepository repository;

    private BookmarkManager() {
        // LATER: Change this to new DatabaseBookmarkRepository()
        this.repository = new de.danoeh.antennapod.core.bookmark.InMemoryBookmarkRepository();
    }

    public static synchronized BookmarkManager getInstance() {
        if (instance == null) {
            instance = new BookmarkManager();
        }
        return instance;
    }

    public void addBookmark(long feedItemId, long position) {
        // We can add validation logic here (e.g. don't add duplicate timestamps)
        Bookmark newBookmark = new Bookmark(feedItemId, position, "Bookmark");
        repository.addBookmark(newBookmark);
    }

    public List<Bookmark> getBookmarks(long feedItemId) {
        return repository.getBookmarksForEpisode(feedItemId);
    }
}
