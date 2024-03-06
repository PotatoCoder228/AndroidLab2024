package database.interfaces
import model.House
public interface HouseCollection{
    fun addHouse(house: House)
    fun getHouseByUserID(id: Int) : House
    fun findById(id: Int) : House
    
}