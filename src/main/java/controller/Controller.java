package controller;

import car.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.CarsRepository;


import java.util.Optional;

@RestController
@RequestMapping("/cars")
public class Controller {

    @Autowired
    private CarsRepository carsRepository;

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carsRepository.save(car);
    }


    @GetMapping
    public Page<Car> getCarsList(@RequestParam(required = false) Optional<Integer> page, @RequestParam(required = false) Optional<Integer> size) {
        if (page.isPresent() && size.isPresent()) {
            Sort sort = Sort.by(new Sort.Order(Sort.Direction.ASC, "modelName"), new Sort.Order(Sort.Direction.DESC, "type"));
            Pageable pageable = PageRequest.of(page.get(), size.get(), sort);
            Page<Car> car = carsRepository.findAll(pageable);
            return car;
        } else {
            Page<Car> pageCars = Page.empty();
            return pageCars;
        }
    }

    @GetMapping("/{id}")
    public Car getSingleCar(@PathVariable long id) {
        if (carsRepository.existsById(id)) {
            Car car = carsRepository.findById(id).get();
            return car;
        } else {
            return new Car();
        }
    }

    @PutMapping("/{id}")
    public Car updateCarType(@PathVariable long id, @RequestParam(required = false) String type) {
        if (carsRepository.existsById(id)) {
            Car myCar = carsRepository.findById(id).get();
            myCar.setType(type);
            return carsRepository.saveAndFlush(myCar);
        } else {
            return new Car();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        Optional<Car> optionalCar = carsRepository.findById(id);
        if (optionalCar.isPresent()) {
            carsRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @DeleteMapping("")
    public void deleteAllCars() {
        carsRepository.deleteAll();
    }
}
