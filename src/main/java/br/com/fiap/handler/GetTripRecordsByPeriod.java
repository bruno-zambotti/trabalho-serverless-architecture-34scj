package br.com.fiap.handler;

import java.util.ArrayList;
import java.util.List;

import br.com.fiap.model.Trip;
import br.com.fiap.service.TripService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import br.com.fiap.model.HandlerRequest;
import br.com.fiap.model.HandlerResponse;
import org.apache.http.HttpStatus;

public class GetTripRecordsByPeriod implements RequestHandler<HandlerRequest, HandlerResponse> {

	private final TripService service = new TripService();
	
	@Override
	public HandlerResponse handleRequest(HandlerRequest request, Context context) {

		final String start = request.getQueryStringParameters().get("start");
		final String end = request.getQueryStringParameters().get("end");

		context.getLogger().log("Searching for registered trips between " + start + " and " + end);

		final List<Trip> trips = service.getTripRecordsByPeriod(start, end);
		
		if(trips == null || trips.isEmpty()) {
			return HandlerResponse.builder().setStatusCode(HttpStatus.SC_OK).setObjectBody(new ArrayList<>()).build();
		}
		
		return HandlerResponse.builder().setStatusCode(HttpStatus.SC_OK).setObjectBody(trips).build();
	}
}