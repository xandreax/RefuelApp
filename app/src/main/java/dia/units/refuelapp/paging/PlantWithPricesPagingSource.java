package dia.units.refuelapp.paging;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.ListenableFuturePagingSource;
import androidx.paging.PagingState;

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import dia.units.refuelapp.model.PlantWithPrices;
/*
public class PlantWithPricesPagingSource extends ListenableFuturePagingSource<Integer, PlantWithPrices> {
    @Nullable
    @Override
    public Integer getRefreshKey(@NonNull PagingState<Integer, PlantWithPrices> pagingState) {
        // Try to find the page key of the closest page to anchorPosition, from
        // either the prevKey or the nextKey, but you need to handle nullability
        // here:
        //  * prevKey == null -> anchorPage is the first page.
        //  * nextKey == null -> anchorPage is the last page.
        //  * both prevKey and nextKey null -> anchorPage is the initial page, so
        //    just return null.
        Integer anchorPosition = pagingState.getAnchorPosition();
        if (anchorPosition == null) {
            return null;
        }
        LoadResult.Page<Integer, PlantWithPrices> anchorPage = pagingState.closestPageToPosition(anchorPosition);
        if (anchorPage == null) {
            return null;
        }
        Integer prevKey = anchorPage.getPrevKey();
        if (prevKey != null) {
            return prevKey + 1;
        }
        Integer nextKey = anchorPage.getNextKey();
        if (nextKey != null) {
            return nextKey - 1;
        }
        return null;
    }

    @NonNull
    @Override
    public ListenableFuture<LoadResult<Integer, PlantWithPrices>> loadFuture(@NonNull LoadParams<Integer> loadParams) {
        // Start refresh at page 1 if undefined.
        Integer nextPageNumber = loadParams.getKey();
        if (nextPageNumber == null) {
            nextPageNumber = 1;
        }
        ListenableFuture<LoadResult<Integer, User>> pageFuture =
                Futures.transform(mBackend.searchUsers(mQuery, nextPageNumber),
                        this::toLoadResult, mBgExecutor);

        ListenableFuture<LoadResult<Integer, User>> partialLoadResultFuture =
                Futures.catching(pageFuture, HttpException.class,
                        LoadResult.Error::new, mBgExecutor);

        return Futures.catching(partialLoadResultFuture,
                IOException.class, LoadResult.Error::new, mBgExecutor);

        return null;
    }

    private LoadResult<Integer, PlantWithPrices> toLoadResult(@NonNull SearchUserResponse response) {
        return new LoadResult.Page<>(response.getUsers(),
                null, // Only paging forward.
                response.getNextPageNumber(),
                LoadResult.Page.COUNT_UNDEFINED,
                LoadResult.Page.COUNT_UNDEFINED);
    }
}
*/