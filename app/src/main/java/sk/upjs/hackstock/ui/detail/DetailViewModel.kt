package sk.upjs.hackstock.ui.detail
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sk.upjs.hackstock.entities.Share
import sk.upjs.hackstock.repositories.AppRepository
import java.text.SimpleDateFormat
import java.util.Date

class DetailViewModel( private val appRepository: AppRepository,  private val currentShare: Share) : ViewModel() {

    val name = currentShare.company
    private val _text = MutableLiveData<List<Pair<String,Float>>>()
    val text: LiveData<List<Pair<String,Float>>> = _text
    private val API_KEY = "5GQf4pHtMpmFCDB0iPVyiCQoDWwaG1cq"
    private val _info = MutableLiveData<String>()
    val info: LiveData<String> = _info
    private val _sp = MutableLiveData<Double>()
    val stockPriceOfCurrentShare: LiveData<Double> = _sp

    init {
        fetchData()
    }

    private fun fetchData() {
        val coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
            try {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://financialmodelingprep.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val service = retrofit.create(StockService::class.java)

                val symbol = currentShare.shortname  // Replace with desired symbol
                val timeframe = "1hour"  // Change to desired timeframe (1min, 15min, etc.)

                val today = Date() // Get today's date
                val yesterday = Date(today.time - 7*( 24 * 60 * 60 * 1000)) // 7 day's ago date (assuming you want last 60 minutes)

                val fromDate = yesterday.formatDate()
                val toDate = today.formatDate()  // Format dates as YYYY-MM-DD
                Log.e("DATE", fromDate)
                Log.e("DATE", toDate)


                val chartData = service.getIntradayPrices(timeframe, symbol, fromDate, toDate, API_KEY)
                Log.e("DATE", chartData.toString())
                val prices = processPriceData(chartData)

                //val recommendations = service.getRecomm(symbol, API_KEY)
                val infos = service.getInfo(symbol,API_KEY)
                var result = ""
                if(!infos.isEmpty()){
                    _sp.postValue(infos.get(0).price)
                    result = result + "  Current price: " + infos.get(0).price +
                            " \n Percentage change: " + infos.get(0).changesPercentage +
                            " \n Day Low: " + infos.get(0).dayLow +
                            " \n Day High: " + infos.get(0).dayHigh+
                            " \n Price average 50: " + infos.get(0).priceAvg50 +
                            " \n My amount: " + currentShare.amount +
                            " \n My profit so far: " + currentShare.price
                }
//                if(!recommendations.isEmpty()){
//                    result = result + "Analyst Ratings Buy: " + recommendations.get(0).analystRatingsbuy +
//                            "\n Analyst Ratings Hold: " + recommendations.get(0).analystRatingsHold +
//                            "\n Analyst Ratings Sell: " + recommendations.get(0).analystRatingsSell
//                }

                Log.e("RESS", result)
                _info.postValue(result)


                _text.postValue(prices)
            } catch (e: Exception) {
                Log.e("API HISTORICAL", "Historical data of stock weren't supplied")
            }
        }
    }

    private fun Date.formatDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")

        return formatter.format(this)

    }

    private fun processPriceData(prices: List<StockPrice>): List<Pair<String,Float>> {
        val list = mutableListOf<Pair<String,Float>>()
        for (price in prices) {
            val highPrice = price.high
            val date = price.date
            list.add(Pair(date, highPrice.toFloat()))
            // Use the price data as needed in your app (e.g., display in a chart)
        }

        return list
    }

    class DetailViewModelFactory(
        private val appRepository: AppRepository,
        private val currentShare: Share
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return DetailViewModel(appRepository, currentShare) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
