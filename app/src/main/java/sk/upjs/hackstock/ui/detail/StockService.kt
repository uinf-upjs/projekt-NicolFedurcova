package sk.upjs.hackstock.ui.detail

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface StockService {

    @GET("/api/v3/historical-price-full/{symbol}")
    suspend fun getHistoricalPrices(
        @Path("symbol") symbol: String,
        @Query("apikey") apikey: String): List<StockPrice>

    @GET("/api/v3/historical-chart/{timeframe}/{symbol}")
    suspend fun getIntradayPrices(
        @Path("timeframe") timeframe: String,
        @Path("symbol") symbol: String,
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("apikey") apikey: String
    ): List<StockPrice>

    @GET("/api/v3/analyst-stock-recommendations/{symbol}")
    suspend fun getRecomm(
        @Path("symbol") symbol: String,
        @Query("apikey") apikey: String
    ): List<StockRecomm>

    @GET("/api/v3/quote-order/{symbol}")
    suspend fun getInfo(
        @Path("symbol") symbol: String,
        @Query("apikey") apikey: String
    ): List<StockInfo>


}