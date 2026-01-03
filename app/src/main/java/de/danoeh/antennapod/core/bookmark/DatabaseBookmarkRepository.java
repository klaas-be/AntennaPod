package de.danoeh.antennapod.core.bookmark;

import java.util.ArrayList;
import java.util.List;

import de.danoeh.antennapod.storage.database.PodDBAdapter;

public class DatabaseBookmarkRepository implements BookmarkRepository {

    @Override
    public void addBookmark(Bookmark bookmark) {
        PodDBAdapter adapter = PodDBAdapter.getInstance();
        if (adapter != null) {
            // Note: getDatabase() returns the open database instance managed by PodDBAdapter
            adapter.addBookmark(bookmark.getFeed(), bookmark.getFeedItem(), bookmark.getPosition(), bookmark.getTitle());
        }
    }

    @Override
    public List<Bookmark> getBookmarksForEpisode(long feedId, long feedItemId) {
        List<Bookmark> bookmarks = new ArrayList<>();
        PodDBAdapter adapter = PodDBAdapter.getInstance();
        if (adapter != null) {
            var info = adapter.getBookmarkForEpisode(feedId, feedItemId);
            if (info == null) {
                return bookmarks;
            }
            for (var entry : info.entrySet()) {
                bookmarks.add(new Bookmark(feedId, feedItemId, entry.getKey(), entry.getValue()));
            }

        }
        return bookmarks;
    }

    @Override
    public void deleteBookmark(Bookmark bookmark) {
        PodDBAdapter adapter = PodDBAdapter.getInstance();
        if (adapter != null) {
            adapter.deleteBookmark(bookmark.getFeed(), bookmark.getFeedItem(), bookmark.getPosition()) ;
        }
    }
}
