package de.danoeh.antennapod.core.bookmark;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import de.danoeh.antennapod.model.Bookmarks.Bookmark;

public class InMemoryBookmarkRepository implements BookmarkRepository {
    private final List<Bookmark> bookmarks = new ArrayList<>();

    @Override
    public void addBookmark(Bookmark bookmark) {
        bookmarks.add(bookmark);
        System.out.println("DEBUG: Added " + bookmark.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public List<Bookmark> getBookmarksForEpisode(long feedId, long feedItemId) {
        // Filter the list for the specific episode
        return bookmarks.stream()
                .filter(b -> b.getFeedItem() == feedItemId)
                .collect(Collectors.toList());
    }

    @Override
    public List<Bookmark> getAllBookmarks() {
        return new ArrayList<>(bookmarks);
    }

    @Override
    public void deleteBookmark(Bookmark bookmark) {
        bookmarks.remove(bookmark);
    }
}
