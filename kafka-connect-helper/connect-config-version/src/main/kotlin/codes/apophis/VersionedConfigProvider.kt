package codes.apophis

import java.util.*

interface VersionedConfigProvider {

    fun getLatestByName(name: String) : VersionedConfig

    fun getLatestByID(identifier: UUID) : VersionedConfig

    fun getByNameAndVersion(name: String, version: String) : VersionedConfig

    fun saveNewVersion(versionedConfig: VersionedConfig) : VersionedConfig

}