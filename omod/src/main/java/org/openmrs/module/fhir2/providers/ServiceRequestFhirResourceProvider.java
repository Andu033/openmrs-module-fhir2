/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.providers;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

import ca.uhn.fhir.rest.annotation.IdParam;
import ca.uhn.fhir.rest.annotation.Read;
import ca.uhn.fhir.rest.annotation.Search;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.Setter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.IdType;
import org.hl7.fhir.r4.model.ServiceRequest;
import org.openmrs.module.fhir2.FhirException;
import org.openmrs.module.fhir2.api.FhirServiceRequestService;
import org.openmrs.module.fhir2.util.FhirUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("fhirResources")
@Setter(AccessLevel.PACKAGE)
public class ServiceRequestFhirResourceProvider implements IResourceProvider {
	
	@Inject
	private FhirServiceRequestService serviceRequestService;
	
	@Override
	public Class<? extends IBaseResource> getResourceType() {
		return ServiceRequest.class;
	}

	@Search
	@SuppressWarnings("unused")
	public Bundle getAllServiceRequests(){
		return FhirUtils.convertSearchResultsToBundle(serviceRequestService.getAllServiceRequests());
	}
	
	@Read
	@SuppressWarnings("unused")
	public ServiceRequest getServiceRequestById(@IdParam @NotNull IdType id) {
		ServiceRequest serviceRequest = serviceRequestService.getServiceRequestByUuid(id.getIdPart());
		if (serviceRequest == null) {
			throw new ResourceNotFoundException("Could not find Service Request with Id " + id.getIdPart());
		}
		return serviceRequest;
	}
}
