package com.devmnv.digipinfinder.utils

import com.devmnv.digipinfinder.model.Bounds
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

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

}