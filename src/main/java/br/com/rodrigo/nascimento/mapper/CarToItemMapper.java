package br.com.rodrigo.nascimento.mapper;

import br.com.rodrigo.nascimento.domain.Car;
import com.amazonaws.services.dynamodbv2.document.Item;

public class CarToItemMapper {

    public static Item mapper(Car car){
        return new Item()
                .withPrimaryKey("IdCar", car.getId())
                .withString("Model", car.getModel())
                .withNumber("Year", car.getYear())
                .withNumber("Mileage", car.getMileage())
                .withNumber("Price", car.getPrice());
    }
}
