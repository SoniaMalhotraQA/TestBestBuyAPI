package org.sonia;

import java.io.IOException;

import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class TestBestBuyServiceAPI {

	private static final String TEST_NAME = "latest";
	private static final String ID = "id";
	private static final String BAD_REQUEST_IN_CLASSNAME = "bad-request";
	private static final String INVALID_PARAMETERS = "Invalid Parameters";
	private static final String BAD_REQUEST = "BadRequest";
	private static final String APPLICATION_JSON = "application/json";
	private static final String CONTENT_TYPE_HEADER = "Content-Type";
	private static final String SERVICES_URL = "/services";
	private static final String SERVICES_SLASH_URL = "/services/";
	private static final String CLASS_NAME = "className";
	private static final String MESSAGE = "message";
	private static final String NAME = "name";

	public RequestSpecification httpRequest;
	public int testId = 10;

	@BeforeTest
	public void setup() throws IOException {
		PropertiesReader reader=new PropertiesReader("configuration.properties");
		RestAssured.baseURI = reader.getProperty("apiServerURL") + ":" + reader.getProperty("apiServerPort");
		httpRequest = RestAssured.given();
	}

	@Test(priority = 1)
	public void testGet() {
		Response response = httpRequest.request(Method.GET, SERVICES_URL);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.header("content-encoding"), "gzip");
	}

	@Test(priority = 3)
	public void testGetByParams() {
		Response response = httpRequest.request(Method.GET, SERVICES_SLASH_URL + testId);
		Assert.assertEquals(response.statusCode(), 200);
		Assert.assertEquals(response.getBody().jsonPath().get(NAME), TEST_NAME);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 2)
	public void testPost() {
		JSONObject param = new JSONObject();
		param.put(NAME, TEST_NAME);
		httpRequest.header(CONTENT_TYPE_HEADER, APPLICATION_JSON);
		httpRequest.body(param.toString());
		Response response = httpRequest.request(Method.POST, SERVICES_URL);
		Assert.assertEquals(response.getStatusCode(), 201);
		Assert.assertEquals(response.getBody().jsonPath().get(NAME), TEST_NAME);
		testId = response.getBody().jsonPath().get(ID);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 4)
	public void testPatch() {
		JSONObject param = new JSONObject();
		final String newName = "Changed Name";
		param.put(NAME, newName);
		httpRequest.header(CONTENT_TYPE_HEADER, APPLICATION_JSON);
		httpRequest.body(param.toString());
		Response response = httpRequest.request(Method.PATCH, SERVICES_SLASH_URL + testId);
		Assert.assertEquals(response.getStatusCode(), 200);
		Assert.assertEquals(response.getBody().jsonPath().get(NAME), newName);
		Assert.assertEquals(response.getBody().jsonPath().get(ID), testId);

	}

	@Test(priority = 5)
	public void testDelete() {
		Response deleteResponse = httpRequest.request(Method.DELETE, SERVICES_SLASH_URL + testId);
		Assert.assertEquals(deleteResponse.statusCode(), 200);
		Response getResponse = httpRequest.request(Method.GET, SERVICES_SLASH_URL + testId);
		Assert.assertEquals(getResponse.statusCode(), 404);
		Assert.assertEquals(getResponse.getBody().jsonPath().get(NAME), "NotFound");
		Assert.assertEquals(getResponse.getBody().jsonPath().get(MESSAGE), "No record found for id '" + testId + "'");
		Assert.assertEquals(getResponse.getBody().jsonPath().get(CLASS_NAME), "not-found");
	}

	@Test(priority = 6)
	public void testPostEmptyBody() {
		JSONObject param = new JSONObject();
		httpRequest.header(CONTENT_TYPE_HEADER, APPLICATION_JSON);
		httpRequest.body(param.toString());
		Response response = httpRequest.request(Method.POST, SERVICES_URL);
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getBody().jsonPath().get(NAME), BAD_REQUEST);
		Assert.assertEquals(response.getBody().jsonPath().get(MESSAGE), INVALID_PARAMETERS);
		Assert.assertEquals(response.getBody().jsonPath().get(CLASS_NAME), BAD_REQUEST_IN_CLASSNAME);
	}

	@SuppressWarnings("unchecked")
	@Test(priority = 7)
	public void testPostInvalidBody() {
		JSONObject param = new JSONObject();
		param.put("invalidKey", "invalidValue");
		httpRequest.header(CONTENT_TYPE_HEADER, APPLICATION_JSON);
		httpRequest.body(param.toString());
		Response response = httpRequest.request(Method.POST, SERVICES_URL);
		Assert.assertEquals(response.getStatusCode(), 400);
		Assert.assertEquals(response.getBody().jsonPath().get(NAME), BAD_REQUEST);
		Assert.assertEquals(response.getBody().jsonPath().get(MESSAGE), INVALID_PARAMETERS);
		Assert.assertEquals(response.getBody().jsonPath().get(CLASS_NAME), BAD_REQUEST_IN_CLASSNAME);
	}

	@Test(priority = 8)
	public void testDeleteInvalid() {
		Response deleteResponse = httpRequest.request(Method.DELETE, SERVICES_SLASH_URL + "abc");
		Assert.assertEquals(deleteResponse.statusCode(), 404);
		Assert.assertEquals(deleteResponse.getBody().jsonPath().get(NAME), "NotFound");
		Assert.assertEquals(deleteResponse.getBody().jsonPath().get(MESSAGE), "No record found for id 'abc'");
		Assert.assertEquals(deleteResponse.getBody().jsonPath().get(CLASS_NAME), "not-found");
	}

}
