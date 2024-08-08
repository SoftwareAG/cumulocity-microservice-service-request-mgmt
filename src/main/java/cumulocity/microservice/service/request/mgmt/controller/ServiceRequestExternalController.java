package cumulocity.microservice.service.request.mgmt.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cumulocity.microservice.service.request.mgmt.model.ServiceRequest;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestComment;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestStatus;
import cumulocity.microservice.service.request.mgmt.service.ServiceRequestCommentService;
import cumulocity.microservice.service.request.mgmt.service.ServiceRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Service request controller
 * 
 * @author APES
 *
 */
@RestController
@RequestMapping("/api/adapter/service/request")
public class ServiceRequestExternalController {

	private ServiceRequestService serviceRequestService;
	
	private ServiceRequestCommentService serviceRequestCommentService;
	
	
	@Autowired
	public ServiceRequestExternalController(ServiceRequestService serviceRequestService, ServiceRequestCommentService serviceRequestCommentService) {
		super();
		this.serviceRequestService = serviceRequestService;
		this.serviceRequestCommentService = serviceRequestCommentService;
	}

	@Operation(summary = "GET service request list", description = "Returns a list of all service requests in IoT Platform. Additional query parameter allow to filter that list. Parameter assigned=false returns all service requests which are not yet synchronized to external system. Parameter assigned=true returns all synchronized service requests.", tags = {})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK") })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<ServiceRequest>> getServiceRequestList(
		@Parameter(in = ParameterIn.QUERY, description = "Filter, \"true\" returns all service request with external Id assigned, \"false\" returns service requests which doesn't have external Id assigned." , schema = @Schema()) @Valid @RequestParam(value = "assigned", required = true) Boolean assigned,
		@Parameter(in = ParameterIn.QUERY, description = "Filter by service request IDs, returns all service requests with the IDs defined in that list", schema = @Schema()) @Valid @RequestParam(value = "serviceRequestIds", required = false) String[] serviceRequestIds) {
		
		Collection<ServiceRequest> serviceRequestList = new ArrayList<>();
		
		if(serviceRequestIds != null && serviceRequestIds.length > 0) {
			serviceRequestList = serviceRequestService.getAllServiceRequestBySyncStatus(assigned, serviceRequestIds);
		}else{
			serviceRequestList = serviceRequestService.getAllServiceRequestBySyncStatus(assigned);
		}

		return new ResponseEntity<Collection<ServiceRequest>>(serviceRequestList, HttpStatus.OK);
	}
	
	@Operation(summary = "Returns all user comments of specific service request by internal Id.", description = "Each service request can have n comments. This endpoint returns the complete list of user comments of a specific service request.")
	@ApiResponses(value = { 
			@ApiResponse(responseCode = "200", description = "Ok", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ServiceRequestComment.class)))),
			@ApiResponse(responseCode = "404", description = "Not found")})
	@GetMapping(path = "/{serviceRequestId}/comment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<ServiceRequestComment>> getServiceRequestCommentList(@PathVariable String serviceRequestId) {
		List<ServiceRequestComment> commentListByFilter = serviceRequestCommentService.getCompleteUserCommentListByServiceRequest(serviceRequestId);
		return new ResponseEntity<List<ServiceRequestComment>>(commentListByFilter, HttpStatus.OK);
	}

	@Operation(summary = "UPDATE service request status by Id", description = "Updates specific service status request.", tags = {})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceRequest.class))),
			@ApiResponse(responseCode = "404", description = "Not Found") })
	@PutMapping(path = "/{serviceRequestId}/status", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceRequest> updateServiceRequestStatusById(
			@PathVariable String serviceRequestId, @RequestBody ServiceRequestStatus serviceRequestStatus) {
		ServiceRequest sr = serviceRequestService.updateServiceRequestStatus(serviceRequestId, serviceRequestStatus);
		if(sr == null) {
			return new ResponseEntity<ServiceRequest>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ServiceRequest>(sr, HttpStatus.OK);
	}

	@Operation(summary = "UPDATE service request active status by Id", description = "Updates specific service status request.", tags = {})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceRequest.class))),
			@ApiResponse(responseCode = "404", description = "Not Found") })
	@PutMapping(path = "/{serviceRequestId}/active", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ServiceRequest> updateServiceRequestIsActiveById(
			@PathVariable String serviceRequestId, @RequestBody Boolean isActive) {
		ServiceRequest sr = serviceRequestService.updateServiceRequestActive(serviceRequestId, isActive);
		if(sr == null) {
			return new ResponseEntity<ServiceRequest>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ServiceRequest>(sr, HttpStatus.OK);
	}
}
