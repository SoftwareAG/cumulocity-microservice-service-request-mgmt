package cumulocity.microservice.service.request.mgmt.controller;

import java.util.Map;

import javax.validation.Valid;

import cumulocity.microservice.service.request.mgmt.model.ServiceOrder;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestPriority;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@Schema(description = "Service Request object at Cumulocity IoT Platform. This object is the representative / proxy for external system object like ticket, issue, notification, incident etc.. It contains the life cycle information of it, IoT data references (device, alarms, events and measurement) and meta data for the service request like owner, creation time etc.. It has a flexible key & value list \"customProperties\" to add custom properties as well.")
public class ServiceRequestPatchRqBody {

	@Schema(description = "Service request status")
	@Valid
	private ServiceRequestStatus status;

	@Schema(description = "Servcie request priority")
	@Valid
	private ServiceRequestPriority priority;

	@Schema(description = "Service request title / summary")
	private String title;

	@Schema(description = "Service request detailed description")
	private String description;

	@Schema(description = "Service request active flag, shows if the service request is active!", example = "true")
	private Boolean isActive;
	
	@Schema(description = "Service request external ID, contains the service request object ID of the external system.", example = "123456789")
	private String externalId;

	@Schema(description = "Service Order")
	private ServiceOrder order;

	@Schema(description = "Custom specific properties")
	private Map<String, String> customProperties;
}
