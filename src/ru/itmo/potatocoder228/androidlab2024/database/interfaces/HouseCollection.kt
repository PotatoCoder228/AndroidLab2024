package database.interfaces
import model.House
public interface HouseCollection{
    fun save(house: House)
    fun findByUserId(id: Int) : House
    fun findById(id: Int) : House
    fun update(house: House)

}