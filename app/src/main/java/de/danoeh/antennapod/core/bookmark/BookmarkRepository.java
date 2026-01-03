package de.danoeh.antennapod.core.bookmark;

import java.util.List;

public interface BookmarkRepository {
    void addBookmark(Bookmark bookmark);
    List<Bookmark> getBookmarksForEpisode(long feedId, long feedItemId);
    void deleteBookmark(Bookmark bookmark);
}
