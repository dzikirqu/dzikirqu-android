package com.wagyufari.dzikirqu.data.paging

//class FlyerPagingDataSource(private val api: ApiService, val mContext: Context) :
//    PagingSource<Int, Flyer>() {
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Flyer> {
//        val lastSkip = params.key
//        return try {
//            val response = api.getFlyers(Prefs.language, 15, lastSkip)
//            LoadResult.Page(
//                data = response.data.orEmpty(),
//                prevKey = null,
//                nextKey = if (response.data?.isEmpty() == true) null else response.pagination?.last_skip
//            )
//        } catch (e: Exception) {
//            LoadResult.Error(e)
//        }
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, Flyer>): Int? {
//        return null
//    }
//}