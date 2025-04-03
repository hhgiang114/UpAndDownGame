package service

import gui.Refreshable

/**
 * Abstract service class that handles multiples [Refreshable]s (usually UI elements) which are notified of
 * changes to refresh via [onAllRefreshables] method
 */
abstract class AbstractRefreshingService {
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * Adds a new [Refreshable] to the list that gets called whenever [onAllRefreshables] is used.
     * @property newRefreshable The [Refreshable] to be added
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        refreshables += (newRefreshable)
    }

    /**
     * Adds each of the provided [Refreshable]s to the list of refreshables.
     * @param method The [Refreshable]s to be added
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}