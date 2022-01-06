package codes.apophis

interface VersionedConfigDeployer {

    fun deploy(target: String, name: String)

    fun getDeployedVersion(target: String, name: String)


}