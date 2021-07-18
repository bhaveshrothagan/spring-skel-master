package com.cepheid.cloud.skel;

import com.cepheid.cloud.skel.controller.ItemController;
import com.cepheid.cloud.skel.model.Description;
import com.cepheid.cloud.skel.model.Item;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ItemControllerTest extends TestBase {

    JSONParser parser = new JSONParser();

    @Test
    public void testGetItems() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items");

        Collection<Item> items = itemController.get(new GenericType<Collection<Item>>() {
        });
        List<Item> itemList = new ArrayList<>(items);
        assertEquals(4, items.size());
        assertEquals(Long.valueOf(1L), itemList.get(0).getId());
        assertEquals("Lord of the rings", itemList.get(0).getName());
        assertEquals(0, itemList.get(0).getDescription().size());
    }

    @Test
    public void testGetItemsThrowException() {
        ItemController spy = mock(ItemController.class);
        when(spy.getItems()).thenThrow(new RuntimeException());
        verify(spy,times(0)).getItems();
    }

    @Test
    public void testGetItemById() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/1", "");

        Response response = itemController.get();
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        JSONObject itemJson = (JSONObject) json.get("body");
        JSONArray descriptions = (JSONArray) itemJson.get("description");
        assertEquals(200, status);
        assertEquals(1, itemJson.get("id"));
        assertEquals("Lord of the rings", itemJson.get("name"));
        assertEquals(0, descriptions.size());
    }

    @Test
    public void testGetItemByInvalidId() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/-1", "");
        Response response = itemController.get();
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("No item found", json.get("body"));
    }

    @Test
    public void testGetItemsByDescriptionName() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/description?name=test");

        Collection<Item> items = itemController.get(new GenericType<Collection<Item>>() {
        });
        assertEquals(0, items.size());
    }

    @Test
    public void testInsertItem() throws Exception {
        Item i = new Item();
        i.setName("test1");

        Builder itemController = getBuilder("/app/api/1.0/items/", "");
        Response response = itemController.post(Entity.json(i));
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Item added successfully", json.get("body"));
    }

    @Test
    public void testInsertItemThrowException() throws Exception {

        Builder itemController = getBuilder("/app/api/1.0/items/", "");
        Response response = itemController.post(Entity.json(null));
        int status = response.getStatus();
        System.out.println(response.getStatus());
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Error occurred", json.get("body"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/1");
        Item i = new Item();
        i.setName("updateName");

        List<Description> descriptions = new ArrayList<>();
        Description d1 = new Description(1L, "Desc1");
        Description d2 = new Description(2L, "Desc2");
        descriptions.add(d1);
        descriptions.add(d2);
        i.setDescription(descriptions);
        Response response = itemController.put(Entity.json(i));
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Item updated", json.get("body"));

        itemController = getBuilder("/app/api/1.0/items/1");
        response = itemController.get();
        httpEntity = response.readEntity(String.class);
        json = (JSONObject) parser.parse(httpEntity);
        JSONObject itemJson = (JSONObject) json.get("body");
        JSONArray descriptionsArr = (JSONArray) itemJson.get("description");
        assertEquals(1, itemJson.get("id"));
        assertEquals("updateName", itemJson.get("name"));
        assertEquals(2, descriptionsArr.size());
        JSONObject descObject = (JSONObject) descriptionsArr.get(0);
        assertEquals(1, descObject.get("did"));
        assertEquals("Desc1", descObject.get("name"));

        itemController = getBuilder("/app/api/1.0/items/description?name=Desc1");

        Collection<Item> items = itemController.get(new GenericType<Collection<Item>>() {
        });
        assertEquals(1, items.size());

    }

    @Test
    public void testUpdateItemToAddItem() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/10");
        Item i = new Item();
        i.setName("add item");

        Response response = itemController.put(Entity.json(i));
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Item added", json.get("body"));
    }

    @Test
    public void testDeleteItem() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/1", "");

        Response response = itemController.delete();
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Deleted successfully", json.get("body"));
    }

    @Test
    public void testInvalidDeleteItem() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/test", "");

        Response response = itemController.delete();
        int status = response.getStatus();
        assertEquals(404, status);
    }

    @Test
    public void testDeleteItemThrowException() throws Exception {
        Builder itemController = getBuilder("/app/api/1.0/items/-1", "");

        Response response = itemController.delete();
        int status = response.getStatus();
        String httpEntity = response.readEntity(String.class);
        JSONObject json = (JSONObject) parser.parse(httpEntity);
        assertEquals(200, status);
        assertEquals("Error occurred", json.get("body"));
    }
}
