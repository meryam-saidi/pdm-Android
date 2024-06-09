package tn.esprit.pdm.network.model

data class SurplusFoodItem(val id: String, val title: String) {
    override fun toString(): String {
        return title // Cela permet au Spinner d'afficher le titre
    }
}