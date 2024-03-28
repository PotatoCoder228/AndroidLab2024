
package database
import model.House
import database.interfaces.*
import repository.HouseRepository

public class HouseDB(private val houseRepository : HouseRepository) : HouseCollection{
    companion object {
        //TODO это очевидно не хорошо. Решение --- хранить в бд и доставать, но медленно?
        var lastId : Long = 0
    }

    override fun save(house: House){
        house.id = lastId
        houseRepository.saveHouse(house)
        lastId++
    }

    override fun update(house: House){
        houseRepository.updateHouse(house);
    }

    override fun findByUserId(id: Long) : House{
        return houseRepository.findByUserId(id);
    }
    override fun findById(id: Long) : House{
        return houseRepository.findById(id)
    }


}