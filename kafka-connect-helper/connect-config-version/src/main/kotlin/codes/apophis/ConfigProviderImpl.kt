package codes.apophis

import java.util.*

class ConfigProvider : VersionedConfigProvider  {
    override fun getLatestByName(name: String): VersionedConfig {
        TODO("Not yet implemented")
    }

    override fun getLatestByID(identifier: UUID): VersionedConfig {
        TODO("Not yet implemented")
    }

    override fun getByNameAndVersion(name: String, version: String): VersionedConfig {
        TODO("Not yet implemented")
    }

    override fun saveNewVersion(versionedConfig: VersionedConfig): VersionedConfig {
        TODO("Not yet implemented")
    }
}