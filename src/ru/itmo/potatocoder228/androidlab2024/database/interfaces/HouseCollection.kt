package database.interfaces
import model.House
public interface HouseCollection{
    fun save(house: House)
    fun findByUserId(id: Long) : House
    fun findById(id: Long) : House
    fun update(house: House)

}