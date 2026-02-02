package de.danoeh.antennapod.ui.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.MaterialToolbar;
import de.danoeh.antennapod.R;
import de.danoeh.antennapod.activity.MainActivity;
import de.danoeh.antennapod.model.Bookmarks.Bookmark;
import de.danoeh.antennapod.core.bookmark.BookmarkManager;
import de.danoeh.antennapod.model.playback.MediaType;
import de.danoeh.antennapod.playback.service.PlaybackService;
import de.danoeh.antennapod.playback.service.PlaybackServiceStarter;
import de.danoeh.antennapod.storage.database.DBReader;
import de.danoeh.antennapod.ui.common.Converter;
import de.danoeh.antennapod.ui.view.EmptyViewHandler;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class BookmarksFragment extends Fragment {
    public static final String TAG = "BookmarksFragment";
    private RecyclerView recyclerView;
    private BookmarkListAdapter adapter;
    private EmptyViewHandler emptyView;
    private Disposable disposable;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.bookmarks_fragment, container, false);
        MaterialToolbar toolbar = root.findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> getParentFragmentManager().popBackStack());
        toolbar.setTitle(R.string.bookmarks_label);

        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new BookmarkListAdapter();
        recyclerView.setAdapter(adapter);

        emptyView = new EmptyViewHandler(getContext());
        emptyView.setTitle(R.string.bookmarks_label);
        emptyView.setMessage(R.string.no_bookmarks_label);
        emptyView.setIcon(R.drawable.ic_tag);
        emptyView.attachToRecyclerView(recyclerView); // Add this line

        loadBookmarks();
        return root;
    }

    private void loadBookmarks() {
        disposable = Observable.fromCallable(() -> BookmarkManager.getInstance().getAllBookmarks())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bookmarks -> {
                    adapter.setBookmarks(bookmarks);
                    emptyView.updateVisibility();
                }, Throwable::printStackTrace);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (disposable != null) {
            disposable.dispose();
        }
    }

    private class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkViewHolder> {
        private List<Bookmark> bookmarks = new ArrayList<>();

        public void setBookmarks(List<Bookmark> bookmarks) {
            this.bookmarks = bookmarks;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new BookmarkViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.bookmark_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
            holder.bind(bookmarks.get(position));
        }

        @Override
        public int getItemCount() {
            return bookmarks.size();
        }
    }

    private class BookmarkViewHolder extends RecyclerView.ViewHolder {
        private final TextView episodeTitle;
        private final TextView bookmarkTitle;
        private final TextView bookmarkPosition;
        private Bookmark bookmark;

        public BookmarkViewHolder(@NonNull View itemView) {
            super(itemView);
            episodeTitle = itemView.findViewById(R.id.episodeTitle);
            bookmarkTitle = itemView.findViewById(R.id.bookmarkTitle);
            bookmarkPosition = itemView.findViewById(R.id.bookmarkPosition);
            itemView.setOnClickListener(v -> {
                if (bookmark != null && getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.loadFeedFragmentById(bookmark.getFeed(), null);
                    
                    Observable.fromCallable(() -> DBReader.getFeedItem(bookmark.getFeedItem()))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(item -> {
                              //  mainActivity.get getMediaController().playMediaObject(item.getMedia(), false, true, true);
                                var media = item.getMedia();
                                var context = itemView.getContext();

                                new PlaybackServiceStarter(context, media)
                                        .callEvenIfRunning(true)
                                        .start();

                                if (media.getMediaType() == MediaType.VIDEO) {
                                    context.startActivity(PlaybackService.getPlayerActivityIntent(context, media));
                                }             //  mainActivity.getPlaybackController().seekTo((int) bookmark.getPosition());
                            }, Throwable::printStackTrace);
                }
            });
        }

        public void bind(Bookmark bookmark) {
            this.bookmark = bookmark;
            bookmarkTitle.setText(bookmark.getTitle());
            bookmarkPosition.setText(Converter.getDurationStringLong((int) bookmark.getPosition()));
            
            // Loading episode title might be slow if done here, but let's try for now.
            // Ideally it should be part of the Bookmark object or pre-loaded.
            Observable.fromCallable(() -> DBReader.getFeedItem(bookmark.getFeedItem()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(item -> episodeTitle.setText(item.getTitle()), Throwable::printStackTrace);
        }
    }
}