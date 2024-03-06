
package database
import kotlin.collections.emptyList
import model.House
import database.interfaces.*
public class HouseDB : HouseCollection{
    var collection = mutableListOf<House>()
    override fun addHouse(house: House){
        collection.add(house)
    }

    override fun getHouseByUserID(id: Int) : House{
        return collection.first { it.hostId == id }
    }
    override fun findById(id: Int) : House{
        return collection.first { it.id == id }
    }

}