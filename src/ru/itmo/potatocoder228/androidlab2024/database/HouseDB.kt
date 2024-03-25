
package database
import kotlin.collections.emptyList
import model.House
import database.interfaces.*
import exceptions.DBException
public class HouseDB : HouseCollection{
    var collection = mutableListOf<House>()

    companion object { 
        var lastId : Int = 0
    }
    override fun save(house: House){
        house.id = lastId;
        lastId++;
        collection.add(house) || throw DBException();
    }

    override fun update(house: House){
        collection.removeIf{ it.id == house.id}
        collection.add(house) || throw DBException();
    }

    override fun findByUserId(id: Int) : House{
        return collection.firstOrNull{ it.hostId == id } ?: throw DBException();
    }
    override fun findById(id: Int) : House{
        return collection.firstOrNull() { it.id == id } ?: throw DBException();
    }


}