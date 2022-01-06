package codes.apophis

data class SemanticVersion(
    val majorVersion: Int,
    val minorVersion: Int,
    val patchVersion: Int
) {

    fun getVersionString() : String {
        return "$majorVersion.$minorVersion.$patchVersion"
    }

    fun incrementPatch(): SemanticVersion {
        return SemanticVersion(majorVersion, minorVersion, patchVersion.inc())
    }

    fun incrementMinor(): SemanticVersion {
        return SemanticVersion(majorVersion, minorVersion.inc(), 0)
    }

    fun incrementMajor(): SemanticVersion {
        return SemanticVersion(majorVersion.inc(), 0, 0)
    }

    companion object {

        class Builder() {
            private var majorVersion: Int = 0
            private var minorVersion: Int = 0
            private var patchVersion: Int = 0

            fun build(): SemanticVersion {
                return SemanticVersion(majorVersion,minorVersion, patchVersion)
            }

            fun fromString(rawString: String) : Builder {
                val stringArray =  rawString.split(".")

                if(stringArray[0].isEmpty()) {
                    return this
                } else {
                    majorVersion = stringArray[0].toInt()
                }

                if(stringArray[1].isEmpty()) {
                    return this
                } else {
                    minorVersion = stringArray[1].toInt()
                }

                if(stringArray[2].isEmpty()) {
                    return this
                } else {
                    patchVersion = stringArray[2].toInt()
                }

                return this

            }

            fun setMajorVersion(majorVersion: Int) : Builder {
                this.majorVersion = majorVersion
                return this
            }

            fun setMinorVersion(minorVersion: Int) : Builder {
                return this
            }

            fun setPatchVersion(patchVersion: Int) : Builder {
                return this
            }
        }
    }
}