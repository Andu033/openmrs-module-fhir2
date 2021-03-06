/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.fhir2.api.translators.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.openmrs.Location;
import org.openmrs.module.fhir2.api.translators.LocationAddressTranslator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class LocationTranslatorImplTest {
	
	private static final String LOCATION_UUID = "c0938432-1691-11df-97a5-7038c432aaba";
	
	private static final String LOCATION_NAME = "Test location 1";
	
	private static final String LOCATION_DESCRIPTION = "Test description";
	
	private static final String LOCATION_LATITUDE = "25.5";
	
	private static final String LOCATION_LONGITUDE = "25.5";
	
	private LocationTranslatorImpl locationTranslator;
	
	private Location omrsLocation;
	
	@Mock
	private LocationAddressTranslator locationAddressTranslator;
	
	@Before
	public void setup() {
		omrsLocation = new Location();
		locationTranslator = new LocationTranslatorImpl();
		locationTranslator.setLocationAddressTranslator(locationAddressTranslator);
	}
	
	@Test
	public void shouldTranslateOpenmrsLocationToFhirLocation() {
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(omrsLocation);
		assertNotNull(fhirLocation);
		assertEquals(omrsLocation.getName(), fhirLocation.getName());
	}
	
	@Test
	public void shouldTranslateLocationUuidToFhiIdType() {
		omrsLocation.setUuid(LOCATION_UUID);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertNotNull(fhirLocation.getId());
		assertEquals(fhirLocation.getId(), LOCATION_UUID);
	}
	
	@Test
	public void shouldTranslateLocationNameToFhirNameType() {
		omrsLocation.setName(LOCATION_NAME);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertNotNull(fhirLocation.getName());
		assertEquals(fhirLocation.getName(), LOCATION_NAME);
	}
	
	@Test
	public void shouldTranslateLocationDescriptionToFhirDescriptionType() {
		omrsLocation.setDescription(LOCATION_DESCRIPTION);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertNotNull(fhirLocation.getDescription());
		assertEquals(fhirLocation.getDescription(), LOCATION_DESCRIPTION);
	}
	
	@Test
	public void toFhirResource_shouldReturnEmptyLocationWhenCalledWitEmptyOmrsLocation() {
		org.hl7.fhir.r4.model.Location location = locationTranslator.toFhirResource(null);
		assertNull(location.getName());
		assertNull(location.getDescription());
		assertNull(location.getId());
		assertNull(location.getPosition().getLatitude());
		assertNull(location.getPosition().getLongitude());
		assertNull(location.getAddress().getCity());
	}
	
	@Test
	public void toOpenmrsType_shouldReturnEmptyLocationWhenCalledWithEmptyFhirLocation() {
		Location location = locationTranslator.toOpenmrsType(null);
		assertNull(location.getName());
		assertNull(location.getDescription());
		assertNull(location.getId());
		assertNull(location.getLatitude());
		assertNull(location.getLongitude());
		assertNull(location.getCountry());
	}
	
	@Test
	public void shouldTranslateFhirLocationToOmrsLocation() {
		org.hl7.fhir.r4.model.Location location = new org.hl7.fhir.r4.model.Location();
		org.openmrs.Location omrsLocation = locationTranslator.toOpenmrsType(location);
		assertNotNull(omrsLocation);
		assertEquals(omrsLocation.getName(), location.getName());
	}
	
	@Test
	public void shouldTranslateLocationIdToUuid() {
		org.hl7.fhir.r4.model.Location location = new org.hl7.fhir.r4.model.Location();
		location.setId(LOCATION_UUID);
		org.openmrs.Location omrsLocation = locationTranslator.toOpenmrsType(location);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getUuid());
		assertEquals(omrsLocation.getUuid(), LOCATION_UUID);
	}
	
	@Test
	public void shouldTranslateLocationNameToOpenmrsNameType() {
		org.hl7.fhir.r4.model.Location location = new org.hl7.fhir.r4.model.Location();
		location.setName(LOCATION_NAME);
		org.openmrs.Location omrsLocation = locationTranslator.toOpenmrsType(location);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getName());
		assertEquals(omrsLocation.getName(), LOCATION_NAME);
	}
	
	@Test
	public void shouldTranslateLocationDescriptionToOpenmrsDescriptionType() {
		org.hl7.fhir.r4.model.Location location = new org.hl7.fhir.r4.model.Location();
		location.setDescription(LOCATION_DESCRIPTION);
		org.openmrs.Location omrsLocation = locationTranslator.toOpenmrsType(location);
		assertNotNull(omrsLocation);
		assertNotNull(omrsLocation.getDescription());
		assertEquals(omrsLocation.getDescription(), LOCATION_DESCRIPTION);
	}
	
	@Test
	public void shouldCreateFhirPositionObjectFromLatitudeAndLongitude() {
		omrsLocation.setLatitude(LOCATION_LATITUDE);
		omrsLocation.setLongitude(LOCATION_LONGITUDE);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertNotNull(fhirLocation.getPosition());
		assertNotNull(fhirLocation.getPosition().getLatitude());
		assertNotNull(fhirLocation.getPosition().getLongitude());
	}
	
	@Test
	public void shouldSetFhirLocationToActiveIfLocationIsNotRetired() {
		omrsLocation.setRetired(false);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertEquals(fhirLocation.getStatus(), org.hl7.fhir.r4.model.Location.LocationStatus.ACTIVE);
	}
	
	@Test
	public void shouldSetFhirLocationToInActiveIfLocationIsRetired() {
		omrsLocation.setRetired(true);
		org.hl7.fhir.r4.model.Location fhirLocation = locationTranslator.toFhirResource(omrsLocation);
		assertNotNull(fhirLocation);
		assertEquals(fhirLocation.getStatus(), org.hl7.fhir.r4.model.Location.LocationStatus.INACTIVE);
	}
	
}
