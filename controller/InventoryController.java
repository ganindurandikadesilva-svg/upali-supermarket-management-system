package lk.upalisupermarket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import lk.upalisupermarket.dao.InventoryDao;
import lk.upalisupermarket.dao.UserDao;
import lk.upalisupermarket.entity.Inventory;
import lk.upalisupermarket.entity.Privilege;
import lk.upalisupermarket.entity.User;

@RestController // api implement krn mapping siyalla servlet container ekt add wenne me
// annotation eken
public class InventoryController {

  @Autowired // create instance for quotationrequest dao
  private InventoryDao inventoryDao;

  @Autowired
  private UserPrivilegeController userPrivilegeController;// create an instance of UserPrivilegeContoller to access
                                                          // the data for secuirity
  // privileged user kenek da kiyl blnna

  

  @Autowired // generate instance
  private UserDao userDao;

  // mapping to return inventory ui(URL-->"/inventory")
  @RequestMapping(value = "/inventory")
  public ModelAndView loadInventoryPage() {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();// get authentication object
    User logUser=userDao.getByUserName(auth.getName());
    ModelAndView InventoryPage = new ModelAndView();

    InventoryPage.setViewName("inventory.html");
    InventoryPage.addObject("title", "Upali Supermarket-Item Inventory Management");// set the title for page
    InventoryPage.addObject("loggedUserName", auth.getName());// get username of the logged user
    
    InventoryPage.addObject("loggedUserPhoto", logUser.getUser_photo());//get userphoto of the logged user
    return InventoryPage;
  }

  // mapping to return all inventory data(URL-->"inventory/findall")
  @GetMapping(value = "/inventory/findall", produces = "application/json")
  public List<Inventory> findAllData() {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Item_Inventory");// Module name should be checked with the database Module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the inventory table
      return inventoryDao.findAll(Sort.by(Direction.DESC, "id"));//

    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }



  // mapping to return batch list based on the selected item
  // supplier(URL-->"inventory/batchlistbyitem?itemid") to invoice form
  @GetMapping(value = "/inventory/batcheslistbyitemid/{itemid}", produces = "application/json")
  public List<Inventory> getBatchListByItemId(@PathVariable("itemid") Integer itemid) {

    // Check user authentication and authorization

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Privilege usrPrivilege = userPrivilegeController.getPrivilegeByUserModule(auth.getName(),
        "Item_Inventory");// Module name should be checked with the database module table

    if (usrPrivilege.getSelect_privilege()) {
      // returning the values in the item table
      return inventoryDao.getBatchListByItem(itemid);
    } else {
      return new ArrayList<>();// returns an empty array since the user has no privilege to see the data
    }

  }


}
