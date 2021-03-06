/*
 *  Copyright (c) 2020 Private Internet Access, Inc.
 *
 *  This file is part of the Private Internet Access Mobile Client.
 *
 *  The Private Internet Access Mobile Client is free software: you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by the Free
 *  Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 *  The Private Internet Access Mobile Client is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with the Private
 *  Internet Access Mobile Client.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.privateinternetaccess.common.regions.internals

import com.privateinternetaccess.common.regions.*
import com.privateinternetaccess.common.regions.model.RegionsResponse
import com.privateinternetaccess.common.regions.model.TranslationsGeoResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlin.coroutines.CoroutineContext


expect object RegionHttpClient {
    fun client(pinnedEndpoint: Pair<String, String>? = null): HttpClient
}

public class RegionsCommon(
        private val clientStateProvider: RegionClientStateProvider,
        private val pingDependency: PingRequest,
        private val messageVerificator: MessageVerificator
) : RegionsCommonAPI, CoroutineScope {

    companion object {
        private const val LOCALIZATION_ENDPOINT = "/vpninfo/regions/v2"
        private const val REGIONS_ENDPOINT = "/vpninfo/servers/v5"
        internal const val REQUEST_TIMEOUT_MS = 6000L
        internal const val CERTIFICATE = "-----BEGIN CERTIFICATE-----\n" +
                "MIIHqzCCBZOgAwIBAgIJAJ0u+vODZJntMA0GCSqGSIb3DQEBDQUAMIHoMQswCQYD\n" +
                "VQQGEwJVUzELMAkGA1UECBMCQ0ExEzARBgNVBAcTCkxvc0FuZ2VsZXMxIDAeBgNV\n" +
                "BAoTF1ByaXZhdGUgSW50ZXJuZXQgQWNjZXNzMSAwHgYDVQQLExdQcml2YXRlIElu\n" +
                "dGVybmV0IEFjY2VzczEgMB4GA1UEAxMXUHJpdmF0ZSBJbnRlcm5ldCBBY2Nlc3Mx\n" +
                "IDAeBgNVBCkTF1ByaXZhdGUgSW50ZXJuZXQgQWNjZXNzMS8wLQYJKoZIhvcNAQkB\n" +
                "FiBzZWN1cmVAcHJpdmF0ZWludGVybmV0YWNjZXNzLmNvbTAeFw0xNDA0MTcxNzQw\n" +
                "MzNaFw0zNDA0MTIxNzQwMzNaMIHoMQswCQYDVQQGEwJVUzELMAkGA1UECBMCQ0Ex\n" +
                "EzARBgNVBAcTCkxvc0FuZ2VsZXMxIDAeBgNVBAoTF1ByaXZhdGUgSW50ZXJuZXQg\n" +
                "QWNjZXNzMSAwHgYDVQQLExdQcml2YXRlIEludGVybmV0IEFjY2VzczEgMB4GA1UE\n" +
                "AxMXUHJpdmF0ZSBJbnRlcm5ldCBBY2Nlc3MxIDAeBgNVBCkTF1ByaXZhdGUgSW50\n" +
                "ZXJuZXQgQWNjZXNzMS8wLQYJKoZIhvcNAQkBFiBzZWN1cmVAcHJpdmF0ZWludGVy\n" +
                "bmV0YWNjZXNzLmNvbTCCAiIwDQYJKoZIhvcNAQEBBQADggIPADCCAgoCggIBALVk\n" +
                "hjumaqBbL8aSgj6xbX1QPTfTd1qHsAZd2B97m8Vw31c/2yQgZNf5qZY0+jOIHULN\n" +
                "De4R9TIvyBEbvnAg/OkPw8n/+ScgYOeH876VUXzjLDBnDb8DLr/+w9oVsuDeFJ9K\n" +
                "V2UFM1OYX0SnkHnrYAN2QLF98ESK4NCSU01h5zkcgmQ+qKSfA9Ny0/UpsKPBFqsQ\n" +
                "25NvjDWFhCpeqCHKUJ4Be27CDbSl7lAkBuHMPHJs8f8xPgAbHRXZOxVCpayZ2SND\n" +
                "fCwsnGWpWFoMGvdMbygngCn6jA/W1VSFOlRlfLuuGe7QFfDwA0jaLCxuWt/BgZyl\n" +
                "p7tAzYKR8lnWmtUCPm4+BtjyVDYtDCiGBD9Z4P13RFWvJHw5aapx/5W/CuvVyI7p\n" +
                "Kwvc2IT+KPxCUhH1XI8ca5RN3C9NoPJJf6qpg4g0rJH3aaWkoMRrYvQ+5PXXYUzj\n" +
                "tRHImghRGd/ydERYoAZXuGSbPkm9Y/p2X8unLcW+F0xpJD98+ZI+tzSsI99Zs5wi\n" +
                "jSUGYr9/j18KHFTMQ8n+1jauc5bCCegN27dPeKXNSZ5riXFL2XX6BkY68y58UaNz\n" +
                "meGMiUL9BOV1iV+PMb7B7PYs7oFLjAhh0EdyvfHkrh/ZV9BEhtFa7yXp8XR0J6vz\n" +
                "1YV9R6DYJmLjOEbhU8N0gc3tZm4Qz39lIIG6w3FDAgMBAAGjggFUMIIBUDAdBgNV\n" +
                "HQ4EFgQUrsRtyWJftjpdRM0+925Y6Cl08SUwggEfBgNVHSMEggEWMIIBEoAUrsRt\n" +
                "yWJftjpdRM0+925Y6Cl08SWhge6kgeswgegxCzAJBgNVBAYTAlVTMQswCQYDVQQI\n" +
                "EwJDQTETMBEGA1UEBxMKTG9zQW5nZWxlczEgMB4GA1UEChMXUHJpdmF0ZSBJbnRl\n" +
                "cm5ldCBBY2Nlc3MxIDAeBgNVBAsTF1ByaXZhdGUgSW50ZXJuZXQgQWNjZXNzMSAw\n" +
                "HgYDVQQDExdQcml2YXRlIEludGVybmV0IEFjY2VzczEgMB4GA1UEKRMXUHJpdmF0\n" +
                "ZSBJbnRlcm5ldCBBY2Nlc3MxLzAtBgkqhkiG9w0BCQEWIHNlY3VyZUBwcml2YXRl\n" +
                "aW50ZXJuZXRhY2Nlc3MuY29tggkAnS7684Nkme0wDAYDVR0TBAUwAwEB/zANBgkq\n" +
                "hkiG9w0BAQ0FAAOCAgEAJsfhsPk3r8kLXLxY+v+vHzbr4ufNtqnL9/1Uuf8NrsCt\n" +
                "pXAoyZ0YqfbkWx3NHTZ7OE9ZRhdMP/RqHQE1p4N4Sa1nZKhTKasV6KhHDqSCt/dv\n" +
                "Em89xWm2MVA7nyzQxVlHa9AkcBaemcXEiyT19XdpiXOP4Vhs+J1R5m8zQOxZlV1G\n" +
                "tF9vsXmJqWZpOVPmZ8f35BCsYPvv4yMewnrtAC8PFEK/bOPeYcKN50bol22QYaZu\n" +
                "LfpkHfNiFTnfMh8sl/ablPyNY7DUNiP5DRcMdIwmfGQxR5WEQoHL3yPJ42LkB5zs\n" +
                "6jIm26DGNXfwura/mi105+ENH1CaROtRYwkiHb08U6qLXXJz80mWJkT90nr8Asj3\n" +
                "5xN2cUppg74nG3YVav/38P48T56hG1NHbYF5uOCske19F6wi9maUoto/3vEr0rnX\n" +
                "JUp2KODmKdvBI7co245lHBABWikk8VfejQSlCtDBXn644ZMtAdoxKNfR2WTFVEwJ\n" +
                "iyd1Fzx0yujuiXDROLhISLQDRjVVAvawrAtLZWYK31bY7KlezPlQnl/D9Asxe85l\n" +
                "8jO5+0LdJ6VyOs/Hd4w52alDW/MFySDZSfQHMTIc30hLBJ8OnCEIvluVQQ2UQvoW\n" +
                "+no177N9L2Y+M9TcTA62ZyMXShHQGeh20rb4kK8f+iFX8NxtdHVSkxMEFSfDDyQ=\n" +
                "-----END CERTIFICATE-----\n"
    }

    private data class RegionEndpointInformation(
            val region: String,
            val name: String,
            val iso: String,
            val dns: String,
            val protocol: String,
            val endpoint: String,
            val portForwarding: Boolean
    )

    private val json = Json { ignoreUnknownKeys = true }
    private var knownRegionsResponse: RegionsResponse? = null

    // region CoroutineScope
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
    // endregion

    // region RegionsCommonAPI
    override fun fetchLocalization(callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit) {
        launch {
            fetchLocalizationAsync(clientStateProvider.regionEndpoints(), callback)
        }
    }

    override fun fetchRegions(callback: (response: RegionsResponse?, error: Error?) -> Unit) {
        launch {
            fetchRegionsAsync(clientStateProvider.regionEndpoints(), callback)
        }
    }

    override fun pingRequests(callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit) {
        launch {
            pingRequestsAsync(callback)
        }
    }
    // endregion

    // region Private
    private fun pingRequestsAsync(
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) = async {
        handlePingRequest(callback)
    }

    private fun fetchLocalizationAsync(
        endpoints: List<RegionEndpoint>,
        callback: (response: TranslationsGeoResponse?, error: Error?) -> Unit
    ) = async {
        var parsedResponse: TranslationsGeoResponse? = null
        var error: Error? = null
        if (endpoints.isNullOrEmpty()) {
            error = Error("No available endpoints to perform the request")
        }

        for (accountEndpoint in endpoints) {
            error = null
            val client = if (accountEndpoint.usePinnedCertificate) {
                RegionHttpClient.client(Pair(accountEndpoint.endpoint, accountEndpoint.certificateCommonName!!))
            } else {
                RegionHttpClient.client()
            }

            val response = client.getCatching<Pair<HttpResponse?, Exception?>> {
                url("https://${accountEndpoint.endpoint}$LOCALIZATION_ENDPOINT")
            }

            response.first?.let { httpResponse ->
                try {
                    val content = httpResponse.receive<String>()
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        error = Error("${httpResponse.status.value} ${httpResponse.status.description}")
                    } else {
                        val responseHandled: Pair<TranslationsGeoResponse?, Error?> = handleFetchLocalizationResponse(content)
                        parsedResponse = responseHandled.first
                        error = responseHandled.second
                    }
                } catch (exception: NoTransformationFoundException) {
                    error = Error("600 - Unexpected response transformation: $exception")
                } catch (exception: DoubleReceiveException) {
                    error = Error("600 - Request receive already invoked: $exception")
                }
            }
            response.second?.let {
                error = Error(it.message)
            }

            // If there were no errors in the request for the current endpoint. No need to try the next endpoint.
            if (error == null) {
                break
            }
        }

        withContext(Dispatchers.Main) {
            callback(parsedResponse, error)
        }
    }

    private fun fetchRegionsAsync(
        endpoints: List<RegionEndpoint>,
        callback: (response: RegionsResponse?, error: Error?) -> Unit
    ) = async {
        var parsedResponse: RegionsResponse? = null
        var error: Error? = null
        if (endpoints.isNullOrEmpty()) {
            error = Error("No available endpoints to perform the request")
        }

        for (accountEndpoint in endpoints) {
            error = null
            val client = if (accountEndpoint.usePinnedCertificate) {
                RegionHttpClient.client(Pair(accountEndpoint.endpoint, accountEndpoint.certificateCommonName!!))
            } else {
                RegionHttpClient.client()
            }

            val response = client.getCatching<Pair<HttpResponse?, Exception?>> {
                url("https://${accountEndpoint.endpoint}$REGIONS_ENDPOINT")
            }

            response.first?.let { httpResponse ->
                try {
                    val content = httpResponse.receive<String>()
                    if (RegionsUtils.isErrorStatusCode(httpResponse.status.value)) {
                        error = Error("${httpResponse.status.value} ${httpResponse.status.description}")
                    } else {
                        val responseHandled: Pair<RegionsResponse?, Error?> = handleFetchRegionsResponse(content)
                        parsedResponse = responseHandled.first
                        error = responseHandled.second
                    }
                } catch (exception: NoTransformationFoundException) {
                    error = Error("600 - Unexpected response transformation: $exception")
                } catch (exception: DoubleReceiveException) {
                    error = Error("600 - Request receive already invoked: $exception")
                }
            }
            response.second?.let {
                error = Error(it.message)
            }

            // If there were no errors in the request for the current endpoint. No need to try the next endpoint.
            if (error == null) {
                break
            }
        }

        withContext(Dispatchers.Main) {
            callback(parsedResponse, error)
        }
    }

    private fun handleFetchLocalizationResponse(
            response: String,
    ): Pair<TranslationsGeoResponse?, Error?> {
        val responseList = response.split("\n\n")
        val message = responseList.first()
        val key = responseList.last()

        var error: Error? = null
        var serializedLocalization: TranslationsGeoResponse? = null
        if (messageVerificator.verifyMessage(message, key)) {
            try {
                serializedLocalization = json.decodeFromString(TranslationsGeoResponse.serializer(), message)
            } catch (exception: SerializationException) {
                error = Error("Decode error $exception")
            }
        } else {
            error = Error("Invalid signature on Locale request")
        }

        return Pair(serializedLocalization, error)
    }

    public fun handleFetchRegionsResponse(
        response: String,
    ): Pair<RegionsResponse?, Error?> {
        val responseList = response.split("\n\n")
        val message = responseList.first()
        val key = responseList.last()

        var error: Error? = null
        if (messageVerificator.verifyMessage(message, key)) {
            try {
                knownRegionsResponse = serializeRegions(message)
            } catch (exception: SerializationException) {
                error = Error("Decode error $exception")
            }
        } else {
            error = Error("Invalid signature on Regions request")
        }

        return Pair(knownRegionsResponse, error)
    }

    public fun serializeRegions(jsonResponse: String) =
            json.decodeFromString(RegionsResponse.serializer(), jsonResponse)

    public suspend fun handlePingRequest(
        callback: (response: List<RegionLowerLatencyInformation>, error: Error?) -> Unit
    ) {
        var error: Error? = null
        var response = listOf<RegionLowerLatencyInformation>()
        knownRegionsResponse?.let {
            response = requestEndpointsLowerLatencies(it)
        } ?: run {
            error = Error("Unknown regions")
        }

        withContext(Dispatchers.Main) {
            callback(response, error)
        }
    }

    private fun requestEndpointsLowerLatencies(regionsResponse: RegionsResponse): List<RegionLowerLatencyInformation> {
        val endpointsToPing = mutableMapOf<String, List<String>>()
        val lowerLatencies = mutableListOf<RegionLowerLatencyInformation>()

        val allKnownEndpointsDetails = flattenEndpointsInformation(regionsResponse)
        for ((region, regionEndpointInformation) in allKnownEndpointsDetails) {
            val regionEndpoints = mutableListOf<String>()
            regionEndpointInformation.forEach {
                regionEndpoints.add(it.endpoint)
            }
            endpointsToPing[region] = regionEndpoints
        }

        pingDependency.platformPingRequest(endpointsToPing) { latencyResults ->
            for ((region, results) in latencyResults) {
                if (results.isNullOrEmpty()) {
                    continue
                }

                results.minByOrNull { it.latency }?.let { minEndpointLatency ->
                    allKnownEndpointsDetails[region]?.let { allKnownEndpointsDetails ->
                        allKnownEndpointsDetails.firstOrNull {
                            it.endpoint == minEndpointLatency.endpoint
                        }?.let { minEndpointLatencyDetails ->
                            lowerLatencies.add(RegionLowerLatencyInformation(
                                    minEndpointLatencyDetails.region,
                                    minEndpointLatencyDetails.endpoint,
                                    minEndpointLatency.latency
                            ))
                        }
                    }
                }
            }
        }
        return lowerLatencies
    }

    private fun flattenEndpointsInformation(response: RegionsResponse): Map<String, List<RegionEndpointInformation>> {
        val result = mutableMapOf<String, MutableList<RegionEndpointInformation>>()
        response.regions.forEach { region ->
            region.servers[RegionsProtocol.META.protocol]?.forEach { regionServerProtocol ->
                if (result[region.id] == null) {
                    result[region.id] = mutableListOf()
                }
                result[region.id]?.add(RegionEndpointInformation(
                        region.id,
                        region.name,
                        region.country,
                        region.dns,
                        RegionsProtocol.META.protocol,
                        regionServerProtocol.ip,
                        region.portForward
                ))
            }
        }
        return result
    }
    // endregion

    // region HttpClient extensions
    private suspend inline fun <reified T> HttpClient.getCatching(
            block: HttpRequestBuilder.() -> Unit = {}
    ): Pair<HttpResponse?, Exception?> {
        var exception: Exception? = null
        var response: HttpResponse? = null
        try {
            response = request {
                method = HttpMethod.Get
                apply(block)
            }
        } catch (e: Exception) {
            exception = e
        }
        return Pair(response, exception)
    }
    // endregion
}