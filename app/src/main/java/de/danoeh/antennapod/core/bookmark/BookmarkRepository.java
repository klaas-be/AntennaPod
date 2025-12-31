package de.danoeh.antennapod.core.bookmark;

import de.danoeh.antennapod.model.feed.Bookmark;
import java.util.List;

public interface BookmarkRepository {
    void addBookmark(Bookmark bookmark);
    List<Bookmark> getBookmarksForEpisode(long feedItemId);
    void deleteBookmark(Bookmark bookmark);
}
