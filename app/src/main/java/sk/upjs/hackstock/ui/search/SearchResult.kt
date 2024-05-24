package sk.upjs.hackstock.ui.search

import io.ktor.client.*
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class SearchResult(val name: String, val symbol: String)

class SearchPage {
    var searchTerm: String = ""
    var searchResults: List<SearchResult> = listOf()
    private var isLoading: Boolean = false
    private var error: String = ""
    private val API_KEY = "iEDFPAm1KWW43YDJxgIEW3aFkVpdLzfr" // Insert API here

    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json {
                ignoreUnknownKeys = true
            })
        }
    }

    private val yourData = SearchResult("Company1", "Symbol1")


    fun handleResultClick(name: String, symbol: String) {
        // Navigate to TradingPage with state from the search results
        // This will depend on your navigation implementation
    }

    fun handleSearch() = runBlocking {
        launch {
            if (searchTerm.isEmpty()) return@launch // Prevent search with empty query

            isLoading = true
            error = ""
            searchResults = listOf() // Reset previous results


            try {
                val response: List<SearchResult> = client.request {
                    url("https://financialmodelingprep.com/api/v3/search")
                    method = HttpMethod.Get
                    parameter("query", searchTerm)
                    parameter("limit", 10)
                    parameter("exchange", "NASDAQ")
                    parameter("apikey", API_KEY)
                    contentType(ContentType.Application.Json)
                }
                System.out.println(response.toString())
                if (response.isNotEmpty()) {
                    searchResults = response
                } else {
                    error = "No results found."
                }
            } catch (err: Exception) {
                println("Error fetching data: $err")
                error = "Failed to fetch data. Please try again later."
            } finally {
                isLoading = false
            }
        }
    }
}