
data class Category(var categoryid : Int,var category : String,var image : String="")

data class Shayari(var shayariid: Int, var shayari: String, val categoryid: Int,var status : Int)

data class Favourite(var shayariid : Int,var shayari: String,var status: Int)
