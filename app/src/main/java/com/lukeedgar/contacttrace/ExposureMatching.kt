package com.lukeedgar.contacttrace

class ExposureMatching(
    unknownCollectedExposures : List<ExposureId>,
    positiveCollectedExposures : List<ExposureId>) {

    // IDs collected in the previous 14 days which are unknown to be positive for infection.
    private val untestedIds by lazy {
        mapIdsWithinInfectiousPeriod(unknownCollectedExposures)
    }
    // IDs collected in the previous 14 days which are positive for infection.
    private val positiveInfectiousIds by lazy {
        mapIdsWithinInfectiousPeriod(positiveCollectedExposures)
    }


    fun checkUnknownIdsContainPositives() : Boolean =
        untestedIds.intersect(positiveInfectiousIds).any()


    private fun mapIdsWithinInfectiousPeriod(exposures : List<ExposureId>) : List<String> =
        exposures.filter(ExposureId::isInInfectiousPeriod).map(ExposureId::id)
}