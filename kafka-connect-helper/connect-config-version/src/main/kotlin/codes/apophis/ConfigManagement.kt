package codes.apophis

class ConfigManagement(
    val versionedConfigProvider: VersionedConfigProvider
) : VersionedConfigDeployer {

    override fun deploy(target: String, name: String) {
        TODO("Not yet implemented")
    }

    override fun getDeployedVersion(target: String, name: String) {
        TODO("Not yet implemented")
    }
}