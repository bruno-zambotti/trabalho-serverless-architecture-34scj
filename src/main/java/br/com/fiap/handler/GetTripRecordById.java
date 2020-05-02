package br.com.fiap.handler;

import java.util.List;

import br.com.fiap.model.Trip;
import br.com.fiap.service.TripService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.fiap.model.HandlerRequest;
import br.com.fiap.model.HandlerResponse;
import org.apache.http.HttpStatus;

public class GetTripRecordById implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripService service = new TripService();

	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String id = request.getPathParameters().get("id");

		context.getLogger().log("Searching for registered trip with id " + id);

		final List<Trip> trips = service.getTripRecordById(id);

		if (trips == null || trips.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(HttpStatus.SC_NOT_FOUND).build();
		}

		return HandlerResponse.builder().setStatusCode(HttpStatus.SC_OK).setObjectBody(trips).build();
	}
}