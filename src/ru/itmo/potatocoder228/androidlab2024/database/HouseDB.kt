
package database
import kotlin.collections.emptyList
import model.House
import database.interfaces.*
public class HouseDB : HouseCollection{
    var collection = mutableListOf<House>()

    companion object { 
        var lastId : Int = 0
    }
    override fun save(house: House): Boolean{
        house.id = lastId;
        lastId++;
        return collection.add(house)
    }

    override fun findByUserId(id: Int) : House{
        return collection.first { it.hostId == id }
    }
    override fun findById(id: Int) : House{
        return collection.first { it.id == id }
    }


}