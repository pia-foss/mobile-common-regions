package com.privateinternetaccess.common.regions

import com.privateinternetaccess.common.regions.model.RegionsResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

object RegionsUtils {

    const val GEN4_DEFAULT_RESPONSE = "{\"groups\": {\"ovpntcp\": [{\"name\": \"openvpn_tcp\", \"ports\": [80, 443, 853, 8443]}], \"ovpnudp\": [{\"name\": \"openvpn_udp\", \"ports\": [53, 123, 853, 8080]}], \"wg\": [{\"name\": \"wireguard\", \"ports\": [1337]}], \"ikev2\": [{\"name\": \"ikev2\", \"ports\": [500, 4500]}], \"proxysocks\": [{\"name\": \"socks\", \"ports\": [1080]}], \"proxyss\": [{\"name\": \"shadowsocks\", \"ports\": [443]}]}, \"regions\": [{\"id\": \"us_california\", \"name\": \"US California\", \"country\": \"US\", \"auto_region\": true, \"dns\": \"us-california.privacy.network\", \"port_forward\": false, \"servers\": {\"ovpnudp\": [{\"ip\": \"176.113.73.108\", \"cn\": \"losangeles455\"}, {\"ip\": \"84.17.45.6\", \"cn\": \"losangeles404\"}, {\"ip\": \"89.41.26.4\", \"cn\": \"losangeles413\"}, {\"ip\": \"195.206.104.3\", \"cn\": \"losangeles414\"}, {\"ip\": \"45.89.173.178\", \"cn\": \"lasvegas402\"}], \"ovpntcp\": [{\"ip\": \"176.113.73.110\", \"cn\": \"losangeles455\"}, {\"ip\": \"84.17.45.11\", \"cn\": \"losangeles404\"}, {\"ip\": \"89.41.26.2\", \"cn\": \"losangeles413\"}, {\"ip\": \"195.206.104.2\", \"cn\": \"losangeles414\"}, {\"ip\": \"45.89.173.179\", \"cn\": \"lasvegas402\"}], \"ikev2\": [{\"ip\": \"176.113.73.108\", \"cn\": \"losangeles455\"}, {\"ip\": \"84.17.45.18\", \"cn\": \"losangeles404\"}, {\"ip\": \"89.41.26.2\", \"cn\": \"losangeles413\"}, {\"ip\": \"195.206.104.2\", \"cn\": \"losangeles414\"}, {\"ip\": \"45.89.173.180\", \"cn\": \"lasvegas402\"}], \"wg\": [{\"ip\": \"176.113.73.109\", \"cn\": \"losangeles455\"}, {\"ip\": \"84.17.45.14\", \"cn\": \"losangeles404\"}, {\"ip\": \"89.41.26.4\", \"cn\": \"losangeles413\"}, {\"ip\": \"195.206.104.3\", \"cn\": \"losangeles414\"}, {\"ip\": \"45.89.173.180\", \"cn\": \"lasvegas402\"}]}}, {\"id\": \"us_florida\", \"name\": \"US Florida\", \"country\": \"US\", \"auto_region\": true, \"dns\": \"us-florida.privacy.network\", \"port_forward\": false, \"servers\": {\"ovpnudp\": [{\"ip\": \"89.38.227.186\", \"cn\": \"miami401\"}, {\"ip\": \"89.38.227.179\", \"cn\": \"miami402\"}], \"ovpntcp\": [{\"ip\": \"89.38.227.186\", \"cn\": \"miami401\"}, {\"ip\": \"89.38.227.179\", \"cn\": \"miami402\"}], \"ikev2\": [{\"ip\": \"89.38.227.187\", \"cn\": \"miami401\"}, {\"ip\": \"89.38.227.180\", \"cn\": \"miami402\"}], \"wg\": [{\"ip\": \"89.38.227.188\", \"cn\": \"miami401\"}, {\"ip\": \"89.38.227.178\", \"cn\": \"miami402\"}]}}, {\"id\": \"us_new_york_city\", \"name\": \"US New York City\", \"country\": \"US\", \"auto_region\": true, \"dns\": \"us-newyorkcity.privacy.network\", \"port_forward\": false, \"servers\": {\"ovpnudp\": [{\"ip\": \"108.62.98.51\", \"cn\": \"newyork406\"}, {\"ip\": \"156.146.36.147\", \"cn\": \"newyork407\"}, {\"ip\": \"217.138.198.18\", \"cn\": \"newyork408\"}], \"ovpntcp\": [{\"ip\": \"108.62.98.51\", \"cn\": \"newyork406\"}, {\"ip\": \"156.146.36.137\", \"cn\": \"newyork407\"}, {\"ip\": \"217.138.198.20\", \"cn\": \"newyork408\"}], \"ikev2\": [{\"ip\": \"108.62.98.53\", \"cn\": \"newyork406\"}, {\"ip\": \"156.146.36.141\", \"cn\": \"newyork407\"}, {\"ip\": \"217.138.198.20\", \"cn\": \"newyork408\"}], \"wg\": [{\"ip\": \"108.62.98.50\", \"cn\": \"newyork406\"}, {\"ip\": \"156.146.36.134\", \"cn\": \"newyork407\"}, {\"ip\": \"217.138.198.19\", \"cn\": \"newyork408\"}]}}, {\"id\": \"uk\", \"name\": \"UK London\", \"country\": \"GB\", \"auto_region\": true, \"dns\": \"uk-london.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"212.102.63.148\", \"cn\": \"london402\"}, {\"ip\": \"185.200.118.155\", \"cn\": \"london403\"}], \"ovpntcp\": [{\"ip\": \"212.102.63.145\", \"cn\": \"london402\"}, {\"ip\": \"185.200.118.156\", \"cn\": \"london403\"}], \"ikev2\": [{\"ip\": \"212.102.63.138\", \"cn\": \"london402\"}, {\"ip\": \"185.200.118.156\", \"cn\": \"london403\"}], \"wg\": [{\"ip\": \"212.102.63.133\", \"cn\": \"london402\"}, {\"ip\": \"185.200.118.156\", \"cn\": \"london403\"}]}}, {\"id\": \"uk_manchester\", \"name\": \"UK Manchester\", \"country\": \"GB\", \"auto_region\": true, \"dns\": \"uk-manchester.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"37.120.159.126\", \"cn\": \"manchester460\"}], \"ovpntcp\": [{\"ip\": \"37.120.159.131\", \"cn\": \"manchester460\"}], \"ikev2\": [{\"ip\": \"37.120.159.126\", \"cn\": \"manchester460\"}], \"wg\": [{\"ip\": \"37.120.159.122\", \"cn\": \"manchester460\"}]}}, {\"id\": \"germany\", \"name\": \"DE Frankfurt\", \"country\": \"DE\", \"auto_region\": true, \"dns\": \"de-frankfurt.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"185.216.33.131\", \"cn\": \"frankfurt438\"}, {\"ip\": \"185.216.33.151\", \"cn\": \"frankfurt439\"}, {\"ip\": \"185.216.33.166\", \"cn\": \"frankfurt440\"}, {\"ip\": \"37.120.196.226\", \"cn\": \"frankfurt441\"}], \"ovpntcp\": [{\"ip\": \"185.216.33.134\", \"cn\": \"frankfurt438\"}, {\"ip\": \"185.216.33.147\", \"cn\": \"frankfurt439\"}, {\"ip\": \"185.216.33.166\", \"cn\": \"frankfurt440\"}, {\"ip\": \"37.120.196.230\", \"cn\": \"frankfurt441\"}], \"ikev2\": [{\"ip\": \"185.216.33.134\", \"cn\": \"frankfurt438\"}, {\"ip\": \"185.216.33.151\", \"cn\": \"frankfurt439\"}, {\"ip\": \"185.216.33.164\", \"cn\": \"frankfurt440\"}, {\"ip\": \"37.120.196.227\", \"cn\": \"frankfurt441\"}], \"wg\": [{\"ip\": \"185.216.33.132\", \"cn\": \"frankfurt438\"}, {\"ip\": \"185.216.33.150\", \"cn\": \"frankfurt439\"}, {\"ip\": \"185.216.33.163\", \"cn\": \"frankfurt440\"}, {\"ip\": \"37.120.196.226\", \"cn\": \"frankfurt441\"}]}}, {\"id\": \"no\", \"name\": \"Norway\", \"country\": \"NO\", \"auto_region\": true, \"dns\": \"no.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"84.247.50.139\", \"cn\": \"oslo401\"}], \"ovpntcp\": [{\"ip\": \"84.247.50.140\", \"cn\": \"oslo401\"}], \"ikev2\": [{\"ip\": \"84.247.50.139\", \"cn\": \"oslo401\"}], \"wg\": [{\"ip\": \"84.247.50.139\", \"cn\": \"oslo401\"}]}}, {\"id\": \"denmark\", \"name\": \"Denmark\", \"country\": \"DK\", \"auto_region\": true, \"dns\": \"denmark.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"89.45.7.59\", \"cn\": \"copenhagen401\"}], \"ovpntcp\": [{\"ip\": \"89.45.7.58\", \"cn\": \"copenhagen401\"}], \"ikev2\": [{\"ip\": \"89.45.7.59\", \"cn\": \"copenhagen401\"}], \"wg\": [{\"ip\": \"89.45.7.59\", \"cn\": \"copenhagen401\"}]}}, {\"id\": \"swiss\", \"name\": \"Switzerland\", \"country\": \"CH\", \"auto_region\": true, \"dns\": \"swiss.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"37.120.137.187\", \"cn\": \"zurich401\"}], \"ovpntcp\": [{\"ip\": \"37.120.137.188\", \"cn\": \"zurich401\"}], \"ikev2\": [{\"ip\": \"37.120.137.187\", \"cn\": \"zurich401\"}], \"wg\": [{\"ip\": \"37.120.137.187\", \"cn\": \"zurich401\"}]}}, {\"id\": \"france\", \"name\": \"France\", \"country\": \"FR\", \"auto_region\": true, \"dns\": \"france.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"185.189.113.68\", \"cn\": \"paris401\"}], \"ovpntcp\": [{\"ip\": \"185.189.113.68\", \"cn\": \"paris401\"}], \"ikev2\": [{\"ip\": \"185.189.113.67\", \"cn\": \"paris401\"}], \"wg\": [{\"ip\": \"185.189.113.66\", \"cn\": \"paris401\"}]}}, {\"id\": \"austria\", \"name\": \"Austria\", \"country\": \"AT\", \"auto_region\": true, \"dns\": \"austria.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"37.120.155.188\", \"cn\": \"vienna401\"}], \"ovpntcp\": [{\"ip\": \"37.120.155.188\", \"cn\": \"vienna401\"}], \"ikev2\": [{\"ip\": \"37.120.155.188\", \"cn\": \"vienna401\"}], \"wg\": [{\"ip\": \"37.120.155.188\", \"cn\": \"vienna401\"}]}}, {\"id\": \"czech\", \"name\": \"Czech Republic\", \"country\": \"CZ\", \"auto_region\": true, \"dns\": \"czech.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"217.138.220.52\", \"cn\": \"prague401\"}], \"ovpntcp\": [{\"ip\": \"217.138.220.52\", \"cn\": \"prague401\"}], \"ikev2\": [{\"ip\": \"217.138.220.51\", \"cn\": \"prague401\"}], \"wg\": [{\"ip\": \"217.138.220.51\", \"cn\": \"prague401\"}]}}, {\"id\": \"italy\", \"name\": \"Italy\", \"country\": \"IT\", \"auto_region\": true, \"dns\": \"italy.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"89.40.182.83\", \"cn\": \"milano401\"}], \"ovpntcp\": [{\"ip\": \"89.40.182.82\", \"cn\": \"milano401\"}], \"ikev2\": [{\"ip\": \"89.40.182.84\", \"cn\": \"milano401\"}], \"wg\": [{\"ip\": \"89.40.182.82\", \"cn\": \"milano401\"}]}}, {\"id\": \"hungary\", \"name\": \"Hungary\", \"country\": \"HU\", \"auto_region\": true, \"dns\": \"hungary.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"217.138.192.219\", \"cn\": \"budapest401\"}], \"ovpntcp\": [{\"ip\": \"217.138.192.218\", \"cn\": \"budapest401\"}], \"ikev2\": [{\"ip\": \"217.138.192.219\", \"cn\": \"budapest401\"}], \"wg\": [{\"ip\": \"217.138.192.220\", \"cn\": \"budapest401\"}]}}, {\"id\": \"poland\", \"name\": \"Poland\", \"country\": \"PL\", \"auto_region\": true, \"dns\": \"poland.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"217.138.209.242\", \"cn\": \"warsaw401\"}], \"ovpntcp\": [{\"ip\": \"217.138.209.242\", \"cn\": \"warsaw401\"}], \"ikev2\": [{\"ip\": \"217.138.209.242\", \"cn\": \"warsaw401\"}], \"wg\": [{\"ip\": \"217.138.209.244\", \"cn\": \"warsaw401\"}]}}, {\"id\": \"hk\", \"name\": \"Hong Kong\", \"country\": \"HK\", \"auto_region\": true, \"dns\": \"hk.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"84.17.56.34\", \"cn\": \"hongkong451\"}, {\"ip\": \"84.17.56.3\", \"cn\": \"hongkong452\"}], \"ovpntcp\": [{\"ip\": \"84.17.56.36\", \"cn\": \"hongkong451\"}, {\"ip\": \"84.17.56.7\", \"cn\": \"hongkong452\"}], \"ikev2\": [{\"ip\": \"84.17.56.41\", \"cn\": \"hongkong451\"}, {\"ip\": \"84.17.56.5\", \"cn\": \"hongkong452\"}], \"wg\": [{\"ip\": \"84.17.56.38\", \"cn\": \"hongkong451\"}, {\"ip\": \"84.17.56.3\", \"cn\": \"hongkong452\"}]}}, {\"id\": \"nl_amsterdam\", \"name\": \"Amsterdam\", \"country\": \"NL\", \"auto_region\": true, \"dns\": \"nl-amsterdam.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"190.2.142.110\", \"cn\": \"amsterdam410\"}, {\"ip\": \"212.102.35.137\", \"cn\": \"amsterdam416\"}, {\"ip\": \"92.119.179.123\", \"cn\": \"amsterdam417\"}], \"ovpntcp\": [{\"ip\": \"190.2.142.72\", \"cn\": \"amsterdam410\"}, {\"ip\": \"212.102.35.134\", \"cn\": \"amsterdam416\"}, {\"ip\": \"92.119.179.123\", \"cn\": \"amsterdam417\"}], \"ikev2\": [{\"ip\": \"190.2.142.75\", \"cn\": \"amsterdam410\"}, {\"ip\": \"212.102.35.141\", \"cn\": \"amsterdam416\"}, {\"ip\": \"92.119.179.122\", \"cn\": \"amsterdam417\"}], \"wg\": [{\"ip\": \"190.2.142.72\", \"cn\": \"amsterdam410\"}, {\"ip\": \"212.102.35.146\", \"cn\": \"amsterdam416\"}, {\"ip\": \"92.119.179.124\", \"cn\": \"amsterdam417\"}]}}, {\"id\": \"sofia\", \"name\": \"Sofia\", \"country\": \"BG\", \"auto_region\": true, \"dns\": \"sofia.privacy.network\", \"port_forward\": true, \"servers\": {\"ovpnudp\": [{\"ip\": \"217.138.221.132\", \"cn\": \"sofia401\"}], \"ovpntcp\": [{\"ip\": \"217.138.221.130\", \"cn\": \"sofia401\"}], \"ikev2\": [{\"ip\": \"217.138.221.131\", \"cn\": \"sofia401\"}], \"wg\": [{\"ip\": \"217.138.221.130\", \"cn\": \"sofia401\"}]}}]}"

    fun stringify(regionsResponse: RegionsResponse) =
        Json.stringify(RegionsResponse.serializer(), regionsResponse)

    fun parse(regionsResponseString: String) =
        Json(JsonConfiguration(
            ignoreUnknownKeys = true
        )).parse(RegionsResponse.serializer(), regionsResponseString)
}
