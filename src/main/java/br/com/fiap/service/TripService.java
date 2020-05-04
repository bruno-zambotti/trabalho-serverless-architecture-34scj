package br.com.fiap.service;

import br.com.fiap.dao.TripsRepository;
import br.com.fiap.model.Trip;
import br.com.fiap.utils.S3Manager;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

public class TripService {

    private final TripsRepository repository = new TripsRepository();
    private static final String bucketNamePattern = "%s-%s-%s-%s";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final String bucketUrlPattern = "https://%s.s3.amazonaws.com";

    public Trip createTripRecord(Trip trip){
        String bucketName = String.format(bucketNamePattern,
                getValue(trip.getCountry()),
                getValue(trip.getCity()),
                LocalDate.now().format(formatter),
                getARandomNumber());

        trip.setUrl(String.format(bucketUrlPattern, getS3BucketName(bucketName)));

        return repository.save(trip);
    }

    private String getS3BucketName(String bucketName) {
        final String localEnvironment = System.getenv("LOCAL_ENVIRONMENT");

        if (localEnvironment != null && !localEnvironment.isEmpty()) {
            return bucketName;
        } else {
            return S3Manager.s3Client.createBucket(bucketName).getName();
        }
    }

    public List<Trip> getTripRecordsByPeriod(String start, String end){
        return this.repository.findByPeriod(start, end);
    }

    public List<Trip> getTripRecordById(String id){
        return this.repository.findById(id);
    }

    private String getValue(String value){
        return value.toLowerCase().replace(" ", "");
    }

    private int getARandomNumber() {
        return 100000 + new Random().nextInt(900000);
    }
}
