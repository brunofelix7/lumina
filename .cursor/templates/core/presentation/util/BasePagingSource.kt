package <basePackage>.presentation.util

import androidx.paging.PagingSource
import androidx.paging.PagingState
import <basePackage>.domain.util.Resource
import <basePackage>.domain.util.fold

private const val FIRST_PAGE = 1
private const val DEFAULT_PAGE_SIZE = 20

/**
 * Generic [PagingSource] that loads pages through a [fetch] lambda returning a [Resource].
 *
 * A page smaller than [pageSize] is treated as the last one.
 *
 * @param T The type of the paged items.
 * @param pageSize The expected number of items per page.
 * @param fetch Loads the given 1-based page, usually by calling a use case.
 */
class BasePagingSource<T : Any>(
    private val pageSize: Int = DEFAULT_PAGE_SIZE,
    private val fetch: suspend (page: Int) -> Resource<List<T>>
) : PagingSource<Int, T>() {

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { position ->
            val anchorPage = state.closestPageToPosition(position)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        val page = params.key ?: FIRST_PAGE
        return fetch(page).fold(
            onSuccess = { data ->
                LoadResult.Page(
                    data = data,
                    prevKey = if (page == FIRST_PAGE) null else page - 1,
                    nextKey = if (data.size < pageSize) null else page + 1
                )
            },
            onFailure = { throwable -> LoadResult.Error(throwable) }
        )
    }
}
