package com.crossover.techtrial.controller;

import com.crossover.techtrial.model.HourlyElectricity;
import com.crossover.techtrial.model.Panel;
import com.crossover.techtrial.service.HourlyElectricityService;
import com.crossover.techtrial.service.PanelService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;


/**
 * PanelControllerTest class will test all APIs in PanelController.java.
 * @author Crossover
 *
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PanelControllerTest {
  
  MockMvc mockMvc;
  
  @Mock
  private PanelController panelController;
  
  
  @Autowired
  private TestRestTemplate template;

  @Before
  public void setup() throws Exception {
    mockMvc = MockMvcBuilders.standaloneSetup(panelController).build();
  }

  
  /*
    registerPanel method TESTS
     
   */
  
  @Test
  public void testPanelShouldBeRegistered() throws Exception {
    HttpEntity<Object> panel = getHttpEntity(
        "{\"serial\": \"5463728981235864\", \"longitude\": \"54.12336\"," 
            + " \"latitude\": \"54.123232\",\"brand\":\"tesla\" }");
    ResponseEntity<Panel> response = template.postForEntity(
        "/api/register", panel, Panel.class);
    Assert.assertEquals(202,response.getStatusCode().value());
  }

  @Test
  public void testPanelSerialLengthValidation() throws Exception {
    HttpEntity<Object> panel = getHttpEntity(
        "{\"serial\": \"54637289812329\", \"longitude\": \"54.123236\"," 
            + " \"latitude\": \"54.123232\",\"brand\":\"tesla\" }");
    ResponseEntity<Panel> response = template.postForEntity(
        "/api/register", panel, Panel.class);

    Assert.assertEquals(400,response.getStatusCode().value());
  }
  
  @Test
  public void testPanelLongitudeSixDecimals() throws Exception {
    HttpEntity<Object> panel = getHttpEntity(
        "{\"serial\": \"5463728981234895\", \"longitude\": \"54.12890776\"," 
            + " \"latitude\": \"54.123092\",\"brand\":\"tesla\" }");
    ResponseEntity<Panel> response = template.postForEntity(
        "/api/register", panel, Panel.class);

    Assert.assertEquals(400,response.getStatusCode().value());
  }
  
  @Test
  public void testPanelLatitudeSixDecimals() throws Exception {
    HttpEntity<Object> panel = getHttpEntity(
        "{\"serial\": \"5463728981230452\", \"longitude\": \"54.143876\"," 
            + " \"latitude\": \"54.12334856\",\"brand\":\"tesla\" }");
    ResponseEntity<Panel> response = template.postForEntity(
        "/api/register", panel, Panel.class);

    Assert.assertEquals(400,response.getStatusCode().value());
  }
  
  @Test
  public void testPanelUniqueSerial() throws Exception {
    HttpEntity<Object> panel2 = getHttpEntity(
        "{\"serial\": \"5463728981235864\", \"longitude\": \"54.123876\"," 
            + " \"latitude\": \"54.123876\",\"brand\":\"tesla\" }");
    ResponseEntity<Panel> response = template.postForEntity(
        "/api/register", panel2, Panel.class);

    Assert.assertEquals(400,response.getStatusCode().value());
  }
  
  /*
    saveHourlyElectricity TESTS
  */
  
  @Test
  public void testHourlyElectricityShouldBeSave() throws Exception {
    HttpEntity<Object> hourlyElectricity = getHttpEntity(
        "{\"generatedElectricity\": \"234\", \"readingAt\": \"2018-02-25T11:00:00\"}");
    ResponseEntity<HourlyElectricity> response = template.postForEntity(
        "/api/panels/5463728981235864/hourly", hourlyElectricity, HourlyElectricity.class);

    Assert.assertEquals(200,response.getStatusCode().value());
  }
  
  @Test
  public void testHourlyElectricityPanelNotFound() throws Exception {
    HttpEntity<Object> hourlyElectricity = getHttpEntity(
        "{\"generatedElectricity\": \"234\", \"readingAt\": \"2018-03-25T11:00:00\"}");
    ResponseEntity<HourlyElectricity> response = template.postForEntity(
        "/api/panels/3456789054332344/hourly", hourlyElectricity, HourlyElectricity.class);

    Assert.assertEquals(404,response.getStatusCode().value());
  }
  
  
  @Test
  public void testGetHourlyElectricity() throws Exception {
    HttpEntity<Object> panel = getHttpEntity(
        "{\"serial\": \"5874685210302159\", \"longitude\": \"54.123336\"," 
            + " \"latitude\": \"54.123232\",\"brand\":\"tesla\" }");
    template.postForEntity(
        "/api/register", panel, Panel.class);  
      
    HttpEntity<Object> hourlyElectricity = getHttpEntity(
        "{\"generatedElectricity\": \"8569\", \"readingAt\": \"2018-10-30T11:00:00\"}");
    template.postForEntity(
        "/api/panels/5874685210302159/hourly", hourlyElectricity, HourlyElectricity.class);
    HttpEntity<Object> hourlyElectricity2 = getHttpEntity(
        "{\"generatedElectricity\": \"9658\", \"readingAt\": \"2018-10-30T11:10:00\"}");
    template.postForEntity(
        "/api/panels/5874685210302159/hourly", hourlyElectricity2, HourlyElectricity.class);
    HttpEntity<Object> hourlyElectricity3 = getHttpEntity(
        "{\"generatedElectricity\": \"2569\", \"readingAt\": \"2018-10-30T11:20:00\"}");
    template.postForEntity(
        "/api/panels/5874685210302159/hourly", hourlyElectricity3, HourlyElectricity.class);
    
    ResponseEntity<?> response = template.getForEntity("/api/panels/5874685210302159/hourly", HourlyElectricity.class);
    
    
    
    Assert.assertEquals(200,response.getStatusCode().value());
  }
  
   @Test
  public void testGetHourlyElectricityPanelNotFound() throws Exception {
    ResponseEntity<?> response = template.getForEntity("/api/panels/5874685226302159/hourly", HourlyElectricity.class);
    Assert.assertEquals(404,response.getStatusCode().value());
  }
  
  private HttpEntity<Object> getHttpEntity(Object body) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    return new HttpEntity<Object>(body, headers);
  }
}
