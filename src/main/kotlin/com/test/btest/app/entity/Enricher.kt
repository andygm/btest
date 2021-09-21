package com.test.btest.app.entity

import com.test.btest.app.entity.EnricherValues.Bands2g
import com.test.btest.app.entity.EnricherValues.Bands3g
import com.test.btest.app.entity.EnricherValues.Bands4g
import com.test.btest.app.entity.EnricherValues.Technology

class Enricher private constructor(
    val technology: Technology,
    val bands2g: Bands2g,
    val bands3g: Bands3g,
    val bands4g: Bands4g
) {
    companion object {
        fun create(technology: Technology, bands2g: Bands2g, bands3g: Bands3g, bands4g: Bands4g): Enricher =
            Enricher(technology, bands2g, bands3g, bands4g)
    }
}