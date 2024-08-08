package cumulocity.microservice.service.request.mgmt.service;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.core.io.Resource;

import cumulocity.microservice.service.request.mgmt.controller.ServiceRequestPatchRqBody;
import cumulocity.microservice.service.request.mgmt.controller.ServiceRequestPostRqBody;
import cumulocity.microservice.service.request.mgmt.model.RequestList;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequest;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestDataRef;
import cumulocity.microservice.service.request.mgmt.model.ServiceRequestStatus;
import cumulocity.microservice.service.request.mgmt.service.c8y.EventAttachment;
import cumulocity.microservice.service.request.mgmt.service.c8y.ServiceRequestServiceC8y.ServiceRequestValidationResult;

public interface ServiceRequestService {
	public ServiceRequestValidationResult validateNewServiceRequest(ServiceRequestPostRqBody serviceRequest, String owner);

	public ServiceRequestValidationResult validateAlarm(ServiceRequestDataRef alarmRef);

	public ServiceRequest createServiceRequest(ServiceRequestPostRqBody serviceRequest, String owner);
	
	public ServiceRequest updateServiceRequest(String id, ServiceRequestPatchRqBody serviceRequest);
	
	public ServiceRequest updateServiceRequestStatus(String id, ServiceRequestStatus status);
	
	public ServiceRequest updateServiceRequestActive(String id, Boolean isActive);
	
	public ServiceRequest getServiceRequestById(String id);
	
	public RequestList<ServiceRequest> getAllServiceRequestByFilter(String deviceId, Integer pageSize, Integer pageNumber, Boolean withTotalPages, String[] statusList, Long[] priorityList, String[] orderBy);
	
	public RequestList<ServiceRequest> getActiveServiceRequestByFilter(String deviceId, Integer pageSize, Integer pageNumber, Boolean withTotalPages, String[] statusList, Long[] priorityList, String[] orderBy);
	
	public Collection<ServiceRequest> getAllServiceRequestBySyncStatus(Boolean assigned);

	public Collection<ServiceRequest> getAllServiceRequestBySyncStatus(Boolean assigned, String[] serviceRequestIds);
	
	public void deleteServiceRequest(String id);

	public void uploadAttachment(Resource resource, String contentType, byte[] fileBytes, String serviceRequestId);
	
	public EventAttachment downloadAttachment(String serviceRequestId);

    public ServiceRequest addAlarmRefToServiceRequest(String serviceRequestId, @Valid ServiceRequestDataRef alarmRef);
}
