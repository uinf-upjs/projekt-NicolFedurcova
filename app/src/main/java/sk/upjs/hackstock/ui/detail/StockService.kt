package sk.upjs.hackstock.ui.detail

import retrofit2.http.GET
import retrofit2.http.Path

interface StockService {

    @GET("/api/v3/historical-price-full/{symbol}")
    suspend fun getHistoricalPrices(@Path("symbol") symbol: String): List<StockPrice>
}