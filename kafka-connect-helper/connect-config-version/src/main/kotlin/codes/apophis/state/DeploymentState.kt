package codes.apophis.state

enum class DeploymentState {
    REQUESTED,
    PROCESSING,
    FAILED,
    SUCCESS;

    fun nextState() : DeploymentState {
        when(this) {
            REQUESTED -> return PROCESSING
            PROCESSING -> return SUCCESS
        }
    }

}