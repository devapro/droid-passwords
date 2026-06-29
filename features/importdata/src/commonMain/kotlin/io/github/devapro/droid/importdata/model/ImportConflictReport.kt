package io.github.devapro.droid.importdata.model

import io.github.devapro.droid.data.vault.VaultItemModel

/**
 * Summary of how parsed items compare against the active vault.
 *
 * @param parsed every item parsed from the source file (preserved in source order)
 * @param matched parsed items whose (title, username) matches an existing item in the active vault
 * @param fresh parsed items that don't match any existing item
 */
data class ImportConflictReport(
    val parsed: List<VaultItemModel>,
    val matched: List<VaultItemModel>,
    val fresh: List<VaultItemModel>,
) {
    val totalParsed: Int get() = parsed.size
    val matchedCount: Int get() = matched.size
    val freshCount: Int get() = fresh.size
}
