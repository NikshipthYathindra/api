package CrudAPI;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

@RestController
@RequestMapping("/api")
public class VegetablesController {
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ConfigurableApplicationContext context;

	@GetMapping("/hello")
	public MyJsonObject sayHello() {
		MyJsonObject jsonObject = new MyJsonObject();
		jsonObject.setMessage("Hello, Spring Boot!");
		return jsonObject;
	}

	@GetMapping("/shutdown")
	public void shutdown() {
		SpringApplication.exit(context, () -> 0);
	}

	@GetMapping("/stop")
	public MyJsonObject getdown() {
		MyJsonObject jsonObject = new MyJsonObject();
		jsonObject.setMessage("Hello, Spring Boot stop!");
		return jsonObject;
	}

	@GetMapping("/fetch")
	public JsonNode fetchData(@RequestParam("key") String key) {
		JsonNode jsonObject = null;
		try {
			String jsonFilePath = "D:/Spring-boot/BookStore/resources/data.json";

			jsonObject = fetchJsonObjectByKey(jsonFilePath, key);

			// Print the JSON object

		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonObject;
	}

	@PostMapping("/write")
	public ResponseEntity<String> postJsonAndWriteToFile(@RequestBody Map<String, Object> requestBody) {
		// Check if the request body is not empty
		if (requestBody != null) {
			// Convert the request body to JSON

			// Serialize the request body to JSON
			HashMap<String, MyData> dataList = null;

			try {
				dataList = loadExistingData("D:/Spring-boot/BookStore/resources/data.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Create new data to append

			Set<String> keySet = requestBody.keySet();
			System.out.println(keySet);
			Object[] element = keySet.toArray();

			Gson gson = new Gson();
			String jsonString = String.valueOf(requestBody.get(String.valueOf(element[0])));
			MyData myData = gson.fromJson(jsonString, MyData.class);
			MyData newData = new MyData(myData.getName(), myData.getAge());

			// Append the new data to the existing list
			dataList.put(String.valueOf(element[0]), newData);

			// Write the updated list back to the JSON file
			writeDataToFile("D:/Spring-boot/BookStore/resources/data.json", dataList);

			// Write the JSON request to a local file

			return ResponseEntity.ok("JSON data received and written to file successfully.");

		} else {
			return ResponseEntity.badRequest().body("Request body is empty.");
		}
	}

	private static void writeDataToFile(String filePath, HashMap<String, MyData> dataList) {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			// Write the updated data to the file (overwrite the entire file)
			objectMapper.writeValue(new File(filePath), dataList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// D:\Spring-boot\BookStore\resources

	public JsonNode fetchJsonObjectByKey(String jsonFilePath, String key) throws IOException {
		// Create an ObjectMapper (Jackson)

		// Read the JSON file into a JsonNode
		JsonNode rootNode = objectMapper.readTree(new File(jsonFilePath));

		// Check if the specified key exists in the JSON object
		if (rootNode.has(key)) {
			return rootNode.get(key);
		} else {
			throw new IllegalArgumentException("Key not found in JSON: " + key);
		}
	}

	@GetMapping("/update")
	public ResponseEntity<String> updateJSONObjectByKey(@RequestBody Map<String, Object> requestBody) {

		if (requestBody != null) {
			// Convert the request body to JSON

			// Serialize the request body to JSON
			HashMap<String, MyData> dataList = null;

			try {
				dataList = loadExistingData("D:/Spring-boot/BookStore/resources/data.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// Create new data to append

			Set<String> keySet = requestBody.keySet();
			System.out.println(keySet);
			Object[] element = keySet.toArray();

			Gson gson = new Gson();
			String jsonString = String.valueOf(requestBody.get(String.valueOf(element[0])));
			MyData myData = gson.fromJson(jsonString, MyData.class);
			MyData newData = new MyData(myData.getName(), myData.getAge());

			// Append the new data to the existing list
			dataList.put(String.valueOf(element[0]), newData);

			// Write the updated list back to the JSON file
			writeDataToFile("D:/Spring-boot/BookStore/resources/data.json", dataList);

			// Write the JSON request to a local file

			return ResponseEntity.ok("JSON data received and written to file successfully.");

		} else {
			return ResponseEntity.badRequest().body("Request body is empty.");
		}

	}

	@GetMapping("/delete")
	public void deleteJSONObjectByKey(@RequestParam("key") String key) {
		HashMap<String, MyData> dataList = null;

		try {
			dataList = loadExistingData("D:/Spring-boot/BookStore/resources/data.json");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dataList.remove(key);

		writeDataToFile("D:/Spring-boot/BookStore/resources/data.json", dataList);

	}

	public static HashMap<String, MyData> loadExistingData(String filePath) throws IOException {
		// Create an ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();

		// Read the JSON data from the file into a HashMap
		File file = new File(filePath);

		if (file.exists()) {
			return objectMapper.readValue(file, new TypeReference<HashMap<String, MyData>>() {
			});
		} else {
			// Handle the case where the file doesn't exist
			return new HashMap<>();
		}
	}

}

class MyJsonObject {
	private String message;

	// Getters and setters

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}

class MyData {
	private String name;
	private int age;

	public MyData() {
	}

	public MyData(String name, int age) {
		this.name = name;
		this.age = age;
	}

	// Getters and setters (or you can use lombok annotations)

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
