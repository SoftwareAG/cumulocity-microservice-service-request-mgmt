package cumulocity.microservice.service.request.mgmt.service.c8y;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.cumulocity.model.idtype.GId;
import com.cumulocity.rest.representation.inventory.ManagedObjectRepresentation;
import com.cumulocity.sdk.client.inventory.InventoryApi;

import cumulocity.microservice.service.request.mgmt.model.ServiceRequestStatusConfig;

class ServiceRequestStatusConfigServiceC8yTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void testCreateOrUpdateStatusList() {
		InventoryApi inventoryApi = mock(InventoryApi.class);
		ManagedObjectRepresentation updatedStatusMo = createMo("5", "6");
		when(inventoryApi.update(Mockito.any())).thenReturn(updatedStatusMo);	
		
		ServiceRequestStatusConfigServiceC8y statusService = mock(ServiceRequestStatusConfigServiceC8y.class, withSettings().useConstructor(inventoryApi));
		ManagedObjectRepresentation statusMo = createMo("1", "2", "3");
		
		when(statusService.getManagedObjectRepresentation()).thenReturn(statusMo);
		when(statusService.createOrUpdateStatusList(Mockito.anyList())).thenCallRealMethod();
		
		
		List<ServiceRequestStatusConfig> newStatusList = new ArrayList<>();
		newStatusList.add(new ServiceRequestStatusConfig("5", "Status5"));
		newStatusList.add(new ServiceRequestStatusConfig("6", "Status6"));
		
		List<ServiceRequestStatusConfig> createOrUpdateStatusList = statusService.createOrUpdateStatusList(newStatusList);
		
		assertNotNull(createOrUpdateStatusList);
		assertEquals(2, createOrUpdateStatusList.size());
	}

	@Test
	void testGetStatusList() {
		ServiceRequestStatusConfigServiceC8y statusService = mock(ServiceRequestStatusConfigServiceC8y.class);
		ManagedObjectRepresentation statusMo = createMo("1", "2", "3");
		
		when(statusService.getManagedObjectRepresentation()).thenReturn(statusMo);
		when(statusService.getStatusList()).thenCallRealMethod();
	}

	@Test
	void testGetStatus() {
		ServiceRequestStatusConfigServiceC8y statusService = mock(ServiceRequestStatusConfigServiceC8y.class);
		ManagedObjectRepresentation statusListMo = createMo("1", "2", "3");

		when(statusService.getManagedObjectRepresentation()).thenReturn(statusListMo);
		when(statusService.getStatus(Mockito.anyString())).thenCallRealMethod();

		Optional<ServiceRequestStatusConfig> status = statusService.getStatus("2");
		assertNotNull(status);
		assertEquals("2", status.get().getId());
		assertEquals("Status2", status.get().getName());

		Optional<ServiceRequestStatusConfig> statusEmpty = statusService.getStatus("4");
		assertEquals(true, statusEmpty.isEmpty());
	}

	@Test
	void testDeleteStatus() {
		InventoryApi inventoryApi = mock(InventoryApi.class);
		ManagedObjectRepresentation updatedStatusMo = createMo("1", "2");
		when(inventoryApi.update(Mockito.any())).thenReturn(updatedStatusMo);

		ServiceRequestStatusConfigServiceC8y statusService = mock(ServiceRequestStatusConfigServiceC8y.class,
				withSettings().useConstructor(inventoryApi));
		ManagedObjectRepresentation statusMo = createMo("1", "2", "3");

		when(statusService.getManagedObjectRepresentation()).thenReturn(statusMo);
		doCallRealMethod().when(statusService).deleteStatus(Mockito.anyString());

		statusService.deleteStatus("3");
		statusService.deleteStatus("5");
	}

	private static ManagedObjectRepresentation createMo(String... statusList) {
		List<HashMap<String, Object>> hashMapList = new ArrayList<>();
		
		for(String status: statusList) {
			HashMap<String, Object> statusObj = new HashMap<>();
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_STATUS_ID, status);
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_STATUS_NAME, "Status"+status);
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_ALARM_STATUS_TRANSITION, "CLEARED");
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_IS_CLOSED_TRANSITION, Boolean.TRUE);
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_IS_DEACTIVATE_TRANSITON, Boolean.TRUE);
			statusObj.put(ServiceRequestStatusConfigObjectMapper.SR_IS_EXCLUDE_FOR_COUNTER, Boolean.TRUE);
			hashMapList.add(statusObj);
		}
		
		ManagedObjectRepresentation StatusMo = new ManagedObjectRepresentation();
		StatusMo.set(hashMapList, ServiceRequestStatusObjectMapper.SR_STATUS_LIST);
		StatusMo.setId(GId.asGId(123L));
		
		return StatusMo;
	}

}
