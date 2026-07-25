package com.bank.retail.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.bank.retail.persistence.entity.MockResponseData;
import com.bank.retail.persistence.repository.MockResponseDataRepositoryy;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class MockDataUtil {

    private final MockResponseDataRepositoryy mockResponseDataRepositoryy;

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final XmlMapper XML_MAPPER = new XmlMapper();

    /**
     * Execute method to fetch mock data and convert XML/JSON to Map (legacy approach)
     * @param microservice the microservice name
     * @param categoryCode the category code
     * @return Map<String, Object> converted from XML/JSON response
     */
    public Map<String, Object> execute(String microservice, String categoryCode) {
        try {
            log.info("Fetching mock data for microservice: {} and category: {}", microservice, categoryCode);

            // Fetch mock data from database
            Optional<MockResponseData> mockDataOptional = mockResponseDataRepositoryy
                    .findByMicroserviceAndCategoryCode(microservice, categoryCode);

            if (mockDataOptional.isEmpty()) {
                log.warn("No mock data found for microservice: {} and category: {}", microservice, categoryCode);
                return new HashMap<>();
            }

            MockResponseData mockData = mockDataOptional.get();
            String responseData = mockData.getResponseXml(); // Note: using responseXml field to store JSON/XML

            if (responseData == null || responseData.trim().isEmpty()) {
                log.warn("Empty response for microservice: {} and category: {}", microservice, categoryCode);
                return new HashMap<>();
            }

            // Try to determine if it's JSON or XML and convert accordingly
            Map<String, Object> result;
            if (responseData.trim().startsWith("{")) {
                // It's JSON
                result = convertJsonToMap(responseData);
                log.info("Successfully converted JSON to Map for microservice: {} and category: {}", microservice, categoryCode);
            } else {
                // It's XML
                result = convertXmlToMapWithJackson(responseData);
                log.info("Successfully converted XML to Map for microservice: {} and category: {}", microservice, categoryCode);
            }

            return result;

        } catch (Exception e) {
            log.error("Error executing mock data for microservice: {} and category: {}: {}",
                    microservice, categoryCode, e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Convert XML to Map using Jackson XML mapper (best approach)
     * @param xmlString the XML string to convert
     * @return Map<String, Object> representation of XML
     */
    private Map<String, Object> convertXmlToMapWithJackson(String xmlString) {
        try {
            // Convert XML to JsonNode
            JsonNode jsonNode = XML_MAPPER.readTree(xmlString);

            // Convert JsonNode to Map
            Map<String, Object> result = OBJECT_MAPPER.convertValue(jsonNode, Map.class);

            // Extract the meaningful content (skip SOAP envelope)
            return extractMeaningfulContent(result);

        } catch (Exception e) {
            log.error("Error converting XML to Map with Jackson: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }

    /**
     * Extract meaningful content from SOAP response and flatten structure
     * @param fullMap the full converted map
     * @return Map with meaningful content only
     */
    private Map<String, Object> extractMeaningfulContent(Map<String, Object> fullMap) {
        // Look for Body -> Response structure
        if (fullMap.containsKey("Body")) {
            Object bodyObj = fullMap.get("Body");
            if (bodyObj instanceof Map) {
                Map<String, Object> body = (Map<String, Object>) bodyObj;
                // Return the first meaningful element (skip SOAP structure)
                for (Map.Entry<String, Object> entry : body.entrySet()) {
                    if (!"Envelope".equals(entry.getKey()) && !"Body".equals(entry.getKey())) {
                        if (entry.getValue() instanceof Map) {
                            Map<String, Object> result = (Map<String, Object>) entry.getValue();
                            // Flatten structure if needed
                            return flattenStructure(result);
                        }
                    }
                }
            }
        }

        // Fallback: return the full map if no Body found
        return flattenStructure(fullMap);
    }

    /**
     * Flatten structure to make arrays direct
     * @param map the map to process
     * @return Map with flattened structure
     */
    private Map<String, Object> flattenStructure(Map<String, Object> map) {
        // Handle LFTBanks structure: LFTBanks={LFTBank=[...]} -> LFTBanks=[...]
        if (map.containsKey("LFTBanks")) {
            Object lftBanksObj = map.get("LFTBanks");
            if (lftBanksObj instanceof Map) {
                Map<String, Object> lftBanksMap = (Map<String, Object>) lftBanksObj;
                if (lftBanksMap.containsKey("LFTBank")) {
                    // Replace LFTBanks={LFTBank=[...]} with LFTBanks=[...]
                    map.put("LFTBanks", lftBanksMap.get("LFTBank"));
                }
            }
        }
        return map;
    }

    /**
     * Convert JSON to Map using Jackson ObjectMapper
     * @param jsonString the JSON string to convert
     * @return Map converted from JSON
     */
    private Map<String, Object> convertJsonToMap(String jsonString) {
        try {
            // Parse JSON to get the nested structure
            JsonNode rootNode = OBJECT_MAPPER.readTree(jsonString);

            // Check if Body node exists
            JsonNode bodyNode = rootNode.get("Body");
            if (bodyNode != null && !bodyNode.isNull()) {
                // Convert the Body node to Map
                Map<String, Object> result = OBJECT_MAPPER.convertValue(bodyNode, Map.class);
                log.debug("Converted JSON Body node to Map");
                return result;
            } else {
                // No Body node found, convert the entire root node to Map
                Map<String, Object> result = OBJECT_MAPPER.convertValue(rootNode, Map.class);
                log.debug("No Body node found, converted entire JSON root to Map");
                return result;
            }

        } catch (Exception e) {
            log.error("Error converting JSON to Map: {}", e.getMessage(), e);
            return new HashMap<>();
        }
    }
}