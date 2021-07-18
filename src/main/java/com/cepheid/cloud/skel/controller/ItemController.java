package com.cepheid.cloud.skel.controller;

import com.cepheid.cloud.skel.model.Item;
import com.cepheid.cloud.skel.repository.ItemRepository;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


// curl http:/localhost:9443/app/api/1.0/items

@Component
@Path("/api/1.0/items")
@Api(value = "Item", description = "Operations pertaining to Item", tags = {"Item"})
public class ItemController {

    private final ItemRepository mItemRepository;

    @Autowired
    public ItemController(ItemRepository itemRepository) {
        mItemRepository = itemRepository;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @ApiOperation(value = "Retrieve All Items", notes = "It will return all the items")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = Item.class, responseContainer = "List")})
    public Collection<Item> getItems() {
        try {
            return mItemRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GET
    @Path("/{mid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @ApiOperation(value = "Retrieve Item based on id", notes = "It will return item based on mid")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = Item.class, responseContainer = "List")})
    public ResponseEntity<?> getItemById(@ApiParam(value = "mid", required = true) @PathParam("mid") Long mid) {
        Optional<Item> item = Optional.of(mItemRepository.findById(mid)).get();
        if (item.isPresent()) {
            return ResponseEntity.ok().body(item.get());
        }
        return ResponseEntity.ok().body("No item found");
    }

    @GET
    @Path("/description")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    @ApiOperation(value = "Retrieve Items based on description name", notes = "It will return items based on description name")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = Item.class)})
    public Collection<Item> getItemsByDescriptionName(@ApiParam(value = "name", required = true, defaultValue = "") @QueryParam("name") String descriptionName) {
        List<Item> items = new ArrayList<>();
        mItemRepository.findAll().forEach(i -> i.getDescription().forEach(d -> {
            if (d.getName().equalsIgnoreCase(descriptionName)) {
                items.add(i);
            }
        }));
        return items;
    }

    @POST
    @Consumes({MediaType.APPLICATION_JSON})
    @ApiOperation(value = "Insert New Item", notes = "REST API to add item")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = String.class)})
    public ResponseEntity<String> insertItem(@RequestBody Item item) {
        try {
            if (mItemRepository.save(item) != null) {
                return ResponseEntity.ok().body("Item added successfully");
            }
        } catch (Exception e) {
            e.printStackTrace(); }
        return ResponseEntity.ok().body("Error occurred");
    }

    @PUT
    @Path("/{mid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Update Existing Item", notes = "REST API to update item")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = String.class)})
    public ResponseEntity<String> updateItem(@ApiParam(value = "mid", required = true) @PathParam("mid") Long mid, @RequestBody Item item) {
        return mItemRepository.findById(mid).map(newItem -> {
            if (item.getName() != null && !"".equalsIgnoreCase(item.getName().trim())) {
                newItem.setName(item.getName());
            }
            if (item.getDescription() != null) {
                newItem.setDescription(item.getDescription());
            }
            mItemRepository.save(newItem);
            return ResponseEntity.ok().body("Item updated");
        }).orElseGet(() -> {
            mItemRepository.save(item);
            return ResponseEntity.ok().body("Item added");
        });
    }

    @DELETE
    @Path("/{mid}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Delete Item", notes = "REST API to delete item")
    @ApiResponses(value = {
            @ApiResponse(code = 500, message = "Internal Error"),
            @ApiResponse(code = 404, message = "Not found"),
            @ApiResponse(code = 200, message = "Successful retrieval", response = String.class)})
    public ResponseEntity<String> deleteItem(@ApiParam(value = "mid", required = true) @PathParam("mid") Long mid) {
        try {
            mItemRepository.deleteById(mid);
            return ResponseEntity.ok().body("Deleted successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().body("Error occurred");
    }
}
