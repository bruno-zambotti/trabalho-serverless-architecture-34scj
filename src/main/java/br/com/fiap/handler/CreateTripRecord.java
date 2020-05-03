package br.com.fiap.handler;

import java.io.IOException;

import br.com.fiap.service.TripService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.fiap.model.HandlerRequest;
import br.com.fiap.model.HandlerResponse;
import br.com.fiap.model.Trip;
import org.apache.http.HttpStatus;

public class CreateTripRecord implements RequestHandler<HandlerRequest, HandlerResponse> {
	
	private final TripService service = new TripService();

	@Override
	public HandlerResponse handleRequest(final HandlerRequest request, final Context context) {

		Trip trip = null;
		try {
			trip = new ObjectMapper().readValue(request.getBody(), Trip.class);
		} catch (IOException e) {
			return HandlerResponse.builder().setStatusCode(HttpStatus.SC_BAD_REQUEST)
					.setRawBody("There is an error in your trip request").build();
		}
		context.getLogger().log("Creating a new trip record for city " + trip.getCity() + " from " + trip.getCountry());
		final Trip tripRecorded = service.createTripRecord(trip);
		return HandlerResponse.builder().setStatusCode(HttpStatus.SC_CREATED).setObjectBody(tripRecorded).build();
	}
}