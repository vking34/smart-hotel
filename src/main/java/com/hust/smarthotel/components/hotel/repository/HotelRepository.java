package com.hust.smarthotel.components.hotel.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.hust.smarthotel.components.hotel.domain_model.Hotel;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {
    @Query(value = "{ active : true }", fields = "{ photos : 0 , active : 0 }")
    public Page<Hotel> findAll(Pageable pageable);

    @Query("{ name: { '$regex': ?0 , '$options': 'i' }}")
    public Page<Hotel> findHotelsByName(String name, Pageable pageable);

    public Hotel findHotelById(String id);

    public Hotel findHotelByPhoneNumber(String phoneNumber);

    public void deleteHotelById(String id);

    @Query("{ location: { $near : { $geometry: { type: \"Point\", coordinates: [ ?0 , ?1 ] }, $maxDistance: ?2 } } }")
    public Page<Hotel> findHotelsAround(Double lng, Double lat, Long radius, Pageable pageable);

    public Page<Hotel> findHotelsByPointBetween(Integer minPoint, Integer maxPoint, Pageable pageable);

    @Query("{ point : { $gt : ?0, $lt : ?1 }, \"facilities.wifi\" : ?2, \"facilities.bar\" : ?3 , \"facilities.laundry\" : ?4 , \"facilities.fitness\" : ?5 }")
    public Page<Hotel> findHotelsByPointBetweenAndFacilitiesWifiAndFacilitiesBarAndFacilitiesLaundryAndFacilitiesFitness(Integer minPoint,
                                                                                                                             Integer maxPoint,
                                                                                                                             Boolean wifi,
                                                                                                                             Boolean bar,
                                                                                                                             Boolean laundry,
                                                                                                                             Boolean fitness,
                                                                                                                             Pageable pageable);

    @Query("{ average_price : { $gte : ?0, $lte : ?1 } }")
    public Page<Hotel> findHotelsByAveragePriceInRange(Integer minPrice, Integer maxPrice, Pageable pageable);
}
