package com.devmnv.digipinfinder.utils

import com.devmnv.digipinfinder.model.Bounds
import com.google.android.gms.maps.model.LatLng

object Digipin {

    private val DIGIPIN_GRID: Array<CharArray> = arrayOf(
        charArrayOf('F', 'C', '9', '8'),
        charArrayOf('J', '3', '2', '7'),
        charArrayOf('K', '4', '5', '6'),
        charArrayOf('L', 'M', 'P', 'T')
    )

    private val BOUNDS = Bounds(
        minLat = 2.5f,
        maxLat = 38.5f,
        minLon = 63.5f,
        maxLon = 99.5f
    )

    fun getDigiPin(lat: Double, lon: Double): String {
        if (lat < BOUNDS.minLat || lat > BOUNDS.maxLat) throw IllegalArgumentException("Latitude out of range")
        if (lon < BOUNDS.minLon || lon > BOUNDS.maxLon) throw IllegalArgumentException("Longitude out of range")

        var minLat = BOUNDS.minLat
        var maxLat = BOUNDS.maxLat
        var minLon = BOUNDS.minLon
        var maxLon = BOUNDS.maxLon

        var digiPin = "";

        for (level in 1..10) {
            val latDiv = (maxLat - minLat) / 4
            val lonDiv = (maxLon - minLon) / 4

            // REVERSED row logic (to match original)
            var row = 3 - ((lat - minLat) / latDiv).toInt()
            var col = ((lon - minLon) / lonDiv).toInt()

            row = row.coerceIn(0, 3)
            col = col.coerceIn(0, 3)

            digiPin += DIGIPIN_GRID[row][col]

            if (level == 3 || level == 6) digiPin += '-'

            // Update bounds (reverse logic for row)
            maxLat = minLat + latDiv * (4 - row);
            minLat += latDiv * (3 - row);

            minLon += lonDiv * col;
            maxLon = minLon + lonDiv;

        }

        return digiPin

    }

    fun getLatLngFromDigiPin(digiPin: String): LatLng {
        val pin = digiPin.replace("-", "")
        if (pin.length != 10) throw IllegalArgumentException("Invalid DIGIPIN")

        var minLat = BOUNDS.minLat
        var maxLat = BOUNDS.maxLat
        var minLon = BOUNDS.minLon
        var maxLon = BOUNDS.maxLon

        for (char in pin) {
            var found = false
            var ri = -1
            var ci = -1

            for (r in DIGIPIN_GRID.indices) {
                for (c in DIGIPIN_GRID[r].indices) {
                    if (DIGIPIN_GRID[r][c] == char) {
                        ri = r
                        ci = c
                        found = true
                        break
                    }
                }
                if (found) break
            }

            if (!found) throw IllegalArgumentException("Invalid character in DIGIPIN")

            val latDiv = (maxLat - minLat) / 4
            val lonDiv = (maxLon - minLon) / 4

            val lat1 = maxLat - latDiv * (ri + 1)
            val lat2 = maxLat - latDiv * ri
            val lon1 = minLon + lonDiv * ci
            val lon2 = minLon + lonDiv * (ci + 1)

            minLat = lat1
            maxLat = lat2
            minLon = lon1
            maxLon = lon2
        }

        val centerLat = (minLat + maxLat) / 2
        val centerLon = (minLon + maxLon) / 2

        return LatLng(centerLat.toDouble(), centerLon.toDouble())
    }

}