package br.com.fiap.service;

import br.com.fiap.dao.TripRepository;
import br.com.fiap.model.Trip;
import br.com.fiap.utils.S3Manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class TripService {

    private final TripRepository repository = new TripRepository();
    private static final String bucketNamePattern = "%s-%s-%s-%s";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String bucketUrlPattern = "https://%s.s3.amazon.aws.com";

    public Trip createTripRecord(Trip trip){
        String bucketName = String.format(bucketNamePattern,
                trip.getCountry(),
                trip.getCity(),
                LocalDate.now().format(formatter),
                100000 + new Random().nextInt(900000));

        trip.setUrl(String.format(bucketUrlPattern,
                S3Manager.s3Client.createBucket(bucketName).getName()));

        return repository.save(trip);
    }

    public List<Trip> getTripRecordsByPeriod(String start, String end){
        return this.repository.findByPeriod(start, end);
    }

    public List<Trip> getTripRecordById(String id){
        return this.repository.findById(id);
    }
}
