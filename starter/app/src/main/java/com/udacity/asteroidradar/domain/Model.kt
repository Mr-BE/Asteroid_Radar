package com.udacity.asteroidradar.domain

//Object to be used by application
data class Asteroid (val id: Long,
                     val codename: String, val closeApproachDate: String,
                     val absoluteMagnitude: Double, val estimatedDiameter: Double,
                     val relativeVelocity: Double, val distanceFromEarth: Double,
                     val isPotentiallyHazardous: Boolean)