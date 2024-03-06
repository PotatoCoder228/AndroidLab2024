

import kotlin.collections.emptyList
public class HouseDB : HouseCollection{
    var collection = mutableListOf<House>()
    override fun addHouse(house: House){
        collection.add(house)
    }

}