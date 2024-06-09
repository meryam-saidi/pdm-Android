package tn.esprit.pdm.network.model

import java.util.Date

data class Post (
    var id: Int,
    var photo: Int,
    var foodType: String,
    var donor: String,
    var quantity: Int,
    var description: String,
    //var expirationDate: String,
)
