
package database
import model.House
import database.interfaces.*
import repository.HouseRepository

public class HouseDB(private val houseRepository : HouseRepository) : HouseCollection{

    override fun save(house: House){
        houseRepository.saveHouse(house);
    }

    override fun update(house: House){
        houseRepository.updateHouse(house);
    }

    override fun findByUserId(id: Int) : House{
        return houseRepository.findByUserId(id);
    }
    override fun findById(id: Int) : House{
        return houseRepository.findById(id)
    }


}