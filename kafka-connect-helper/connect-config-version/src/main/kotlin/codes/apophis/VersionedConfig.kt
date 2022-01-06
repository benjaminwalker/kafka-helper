package codes.apophis

import java.util.*

data class VersionedConfig(
    val versionedId: UUID,
    val configId: UUID,
    val type: String,
    val name: String,
    val version: SemanticVersion,
    val configData: String,
    val versionCreateAudit: AuditAttribute
)
