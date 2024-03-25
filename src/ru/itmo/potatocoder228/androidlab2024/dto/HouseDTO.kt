package model
public data class HouseDTO(
    val description: String,
    val lampochka: Boolean,
)
public fun HouseDTO.toHouse() = House(0,description, lampochka,0)
public fun House.toHouseDTO() = HouseDTO(description, lampochka)
public fun House.setFromDTO(houseDTO: HouseDTO){
    description = houseDTO.description;
    lampochka = houseDTO.lampochka;
}