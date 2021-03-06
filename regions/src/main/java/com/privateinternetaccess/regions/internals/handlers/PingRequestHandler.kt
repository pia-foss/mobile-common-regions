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

package com.privateinternetaccess.regions.internals.handlers

import com.privateinternetaccess.common.regions.PingRequest
import com.privateinternetaccess.common.regions.PingRequest.PlatformPingResult
import com.privateinternetaccess.regions.REGIONS_PING_TIMEOUT
import kotlinx.coroutines.*
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import kotlin.system.measureTimeMillis


internal class PingRequestHandler : PingRequest {

    companion object {
        private const val REGIONS_PING_PORT = 443
    }

    // region PingRequest
    override fun platformPingRequest(
        endpoints: Map<String, List<String>>,
        callback: (result: Map<String, List<PlatformPingResult>>) -> Unit
    ) {
        runBlocking(Dispatchers.IO) {
            val result = mutableMapOf<String, List<PlatformPingResult>>()
            val requests: MutableList<Job> = mutableListOf()
            for ((region, endpointsInRegion) in endpoints) {
                val regionEndpointsResults = mutableListOf<PlatformPingResult>()
                endpointsInRegion.forEach {
                    requests.add(async(Dispatchers.IO) {
                        var error: Error? = null
                        var latency = measureTimeMillis {
                            error = ping(it)
                        }
                        latency = error?.let {
                            REGIONS_PING_TIMEOUT.toLong()
                        } ?: latency
                        regionEndpointsResults.add(PlatformPingResult(it, latency))
                        result[region] = regionEndpointsResults
                    })
                }
            }
            requests.joinAll()
            callback(result)
        }
    }
    // endregion

    // region private
    private fun ping(endpoint: String): Error? {
        var error: Error? = null
        try {
            val socket = Socket()
            socket.tcpNoDelay = true
            socket.connect(InetSocketAddress(endpoint, REGIONS_PING_PORT), REGIONS_PING_TIMEOUT)
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            error = Error("Error reaching endpoint: $endpoint exception: $e")
        }
        return error
    }
    // endregion
}