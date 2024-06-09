package tn.esprit.pdm.network.model

import java.util.UUID

data class Collection(
    var id: String,
    var foodType: String,
    var requestorId: String,
    var startDate: String,
    var endDate: String,
    var comment: String
)