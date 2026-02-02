package de.danoeh.antennapod.core.bookmark;

import java.util.List;

import de.danoeh.antennapod.model.Bookmarks.Bookmark;

public interface BookmarkRepository {
    void addBookmark(Bookmark bookmark);
    List<Bookmark> getBookmarksForEpisode(long feedId, long feedItemId);
    List<Bookmark> getAllBookmarks();
    void deleteBookmark(Bookmark bookmark);
}
